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
     * todo 1. Current saving way is lacking options for packed/non-resizable windows and might even break size
     * todo 2. Add support for custom west/east maximized states which are not natively supported in extended states
     */

    /**
     * {@link Window} location.
     * In case of {@link Frame} it is only used for non-maximized state.
     * {@code null} value will position {@link Window} relative to its parent.
     */
    @XStreamAsAttribute
    @XStreamConverter ( PointConverter.class )
    protected Point location;

    /**
     * {@link Window} bounds.
     * In case of {@link Frame} it is only used for non-maximized state.
     * {@code null} value will pack {@link Window} to its preferred size.
     */
    @XStreamAsAttribute
    @XStreamConverter ( DimensionConverter.class )
    protected Dimension size;

    /**
     * {@link Frame}-exclusive state.
     * {@code null} value will ensure that {@link Frame} state is not affected.
     */
    @XStreamAsAttribute
    protected Integer state;

    /**
     * Constructs new {@link WindowState} with preferred size, location relative to parent and in default state.
     */
    public WindowState ()
    {
        this ( null, null, null );
    }

    /**
     * Constructs new {@link WindowState} with preferred size, specified location and in default state.
     *
     * @param location {@link Window} location,
     */
    public WindowState ( final Point location )
    {
        this ( location, null, null );
    }

    /**
     * Constructs new {@link WindowState} with specified size, location relative to parent and in default state.
     *
     * @param size {@link Window} size
     */
    public WindowState ( final Dimension size )
    {
        this ( null, size, null );
    }

    /**
     * Constructs new {@link WindowState} with specified size, specified location and in default state.
     *
     * @param location {@link Window} location
     * @param size     {@link Window} size
     */
    public WindowState ( final Point location, final Dimension size )
    {
        this ( location, size, null );
    }

    /**
     * Constructs new {@link WindowState} with preferred size, location relative to parent and in specified state.
     *
     * @param state {@link Frame}-exclusive state
     */
    public WindowState ( final Integer state )
    {
        this ( null, null, state );
    }

    /**
     * Constructs new {@link WindowState} with preferred size, specified location and in specified state.
     *
     * @param location {@link Window} location
     * @param state    {@link Frame}-exclusive state
     */
    public WindowState ( final Point location, final Integer state )
    {
        this ( location, null, state );
    }

    /**
     * Constructs new {@link WindowState} with specified size, location relative to parent and in specified state.
     *
     * @param size  {@link Window} size
     * @param state {@link Frame}-exclusive state
     */
    public WindowState ( final Dimension size, final Integer state )
    {
        this ( null, size, state );
    }

    /**
     * Constructs new {@link WindowState} with specified size, location and in specified state.
     *
     * @param location {@link Window} location
     * @param size     {@link Window} size
     * @param state    {@link Frame}-exclusive state
     */
    public WindowState ( final Point location, final Dimension size, final Integer state )
    {
        this.location = location;
        this.size = size;
        this.state = state;
    }

    /**
     * Constructs new {@link WindowState} with settings from the specified {@link Window}.
     *
     * @param window {@link Window} to retrieve settings from
     */
    public WindowState ( final Window window )
    {
        this ( CoreSwingUtils.getRootPane ( window ) );
    }

    /**
     * Constructs new {@link WindowState} with settings from the specified {@link JRootPane}'s {@link Window}.
     *
     * @param rootPane {@link JRootPane} used to find {@link Window} to retrieve settings from
     */
    public WindowState ( final JRootPane rootPane )
    {
        retrieve ( rootPane );
    }

    /**
     * Returns {@link Window} location.
     *
     * @return {@link Window} location
     */
    public Point location ()
    {
        return location;
    }

    /**
     * Returns {@link Window} bounds.
     *
     * @return {@link Window} bounds
     */
    public Dimension size ()
    {
        return size;
    }

    /**
     * Returns {@link Frame}-exclusive state.
     *
     * @return {@link Frame}-exclusive state
     */
    public Integer state ()
    {
        return state;
    }

    /**
     * Returns settings retrieved from the specified {@link JRootPane}'s {@link Window}.
     * Calling this method when this {@link WindowState} is not empty will apply settings from the specified {@link JRootPane}'s
     * {@link Window} on top of settings in this {@link WindowState}.
     *
     * @param rootPane {@link JRootPane} used to find {@link Window} to retrieve settings from
     * @return settings retrieved from the specified {@link JRootPane}'s {@link Window}
     */
    public WindowState retrieve ( final JRootPane rootPane )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( rootPane );

        // Saving frame-exclusive settings
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
     * Applies this {@link WindowState} to the specified {@link JRootPane}'s {@link Window}.
     *
     * @param rootPane {@link JRootPane} used to find {@link Window} to apply this {@link WindowState} to
     */
    public void apply ( final JRootPane rootPane )
    {
        // Searching for window
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

        // Frame-exclusive settings
        if ( window instanceof Frame )
        {
            // Restoring frame state
            if ( state != null )
            {
                state &= ~Frame.ICONIFIED;
                ( ( Frame ) window ).setExtendedState ( state );
            }
        }
    }
}