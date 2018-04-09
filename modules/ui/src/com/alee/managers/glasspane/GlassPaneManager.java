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

import com.alee.api.jdk.Function;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import java.awt.*;

/**
 * This manager provides an instance of {@link WebGlassPane} for specified {@link JRootPane} instance.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-GlassPaneManager">How to use GlassPaneManager</a>
 * @see WebGlassPane
 */
public final class GlassPaneManager
{
    /**
     * {@link WebGlassPane}s used within various windows.
     */
    private static final WeakComponentData<JComponent, WebGlassPane> glassPanes =
            new WeakComponentData<JComponent, WebGlassPane> ( "GlassPaneManager.WebGlassPane", 3 );

    /**
     * Returns registered {@link WebGlassPane} for {@link JRootPane} under the specified component.
     * If {@link WebGlassPane} is not yet registered for that {@link JRootPane} then it will be created.
     * Might return null if no {@link WebGlassPane} could be registered for that {@link JRootPane}.
     *
     * @param component component to process
     * @return registered {@link WebGlassPane} for {@link JRootPane} under the specified component or null if it cannot be registered
     */
    public static WebGlassPane getGlassPane ( final Component component )
    {
        return getGlassPane ( CoreSwingUtils.getRootPane ( component ) );
    }

    /**
     * Returns registered {@link WebGlassPane} for the specified {@link JRootPane}.
     * If {@link WebGlassPane} is not yet registered for that {@link JRootPane} then it will be created.
     * Might return null if no {@link WebGlassPane} could be registered for that {@link JRootPane}.
     *
     * @param rootPane {@link JRootPane} to process
     * @return registered {@link WebGlassPane} for {@link JRootPane} under the specified component or null if it cannot be registered
     */
    public static WebGlassPane getGlassPane ( final JRootPane rootPane )
    {
        if ( rootPane != null )
        {
            return glassPanes.get ( rootPane, new Function<JComponent, WebGlassPane> ()
            {
                @Override
                public WebGlassPane apply ( final JComponent component )
                {
                    final WebGlassPane glassPane = new WebGlassPane ();
                    rootPane.setGlassPane ( glassPane );
                    glassPane.setVisible ( true );
                    rootPane.invalidate ();
                    return glassPane;
                }
            } );
        }
        else
        {
            throw new GlassPaneException ( "JRootPane is not specified for WebGlassPane" );
        }
    }
}