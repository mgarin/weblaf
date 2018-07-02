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

package com.alee.extended.dock;

import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link WebDockablePane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
public abstract class WDockablePaneUI<C extends WebDockablePane> extends ComponentUI implements WebUI<C>
{
    /**
     * Runtime variables.
     */
    protected C pane;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving dockable pane reference
        pane = ( C ) c;

        // Installing default component settings
        installDefaults ();

        // Installing default component listeners
        installListeners ();
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing dockable pane reference
        pane = null;
    }

    @Override
    public String getPropertyPrefix ()
    {
        return "DockablePane.";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( pane, getPropertyPrefix () );
        pane.setSidebarVisibility ( SidebarVisibility.minimized );
        pane.setSidebarButtonAction ( SidebarButtonAction.restore );
        pane.setContentSpacing ( 0 );
        pane.setResizeGripper ( 10 );
        pane.setMinimumElementSize ( new Dimension ( 40, 40 ) );
        pane.setOccupyMinimumSizeForChildren ( true );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( pane );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        // Do nothing by default
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        // Do nothing by default
    }

    /**
     * Returns dockable pane glass layer component.
     *
     * @return dockable pane glass layer component
     */
    public abstract JComponent createGlassLayer ();
}