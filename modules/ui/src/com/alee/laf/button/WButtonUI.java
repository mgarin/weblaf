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

package com.alee.laf.button;

import com.alee.laf.UIInputListener;
import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

/**
 * Pluggable look and feel interface for any component based on {@link AbstractButton}.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WButtonUI<C extends AbstractButton> extends ButtonUI implements WebUI<C>
{
    /**
     * {@link UIInputListener}.
     */
    protected UIInputListener inputListener;

    /**
     * Runtime variables.
     */
    protected C button;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving label reference
        button = ( C ) c;

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

        // Removing label reference
        button = null;
    }

    @Override
    public String getPropertyPrefix ()
    {
        return "Button.";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( button, getPropertyPrefix () );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( button );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        inputListener = createInputListener ();
        inputListener.install ( button );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( button );
        inputListener = null;
    }

    /**
     * Returns {@link UIInputListener} for the button.
     *
     * @return {@link UIInputListener} for the button
     */
    protected UIInputListener createInputListener ()
    {
        return new WButtonListener ();
    }
}