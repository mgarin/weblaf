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

package com.alee.extended.canvas;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Pluggable look and feel interface for {@link com.alee.extended.canvas.WebCanvas} component.
 *
 * @author Mikle Garin
 */

public abstract class WCanvasUI extends ComponentUI
{
    /**
     * Runtime variables.
     */
    protected WebCanvas canvas;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving canvas reference
        canvas = ( WebCanvas ) c;

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

        // Removing canvas reference
        canvas = null;
    }

    /**
     * Returns component default font key.
     *
     * @return component default font key
     */
    protected String getFontKey ()
    {
        return "Canvas.font";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        // Default component settings
        if ( SwingUtils.isUIResource ( canvas.getFont () ) )
        {
            canvas.setFont ( UIManager.getFont ( getFontKey () ) );
        }
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LookAndFeel.uninstallBorder ( canvas );
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
}