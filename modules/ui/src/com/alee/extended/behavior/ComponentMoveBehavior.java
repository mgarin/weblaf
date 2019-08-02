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

import com.alee.managers.style.BoundsType;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This {@link Behavior} enables dragging {@link Component} or {@link Window} around using {@link #gripper}.
 * You can explicitely specify {@link #target} {@link Component} or {@link Window} to be dragged.
 * Otherwise behavior will use {@link #gripper}'s ancestor {@link Window}.
 *
 * This is basically simplified version of {@link ComponentResizeBehavior} as that one also allows dragging {@link #target}.
 * Although this behavior is more flavorful and convenient to use when dragging is the only thing required.
 *
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 * You don't need to explicitely store this behavior if your only intent is to install it once and keep forever.
 * Although if you want to uninstall this behavior at some point you might want to keep its instance.
 *
 * @author Mikle Garin
 * @see #install()
 * @see #uninstall()
 */
public class ComponentMoveBehavior extends MouseAdapter implements Behavior
{
    /**
     * todo 1. Fix incorrect behavior on Unix systems
     */

    /**
     * {@link Component} that controls movement.
     */
    protected final Component gripper;

    /**
     * {@link Component} that is dragged using this behavior.
     * If set to {@code null} - {@link #gripper}'s parent {@link Window} will be used instead.
     */
    protected final Component target;

    /**
     * Whether or not this behavior is currently enabled.
     */
    protected boolean enabled;

    /**
     * Whether component is being dragged or not.
     */
    protected boolean dragging;

    /**
     * Currently dragged component.
     */
    protected Component dragged;

    /**
     * Drag start point.
     */
    protected Point initialPoint;

    /**
     * Dragged component initial bounds.
     */
    protected Rectangle initialBounds;

    /**
     * Constructs new component move adapter that alows source component parent window dragging.
     *
     * @param gripper component that will act as gripper
     */
    public ComponentMoveBehavior ( final Component gripper )
    {
        this ( gripper, null );
    }

    /**
     * Constructs new component move adapter that allows specified component dragging.
     *
     * @param gripper component that will act as gripper
     * @param target  component to be moved by the gripper component
     */
    public ComponentMoveBehavior ( final Component gripper, final Component target )
    {
        super ();
        this.gripper = gripper;
        this.target = target;
        this.enabled = true;
        this.dragging = false;
        this.dragged = null;
        this.initialPoint = null;
        this.initialBounds = null;
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        gripper.addMouseListener ( this );
        gripper.addMouseMotionListener ( this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        gripper.removeMouseMotionListener ( this );
        gripper.removeMouseListener ( this );
    }

    /**
     * Returns whether or not this behavior is currently enabled.
     *
     * @return {@code true} if this behavior is currently enabled, {@code false} otherwise
     */
    public boolean isEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether or not this behavior is currently enabled.
     *
     * @param enabled whether or not this behavior is currently enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( !e.isConsumed () && SwingUtilities.isLeftMouseButton ( e ) &&
                e.getSource () == gripper && isEnabled () && gripper.isEnabled () )
        {
            dragged = getDraggedComponent ( e );
            if ( dragged != null )
            {
                final Rectangle dragStartBounds = getDragStartBounds ( e );
                if ( dragStartBounds != null && dragStartBounds.contains ( e.getPoint () ) )
                {
                    dragging = true;
                    initialPoint = CoreSwingUtils.getMouseLocation ();
                    initialBounds = dragged.getBounds ();
                    componentMoveStarted ( initialPoint, initialBounds.getLocation () );

                    // Consume event
                    e.consume ();
                }
                else
                {
                    dragged = null;
                }
            }
        }
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( !e.isConsumed () && SwingUtilities.isLeftMouseButton ( e ) && dragging )
        {
            final Point mouse = CoreSwingUtils.getMouseLocation ();
            final int x = initialBounds.x + mouse.x - initialPoint.x;
            final int y = initialBounds.y + mouse.y - initialPoint.y;
            final Point location = new Point ( x, y );
            dragged.setLocation ( location );
            componentMoved ( mouse, location );

            // Consume event
            e.consume ();
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( !e.isConsumed () && SwingUtilities.isLeftMouseButton ( e ) && dragging )
        {
            final Point mouse = CoreSwingUtils.getMouseLocation ();
            final Point location = dragged.getLocation ();
            dragging = false;
            dragged = null;
            initialPoint = null;
            initialBounds = null;
            componentMoveEnded ( mouse, location );

            // Consume event
            e.consume ();
        }
    }

    /**
     * Informs about component move start.
     *
     * @param mouse    mouse location
     * @param location component location
     */
    protected void componentMoveStarted ( final Point mouse, final Point location )
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Informs about component being moved due to drag.
     *
     * @param mouse    new mouse location
     * @param location new component location
     */
    protected void componentMoved ( final Point mouse, final Point location )
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Informs about component move end.
     *
     * @param mouse    mouse location
     * @param location component location
     */
    protected void componentMoveEnded ( final Point mouse, final Point location )
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Returns actual dragged component.
     *
     * @param e occured mouse event
     * @return actual dragged component
     */
    protected Component getDraggedComponent ( final MouseEvent e )
    {
        return target != null ? target : CoreSwingUtils.getWindowAncestor ( gripper );
    }

    /**
     * Returns bounds within which component will act as a gripper.
     *
     * @param e occured mouse event
     * @return bounds within which component will act as a gripper
     */
    protected Rectangle getDragStartBounds ( final MouseEvent e )
    {
        return BoundsType.margin.bounds ( gripper );
    }
}