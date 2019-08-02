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
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 * @author Mikle Garin
 */
public class ZigzagStroke implements Stroke
{
    private float amplitude = 10.0f;
    private float wavelength = 10.0f;
    private final Stroke stroke;
    private static final float FLATNESS = 1;

    public ZigzagStroke ( final float amplitude, final float wavelength )
    {
        this ( new BasicStroke ( 1f ), amplitude, wavelength );
    }

    public ZigzagStroke ( final Stroke stroke, final float amplitude, final float wavelength )
    {
        this.stroke = stroke;
        this.amplitude = amplitude;
        this.wavelength = wavelength;
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
        int phase = 0;

        while ( !it.isDone () )
        {
            type = it.currentSegment ( points );
            switch ( type )
            {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = points[ 0 ];
                    moveY = lastY = points[ 1 ];
                    result.moveTo ( moveX, moveY );
                    next = wavelength / 2;
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
                        while ( distance >= next )
                        {
                            final float x = lastX + next * dx * r;
                            final float y = lastY + next * dy * r;
                            if ( ( phase & 1 ) == 0 )
                            {
                                result.lineTo ( x + amplitude * dy * r, y - amplitude * dx * r );
                            }
                            else
                            {
                                result.lineTo ( x - amplitude * dy * r, y + amplitude * dx * r );
                            }
                            next += wavelength;
                            phase++;
                        }
                    }
                    next -= distance;
                    lastX = thisX;
                    lastY = thisY;
                    if ( type == PathIterator.SEG_CLOSE )
                    {
                        result.closePath ();
                    }
                    break;
            }
            it.next ();
        }

        return stroke.createStrokedShape ( result );
    }
}