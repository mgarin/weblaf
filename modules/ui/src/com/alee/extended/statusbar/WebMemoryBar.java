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

import com.alee.global.StyleConstants;
import com.alee.laf.Styles;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.swing.ComponentUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebMemoryBar extends WebPanel
{
    public static final String THREAD_NAME = "WebMemoryBar.updater";

    private ImageIcon memoryIcon = WebMemoryBarStyle.memoryIcon;
    private Color allocatedBorderColor = WebMemoryBarStyle.allocatedBorderColor;
    private Color allocatedDisabledBorderColor = WebMemoryBarStyle.allocatedDisabledBorderColor;
    private Color usedBorderColor = WebMemoryBarStyle.usedBorderColor;
    private Color usedFillColor = WebMemoryBarStyle.usedFillColor;

    public boolean drawBorder = WebMemoryBarStyle.drawBorder;
    public boolean fillBackground = WebMemoryBarStyle.fillBackground;
    private int leftRightSpacing = WebMemoryBarStyle.leftRightSpacing;
    private int shadeWidth = WebMemoryBarStyle.shadeWidth;
    private int round = WebMemoryBarStyle.round;

    private boolean allowGcAction = WebMemoryBarStyle.allowGcAction;

    private boolean showTooltip = WebMemoryBarStyle.showTooltip;
    private int tooltipDelay = WebMemoryBarStyle.tooltipDelay;

    private boolean showMaximumMemory = WebMemoryBarStyle.showMaximum;

    private final List<MemoryBarListener> listeners = new ArrayList<MemoryBarListener> ( 1 );

    private long usedMemory = 0;
    private long allocatedMemory = 0;
    private long maxMemory = 0;

    private int refreshRate = 1000;
    private ComponentUpdater updater = null;

    private boolean pressed = false;

    private final WebLabel label;
    private WebCustomTooltip tooltip;
    private final WebLabel tooltipLabel;

    public WebMemoryBar ()
    {
        super ( Styles.memorybar );
        setFocusable ( true );

        label = new WebLabel ();
        label.setStyleId ( Styles.memorybarLabel );
        add ( label );

        tooltipLabel = new WebLabel ( memoryIcon );
        tooltipLabel.setStyleId ( Styles.memorybarTooltipLabel );
        updateTooltip ();

        updateMemory ();

        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( isEnabled () )
                {
                    if ( Hotkey.SPACE.isTriggered ( e ) || Hotkey.ENTER.isTriggered ( e ) )
                    {
                        pressed = true;
                        repaint ();
                    }
                }
            }

            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.SPACE.isTriggered ( e ) || Hotkey.ENTER.isTriggered ( e ) )
                {
                    pressed = false;
                    if ( isEnabled () )
                    {
                        doGC ();
                    }
                    else
                    {
                        repaint ();
                    }
                }
            }
        } );
        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( allowGcAction && isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    pressed = true;
                    requestFocusInWindow ();
                    doGC ();
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( pressed && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    pressed = false;
                    repaint ();
                }
            }
        } );

        addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusGained ( final FocusEvent e )
            {
                repaint ();
            }

            @Override
            public void focusLost ( final FocusEvent e )
            {
                repaint ();
            }
        } );

        // Values updater
        updater = ComponentUpdater.install ( this, THREAD_NAME, refreshRate, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                updateMemory ();
            }
        } );
    }

    public void doGC ()
    {
        fireGcCalled ();
        System.gc ();
        updateMemory ();
        fireGcCompleted ();
    }

    protected void updateMemory ()
    {
        // Determining current memory usage state
        final MemoryUsage mu = ManagementFactory.getMemoryMXBean ().getHeapMemoryUsage ();
        usedMemory = mu.getUsed ();
        allocatedMemory = mu.getCommitted ();
        maxMemory = mu.getMax ();

        // Updating bar text
        label.setText ( getMemoryBarText () );

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
                LanguageManager.get ( "weblaf.ex.memorybar.of" ) + " " +
                FileUtils.getFileSizeString ( total, getDigits ( total ) );
    }

    protected String getMemoryBarTooltipText ()
    {
        return "<html>" + LanguageManager.get ( "weblaf.ex.memorybar.alloc" ) + " <b>" +
                FileUtils.getFileSizeString ( allocatedMemory, getDigits ( allocatedMemory ) ) +
                "</b> " + LanguageManager.get ( "weblaf.ex.memorybar.used" ) + " <b>" +
                FileUtils.getFileSizeString ( usedMemory, getDigits ( usedMemory ) ) +
                getMaximumText () + "</b></html>";
    }

    private String getMaximumText ()
    {
        if ( showMaximumMemory )
        {
            return "</b> " + LanguageManager.get ( "weblaf.ex.memorybar.max" ) + " <b>" +
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

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
    }

    public Color getAllocatedBorderColor ()
    {
        return allocatedBorderColor;
    }

    public void setAllocatedBorderColor ( final Color allocatedBorderColor )
    {
        this.allocatedBorderColor = allocatedBorderColor;
    }

    public Color getAllocatedDisabledBorderColor ()
    {
        return allocatedDisabledBorderColor;
    }

    public void setAllocatedDisabledBorderColor ( final Color allocatedDisabledBorderColor )
    {
        this.allocatedDisabledBorderColor = allocatedDisabledBorderColor;
    }

    public Color getUsedBorderColor ()
    {
        return usedBorderColor;
    }

    public void setUsedBorderColor ( final Color usedBorderColor )
    {
        this.usedBorderColor = usedBorderColor;
    }

    public Color getUsedFillColor ()
    {
        return usedFillColor;
    }

    public void setUsedFillColor ( final Color usedFillColor )
    {
        this.usedFillColor = usedFillColor;
    }

    public int getLeftRightSpacing ()
    {
        return leftRightSpacing;
    }

    public void setLeftRightSpacing ( final int leftRightSpacing )
    {
        this.leftRightSpacing = leftRightSpacing;
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        this.drawBorder = drawBorder;
    }

    public boolean isFillBackground ()
    {
        return fillBackground;
    }

    public void setFillBackground ( final boolean fillBackground )
    {
        this.fillBackground = fillBackground;
    }

    public boolean isAllowGcAction ()
    {
        return allowGcAction;
    }

    public void setAllowGcAction ( final boolean allowGcAction )
    {
        this.allowGcAction = allowGcAction;
        if ( !allowGcAction && pressed )
        {
            pressed = false;
            repaint ();
        }
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

    public ImageIcon getMemoryIcon ()
    {
        return memoryIcon;
    }

    public void setMemoryIcon ( final ImageIcon memoryIcon )
    {
        this.memoryIcon = memoryIcon;
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

    @Override
    protected void paintComponent ( final Graphics g )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object old = GraphicsUtils.setupAntialias ( g2d );

        final boolean enabled = isEnabled ();

        // Border and background
        if ( drawBorder )
        {
            LafUtils.drawWebStyle ( g2d, this, isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor, shadeWidth,
                    round, fillBackground, !pressed );
        }
        else if ( fillBackground )
        {
            g2d.setPaint ( !pressed ? LafUtils.getWebGradientPaint ( 0, 0, 0, getHeight () ) : getBackground () );
            g2d.fill ( getVisibleRect () );
        }

        // Allocated memory line
        if ( showMaximumMemory )
        {
            g2d.setPaint ( enabled ? allocatedBorderColor : allocatedDisabledBorderColor );
            final int allocatedWidth = getProgressWidth ( allocatedMemory, false );
            g2d.drawLine ( shadeWidth + allocatedWidth, shadeWidth + 2, shadeWidth + allocatedWidth, getHeight () - shadeWidth - 3 );
        }

        // Disabled state background transparency
        final Composite composite = GraphicsUtils.setupAlphaComposite ( g2d, 0.5f, !enabled );

        // Used memory background
        g2d.setPaint ( usedFillColor );
        g2d.fill ( getProgressShape ( usedMemory, true ) );

        // Used memory border
        g2d.setPaint ( usedBorderColor );
        g2d.draw ( getProgressShape ( usedMemory, false ) );

        GraphicsUtils.restoreComposite ( g2d, composite, !enabled );
        GraphicsUtils.restoreAntialias ( g2d, old );

        super.paintComponent ( g2d );
    }

    private Shape getProgressShape ( final long progress, final boolean fill )
    {
        final int arcRound = Math.max ( 0, round - 1 ) * 2;
        if ( drawBorder )
        {
            return new RoundRectangle2D.Double ( shadeWidth + 2, shadeWidth + 2, getProgressWidth ( progress, fill ),
                    getHeight () - 4 - shadeWidth * 2 - ( fill ? 0 : 1 ), arcRound, arcRound );
        }
        else
        {
            return new RoundRectangle2D.Double ( 1, 1, getProgressWidth ( progress, fill ), getHeight () - 2 - ( fill ? 0 : 1 ), arcRound,
                    arcRound );
        }
    }

    private int getProgressWidth ( final long progress, final boolean fill )
    {
        return Math.round ( ( float ) ( getWidth () - ( drawBorder ? 4 + shadeWidth * 2 : 2 ) -
                ( fill ? 0 : 1 ) ) * progress / ( showMaximumMemory ? maxMemory : allocatedMemory ) );
    }

    public void addMemoryBarListener ( final MemoryBarListener listener )
    {
        listeners.add ( listener );
    }

    public void removeMemoryBarListener ( final MemoryBarListener listener )
    {
        listeners.remove ( listener );
    }

    public void fireGcCalled ()
    {
        for ( final MemoryBarListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.gcCalled ();
        }
    }

    public void fireGcCompleted ()
    {
        for ( final MemoryBarListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.gcCompleted ();
        }
    }
}