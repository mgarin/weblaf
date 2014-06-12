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
 * User: mgarin Date: 13.06.11 Time: 15:50
 */

public class RandomStroke implements Stroke
{
    private float detail = 2;
    private float amplitude = 2;
    private static final float FLATNESS = 1;

    public RandomStroke ( float detail, float amplitude )
    {
        this.detail = detail;
        this.amplitude = amplitude;
    }

    @Override
    public Shape createStrokedShape ( Shape shape )
    {
        GeneralPath result = new GeneralPath ();
        shape = new BasicStroke ( 10 ).createStrokedShape ( shape );
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

        while ( !it.isDone () )
        {
            type = it.currentSegment ( points );
            switch ( type )
            {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = randomize ( points[ 0 ] );
                    moveY = lastY = randomize ( points[ 1 ] );
                    result.moveTo ( moveX, moveY );
                    next = 0;
                    break;

                case PathIterator.SEG_CLOSE:
                    points[ 0 ] = moveX;
                    points[ 1 ] = moveY;
                    // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = randomize ( points[ 0 ] );
                    thisY = randomize ( points[ 1 ] );
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
                            result.lineTo ( randomize ( x ), randomize ( y ) );
                            next += detail;
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

    private float randomize ( float x )
    {
        return x + ( float ) Math.random () * amplitude * 2 - 1;
    }
}

