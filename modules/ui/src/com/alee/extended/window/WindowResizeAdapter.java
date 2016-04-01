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
 * @author Mikle Garin
 */

public class WindowResizeAdapter extends MouseAdapter implements SwingConstants
{
    private boolean resizing = false;
    private int prevX = -1;
    private int prevY = -1;

    private int resizeSide = 0;

    public static void install ( final Component component, final int resizeSide )
    {
        final WindowResizeAdapter wra = new WindowResizeAdapter ( resizeSide );
        component.addMouseListener ( wra );
        component.addMouseMotionListener ( wra );
    }

    public WindowResizeAdapter ( final int resizeSide )
    {
        super ();
        this.resizeSide = resizeSide;
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            resizing = true;
        }
        prevX = e.getXOnScreen ();
        prevY = e.getYOnScreen ();
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( prevX != -1 && prevY != -1 && resizing )
        {
            final Window w = SwingUtils.getWindowAncestor ( e.getComponent () );
            final Rectangle rect = w.getBounds ();

            final Dimension dim;
            final boolean undecorated;
            if ( w instanceof JDialog )
            {
                dim = ( ( JDialog ) w ).getRootPane ().getMinimumSize ();
                undecorated = ( ( JDialog ) w ).isUndecorated ();
            }
            else if ( w instanceof JFrame )
            {
                dim = ( ( JFrame ) w ).getRootPane ().getMinimumSize ();
                undecorated = ( ( JFrame ) w ).isUndecorated ();
            }
            else if ( w instanceof JWindow )
            {
                dim = ( ( JWindow ) w ).getRootPane ().getMinimumSize ();
                undecorated = true;
            }
            else
            {
                dim = w.getMinimumSize ();
                undecorated = true;
            }

            // Checking for minimal width and height
            int xInc = e.getXOnScreen () - prevX;
            int yInc = e.getYOnScreen () - prevY;
            if ( undecorated )
            {
                if ( resizeSide == SwingConstants.NORTH_WEST || resizeSide == SwingConstants.WEST ||
                        resizeSide == SwingConstants.SOUTH_WEST )
                {
                    if ( rect.width - xInc < dim.width )
                    {
                        xInc = 0;
                    }
                }
                else if ( resizeSide == SwingConstants.NORTH_EAST ||
                        resizeSide == SwingConstants.EAST ||
                        resizeSide == SwingConstants.SOUTH_EAST )
                {
                    if ( rect.width + xInc < dim.width )
                    {
                        xInc = 0;
                    }
                }
                if ( resizeSide == SwingConstants.NORTH_WEST ||
                        resizeSide == SwingConstants.NORTH ||
                        resizeSide == SwingConstants.NORTH_EAST )
                {
                    if ( rect.height - yInc < dim.height )
                    {
                        yInc = 0;
                    }
                }
                else if ( resizeSide == SwingConstants.SOUTH_WEST ||
                        resizeSide == SwingConstants.SOUTH ||
                        resizeSide == SwingConstants.SOUTH_EAST )
                {
                    if ( rect.height + yInc < dim.height )
                    {
                        yInc = 0;
                    }
                }
            }

            // Resizing the winow if any changes were done
            if ( xInc != 0 || yInc != 0 )
            {
                if ( resizeSide == SwingConstants.NORTH_WEST )
                {
                    w.setBounds ( rect.x + xInc, rect.y + yInc, rect.width - xInc, rect.height - yInc );
                }
                else if ( resizeSide == SwingConstants.NORTH )
                {
                    w.setBounds ( rect.x, rect.y + yInc, rect.width, rect.height - yInc );
                }
                else if ( resizeSide == SwingConstants.NORTH_EAST )
                {
                    w.setBounds ( rect.x, rect.y + yInc, rect.width + xInc, rect.height - yInc );
                }
                else if ( resizeSide == SwingConstants.WEST )
                {
                    w.setBounds ( rect.x + xInc, rect.y, rect.width - xInc, rect.height );
                }
                else if ( resizeSide == SwingConstants.EAST )
                {
                    w.setBounds ( rect.x, rect.y, rect.width + xInc, rect.height );
                }
                else if ( resizeSide == SwingConstants.SOUTH_WEST )
                {
                    w.setBounds ( rect.x + xInc, rect.y, rect.width - xInc, rect.height + yInc );
                }
                else if ( resizeSide == SwingConstants.SOUTH )
                {
                    w.setBounds ( rect.x, rect.y, rect.width, rect.height + yInc );
                }
                else if ( resizeSide == SwingConstants.SOUTH_EAST )
                {
                    w.setBounds ( rect.x, rect.y, rect.width + xInc, rect.height + yInc );
                }
                prevX = e.getXOnScreen ();
                prevY = e.getYOnScreen ();
            }
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        resizing = false;
    }
}
