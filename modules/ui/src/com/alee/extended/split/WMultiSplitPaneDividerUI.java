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

package com.alee.extended.split;

import com.alee.laf.WebUI;
import com.alee.laf.button.WebButton;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Pluggable look and feel interface for {@link WebMultiSplitPaneDivider} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public abstract class WMultiSplitPaneDividerUI<C extends WebMultiSplitPaneDivider> extends ComponentUI implements WebUI<C>
{
    /**
     * Runtime variables.
     */
    protected C divider;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving divider reference
        divider = ( C ) c;

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

        // Removing divider reference
        divider = null;
    }

    @Override
    public String getPropertyPrefix ()
    {
        return "MultiSplitPaneDivider.";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( divider, getPropertyPrefix () );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( divider );
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
     * Returns {@link WebButton} that can be used to expand component to the right of this {@link WebMultiSplitPaneDivider}.
     *
     * @return {@link WebButton} that can be used to expand component to the right of this {@link WebMultiSplitPaneDivider}
     */
    public abstract WebButton getLeftOneTouchButton ();

    /**
     * Returns {@link WebButton} that can be used to expand component to the left of this {@link WebMultiSplitPaneDivider}.
     *
     * @return {@link WebButton} that can be used to expand component to the left of this {@link WebMultiSplitPaneDivider}
     */
    public abstract WebButton getRightOneTouchButton ();
}