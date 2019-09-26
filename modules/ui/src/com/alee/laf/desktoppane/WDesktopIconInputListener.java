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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.laf.AbstractUIInputListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;

/**
 * Basic UI input listener for {@link javax.swing.JInternalFrame.JDesktopIcon} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicDesktopIconUI} but cleaned up and optimized.
 *
 * @param <C> {@link javax.swing.JInternalFrame.JDesktopIcon} type
 * @param <U> {@link WDesktopIconUI} type
 * @author Steve Wilson
 * @author Mikle Garin
 */
public class WDesktopIconInputListener<C extends JInternalFrame.JDesktopIcon, U extends WDesktopIconUI<C>>
        extends AbstractUIInputListener<C, U> implements DesktopIconInputListener<C>, MouseListener, MouseMotionListener
{
    /**
     * Mouse X location in absolute coordinate system.
     */
    protected int absX;

    /**
     * Mouse Y location in absolute coordinate system.
     */
    protected int absY;

    /**
     * Mouse X location in source view's coordinate system.
     */
    protected int relX;

    /**
     * Mouse Y location in source view's coordinate system.
     */
    protected int relY;

    /**
     * Starting {@link JInternalFrame} bounds.
     */
    protected Rectangle startingBounds;

    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addMouseListener ( this );
        component.addMouseMotionListener ( this );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling listeners
        component.removeMouseMotionListener ( this );
        component.removeMouseListener ( this );

        super.uninstall ( component );
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        final Point p = SwingUtilities.convertPoint ( ( Component ) e.getSource (), e.getX (), e.getY (), null );
        relX = e.getX ();
        relY = e.getY ();
        absX = p.x;
        absY = p.y;
        startingBounds = component.getBounds ();

        final JDesktopPane desktop = component.getDesktopPane ();
        if ( desktop != null )
        {
            final DesktopManager dm = desktop.getDesktopManager ();
            dm.beginDraggingFrame ( component );
        }

        try
        {
            component.getInternalFrame ().setSelected ( true );
        }
        catch ( final PropertyVetoException ignored )
        {
        }

        if ( component.getParent () instanceof JLayeredPane )
        {
            final JLayeredPane layeredPane = ( JLayeredPane ) component.getParent ();
            layeredPane.moveToFront ( component );
        }

        if ( e.getClickCount () > 1 )
        {
            final JInternalFrame internalFrame = component.getInternalFrame ();
            if ( internalFrame.isIconifiable () && internalFrame.isIcon () )
            {
                try
                {
                    internalFrame.setIcon ( false );
                }
                catch ( final PropertyVetoException ignored )
                {
                }
            }
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        absX = 0;
        absY = 0;
        relX = 0;
        relY = 0;
        startingBounds = null;

        final JDesktopPane desktop = component.getDesktopPane ();
        if ( desktop != null )
        {
            final DesktopManager dm = desktop.getDesktopManager ();
            dm.endDraggingFrame ( component );
        }
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        // (STEVE) Yucky work around for bug ID 4106552
        if ( startingBounds != null )
        {
            final Point p = SwingUtilities.convertPoint ( ( Component ) e.getSource (), e.getX (), e.getY (), null );

            final Insets i = component.getInsets ();
            final int pWidth = component.getParent ().getWidth ();
            final int pHeight = component.getParent ().getHeight ();

            int newX = startingBounds.x - ( absX - p.x );
            int newY = startingBounds.y - ( absY - p.y );

            // Make sure we stay in-bounds
            if ( newX + i.left <= -relX )
            {
                newX = -relX - i.left;
            }
            if ( newY + i.top <= -relY )
            {
                newY = -relY - i.top;
            }
            if ( newX + relX + i.right > pWidth )
            {
                newX = pWidth - relX - i.right;
            }
            if ( newY + relY + i.bottom > pHeight )
            {
                newY = pHeight - relY - i.bottom;
            }

            final JDesktopPane desktop = component.getDesktopPane ();
            if ( desktop != null )
            {
                final DesktopManager dm = desktop.getDesktopManager ();
                dm.dragFrame ( component, newX, newY );
            }
            else
            {
                final int newWidth = component.getWidth ();
                final int newHeight = component.getHeight ();
                final Rectangle bounds = component.getBounds ();
                component.setBounds ( newX, newY, newWidth, newHeight );
                SwingUtilities.computeUnion ( newX, newY, newWidth, newHeight, bounds );
                component.getParent ().repaint ( bounds.x, bounds.y, bounds.width, bounds.height );
            }
        }
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
    }
}