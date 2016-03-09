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

package com.alee.extended.window;

import com.alee.api.Behavior;
import com.alee.managers.style.Bounds;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This listener allows you to simplify window/component move action.
 * Simply install this listener onto any component to make it move the window when dragged.
 * You can specify moved window/component or simply let the listener detect it.
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
    protected Component toDrag;

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
     * @param toDrag component to drag
     */
    public ComponentMoveBehavior ( final Component toDrag )
    {
        super ();
        this.toDrag = toDrag;
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) )
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
        return toDrag == null ? SwingUtils.getWindowAncestor ( e.getComponent () ) : toDrag;
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
     * @param component window component that will act as gripper
     */
    public static void install ( final Component component )
    {
        install ( component, null );
    }

    /**
     * Installs behavior to the specified component.
     *
     * @param component component that will act as gripper
     * @param toDrag    component to be moved by the gripper component
     * @return installed behavior
     */
    public static ComponentMoveBehavior install ( final Component component, final Component toDrag )
    {
        // Uninstall old behavior first
        uninstall ( component );

        // Add new behavior
        final ComponentMoveBehavior behavior = new ComponentMoveBehavior ( toDrag );
        component.addMouseListener ( behavior );
        component.addMouseMotionListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls behavior from the specified gripper component.
     *
     * @param component gripper component
     */
    public static void uninstall ( final Component component )
    {
        for ( final MouseListener listener : component.getMouseListeners () )
        {
            if ( listener instanceof ComponentMoveBehavior )
            {
                component.removeMouseListener ( listener );
            }
        }
        for ( final MouseMotionListener listener : component.getMouseMotionListeners () )
        {
            if ( listener instanceof ComponentMoveBehavior )
            {
                component.removeMouseMotionListener ( listener );
            }
        }
    }

    /**
     * Returns whether or not the specified tree has this behavior installed.
     *
     * @param tree tree to process
     * @return true if the specified tree has this behavior installed, false otherwise
     */
    public static boolean isInstalled ( final JTree tree )
    {
        for ( final MouseMotionListener listener : tree.getMouseMotionListeners () )
        {
            if ( listener instanceof ComponentMoveBehavior )
            {
                return true;
            }
        }
        return false;
    }
}