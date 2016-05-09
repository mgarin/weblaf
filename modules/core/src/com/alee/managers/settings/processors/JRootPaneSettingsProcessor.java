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

package com.alee.managers.settings.processors;

import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;
import com.alee.utils.CompareUtils;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Custom SettingsProcessor for JRootPane component.
 * JRootPane used instead of Window since it is a JComponent, but this settings processor basically handles window state.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class JRootPaneSettingsProcessor extends SettingsProcessor<JRootPane, Rectangle>
{
    /**
     * todo 1. Save window normal/maximized window states, iconified should be converted into normal
     * todo 2. Save screen where window was located? Probably saved by coordinates but might be wrong if screen settings changed
     */

    /**
     * Window move and resize listener.
     */
    private ComponentAdapter componentAdapter;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public JRootPaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        componentAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                save ();
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                save ();
            }
        };
        window.addComponentListener ( componentAdapter );
    }

    @Override
    protected void doDestroy ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        window.removeComponentListener ( componentAdapter );
        componentAdapter = null;
    }

    @Override
    protected void doLoad ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        final Rectangle bounds = loadValue ();
        if ( bounds != null && !CompareUtils.equals ( bounds, window.getBounds () ) )
        {
            final Dimension size = bounds.getSize ();
            if ( size.width > 0 && size.height > 0 )
            {
                window.setSize ( size );
            }
            else
            {
                window.pack ();
            }

            final Point location = bounds.getLocation ();
            if ( location.x > 0 && location.y > 0 )
            {
                window.setLocation ( location );
            }
            else
            {
                window.setLocationRelativeTo ( null );
            }
        }
    }

    @Override
    protected void doSave ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        saveValue ( window.getBounds () );
    }
}