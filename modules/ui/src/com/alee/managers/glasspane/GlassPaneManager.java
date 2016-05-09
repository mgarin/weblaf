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

package com.alee.managers.glasspane;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This manager provides an instance of WebGlassPane for specified JRootPane instance.
 *
 * @author Mikle Garin
 * @see com.alee.managers.glasspane.WebGlassPane
 */

public class GlassPaneManager
{
    public static final String WEB_GLASS_PANE_KEY = "web.glass.pane";

    /**
     * Returns registered WebGlassPane for JRootPane under the specified component.
     * If WebGlassPane is not yet registered for that JRootPane then it will be created.
     * Might return null if no WebGlassPane could be registered for that JRootPane.
     *
     * @param component component to process
     * @return registered WebGlassPane for JRootPane under the specified component or null if it cannot be registered
     */
    public static WebGlassPane getGlassPane ( final Component component )
    {
        return getGlassPane ( SwingUtils.getRootPane ( component ) );
    }

    /**
     * Returns registered WebGlassPane for the specified JRootPane.
     * If WebGlassPane is not yet registered for that JRootPane then it will be created.
     * Might return null if no WebGlassPane could be registered for that JRootPane.
     *
     * @param rootPane JRootPane to process
     * @return registered WebGlassPane for JRootPane under the specified component or null if it cannot be registered
     */
    public static WebGlassPane getGlassPane ( final JRootPane rootPane )
    {
        if ( rootPane != null )
        {
            WebGlassPane glassPane = ( WebGlassPane ) rootPane.getClientProperty ( WEB_GLASS_PANE_KEY );
            if ( glassPane == null )
            {
                glassPane = new WebGlassPane ();
                rootPane.setGlassPane ( glassPane );
                glassPane.setVisible ( true );
                rootPane.invalidate ();
                rootPane.putClientProperty ( WEB_GLASS_PANE_KEY, glassPane );
            }
            return glassPane;
        }
        else
        {
            return null;
        }
    }
}