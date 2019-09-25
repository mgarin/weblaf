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

package com.alee.laf.rootpane;

import com.alee.api.annotations.NotNull;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@link SettingsProcessor} implementation that handles {@link JRootPane}'s ancestor {@link Window} position and state.
 * {@link JRootPane} is used instead of {@link Window} since it is a {@link JComponent} and {@link Window} is not.
 *
 * @author bspkrs
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see WindowState
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class RootPaneSettingsProcessor extends SettingsProcessor<JRootPane, WindowState, Configuration<WindowState>>
{
    /**
     * {@link ComponentListener} for tracking {@link Window} movement.
     */
    protected transient ComponentListener componentListener;

    /**
     * {@link WindowStateListener} for tracking {@link Window} state.
     */
    protected transient WindowStateListener windowStateListener;

    /**
     * {@link VisibilityBehavior } for updating {@link JRootPane}'s {@link Window} listeners appropriately.
     */
    protected transient VisibilityBehavior<Window> windowVisibilityBehavior;

    /**
     * Constructs new {@link RootPaneSettingsProcessor}.
     *
     * @param rootPane      {@link JRootPane} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public RootPaneSettingsProcessor ( final JRootPane rootPane, final Configuration configuration )
    {
        super ( rootPane, configuration );
    }

    @Override
    protected void register ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        /**
         * Tracking {@link Window} movement.
         */
        componentListener = new ComponentAdapter ()
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

        /**
         * Tracking {@link Window} state.
         */
        windowStateListener = new WindowStateListener ()
        {
            @Override
            public void windowStateChanged ( final WindowEvent e )
            {
                save ();
            }
        };

        /**
         * Ensure listeners are only used when {@link Window} is displayed.
         */
        windowVisibilityBehavior = new VisibilityBehavior<Window> ( window, true )
        {
            @Override
            protected void displayed ( @NotNull final Window window )
            {
                window.addComponentListener ( componentListener );
                window.addWindowStateListener ( windowStateListener );
            }

            @Override
            protected void hidden ( @NotNull final Window window )
            {
                window.removeWindowStateListener ( windowStateListener );
                window.removeComponentListener ( componentListener );
            }
        };
        windowVisibilityBehavior.install ();
    }

    @Override
    protected void unregister ( final JRootPane rootPane )
    {
        windowVisibilityBehavior.uninstall ();
        windowVisibilityBehavior = null;
        windowStateListener = null;
        componentListener = null;
    }

    @Override
    protected WindowState createDefaultValue ()
    {
        return new WindowState ( component () );
    }

    @Override
    protected void loadSettings ( final JRootPane rootPane )
    {
        loadSettings ().apply ( rootPane );
    }

    @Override
    protected void saveSettings ( final JRootPane rootPane )
    {
        saveSettings ( loadSettings ().retrieve ( rootPane ) );
    }
}