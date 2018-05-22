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

package com.alee.extended.behavior;

import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Function;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This {@link Behavior} enables resizing and/or dragging {@link Component} or {@link Window} around using {@link #gripper}.
 * You can explicitely specify {@link #target} {@link Component} or {@link Window} to be resized/dragged.
 * Otherwise behavior will use {@link #gripper}'s ancestor {@link Window}.
 *
 * Special {@link Function} is used to determine whether resize or drag should be performed and to determine resize direction.
 * If {@link Function} returns {@link CompassDirection#center} - drag operation should take place.
 * Any other {@link CompassDirection} literal means that resize operation should take place.
 * Whenever {@code null} is returned - no operations will be performed.
 *
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 * You don't need to explicitely store this behavior if your only intent is to install it once and keep forever.
 * Although if you want to uninstall this behavior at some point you might want to keep its instance.
 *
 * @author Mikle Garin
 * @see #install()
 * @see #uninstall()
 */
public class ComponentResizeBehavior extends MouseAdapter implements Behavior, SwingConstants
{
    /**
     * {@link Component} that controls resize.
     */
    protected final Component gripper;

    /**
     * {@link Component} that is resized using this behavior.
     * If set to {@code null} - {@link #gripper}'s parent {@link Window} will be used instead.
     */
    protected final Component target;

    /**
     * Function providing resize direction.
     * This way we could provide multiple resize directions for single control {@link Component}.
     */
    protected Function<Point, CompassDirection> direction;

    /**
     * Whether or not {@link Component} is being resized right now.
     */
    protected boolean resizing = false;

    /**
     * Resize starting point on the {@link Component}.
     */
    protected Point initialPoint = null;

    /**
     * Initial {@link Component} bounds.
     */
    protected Rectangle initialBounds = null;

    /**
     * Current resize direction.
     */
    protected CompassDirection currentDirection = null;

    /**
     * Initially set cursor.
     */
    protected Cursor initialCursor = null;

    /**
     * Constructs new {@link ComponentResizeBehavior}.
     *
     * @param gripper   {@link Component} that controls resize
     * @param direction resize {@link CompassDirection}
     */
    public ComponentResizeBehavior ( final Component gripper, final CompassDirection direction )
    {
        this ( gripper, null, new SingleResizeDirection ( direction ) );
    }

    /**
     * Constructs new {@link ComponentResizeBehavior}.
     *
     * @param gripper   {@link Component} that controls resize
     * @param target    {@link Component} that can be resized through this behavior
     * @param direction resize {@link CompassDirection}
     */
    public ComponentResizeBehavior ( final Component gripper, final Component target, final CompassDirection direction )
    {
        this ( gripper, target, new SingleResizeDirection ( direction ) );
    }

    /**
     * Constructs new {@link ComponentResizeBehavior}.
     *
     * @param gripper   {@link Component} that controls resize
     * @param direction {@link Function} providing resize direction
     */
    public ComponentResizeBehavior ( final Component gripper, final Function<Point, CompassDirection> direction )
    {
        this ( gripper, null, direction );
    }

    /**
     * Constructs new {@link ComponentResizeBehavior}.
     *
     * @param gripper   {@link Component} that controls resize
     * @param target    {@link Component} that can be resized through this behavior
     * @param direction {@link Function} providing resize direction
     */
    public ComponentResizeBehavior ( final Component gripper, final Component target, final Function<Point, CompassDirection> direction )
    {
        super ();
        this.gripper = gripper;
        this.target = target;
        this.direction = direction;
    }

    /**
     * Installs behavior into the {@link #gripper}.
     */
    public void install ()
    {
        gripper.addMouseListener ( this );
        gripper.addMouseMotionListener ( this );
    }

    /**
     * Uninstalls behavior from the {@link #gripper}.
     */
    public void uninstall ()
    {
        gripper.removeMouseMotionListener ( this );
        gripper.removeMouseListener ( this );
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
        final int cursor = getCursor ( e.getPoint () );
        if ( cursor != -1 )
        {
            if ( initialCursor == null )
            {
                initialCursor = e.getComponent ().getCursor ();
            }
            e.getComponent ().setCursor ( Cursor.getPredefinedCursor ( cursor ) );
        }
        else if ( initialCursor != null )
        {
            e.getComponent ().setCursor ( initialCursor );
            initialCursor = null;
        }
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        if ( !resizing && initialCursor != null )
        {
            e.getComponent ().setCursor ( initialCursor );
            initialCursor = null;
        }
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( !e.isConsumed () && SwingUtilities.isLeftMouseButton ( e ) )
        {
            final CompassDirection d = direction.apply ( e.getPoint () );
            if ( d != null )
            {
                resizing = true;
                final Point los = CoreSwingUtils.locationOnScreen ( e.getComponent () );
                initialPoint = new Point ( los.x + e.getX (), los.y + e.getY () );
                initialBounds = getResized ( e ).getBounds ();
                currentDirection = d;

                // Consume event
                e.consume ();
            }
        }
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( !e.isConsumed () && SwingUtilities.isLeftMouseButton ( e ) && resizing )
        {
            final Component resized = getResized ( e );
            if ( currentDirection == CompassDirection.center )
            {
                // Moving component
                final Point mouse = CoreSwingUtils.getMouseLocation ();
                final int x = initialBounds.x + mouse.x - initialPoint.x;
                final int y = initialBounds.y + mouse.y - initialPoint.y;
                final Point location = new Point ( x, y );
                resized.setLocation ( location );
            }
            else
            {
                // Retrieving component minimum size
                // It will only be used for components and undecorated windows
                final Dimension ms;
                final boolean undecorated;
                if ( resized instanceof JDialog )
                {
                    ms = ( ( JDialog ) resized ).getRootPane ().getMinimumSize ();
                    undecorated = ( ( JDialog ) resized ).isUndecorated ();
                }
                else if ( resized instanceof JFrame )
                {
                    ms = ( ( JFrame ) resized ).getRootPane ().getMinimumSize ();
                    undecorated = ( ( JFrame ) resized ).isUndecorated ();
                }
                else if ( resized instanceof JWindow )
                {
                    ms = ( ( JWindow ) resized ).getRootPane ().getMinimumSize ();
                    undecorated = true;
                }
                else
                {
                    ms = resized.getMinimumSize ();
                    undecorated = true;
                }

                // Calculating new resulting bounds
                final Point ml = CoreSwingUtils.getMouseLocation ();
                final Point shift = new Point ( ml.x - initialPoint.x, ml.y - initialPoint.y );
                final Rectangle newBounds = new Rectangle ( initialBounds );
                switch ( currentDirection )
                {
                    case northWest:
                    {
                        newBounds.x += shift.x;
                        newBounds.width -= shift.x;
                        newBounds.y += shift.y;
                        newBounds.height -= shift.y;
                        break;
                    }
                    case north:
                    {
                        newBounds.y += shift.y;
                        newBounds.height -= shift.y;
                        break;
                    }
                    case northEast:
                    {
                        newBounds.width += shift.x;
                        newBounds.y += shift.y;
                        newBounds.height -= shift.y;
                        break;
                    }
                    case west:
                    {
                        newBounds.x += shift.x;
                        newBounds.width -= shift.x;
                        break;
                    }
                    case east:
                    {
                        newBounds.width += shift.x;
                        break;
                    }
                    case southWest:
                    {
                        newBounds.x += shift.x;
                        newBounds.width -= shift.x;
                        newBounds.height += shift.y;
                        break;
                    }
                    case south:
                    {
                        newBounds.height += shift.y;
                        break;
                    }
                    case southEast:
                    default:
                    {
                        newBounds.width += shift.x;
                        newBounds.height += shift.y;
                        break;
                    }
                }

                // Correcting resulting bounds with minimum values
                if ( undecorated )
                {
                    if ( newBounds.width < ms.width )
                    {
                        final int diff = ms.width - newBounds.width;
                        if ( currentDirection == CompassDirection.northWest || currentDirection == CompassDirection.west ||
                                currentDirection == CompassDirection.southWest )
                        {
                            newBounds.x -= diff;
                        }
                        newBounds.width += diff;
                    }
                    if ( newBounds.height < ms.height )
                    {
                        final int diff = ms.height - newBounds.height;
                        if ( currentDirection == CompassDirection.northWest || currentDirection == CompassDirection.north ||
                                currentDirection == CompassDirection.northEast )
                        {
                            newBounds.y -= diff;
                        }
                        newBounds.height += diff;
                    }
                }

                // Updating resized component bounds
                // We don't need to check whether they have changed here since it is done inside this call
                resized.setBounds ( newBounds );
            }

            // Consume event
            e.consume ();
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( !e.isConsumed () && SwingUtilities.isLeftMouseButton ( e ) && resizing )
        {
            final int cursor = getCursor ( e.getPoint () );
            if ( cursor != -1 )
            {
                e.getComponent ().setCursor ( Cursor.getPredefinedCursor ( cursor ) );
            }
            else if ( initialCursor != null )
            {
                e.getComponent ().setCursor ( initialCursor );
                initialCursor = null;
            }

            resizing = false;
            initialPoint = null;
            initialBounds = null;
            currentDirection = null;

            // Consume event
            e.consume ();
        }
    }

    /**
     * Returns {@link Component} currently being resized.
     *
     * @param e {@link MouseEvent}
     * @return {@link Component} currently being resized
     */
    protected Component getResized ( final MouseEvent e )
    {
        return target != null ? target : CoreSwingUtils.getWindowAncestor ( e.getComponent () );
    }

    /**
     * Returns resize cursor for the mouse position.
     *
     * @param position mouse position
     * @return resize cursor for the mouse position
     */
    protected int getCursor ( final Point position )
    {
        final CompassDirection d = direction.apply ( position );
        if ( d != null )
        {
            switch ( d )
            {
                case northWest:
                    return Cursor.NW_RESIZE_CURSOR;

                case north:
                    return Cursor.N_RESIZE_CURSOR;

                case northEast:
                    return Cursor.NE_RESIZE_CURSOR;

                case west:
                    return Cursor.W_RESIZE_CURSOR;

                case east:
                    return Cursor.E_RESIZE_CURSOR;

                case southWest:
                    return Cursor.SW_RESIZE_CURSOR;

                case south:
                    return Cursor.S_RESIZE_CURSOR;

                case southEast:
                    return Cursor.SE_RESIZE_CURSOR;

                case center:
                    return Cursor.MOVE_CURSOR;

                default:
                    return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    /**
     * {@link Function} providing single resize {@link CompassDirection}.
     */
    public static class SingleResizeDirection implements Function<Point, CompassDirection>
    {
        /**
         * Resize {@link CompassDirection}.
         */
        private final CompassDirection direction;

        /**
         * Constructs new {@link SingleResizeDirection}.
         *
         * @param direction resize {@link CompassDirection}
         */
        public SingleResizeDirection ( final CompassDirection direction )
        {
            super ();
            this.direction = direction;
        }

        @Override
        public CompassDirection apply ( final Point point )
        {
            return direction;
        }
    }
}