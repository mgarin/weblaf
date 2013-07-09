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

package com.alee.graphics.shapes;

import java.awt.*;

/**
 * User: mgarin Date: 17.06.11 Time: 16:17
 */

public class StarPolygon extends Polygon
{
    public StarPolygon ( int x, int y, int r, int innerR, int vertexCount )
    {
        this ( x, y, r, innerR, vertexCount, 0 );
    }

    public StarPolygon ( int x, int y, int r, int innerR, int vertexCount, double startAngle )
    {
        super ( getXCoordinates ( x, y, r, innerR, vertexCount, startAngle ), getYCoordinates ( x, y, r, innerR, vertexCount, startAngle ),
                vertexCount * 2 );
    }

    protected static int[] getXCoordinates ( int x, int y, int r, int innerR, int vertexCount, double startAngle )
    {
        int res[] = new int[ vertexCount * 2 ];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        double innerAngle = startAngle + Math.PI / vertexCount;
        for ( int i = 0; i < vertexCount; i++ )
        {
            res[ i * 2 ] = ( int ) Math.round ( r * Math.cos ( angle ) ) + x;
            angle += addAngle;
            res[ i * 2 + 1 ] = ( int ) Math.round ( innerR * Math.cos ( innerAngle ) ) + x;
            innerAngle += addAngle;
        }
        return res;
    }

    protected static int[] getYCoordinates ( int x, int y, int r, int innerR, int vertexCount, double startAngle )
    {
        int res[] = new int[ vertexCount * 2 ];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        double innerAngle = startAngle + Math.PI / vertexCount;
        for ( int i = 0; i < vertexCount; i++ )
        {
            res[ i * 2 ] = ( int ) Math.round ( r * Math.sin ( angle ) ) + y;
            angle += addAngle;
            res[ i * 2 + 1 ] = ( int ) Math.round ( innerR * Math.sin ( innerAngle ) ) + y;
            innerAngle += addAngle;
        }
        return res;
    }
}

