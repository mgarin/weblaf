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

package com.alee.utils;

import java.awt.*;
import java.util.List;

/**
 * This class provides a set of utilities to work with points and various shapes.
 *
 * @author Mikle Garin
 */

public final class GeometryUtils
{
    /**
     * Returns rectangle containing all specified points.
     *
     * @param points points to process
     * @return rectangle containing all specified points
     */
    public static Rectangle getContainingRect ( List<Point> points )
    {
        return points != null && points.size () > 0 ? getContainingRect ( points.toArray ( new Point[ points.size () ] ) ) : null;
    }

    /**
     * Returns rectangle containing all specified points.
     *
     * @param points points to process
     * @return rectangle containing all specified points
     */
    public static Rectangle getContainingRect ( Point... points )
    {
        if ( points != null && points.length > 0 )
        {
            Rectangle rect = new Rectangle ( points[ 0 ], new Dimension ( 0, 0 ) );
            int i = 1;
            while ( i < points.length )
            {
                Point p = points[ i ];
                if ( p.x < rect.x )
                {
                    int diff = rect.x - p.x;
                    rect.x = p.x;
                    rect.width += diff;
                }
                else if ( rect.x + rect.width < p.x )
                {
                    rect.width = p.x - rect.x;
                }
                if ( p.y < rect.y )
                {
                    int diff = rect.y - p.y;
                    rect.y = p.y;
                    rect.height += diff;
                }
                else if ( rect.y + rect.height < p.y )
                {
                    rect.height = p.y - rect.y;
                }
                i++;
            }
            if ( rect.width == 0 )
            {
                rect.width = 1;
            }
            if ( rect.height == 0 )
            {
                rect.height = 1;
            }
            return rect;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns rectangle containing all specified rectangles.
     *
     * @param rects rectangles to process
     * @return rectangle containing all specified rectangles
     */
    public static Rectangle getContainingRect ( Rectangle... rects )
    {
        if ( rects != null && rects.length > 0 )
        {
            Rectangle rect = rects[ 0 ];
            int i = 1;
            while ( i < rects.length )
            {
                rect = getContainingRect ( rect, rects[ i ] );
                i++;
            }
            return rect;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns rectangle containing two others.
     *
     * @param r1 first rectangle
     * @param r2 second rectangle
     * @return rectangle containing two others
     */
    public static Rectangle getContainingRect ( Rectangle r1, Rectangle r2 )
    {
        if ( r1 == null && r2 != null )
        {
            return r2;
        }
        else if ( r2 == null && r1 != null )
        {
            return r1;
        }
        else if ( r1 == null && r2 == null )
        {
            return new Rectangle ( 0, 0, 0, 0 );
        }

        int minX = Math.min ( r1.x, r2.x );
        int minY = Math.min ( r1.y, r2.y );
        int maxX = Math.max ( r1.x + r1.width, r2.x + r2.width );
        int maxY = Math.max ( r1.y + r1.height, r2.y + r2.height );
        return new Rectangle ( minX, minY, maxX - minX, maxY - minY );
    }

    /**
     * Returns valid rectangle with non-negative width and height.
     *
     * @param rect rectangle to validate
     * @return valid rectangle with non-negative width and height
     */
    public static Rectangle validateRect ( Rectangle rect )
    {
        if ( rect.width >= 0 && rect.height >= 0 )
        {
            return rect;
        }
        else
        {
            int x = rect.x;
            int width = Math.abs ( rect.width );
            if ( rect.width < 0 )
            {
                x = x - width;
            }
            int y = rect.y;
            int height = Math.abs ( rect.height );
            if ( rect.height < 0 )
            {
                y = y - height;
            }
            return new Rectangle ( x, y, width, height );
        }
    }

    /**
     * Returns middle point for the specified rectangle.
     *
     * @param rectangle rectangle to process
     * @return middle point for the specified rectangle
     */
    public static Point middle ( Rectangle rectangle )
    {
        return new Point ( rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2 );
    }

    /**
     * Returns middle point between the specified points.
     *
     * @param p1 first point
     * @param p2 second point
     * @return middle point between the specified points
     */
    public static Point middle ( Point p1, Point p2 )
    {
        return new Point ( ( p1.x + p2.x ) / 2, ( p1.y + p2.y ) / 2 );
    }

    /**
     * Returns middle point between the specified points.
     *
     * @param x1 first point X coordinate
     * @param y1 first point Y coordinate
     * @param x2 second point X coordinate
     * @param y2 second point Y coordinate
     * @return middle point between the specified points
     */
    public static Point middle ( int x1, int y1, int x2, int y2 )
    {
        return new Point ( ( x1 + x2 ) / 2, ( y1 + y2 ) / 2 );
    }

    /**
     * Returns rectangle expanded in four directions for the specified value.
     *
     * @param rect      rectangle to expand
     * @param expansion expansion
     * @return rectangle expanded in four directions for the specified values
     */
    public static Rectangle expand ( Rectangle rect, int expansion )
    {
        return expand ( rect, expansion, expansion, expansion, expansion );
    }

    /**
     * Returns rectangle expanded in four directions for the specified values.
     *
     * @param rect   rectangle to expand
     * @param top    top expansion
     * @param left   left expansion
     * @param bottom bottom expansion
     * @param right  right expansion
     * @return rectangle expanded in four directions for the specified values
     */
    public static Rectangle expand ( Rectangle rect, int top, int left, int bottom, int right )
    {
        return new Rectangle ( rect.x - left, rect.y - top, rect.width + left + right, rect.height + top + bottom );
    }

    /**
     * Returns modified point.
     *
     * @param point point to modify
     * @param xMod  X coordinate modifier
     * @param yMod  Y coordinate modifier
     * @return modified point
     */
    public static Point modify ( Point point, int xMod, int yMod )
    {
        return new Point ( point.x + xMod, point.y + yMod );
    }

    /**
     * Returns angle between the line specified by points and y=0 line.
     *
     * @param p1 first line point
     * @param p2 second line point
     * @return angle between the line specified by points and y=0 line
     */
    public static double getAngle ( Point p1, Point p2 )
    {
        return getAngle ( p1.x, p1.y, p2.x, p2.y );
    }

    /**
     * Returns angle between the line specified by points and y=0 line.
     *
     * @param x1 first point X coordinate
     * @param y1 first point Y coordinate
     * @param x2 second point X coordinate
     * @param y2 second point Y coordinate
     * @return angle between the line specified by points and y=0 line
     */
    public static double getAngle ( int x1, int y1, int x2, int y2 )
    {
        double angle = Math.asin ( ( y2 - y1 ) / Math.sqrt ( MathUtils.sqr ( x2 - x1 ) + MathUtils.sqr ( y2 - y1 ) ) );
        return x1 > x2 ? -angle - Math.PI : angle;
    }

    /**
     * Returns intersection point of the rectangle and the line goin from the middle of that rectangle to the outer point.
     *
     * @param rect  rectangle to process
     * @param outer outer point to process
     * @return intersection point of the rectangle and the line goin from the middle of that rectangle to the outer point
     */
    private Point findMiddleLineIntersection ( Rectangle rect, Point outer )
    {
        final Point middle = GeometryUtils.middle ( rect );
        final int x1 = middle.x;
        final int y1 = middle.y;
        final int x2 = outer.x;
        final int y2 = outer.y;
        if ( x2 < rect.x )
        {
            int x = rect.x;
            int y = ( x1 * ( y2 - y1 ) - y1 * ( x2 - x1 ) - x * ( y2 - y1 ) ) / ( x1 - x2 );
            if ( y >= rect.y && y <= rect.y + rect.height )
            {
                return new Point ( x, y );
            }
        }
        else if ( x2 > rect.x + rect.width )
        {
            int x = rect.x + rect.width;
            int y = ( x1 * ( y2 - y1 ) - y1 * ( x2 - x1 ) - x * ( y2 - y1 ) ) / ( x1 - x2 );
            if ( y >= rect.y && y <= rect.y + rect.height )
            {
                return new Point ( x, y );
            }
        }
        if ( y2 < rect.y )
        {
            int y = rect.y;
            int x = ( x1 * ( y2 - y1 ) - y1 * ( x2 - x1 ) - y * ( x1 - x2 ) ) / ( y2 - y1 );
            if ( x >= rect.x && x <= rect.x + rect.width )
            {
                return new Point ( x, y );
            }
        }
        else if ( y2 > rect.y + rect.height )
        {
            int y = rect.y + rect.height;
            int x = ( x1 * ( y2 - y1 ) - y1 * ( x2 - x1 ) - y * ( x1 - x2 ) ) / ( y2 - y1 );
            if ( x >= rect.x && x <= rect.x + rect.width )
            {
                return new Point ( x, y );
            }
        }
        return middle;
    }
}