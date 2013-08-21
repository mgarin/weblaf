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

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 02.11.11 Time: 15:59
 */

public class ComponentMoveAdapter extends MouseAdapter
{
    // todo Works incorrect on Unix systems

    protected Component toDrag;

    protected boolean dragging = false;
    protected Component dragged = null;
    protected Point initialPoint = null;
    protected Rectangle initialBounds = null;

    public static void install ( Component component )
    {
        install ( component, null );
    }

    public static void install ( Component component, Component toDrag )
    {
        ComponentMoveAdapter wma = new ComponentMoveAdapter ( toDrag );
        component.addMouseListener ( wma );
        component.addMouseMotionListener ( wma );
    }

    public ComponentMoveAdapter ()
    {
        this ( null );
    }

    public ComponentMoveAdapter ( Component toDrag )
    {
        super ();
        this.toDrag = toDrag;
    }

    @Override
    public void mousePressed ( MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            dragged = getDraggedComponent ( e );
            if ( dragged != null )
            {
                dragging = true;
                initialPoint = MouseInfo.getPointerInfo ().getLocation ();
                initialBounds = dragged.getBounds ();
            }
        }
    }

    private Component getDraggedComponent ( MouseEvent e )
    {
        return toDrag == null ? SwingUtils.getWindowAncestor ( e.getComponent () ) : toDrag;
    }

    @Override
    public void mouseDragged ( MouseEvent e )
    {
        if ( dragging )
        {
            Point point = MouseInfo.getPointerInfo ().getLocation ();
            dragged.setLocation ( initialBounds.x + ( point.x - initialPoint.x ), initialBounds.y + ( point.y - initialPoint.y ) );
        }
    }

    @Override
    public void mouseReleased ( MouseEvent e )
    {
        dragging = false;
        dragged = null;
        initialPoint = null;
        initialBounds = null;
    }
}