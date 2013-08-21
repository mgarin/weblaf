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
 * User: mgarin Date: 13.06.11 Time: 15:54
 */

public class ZigzagStroke implements Stroke
{
    private float amplitude = 10.0f;
    private float wavelength = 10.0f;
    private Stroke stroke;
    private static final float FLATNESS = 1;

    public ZigzagStroke ( float amplitude, float wavelength )
    {
        this ( new BasicStroke ( 1f ), amplitude, wavelength );
    }

    public ZigzagStroke ( Stroke stroke, float amplitude, float wavelength )
    {
        this.stroke = stroke;
        this.amplitude = amplitude;
        this.wavelength = wavelength;
    }

    @Override
    public Shape createStrokedShape ( Shape shape )
    {
        GeneralPath result = new GeneralPath ();
        PathIterator it = new FlatteningPathIterator ( shape.getPathIterator ( null ), FLATNESS );
        float points[] = new float[ 6 ];
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
                    float dx = thisX - lastX;
                    float dy = thisY - lastY;
                    float distance = ( float ) Math.sqrt ( dx * dx + dy * dy );
                    if ( distance >= next )
                    {
                        float r = 1.0f / distance;
                        while ( distance >= next )
                        {
                            float x = lastX + next * dx * r;
                            float y = lastY + next * dy * r;
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
