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

import com.alee.managers.style.Bounds;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This behavior allows you to easily move window/component.
 * Simply install this behavior onto any component to make it move the window when dragged.
 * You can specify moved window/component or simply let the behavior detect it.
 * You can use {@link #install(java.awt.Component)} and {@link #uninstall(java.awt.Component)} methods for quick install and uninstall.
 *
 * @author Mikle Garin
 */

public class ComponentMoveBehavior extends MouseAdapter implements Behavior
{
    /**
     * todo 1. Fix incorrect behavior on Unix systems
     */

    /**
     * Component that should be dragged.
     * If set to null mouse events source component parent window will be dragged instead.
     */
    protected final Component target;

    /**
     * Whether or not this behavior is currently enabled.
     */
    protected boolean enabled = true;

    /**
     * Whether component is being dragged or not.
     */
    protected boolean dragging = false;

    /**
     * Currently dragged component.
     */
    protected Component dragged = null;

    /**
     * Drag start point.
     */
    protected Point initialPoint = null;

    /**
     * Dragged component initial bounds.
     */
    protected Rectangle initialBounds = null;

    /**
     * Constructs new component move adapter that alows source component parent window dragging.
     */
    public ComponentMoveBehavior ()
    {
        this ( null );
    }

    /**
     * Constructs new component move adapter that allows specified component dragging.
     *
     * @param target component to drag
     */
    public ComponentMoveBehavior ( final Component target )
    {
        super ();
        this.target = target;
    }

    /**
     * Returns whether or not this behavior is currently enabled.
     *
     * @return true if this behavior is currently enabled, false otherwise
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
        if ( isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
        {
            if ( e.getSource () instanceof Component && ( ( Component ) e.getSource () ).isEnabled () )
            {
                dragged = getDraggedComponent ( e );
                if ( dragged != null )
                {
                    final Rectangle dragStartBounds = getDragStartBounds ( e );
                    if ( dragStartBounds != null && dragStartBounds.contains ( e.getPoint () ) )
                    {
                        dragging = true;
                        initialPoint = MouseInfo.getPointerInfo ().getLocation ();
                        initialBounds = dragged.getBounds ();
                    }
                    else
                    {
                        dragged = null;
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( dragging )
        {
            final Point point = MouseInfo.getPointerInfo ().getLocation ();
            dragged.setLocation ( initialBounds.x + ( point.x - initialPoint.x ), initialBounds.y + ( point.y - initialPoint.y ) );
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( dragging )
        {
            dragging = false;
            dragged = null;
            initialPoint = null;
            initialBounds = null;
        }
    }

    /**
     * Returns actual dragged component.
     *
     * @param e occured mouse event
     * @return actual dragged component
     */
    protected Component getDraggedComponent ( final MouseEvent e )
    {
        return target == null ? SwingUtils.getWindowAncestor ( e.getComponent () ) : target;
    }

    /**
     * Returns bounds within which component will act as a gripper.
     *
     * @param e occured mouse event
     * @return bounds within which component will act as a gripper
     */
    protected Rectangle getDragStartBounds ( final MouseEvent e )
    {
        return Bounds.margin.of ( e.getComponent () );
    }

    /**
     * Installs window move adapter to the specified window component.
     *
     * @param gripper window component that will act as gripper
     * @return installed behavior
     */
    public static ComponentMoveBehavior install ( final Component gripper )
    {
        return install ( gripper, null );
    }

    /**
     * Installs behavior to the specified component.
     *
     * @param gripper component that will act as gripper
     * @param target  component to be moved by the gripper component
     * @return installed behavior
     */
    public static ComponentMoveBehavior install ( final Component gripper, final Component target )
    {
        // Uninstall old behavior first
        uninstall ( gripper );

        // Add new behavior
        final ComponentMoveBehavior behavior = new ComponentMoveBehavior ( target );
        gripper.addMouseListener ( behavior );
        gripper.addMouseMotionListener ( behavior );
        return behavior;
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
            if ( listener instanceof ComponentMoveBehavior )
            {
                gripper.removeMouseListener ( listener );
            }
        }
        for ( final MouseMotionListener listener : gripper.getMouseMotionListeners () )
        {
            if ( listener instanceof ComponentMoveBehavior )
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
            if ( listener instanceof ComponentMoveBehavior )
            {
                return true;
            }
        }
        return false;
    }
}