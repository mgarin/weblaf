/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.statusbar;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.LM;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.FileUtils;
import com.alee.utils.swing.ComponentUpdater;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 * @author Mikle Garin
 */

public class WebMemoryBar extends WebButton
{
    /**
     * todo 1. Create memory bar UI
     * todo 2. Cleanup code mess here
     * todo 3. Optimize updaters to just 1 used across all memory bars
     * todo 4. Add appropriate LanguageUpdater support
     */

    /**
     * Updater thread name.
     */
    public static final String THREAD_NAME = "WebMemoryBar.updater";

    /**
     * Settings.
     */
    protected boolean allowGcAction = true;
    protected boolean showTooltip = true;
    protected int tooltipDelay = 1000;
    protected boolean showMaximumMemory = false;

    /**
     * Runtime variables.
     */
    protected long usedMemory = 0;
    protected long allocatedMemory = 0;
    protected long maxMemory = 0;
    protected int refreshRate = 1000;
    protected ComponentUpdater updater = null;
    private WebCustomTooltip tooltip;
    private final WebLabel tooltipLabel;

    public WebMemoryBar ()
    {
        this ( StyleId.auto );
    }

    public WebMemoryBar ( final StyleId id )
    {
        super ( id );
        setFocusable ( true );

        final ImageIcon icon = new ImageIcon ( WebMemoryBar.class.getResource ( "icons/memory.png" ) );
        tooltipLabel = new WebLabel ( StyleId.memorybarTooltip.at ( this ), icon );
        updateTooltip ();

        // Values updater
        updateMemory ();
        updater = ComponentUpdater.install ( this, THREAD_NAME, refreshRate, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                updateMemory ();
            }
        } );

        // GC action
        addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                gc ();
            }
        } );

        // Language update
        // todo Replace with proper LanguageUpdater
        addLanguageListener ( new LanguageListener ()
        {
            @Override
            public void languageChanged ( final Language oldLanguage, final Language newLanguage )
            {
                updateMemory ();
            }
        } );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.memorybar;
    }

    public void gc ()
    {
        if ( allowGcAction )
        {
            fireGcCalled ();
            System.gc ();
            updateMemory ();
            fireGcCompleted ();
        }
    }

    protected void updateMemory ()
    {
        // todo Perform memory request asynchronously?
        // todo Probably make a queue with requests throttling to avoid excessive updates

        // Determining current memory usage state
        final MemoryUsage mu = ManagementFactory.getMemoryMXBean ().getHeapMemoryUsage ();
        usedMemory = mu.getUsed ();
        allocatedMemory = mu.getCommitted ();
        maxMemory = mu.getMax ();

        // Updating bar text
        setText ( getMemoryBarText () );

        // Updating tooltip text
        if ( showTooltip )
        {
            tooltipLabel.setText ( getMemoryBarTooltipText () );
            tooltip.updateLocation ();
        }

        // Updating view
        repaint ();
    }

    protected String getMemoryBarText ()
    {
        final long total = showMaximumMemory ? maxMemory : allocatedMemory;
        return FileUtils.getFileSizeString ( usedMemory, getDigits ( usedMemory ) ) + " " +
                LM.get ( "weblaf.ex.memorybar.of" ) + " " +
                FileUtils.getFileSizeString ( total, getDigits ( total ) );
    }

    protected String getMemoryBarTooltipText ()
    {
        return "<html>" + LM.get ( "weblaf.ex.memorybar.alloc" ) + " <b>" +
                FileUtils.getFileSizeString ( allocatedMemory, getDigits ( allocatedMemory ) ) +
                "</b> " + LM.get ( "weblaf.ex.memorybar.used" ) + " <b>" +
                FileUtils.getFileSizeString ( usedMemory, getDigits ( usedMemory ) ) +
                getMaximumText () + "</b></html>";
    }

    private String getMaximumText ()
    {
        if ( showMaximumMemory )
        {
            return "</b> " + LM.get ( "weblaf.ex.memorybar.max" ) + " <b>" +
                    FileUtils.getFileSizeString ( maxMemory, getDigits ( maxMemory ) );
        }
        else
        {
            return "";
        }
    }

    private int getDigits ( final long size )
    {
        return size < FileUtils.GB ? 0 : 2;
    }

    public int getRefreshRate ()
    {
        return refreshRate;
    }

    public void setRefreshRate ( final int refreshRate )
    {
        this.refreshRate = refreshRate;
        updater.setDelay ( refreshRate );
    }

    public boolean isAllowGcAction ()
    {
        return allowGcAction;
    }

    public void setAllowGcAction ( final boolean allowGcAction )
    {
        this.allowGcAction = allowGcAction;
    }

    public boolean isShowTooltip ()
    {
        return showTooltip;
    }

    public void setShowTooltip ( final boolean showTooltip )
    {
        this.showTooltip = showTooltip;
        updateTooltip ();
    }

    private void updateTooltip ()
    {
        if ( showTooltip )
        {
            tooltip = TooltipManager.setTooltip ( this, tooltipLabel, tooltipDelay );
        }
        else
        {
            TooltipManager.removeTooltips ( tooltipLabel );
        }
    }

    public long getAllocatedMemory ()
    {
        return allocatedMemory;
    }

    public long getUsedMemory ()
    {
        return usedMemory;
    }

    public long getMaxMemory ()
    {
        return maxMemory;
    }

    public Icon getMemoryIcon ()
    {
        return tooltipLabel.getIcon ();
    }

    public void setMemoryIcon ( final Icon memoryIcon )
    {
        tooltipLabel.setIcon ( memoryIcon );
    }

    public int getTooltipDelay ()
    {
        return tooltipDelay;
    }

    public void setTooltipDelay ( final int tooltipDelay )
    {
        this.tooltipDelay = tooltipDelay;
    }

    public boolean isShowMaximumMemory ()
    {
        return showMaximumMemory;
    }

    public void setShowMaximumMemory ( final boolean showMaximumMemory )
    {
        this.showMaximumMemory = showMaximumMemory;
    }

    public void addMemoryBarListener ( final MemoryBarListener listener )
    {
        listenerList.add ( MemoryBarListener.class, listener );
    }

    public void removeMemoryBarListener ( final MemoryBarListener listener )
    {
        listenerList.remove ( MemoryBarListener.class, listener );
    }

    public void fireGcCalled ()
    {
        for ( final MemoryBarListener listener : listenerList.getListeners ( MemoryBarListener.class ) )
        {
            listener.gcCalled ();
        }
    }

    public void fireGcCompleted ()
    {
        for ( final MemoryBarListener listener : listenerList.getListeners ( MemoryBarListener.class ) )
        {
            listener.gcCompleted ();
        }
    }
}