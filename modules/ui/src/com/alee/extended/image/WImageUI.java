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

package com.alee.extended.image;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Pluggable look and feel interface for {@link com.alee.extended.image.WebImage} component.
 *
 * @author Mikle Garin
 */

public abstract class WImageUI extends ComponentUI
{
    /**
     * Runtime variables.
     */
    protected WebImage image;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving image reference
        image = ( WebImage ) c;

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

        // Removing image reference
        image = null;
    }

    /**
     * Returns component default font key.
     *
     * @return component default font key
     */
    protected String getFontKey ()
    {
        return "Image.font";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        if ( SwingUtils.isUIResource ( image.getFont () ) )
        {
            image.setFont ( UIManager.getFont ( getFontKey () ) );
        }

        image.setOpacity ( 1f );
        image.setDisplayType ( DisplayType.preferred );
        image.setHorizontalAlignment ( SwingConstants.CENTER );
        image.setVerticalAlignment ( SwingConstants.CENTER );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LookAndFeel.uninstallBorder ( image );
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