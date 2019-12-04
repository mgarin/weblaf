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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebUI;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 * Pluggable look and feel interface for {@link WebMemoryBar} component.
 *
 * @param <C> {@link WebMemoryBar} type
 * @author Mikle Garin
 */
public abstract class WMemoryBarUI<C extends WebMemoryBar> extends ComponentUI implements WebUI<C>
{
    /**
     * {@link WebMemoryBar} instance.
     */
    protected C memoryBar;

    /**
     * Currently displayed {@link MemoryUsage}.
     */
    protected MemoryUsage memoryUsage;

    /**
     * {@link WebStyledLabel} used as {@link WebMemoryBar} tooltip.
     */
    protected WebStyledLabel toolTipLabel;

    /**
     * {@link WebTimer} used to update {@link MemoryUsage}.
     */
    protected WebTimer updater;

    /**
     * {@link PropertyChangeListener}.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * {@link MemoryBarInputListener} for the {@link WebMemoryBar}.
     */
    protected MemoryBarInputListener<C> inputListener;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "MemoryBar.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving canvas reference
        memoryBar = ( C ) c;

        // Installing default component settings
        installDefaults ();

        // Installing default component listeners
        installListeners ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing canvas reference
        memoryBar = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( memoryBar, getPropertyPrefix () );
        updateMemoryUsage ();
        installToolTip ();
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        uninstallToolTip ();
        LafUtils.uninstallDefaults ( memoryBar );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        updater = WebTimer.repeat ( true, "WebMemoryBar#" + memoryBar.hashCode () + "#memoryUsageUpdater", 1000L, new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                final MemoryUsage memoryUsage = getRuntimeMemoryUsage ();
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        updateMemoryUsage ( memoryUsage );
                    }
                } );
            }
        } );

        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( @NotNull final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( Objects.equals ( property, WebMemoryBar.DISPLAY_MAXIMUM_MEMORY_PROPERTY ) )
                {
                    updateMemoryUsage ( memoryUsage );
                    memoryBar.repaint ();
                }
                else if ( Objects.equals ( property, WebMemoryBar.DISPLAY_TOOL_TIP_PROPERTY ) )
                {
                    if ( memoryBar.isToolTipDisplayed () )
                    {
                        installToolTip ();
                    }
                    else
                    {
                        uninstallToolTip ();
                    }
                }
                else if ( Objects.equals ( property, WebMemoryBar.REFRESH_RATE_PROPERTY ) )
                {
                    updater.setDelay ( memoryBar.getRefreshRate () );
                }
            }
        };
        memoryBar.addPropertyChangeListener ( propertyChangeListener );

        inputListener = createButtonInputListener ();
        inputListener.install ( memoryBar );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( memoryBar );
        inputListener = null;

        memoryBar.removePropertyChangeListener ( propertyChangeListener );
        propertyChangeListener = null;

        updater.stop ();
        updater = null;
    }

    /**
     * Returns {@link MemoryBarInputListener} for the {@link WebMemoryBar}.
     *
     * @return {@link MemoryBarInputListener} for the {@link WebMemoryBar}
     */
    @NotNull
    protected MemoryBarInputListener<C> createButtonInputListener ()
    {
        return new WMemoryBarInputListener<C, WMemoryBarUI<C>> ();
    }

    /**
     * Installs tooltip on {@link WebMemoryBar}.
     */
    protected void installToolTip ()
    {
        if ( toolTipLabel == null && memoryBar.isToolTipDisplayed () )
        {
            toolTipLabel = new WebStyledLabel ( StyleId.memorybarToolTip.at ( memoryBar ), Icons.memory );
            toolTipLabel.setLanguage ( memoryBar.getToolTipKey (), memoryBar.getToolTipData ( memoryUsage ) );
            memoryBar.setToolTip ( toolTipLabel );
        }
    }

    /**
     * Uninstalls tooltip from {@link WebMemoryBar}.
     */
    protected void uninstallToolTip ()
    {
        if ( toolTipLabel != null )
        {
            memoryBar.removeToolTips ();
            toolTipLabel = null;
        }
    }

    /**
     * Returns current runtime {@link MemoryUsage}.
     *
     * @return current runtime {@link MemoryUsage}
     */
    @NotNull
    protected MemoryUsage getRuntimeMemoryUsage ()
    {
        return ManagementFactory.getMemoryMXBean ().getHeapMemoryUsage ();
    }

    /**
     * Returns currently displayed {@link MemoryUsage}.
     *
     * @return currently displayed {@link MemoryUsage}
     */
    @NotNull
    public MemoryUsage getMemoryUsage ()
    {
        return memoryUsage;
    }

    /**
     * Updates currently displayed {@link MemoryUsage}.
     */
    public void updateMemoryUsage ()
    {
        updateMemoryUsage ( getRuntimeMemoryUsage () );
    }

    /**
     * Updates currently displayed {@link MemoryUsage}.
     *
     * @param memoryUsage {@link MemoryUsage} to display
     */
    protected void updateMemoryUsage ( @NotNull final MemoryUsage memoryUsage )
    {
        this.memoryUsage = memoryUsage;
        if ( toolTipLabel != null )
        {
            toolTipLabel.updateLanguage ( memoryBar.getToolTipData ( memoryUsage ) );
        }
        memoryBar.repaint ();
    }
}