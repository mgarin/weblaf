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
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * Custom SettingsProcessor for {@link javax.swing.JRootPane} component.
 * Root pane is used instead of Window since it is a JComponent, but this settings processor basically handles window state.
 *
 * @author bspkrs
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class RootPaneSettingsProcessor extends SettingsProcessor<JRootPane, WindowSettings>
        implements ComponentListener, WindowStateListener
{
    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public RootPaneSettingsProcessor ( final SettingsProcessorData data )
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
        window.addComponentListener ( this );
        window.addWindowStateListener ( this );
    }

    @Override
    protected void doDestroy ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        window.removeWindowStateListener ( this );
        window.removeComponentListener ( this );
    }

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

    @Override
    public void componentShown ( final ComponentEvent e )
    {
        // This event is irrelevant
    }

    @Override
    public void componentHidden ( final ComponentEvent e )
    {
        // This event is irrelevant
    }

    @Override
    public void windowStateChanged ( final WindowEvent e )
    {
        save ();
    }

    @Override
    protected void doLoad ( final JRootPane rootPane )
    {
        // Loading settings into {@link javax.swing.JRootPane}
        loadValue ().apply ( rootPane );
    }

    @Override
    protected void doSave ( final JRootPane rootPane )
    {
        // Saving settings from {@link javax.swing.JRootPane}
        // This will apply current settings on top of existing ones
        saveValue ( loadValue ().retrieve ( rootPane ) );
    }
}