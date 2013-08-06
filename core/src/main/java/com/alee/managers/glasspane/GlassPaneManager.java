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
import java.util.HashMap;
import java.util.Map;

/**
 * This manager provides an instance of WebGlassPane for specified JRootPane instance.
 *
 * @author Mikle Garin
 * @see WebGlassPane
 * @since 1.4
 */

public class GlassPaneManager
{
    /**
     * Registered glass panes per JRootPane.
     */
    private static Map<JRootPane, WebGlassPane> registeredWindows = new HashMap<JRootPane, WebGlassPane> ();

    /**
     * Returns registered WebGlassPane for JRootPane under the specified component.
     * If WebGlassPane is not yet registered for that JRootPane then it will be created.
     * Might return null if no WebGlassPane could be registered for that JRootPane.
     *
     * @param component component to process
     * @return registered WebGlassPane for JRootPane under the specified component or null if it cannot be registered
     */
    public static WebGlassPane getGlassPane ( Component component )
    {
        return getGlassPane ( getRootPane ( component ) );
    }

    /**
     * Returns root pane for the component's window.
     *
     * @param component component to process
     * @return root pane for the component's window
     */
    private static JRootPane getRootPane ( Component component )
    {
        if ( component instanceof JRootPane )
        {
            return ( JRootPane ) component;
        }
        else if ( component instanceof JWindow )
        {
            return ( ( JWindow ) component ).getRootPane ();
        }
        else if ( component instanceof JDialog )
        {
            return ( ( JDialog ) component ).getRootPane ();
        }
        else if ( component instanceof JFrame )
        {
            return ( ( JFrame ) component ).getRootPane ();
        }
        return SwingUtils.getRootPaneAncestor ( component );
    }

    /**
     * Returns registered WebGlassPane for the specified JRootPane.
     * If WebGlassPane is not yet registered for that JRootPane then it will be created.
     * Might return null if no WebGlassPane could be registered for that JRootPane.
     *
     * @param rootPane JRootPane to process
     * @return registered WebGlassPane for JRootPane under the specified component or null if it cannot be registered
     */
    public static WebGlassPane getGlassPane ( JRootPane rootPane )
    {
        if ( rootPane != null )
        {
            if ( registeredWindows.containsKey ( rootPane ) )
            {
                return registeredWindows.get ( rootPane );
            }
            else
            {
                WebGlassPane glassPane = new WebGlassPane ( rootPane );
                rootPane.setGlassPane ( glassPane );
                glassPane.setVisible ( true );
                rootPane.invalidate ();
                registeredWindows.put ( rootPane, glassPane );
                return glassPane;
            }
        }
        else
        {
            return null;
        }
    }
}