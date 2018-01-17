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
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom {@link Behavior} that allows you to easily move window/component.
 * Install this behavior onto any component to make it move the window when dragged.
 * You can specify moved component or window, or simply let the behavior detect it.
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 *
 * @author Mikle Garin
 */

public class ComponentMoveBehavior extends MouseAdapter implements Behavior
{
    /**
     * todo 1. Fix incorrect behavior on Unix systems
     */

    /**
     * Component that acts as gripper.
     */
    protected final Component gripper;

    /**
     * Component that should be dragged.
     * If set to null mouse events source component parent window will be dragged instead.
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
    protected Rectangle initialBounds = null;

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
        if ( e.getSource () == gripper && isEnabled () && gripper.isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
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
        if ( dragging )
        {
            final Point mouse = CoreSwingUtils.getMouseLocation ();
            final int x = initialBounds.x + mouse.x - initialPoint.x;
            final int y = initialBounds.y + mouse.y - initialPoint.y;
            final Point location = new Point ( x, y );
            dragged.setLocation ( location );
            componentMoved ( mouse, location );
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( dragging )
        {
            final Point mouse = CoreSwingUtils.getMouseLocation ();
            final Point location = dragged.getLocation ();
            dragging = false;
            dragged = null;
            initialPoint = null;
            initialBounds = null;
            componentMoveEnded ( mouse, location );
        }
    }

    /**
     * Informs about component move start.
     *
     * @param mouse    mouse location
     * @param location component location
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void componentMoveStarted ( final Point mouse, final Point location )
    {
        // Do nothing by default
    }

    /**
     * Informs about component being moved due to drag.
     *
     * @param mouse    new mouse location
     * @param location new component location
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void componentMoved ( final Point mouse, final Point location )
    {
        // Do nothing by default
    }

    /**
     * Informs about component move end.
     *
     * @param mouse    mouse location
     * @param location component location
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void componentMoveEnded ( final Point mouse, final Point location )
    {
        // Do nothing by default
    }

    /**
     * Returns actual dragged component.
     *
     * @param e occured mouse event
     * @return actual dragged component
     */
    protected Component getDraggedComponent ( final MouseEvent e )
    {
        return target == null ? SwingUtils.getWindowAncestor ( gripper ) : target;
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