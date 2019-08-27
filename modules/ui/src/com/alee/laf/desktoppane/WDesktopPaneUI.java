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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.DesktopPaneUI;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Pluggable look and feel interface for any component based on {@link JDesktopPane}.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WDesktopPaneUI<C extends JDesktopPane> extends DesktopPaneUI implements WebUI<C>
{
    /**
     * {@link PropertyChangeListener} for the {@link JDesktopPane}.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * {@link DesktopPaneInputListener} for the {@link JDesktopPane}.
     */
    protected DesktopPaneInputListener<C> inputListener;

    /**
     * Runtime variables.
     */
    protected C desktop;
    protected DesktopManager desktopManager;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "Desktop.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving component reference
        desktop = ( C ) c;

        // Installing desktop manager
        installDesktopManager ();

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

        // Installing desktop manager
        uninstallDesktopManager ();

        // Removing component reference
        desktop = null;
    }

    /**
     * Installs {@link DesktopManager}.
     */
    protected void installDesktopManager ()
    {
        desktopManager = desktop.getDesktopManager ();
        if ( desktopManager == null )
        {
            desktopManager = new WebDesktopManager.UIResource ();
            desktop.setDesktopManager ( desktopManager );
        }
    }

    /**
     * Uninstalls {@link DesktopManager}.
     */
    protected void uninstallDesktopManager ()
    {
        if ( desktop.getDesktopManager () instanceof UIResource )
        {
            desktop.setDesktopManager ( null );
        }
        desktopManager = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( desktop, getPropertyPrefix () );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( desktop );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final String prop = evt.getPropertyName ();
                if ( Objects.equals ( prop, WebDesktopPane.DESKTOP_MANAGER_PROPERTY ) )
                {
                    installDesktopManager ();
                }
            }
        };
        desktop.addPropertyChangeListener ( propertyChangeListener );

        inputListener = createDesktopPaneInputListener ();
        inputListener.install ( desktop );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( desktop );
        inputListener = null;

        desktop.removePropertyChangeListener ( propertyChangeListener );
        propertyChangeListener = null;
    }

    /**
     * Returns {@link WDesktopPaneInputListener} for the {@link AbstractButton}.
     *
     * @return {@link WDesktopPaneInputListener} for the {@link AbstractButton}
     */
    protected DesktopPaneInputListener<C> createDesktopPaneInputListener ()
    {
        return new WDesktopPaneInputListener<C, WDesktopPaneUI<C>> ();
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return null;
    }

    @Override
    public Dimension getMinimumSize ( final JComponent c )
    {
        return new Dimension ( 0, 0 );
    }

    @Override
    public Dimension getMaximumSize ( final JComponent c )
    {
        return new Dimension ( Integer.MAX_VALUE, Integer.MAX_VALUE );
    }
}