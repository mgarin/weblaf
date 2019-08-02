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

package com.alee.graphics.strokes;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author Mikle Garin
 */
public class ShapeStroke implements Stroke
{
    private final Shape shapes[];
    private final float advance;
    private final boolean repeat = true;
    private final AffineTransform t = new AffineTransform ();
    private static final float FLATNESS = 1;

    public ShapeStroke ()
    {
        this ( new Shape[]{ new Ellipse2D.Float ( 0, 0, 4, 4 ) }, 8f );
    }

    public ShapeStroke ( final Shape shapes, final float advance )
    {
        this ( new Shape[]{ shapes }, advance );
    }

    public ShapeStroke ( final Shape[] shapes, final float advance )
    {
        this.advance = advance;
        this.shapes = new Shape[ shapes.length ];

        for ( int i = 0; i < this.shapes.length; i++ )
        {
            final Rectangle2D bounds = shapes[ i ].getBounds2D ();
            t.setToTranslation ( -bounds.getCenterX (), -bounds.getCenterY () );
            this.shapes[ i ] = t.createTransformedShape ( shapes[ i ] );
        }
    }

    @Override
    public Shape createStrokedShape ( final Shape shape )
    {
        final GeneralPath result = new GeneralPath ();
        final PathIterator it = new FlatteningPathIterator ( shape.getPathIterator ( null ), FLATNESS );
        final float[] points = new float[ 6 ];
        float moveX = 0;
        float moveY = 0;
        float lastX = 0;
        float lastY = 0;
        float thisX;
        float thisY;
        int type;
        float next = 0;
        int currentShape = 0;
        final int length = shapes.length;

        while ( currentShape < length && !it.isDone () )
        {
            type = it.currentSegment ( points );
            switch ( type )
            {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = points[ 0 ];
                    moveY = lastY = points[ 1 ];
                    result.moveTo ( moveX, moveY );
                    next = 0;
                    break;

                case PathIterator.SEG_CLOSE:
                    points[ 0 ] = moveX;
                    points[ 1 ] = moveY;
                    // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = points[ 0 ];
                    thisY = points[ 1 ];
                    final float dx = thisX - lastX;
                    final float dy = thisY - lastY;
                    final float distance = ( float ) Math.sqrt ( dx * dx + dy * dy );
                    if ( distance >= next )
                    {
                        final float r = 1.0f / distance;
                        final float angle = ( float ) Math.atan2 ( dy, dx );
                        while ( currentShape < length && distance >= next )
                        {
                            final float x = lastX + next * dx * r;
                            final float y = lastY + next * dy * r;
                            t.setToTranslation ( x, y );
                            t.rotate ( angle );
                            result.append ( t.createTransformedShape ( shapes[ currentShape ] ), false );
                            next += advance;
                            currentShape++;
                            if ( repeat )
                            {
                                currentShape %= length;
                            }
                        }
                    }
                    next -= distance;
                    lastX = thisX;
                    lastY = thisY;
                    break;
            }
            it.next ();
        }
        return result;
    }
}