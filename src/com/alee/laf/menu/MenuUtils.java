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

package com.alee.laf.menu;

import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 23.04.12 Time: 16:58
 */

public class MenuUtils implements SwingConstants
{
    public static void setupPopupMenu ( final JComponent component, final JPopupMenu menu )
    {
        setupPopupMenu ( component, menu, 0 );
    }

    public static void setupPopupMenu ( final JComponent component, final JPopupMenu menu, int spacing )
    {
        setupPopupMenu ( component, menu, spacing, RIGHT );
    }

    public static void setupPopupMenu ( final JComponent component, final JPopupMenu menu, int spacing, int hway )
    {
        setupPopupMenu ( component, menu, spacing, hway, SOUTH );
    }

    public static void setupPopupMenu ( final JComponent component, final JPopupMenu menu, final int spacing, final int hway,
                                        final int vway )
    {
        setupPopupMenu ( component, menu, new DataProvider<Point> ()
        {
            @Override
            public Point provide ()
            {
                Dimension ps = menu.getPreferredSize ();
                int x;
                int y;
                ShapeProvider shapeProvider = SwingUtils.getShapeProvider ( component );
                if ( shapeProvider != null )
                {
                    // Placing menu according to shape
                    Shape shape = shapeProvider.provideShape ();
                    Rectangle bounds = shape.getBounds ();
                    if ( hway == RIGHT )
                    {
                        x = bounds.x;
                    }
                    else if ( hway == LEFT )
                    {
                        x = bounds.x + bounds.width - ps.width;
                    }
                    else
                    {
                        x = bounds.x + bounds.width / 2 - ps.width / 2;
                    }
                    if ( vway == TOP )
                    {
                        y = bounds.y - ps.height - spacing;
                    }
                    else
                    {
                        y = bounds.y + bounds.height + spacing;
                    }
                }
                else
                {
                    // Placing menu according to size
                    Dimension cs = component.getSize ();
                    if ( hway == RIGHT )
                    {
                        x = 0;
                    }
                    else if ( hway == LEFT )
                    {
                        x = cs.width - ps.width;
                    }
                    else
                    {
                        x = cs.width / 2 - ps.width / 2;
                    }
                    if ( vway == TOP )
                    {
                        y = -ps.height - spacing;
                    }
                    else
                    {
                        y = cs.height + spacing;
                    }
                }
                return new Point ( x, y );
            }
        } );
    }

    public static void setupPopupMenu ( final JComponent component, final JPopupMenu menu, final DataProvider<Point> pointProvider )
    {
        if ( component instanceof AbstractButton )
        {
            AbstractButton button = ( AbstractButton ) component;
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( ActionEvent e )
                {
                    Point point = pointProvider.provide ();
                    menu.show ( component, point.x, point.y );
                }
            } );
        }
        else
        {
            component.addMouseListener ( new MouseAdapter ()
            {
                @Override
                public void mousePressed ( MouseEvent e )
                {
                    showMenu ( e );
                }

                @Override
                public void mouseReleased ( MouseEvent e )
                {
                    showMenu ( e );
                }

                private void showMenu ( MouseEvent e )
                {
                    if ( e.isPopupTrigger () )
                    {
                        if ( !component.isFocusOwner () )
                        {
                            component.requestFocusInWindow ();
                        }
                        Point point = pointProvider.provide ();
                        menu.show ( component, point.x, point.y );
                    }
                }
            } );
        }
    }
}