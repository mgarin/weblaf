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

import com.alee.api.jdk.Objects;
import com.alee.api.merge.Mergeable;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.xml.DimensionConverter;
import com.alee.utils.xml.PointConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * {@link Window} location, size and state holder.
 *
 * @author bspkrs
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see RootPaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "WindowState" )
public class WindowState implements Mergeable, Cloneable, Serializable
{
    /**
     * todo 1. Unify all constructors to avoid issues in future
     * todo 2. Current saving way is lacking options for packed/non-resizable windows and might even break size
     * todo 3. Add support for custom west/east maximized states which are not natively supported in extended states
     */

    /**
     * {@link Window} location for non-maximized window state.
     */
    @XStreamAsAttribute
    @XStreamConverter ( PointConverter.class )
    protected Point location;

    /**
     * {@link Window} bounds for non-maximized state.
     */
    @XStreamAsAttribute
    @XStreamConverter ( DimensionConverter.class )
    protected Dimension size;

    /**
     * {@link Frame}-exclusive state.
     */
    @XStreamAsAttribute
    protected Integer state;

    /**
     * Constructs default {@link WindowState}.
     */
    public WindowState ()
    {
        this ( null, null, null );
    }

    /**
     * Constructs new {@link WindowState} with the specified {@link Window} width and height.
     *
     * @param location {@link Window} location for non-maximized {@link Window} state
     * @param size     {@link Window} bounds for non-maximized state
     */
    public WindowState ( final Point location, final Dimension size )
    {
        this ( location, size, Frame.NORMAL );
    }

    /**
     * Constructs new {@link WindowState} with the specified window width and height.
     *
     * @param location {@link Window} location for non-maximized {@link Window} state
     * @param size     {@link Window} bounds for non-maximized state
     * @param state    frame-exclusive state
     */
    public WindowState ( final Point location, final Dimension size, final Integer state )
    {
        this.location = location;
        this.size = size;
        this.state = state;
    }

    /**
     * Constructs new {@link WindowState} with settings from {@link Window}.
     *
     * @param window {@link Window} to retreive settings from
     */
    public WindowState ( final Window window )
    {
        this ( CoreSwingUtils.getRootPane ( window ) );
    }

    /**
     * Constructs new {@link WindowState} with settings from {@link JRootPane}.
     *
     * @param rootPane {@link JRootPane} to retreive settings from
     */
    public WindowState ( final JRootPane rootPane )
    {
        retrieve ( rootPane );
    }

    /**
     * Returns settings retrieved from the specified {@link JRootPane}.
     * This will apply specified {@link JRootPane} settings on top of existing ones.
     *
     * @param rootPane {@link JRootPane} to retrieve settings from
     * @return settings retrieved from the specified {@link JRootPane}
     */
    public WindowState retrieve ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Saving frame state
        if ( window instanceof Frame )
        {
            state = ( ( Frame ) window ).getExtendedState ();
        }

        // Saving bounds only if window is not maximized or existing bounds are {@code null}
        if ( state == null || ( state & Frame.MAXIMIZED_BOTH ) == 0 || size == null || location == null )
        {
            final Rectangle bounds = window.getBounds ();
            location = bounds.getLocation ();
            size = bounds.getSize ();
        }

        return this;
    }

    /**
     * Applies this {@link WindowState} to the specified {@link JRootPane}.
     *
     * @param rootPane {@link JRootPane} to apply this {@link WindowState} to
     */
    public void apply ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );
        final Rectangle bounds = window.getBounds ();

        // Restoring window size
        // We have to restore size even for maximized frame so it can go back to normal state properly
        if ( size != null )
        {
            // Restoring size only if it's not the same
            if ( Objects.notEquals ( size, bounds.getSize () ) )
            {
                if ( size.width > 0 && size.height > 0 )
                {
                    // Size was specified
                    window.setSize ( size );
                }
                else if ( size.width > 0 || size.height > 0 )
                {
                    // Part of the size was specified
                    final Dimension ps = window.getPreferredSize ();
                    size.width = size.width > 0 ? size.width : ps.width;
                    size.height = size.height > 0 ? size.height : ps.height;
                    window.setSize ( size );
                }
                else
                {
                    // Wrong size specified
                    size = window.getPreferredSize ();
                    window.setSize ( size );
                }
            }
        }
        else
        {
            // Using preferred window size
            // We need to pack window first, otherwise preferred size doesn't take native decoration into account
            // since the window peer is not yet initialized and it is impossible for the window to tell its size
            window.pack ();
            size = window.getPreferredSize ();
        }

        // Restoring window location
        // We have to restore location even for maximized frame so it can go back to normal state properly
        if ( location != null )
        {
            // Restoring bounds only if they aren't the same
            if ( Objects.notEquals ( location, bounds.getLocation () ) )
            {
                if ( location.x > 0 && location.y > 0 )
                {
                    // Ensure that window title stays on at least one of the screens
                    final Rectangle b = new Rectangle ( location, size );
                    final List<Rectangle> devicesBounds = SystemUtils.getDevicesBounds ( false );
                    boolean intersects = false;
                    for ( final Rectangle deviceBounds : devicesBounds )
                    {
                        if ( b.intersects ( deviceBounds ) )
                        {
                            intersects = true;
                            break;
                        }
                    }
                    if ( intersects )
                    {
                        // Location is correct
                        window.setLocation ( location );
                    }
                    else
                    {
                        // Location is outside of available screens
                        window.setLocationRelativeTo ( window.getOwner () );
                        location = window.getLocation ();
                    }
                }
                else
                {
                    // Wrong location specified
                    window.setLocationRelativeTo ( window.getOwner () );
                    location = window.getLocation ();
                }
            }
        }
        else
        {
            // No location specified
            window.setLocationRelativeTo ( window.getOwner () );
            location = window.getLocation ();
        }

        // Restoring frame state
        if ( state != null && window instanceof Frame )
        {
            state &= ~Frame.ICONIFIED;
            ( ( Frame ) window ).setExtendedState ( state );
        }
    }
}