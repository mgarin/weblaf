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

import com.alee.api.annotations.NotNull;
import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link WebDockableFrame} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
public abstract class WDockableFrameUI<C extends WebDockableFrame> extends ComponentUI implements WebUI<C>
{
    /**
     * Runtime variables.
     */
    protected C frame;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "DockableFrame.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving dockable frame reference
        frame = ( C ) c;

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

        // Removing dockable frame reference
        frame = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( frame, getPropertyPrefix () );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( frame );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Returns {@link SidebarButton} for {@link WebDockableFrame} represented by this UI.
     *
     * @return {@link SidebarButton} for {@link WebDockableFrame} represented by this UI
     */
    @NotNull
    public abstract SidebarButton getSidebarButton ();

    /**
     * Returns minimum frame dialog size.
     *
     * @return minimum frame dialog size
     */
    @NotNull
    public abstract Dimension getMinimumDialogSize ();
}