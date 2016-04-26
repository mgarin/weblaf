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

package com.alee.managers.settings.processors.data;

import com.alee.utils.CompareUtils;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Custom {@link javax.swing.JRootPane} settings holder.
 *
 * @author bspkrs
 */

public class JRootPaneSettings implements Serializable
{
    /**
     * Window non-maximized bounds.
     */
    private Rectangle bounds;

    /**
     * Frame state.
     */
    private int state = Frame.NORMAL;

    /**
     * Constructs new object holding settings for {@link javax.swing.JRootPane}.
     *
     * @param rootPane {@link javax.swing.JRootPane} to hold settings for
     */
    public JRootPaneSettings ( final JRootPane rootPane )
    {
        super ();
        retrieve ( rootPane );
    }

    /**
     * Returns settings retrieved from the specified {@link javax.swing.JRootPane}.
     *
     * @param rootPane {@link javax.swing.JRootPane} to retrieve settings from
     * @return settings retrieved from the specified {@link javax.swing.JRootPane}
     */
    public JRootPaneSettings retrieve ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Saving frame state
        if ( window instanceof Frame )
        {
            state = ( ( Frame ) window ).getExtendedState ();
        }

        // Saving bounds only if window is not maximized or existing bounds are {@code null}
        if ( ( state & Frame.MAXIMIZED_BOTH ) == 0 || bounds == null )
        {
            bounds = window.getBounds ();
        }

        return this;
    }

    /**
     * Apply existing settings to the specified {@link javax.swing.JRootPane}.
     *
     * @param rootPane {@link javax.swing.JRootPane} to apply settings to
     */
    public void apply ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Restoring bounds only if they were saved before and aren't the same
        // We have to restore bounds even for maximized frame so it can go back to normal state with proper bounds
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

        // Restoring frame state after bounds
        if ( window instanceof Frame )
        {
            state &= ~Frame.ICONIFIED;
            ( ( Frame ) window ).setExtendedState ( state );
        }
    }
}