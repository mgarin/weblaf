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

package com.alee.examples.groups.desktoppane.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * User: mgarin Date: 02.06.11 Time: 17:09
 */

public class Block
{
    private static Color bgTop = Color.WHITE;
    private static Color bgBottom = new Color ( 223, 223, 223 );

    private String text = null;
    private ImageIcon icon = null;

    private int rotation;
    private Point blockPoint;

    public Block ()
    {
        super ();
    }

    public Block ( String text )
    {
        super ();
        this.text = text;
    }

    public Block ( ImageIcon icon )
    {
        super ();
        this.icon = icon;
    }

    public int getRotation ()
    {
        return rotation;
    }

    public void setRotation ( int rotation )
    {
        this.rotation = rotation;
    }

    public Point getBlockPoint ()
    {
        return blockPoint;
    }

    public void setBlockPoint ( Point blockPoint )
    {
        this.blockPoint = blockPoint;
    }

    public static Font blockFont = new Font ( "Comic Sans MS", Font.BOLD, 10 );

    public void paintBlock ( Graphics2D g2d, Rectangle rect, int rotation )
    {
        rect.x += 1;
        rect.y += 1;
        rect.width -= 1;
        rect.height -= 1;

        g2d.translate ( rect.x + rect.width / 2, rect.y + rect.height / 2 );
        if ( rotation != 0 )
        {
            g2d.rotate ( rotation * Math.PI / 2 );
        }

        // Position fix when rotating
        // 3 = x+1
        // 2 = x+1 y+1
        // 1 = y+1
        RoundRectangle2D rr = new RoundRectangle2D.Double ( -rect.width / 2 + ( rotation == 2 || rotation == 3 ? -1 : 0 ),
                -rect.height / 2 + ( rotation == 1 || rotation == 2 ? -1 : 0 ), rect.width, rect.height, 4, 4 );
        Rectangle bounds = rr.getBounds ();

        // Background
        g2d.setPaint ( new GradientPaint ( 0, bounds.y, bgTop, 0, bounds.y + bounds.height, bgBottom ) );
        g2d.fill ( rr );

        // Border
        g2d.setPaint ( Color.GRAY );
        g2d.draw ( rr );

        if ( text != null )
        {
            g2d.setFont ( blockFont );
            g2d.setPaint ( Color.BLACK );
            g2d.drawString ( text, bounds.x + bounds.width / 2 - g2d.getFontMetrics ().stringWidth ( text ) / 2 + 1,
                    bounds.y + bounds.height / 2 + g2d.getFontMetrics ().getAscent () / 2 + 1 );
        }
        else if ( icon != null )
        {
            g2d.drawImage ( icon.getImage (), bounds.x + bounds.width / 2 - icon.getIconWidth () / 2 + 1,
                    bounds.y + bounds.height / 2 - icon.getIconHeight () / 2 + 1, null );
        }

        if ( rotation != 0 )
        {
            g2d.rotate ( -rotation * Math.PI / 2 );
        }
        g2d.translate ( -( rect.x + rect.width / 2 ), -( rect.y + rect.height / 2 ) );
    }
}
