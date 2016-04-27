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
import com.alee.managers.settings.processors.data.WindowSettings;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * Custom SettingsProcessor for JRootPane component.
 * JRootPane used instead of Window since it is a JComponent, but this settings processor basically handles window state.
 *
 * @author bspkrs
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class JRootPaneSettingsProcessor extends SettingsProcessor<JRootPane, WindowSettings>
{
    /**
     * Window move and resize listener.
     */
    private ComponentAdapter componentAdapter;

    /**
     * Window state change listener.
     */
    private WindowStateListener stateListener;

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
    public WindowSettings getDefaultValue ()
    {
        WindowSettings defaultValue = super.getDefaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = new WindowSettings ( getComponent () );
        }
        return defaultValue;
    }

    @Override
    protected void doInit ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Adding move and resize listener
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

        // Adding window state listener
        stateListener = new WindowStateListener ()
        {
            @Override
            public void windowStateChanged ( final WindowEvent e )
            {
                save ();
            }
        };
        window.addWindowStateListener ( stateListener );
    }

    @Override
    protected void doDestroy ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Removing move and resize listener
        window.removeComponentListener ( componentAdapter );
        componentAdapter = null;

        // Removing state listener
        window.removeWindowStateListener ( stateListener );
        stateListener = null;
    }

    @Override
    protected void doLoad ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Disabling listeners for the settings load time
        window.removeWindowStateListener ( stateListener );
        window.removeComponentListener ( componentAdapter );

        // Loading settings into {@link javax.swing.JRootPane}
        loadValue ().apply ( rootPane );

        // Restoring listeners
        window.addComponentListener ( componentAdapter );
        window.addWindowStateListener ( stateListener );
    }

    @Override
    protected void doSave ( final JRootPane rootPane )
    {
        // Saving settings from {@link javax.swing.JRootPane}
        // This will apply current settings on top of existing ones
        saveValue ( loadValue ().retrieve ( rootPane ) );
    }
}