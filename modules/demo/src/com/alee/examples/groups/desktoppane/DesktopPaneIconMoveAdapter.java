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

package com.alee.examples.groups.desktoppane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 09.09.11 Time: 19:04
 */

public class DesktopPaneIconMoveAdapter extends MouseAdapter
{
    public static final String DRAGGED_MARK = "was.dragged";

    private boolean dragging = false;
    private Point startPoint = null;
    private Rectangle startBounds = null;

    @Override
    public void mousePressed ( MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            dragging = true;
            startPoint = e.getLocationOnScreen ();
            startBounds = e.getComponent ().getBounds ();
        }
    }

    @Override
    public void mouseDragged ( MouseEvent e )
    {
        if ( dragging )
        {
            e.getComponent ().setBounds ( new Rectangle ( startBounds.x + e.getLocationOnScreen ().x - startPoint.x,
                    startBounds.y + e.getLocationOnScreen ().y - startPoint.y, startBounds.width, startBounds.height ) );
            if ( e.getComponent () instanceof JComponent )
            {
                ( ( JComponent ) e.getComponent () ).putClientProperty ( DRAGGED_MARK, true );
            }
        }
    }

    @Override
    public void mouseReleased ( MouseEvent e )
    {
        if ( SwingUtilities.isLeftMouseButton ( e ) && dragging )
        {
            Rectangle bounds = e.getComponent ().getBounds ();

            Container parent = e.getComponent ().getParent ();
            boolean setBounds = false;
            for ( int i = 25; i < parent.getWidth (); i += 125 )
            {
                for ( int j = 25; j < parent.getHeight (); j += 100 )
                {
                    Rectangle cell = new Rectangle ( i, j, 100, 75 );
                    if ( cell.intersects ( bounds ) )
                    {
                        Rectangle intersection = cell.intersection ( bounds );
                        if ( intersection.width * intersection.height >= bounds.width * bounds.height / 8 )
                        {
                            e.getComponent ().setBounds ( cell );
                            setBounds = true;
                            break;
                        }
                    }
                }
                if ( setBounds )
                {
                    break;
                }
            }

            if ( e.getComponent () instanceof JComponent )
            {
                ( ( JComponent ) e.getComponent () ).putClientProperty ( DRAGGED_MARK, null );
            }
            dragging = false;
        }
    }
}
