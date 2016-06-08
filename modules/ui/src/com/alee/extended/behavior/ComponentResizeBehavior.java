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

import com.alee.api.jdk.Function;
import com.alee.api.data.CompassDirection;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This listener allows you to simplify window/component resize action.
 *
 * @author Mikle Garin
 */

public class ComponentResizeBehavior extends MouseAdapter implements Behavior, SwingConstants
{
    /**
     * Component that can be resized through this behavior.
     */
    protected final Component resized;

    /**
     * Function providing resize direction.
     * This way we could provide multiple resize directions for single control component.
     */
    protected Function<Point, CompassDirection> direction;

    /**
     * Whether or not component is being resized right now.
     */
    protected boolean resizing = false;

    /**
     * Resize starting point on the component.
     */
    protected Point initialPoint = null;

    /**
     * Initial component bounds.
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
     * Constructs new component resize behavior.
     *
     * @param resized   component that can be resized through this behavior
     * @param direction function providing resize direction
     */
    public ComponentResizeBehavior ( final Component resized, final Function<Point, CompassDirection> direction )
    {
        super ();
        this.direction = direction;
        this.resized = resized;
    }

    /**
     * Returns component currently being resized.
     *
     * @param e mouse event
     * @return component currently being resized
     */
    protected Component getResized ( final MouseEvent e )
    {
        return resized != null ? resized : SwingUtils.getWindowAncestor ( e.getComponent () );
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
        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            final CompassDirection d = direction.apply ( e.getPoint () );
            if ( d != null )
            {
                resizing = true;
                final Point los = e.getComponent ().getLocationOnScreen ();
                initialPoint = new Point ( los.x + e.getX (), los.y + e.getY () );
                initialBounds = getResized ( e ).getBounds ();
                currentDirection = d;
            }
        }
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

                default:
                    return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( resizing )
        {
            final Component resized = getResized ( e );

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
            final Point ml = MouseInfo.getPointerInfo ().getLocation ();
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
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) && resizing )
        {
            e.getComponent ().setCursor ( initialCursor );
            resizing = false;
            initialPoint = null;
            initialBounds = null;
            currentDirection = null;
            initialCursor = null;
        }
    }

    /**
     * Installs behavior onto the specified gripper component.
     *
     * @param gripper   component in control of the resize
     * @param direction resize direction
     * @return installed behavior
     */
    public static ComponentResizeBehavior install ( final Component gripper, final CompassDirection direction )
    {
        return install ( gripper, null, getSingleDirection ( direction ) );
    }

    /**
     * Installs behavior onto the specified gripper component.
     *
     * @param gripper   component in control of the resize
     * @param resized   resized component
     * @param direction resize direction
     * @return installed behavior
     */
    public static ComponentResizeBehavior install ( final Component gripper, final Component resized, final CompassDirection direction )
    {
        return install ( gripper, resized, getSingleDirection ( direction ) );
    }

    /**
     * Installs behavior onto the specified gripper component.
     *
     * @param gripper   component in control of the resize
     * @param direction function providing resize direction
     * @return installed behavior
     */
    public static ComponentResizeBehavior install ( final Component gripper, final Function<Point, CompassDirection> direction )
    {
        return install ( gripper, null, direction );
    }

    /**
     * Installs behavior onto the specified gripper component.
     *
     * @param gripper   component in control of the resize
     * @param resized   resized component
     * @param direction function providing resize direction
     * @return installed behavior
     */
    public static ComponentResizeBehavior install ( final Component gripper, final Component resized,
                                                    final Function<Point, CompassDirection> direction )
    {
        final ComponentResizeBehavior wra = new ComponentResizeBehavior ( resized, direction );
        gripper.addMouseListener ( wra );
        gripper.addMouseMotionListener ( wra );
        return wra;
    }

    /**
     * Returns function providing single resize direction.
     *
     * @param direction resize direction
     * @return function providing single resize direction
     */
    protected static Function<Point, CompassDirection> getSingleDirection ( final CompassDirection direction )
    {
        return new Function<Point, CompassDirection> ()
        {
            @Override
            public CompassDirection apply ( final Point point )
            {
                return direction;
            }
        };
    }

    /**
     * Uninstalls behavior from the specified gripper component.
     *
     * @param gripper gripper component
     */
    public static void uninstall ( final Component gripper )
    {
        for ( final MouseListener listener : gripper.getMouseListeners () )
        {
            if ( listener instanceof ComponentResizeBehavior )
            {
                gripper.removeMouseListener ( listener );
            }
        }
        for ( final MouseMotionListener listener : gripper.getMouseMotionListeners () )
        {
            if ( listener instanceof ComponentResizeBehavior )
            {
                gripper.removeMouseMotionListener ( listener );
            }
        }
    }

    /**
     * Returns whether or not the specified component has this behavior installed.
     *
     * @param gripper component that acts as gripper
     * @return true if the specified component has this behavior installed, false otherwise
     */
    public static boolean isInstalled ( final Component gripper )
    {
        for ( final MouseMotionListener listener : gripper.getMouseMotionListeners () )
        {
            if ( listener instanceof ComponentResizeBehavior )
            {
                return true;
            }
        }
        return false;
    }
}