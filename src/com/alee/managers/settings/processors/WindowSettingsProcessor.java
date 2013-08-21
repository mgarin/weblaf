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

import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;
import com.alee.utils.CompareUtils;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Custom SettingsProcessor for Window component.
 *
 * @author Mikle Garin
 */

// todo Save window normal/maximized window states, iconified should be converted into normal
public class WindowSettingsProcessor extends SettingsProcessor<Window, Rectangle>
{
    /**
     * Window move and resize listener.
     */
    private ComponentAdapter componentAdapter;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public WindowSettingsProcessor ( SettingsProcessorData data )
    {
        super ( data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doInit ( Window window )
    {
        componentAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( ComponentEvent e )
            {
                if ( SettingsManager.isSaveOnChange () )
                {
                    save ();
                }
            }

            @Override
            public void componentMoved ( ComponentEvent e )
            {
                if ( SettingsManager.isSaveOnChange () )
                {
                    save ();
                }
            }
        };
        window.addComponentListener ( componentAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDestroy ( Window window )
    {
        window.removeComponentListener ( componentAdapter );
        componentAdapter = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoad ( Window window )
    {
        Rectangle bounds = loadValue ();
        if ( bounds == null )
        {
            window.pack ();
            window.setLocationRelativeTo ( null );
        }
        else if ( CompareUtils.equals ( bounds, window.getBounds () ) )
        {
            Dimension size = bounds.getSize ();
            if ( size.width > 0 && size.height > 0 )
            {
                window.setSize ( size );
            }
            else
            {
                window.pack ();
            }

            Point location = bounds.getLocation ();
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSave ( Window window )
    {
        saveValue ( window.getBounds () );
    }
}