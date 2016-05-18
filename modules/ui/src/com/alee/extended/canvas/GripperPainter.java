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

package com.alee.extended.canvas;

import com.alee.painter.AbstractPainter;
import com.alee.painter.decoration.states.CompassDirection;

import java.awt.*;

/**
 * Custom painter for gripper.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public class GripperPainter<E extends WebCanvas, U extends WebCanvasUI> extends AbstractPainter<E, U> implements ICanvasPainter<E, U>
{
    /**
     * Resize direction.
     */
    protected CompassDirection direction;

    /**
     * Grip element size.
     */
    protected Dimension gripSize;

    /**
     * Spacing between grip elements.
     */
    protected int gripSpacing;

    /**
     * Grip element color.
     */
    protected Color gripColor;

    /**
     * Grip element shadow color.
     */
    protected Color gripShadowColor;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        int x = bounds.x + gripSpacing;
        for ( int col = 0; col <= 2; col++ )
        {
            int y = bounds.y + gripSpacing;
            for ( int row = 0; row <= 2; row++ )
            {
                paintGrip ( g2d, col, row, x, y );
                y += gripSize.height * 1.5 + gripSpacing;
            }
            x += gripSize.width * 1.5 + gripSpacing;
        }
    }

    /**
     * Paints grip element at the specified column and row if it is necessary.
     *
     * @param g2d graphics context
     * @param col grip element column
     * @param row grip element row
     * @param x   grip element X coordinate
     * @param y   grip element Y coordinate
     */
    protected void paintGrip ( final Graphics2D g2d, final int col, final int row, final int x, final int y )
    {
        boolean paint = false;
        switch ( direction )
        {
            case northWest:
            {
                paint = row == 0 || row == 1 && col < 2 || row == 2 && col == 0;
                break;
            }
            case north:
            {
                paint = row == 0;
                break;
            }
            case northEast:
            {
                paint = row == 0 || row == 1 && col > 0 || row == 2 && col == 2;
                break;
            }
            case west:
            {
                paint = col == 0;
                break;
            }
            case center:
            {
                paint = true;
                break;
            }
            case east:
            {
                paint = col == 2;
                break;
            }
            case southWest:
            {
                paint = row == 0 && col == 0 || row == 1 && col < 2 || row == 2;
                break;
            }
            case south:
            {
                paint = row == 2;
                break;
            }
            case southEast:
            {
                paint = row == 0 && col == 2 || row == 1 && col > 0 || row == 2;
                break;
            }
        }
        if ( paint )
        {
            paintGrip ( g2d, x, y );
        }

    }

    /**
     * Paints grip element at the specified column and row.
     *
     * @param g2d graphics context
     * @param x   grip element X coordinate
     * @param y   grip element Y coordinate
     */
    protected void paintGrip ( final Graphics2D g2d, final int x, final int y )
    {
        final int w = Math.round ( gripSize.width * 1.5f );
        final int h = Math.round ( gripSize.height * 1.5f );
        g2d.setPaint ( gripShadowColor );
        g2d.fillRect ( x + w - gripSize.width, y + h - gripSize.height, gripSize.width, gripSize.height );
        g2d.setPaint ( gripColor );
        g2d.fillRect ( x, y, gripSize.width, gripSize.height );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        ps.width += gripSize.width * 1.5 * 3 + gripSpacing * 4;
        ps.height += gripSize.height * 1.5 * 3 + gripSpacing * 4;
        return ps;
    }
}