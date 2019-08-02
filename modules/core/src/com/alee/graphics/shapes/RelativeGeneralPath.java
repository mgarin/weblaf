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
import java.awt.geom.*;
import java.io.Serializable;

/**
 * {@link GeneralPath} decorator that adjusts all shape pathing according to specified bounds and orientation.
 * It is used to simplify shape construction code whenever bounds and/or orientation have to be taken into account
 *
 * @author Mikle Garin
 */
public class RelativeGeneralPath implements Shape, Cloneable, Serializable
{
    /**
     * Additional bounds.
     */
    protected final Rectangle bounds;

    /**
     * Whether or not orientation is left-to-right.
     */
    protected final boolean ltr;

    /**
     * {@link GeneralPath} used as an actual {@link Shape} implementation.
     */
    protected final GeneralPath shape;

    /**
     * Constructs new {@link RelativeGeneralPath}.
     *
     * @param bounds additional bounds
     */
    public RelativeGeneralPath ( final Rectangle bounds )
    {
        this ( bounds, true );
    }

    /**
     * Constructs new {@link RelativeGeneralPath}.
     *
     * @param bounds additional bounds
     * @param ltr    whether or not orientation is left-to-right
     */
    public RelativeGeneralPath ( final Rectangle bounds, final boolean ltr )
    {
        this.bounds = bounds;
        this.ltr = ltr;
        this.shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
    }

    /**
     * Adds a point to the path by moving to the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public final synchronized void moveTo ( final float x, final float y )
    {
        shape.moveTo ( bounds.x + orientX ( x ), bounds.y + y );
    }

    /**
     * Adds a point to the path by drawing a straight line from the current coordinates to the new specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public final synchronized void lineTo ( final float x, final float y )
    {
        shape.lineTo ( bounds.x + orientX ( x ), bounds.y + y );
    }

    /**
     * Adds a curved segment, defined by two new points, to the path by drawing a Quadratic curve that intersects both the current
     * coordinates and the specified coordinates {@code (x2,y2)}, using the specified point {@code (x1,y1)} as a quadratic parametric
     * control point.
     *
     * @param x1 X coordinate of the quadratic control point
     * @param y1 Y coordinate of the quadratic control point
     * @param x2 X coordinate of the final end point
     * @param y2 Y coordinate of the final end point
     */
    public final synchronized void quadTo ( final float x1, final float y1,
                                            final float x2, final float y2 )
    {
        shape.quadTo ( bounds.x + orientX ( x1 ), bounds.y + y1, bounds.x + orientX ( x2 ), bounds.y + y2 );
    }

    /**
     * Adds a curved segment, defined by three new points, to the path by drawing a Bezier curve that intersects both the current
     * coordinates and the specified coordinates {@code (x3,y3)}, using the specified points {@code (x1,y1)} and {@code (x2,y2)} as
     * Bezier control points. All coordinates are specified in float precision.
     *
     * @param x1 X coordinate of the first Bezier control point
     * @param y1 Y coordinate of the first Bezier control point
     * @param x2 X coordinate of the second Bezier control point
     * @param y2 Y coordinate of the second Bezier control point
     * @param x3 X coordinate of the final end point
     * @param y3 Y coordinate of the final end point
     */
    public final synchronized void curveTo ( final float x1, final float y1,
                                             final float x2, final float y2,
                                             final float x3, final float y3 )
    {
        shape.curveTo ( bounds.x + orientX ( x1 ), bounds.y + y1,
                bounds.x + orientX ( x2 ), bounds.y + y2,
                bounds.x + orientX ( x3 ), bounds.y + y3 );
    }

    /**
     * Closes the current subpath by drawing a straight line back to the coordinates of the last {@code moveTo}.
     * If the path is already closed then this method has no effect.
     */
    public final synchronized void closePath ()
    {
        shape.closePath ();
    }

    /**
     * Returns X coordinate oriented according to {@link #ltr} orientation.
     *
     * @param x X coordinate to orient
     * @return X coordinate oriented according to {@link #ltr} orientation
     */
    protected float orientX ( final float x )
    {
        return ltr ? x : bounds.width - x;
    }

    @Override
    public Rectangle getBounds ()
    {
        return shape.getBounds ();
    }

    @Override
    public Rectangle2D getBounds2D ()
    {
        return shape.getBounds2D ();
    }

    @Override
    public boolean contains ( final double x, final double y )
    {
        return shape.contains ( x, y );
    }

    @Override
    public boolean contains ( final Point2D p )
    {
        return shape.contains ( p );
    }

    @Override
    public boolean intersects ( final double x, final double y, final double w, final double h )
    {
        return shape.intersects ( x, y, w, h );
    }

    @Override
    public boolean intersects ( final Rectangle2D r )
    {
        return shape.intersects ( r );
    }

    @Override
    public boolean contains ( final double x, final double y, final double w, final double h )
    {
        return shape.contains ( x, y, w, h );
    }

    @Override
    public boolean contains ( final Rectangle2D r )
    {
        return shape.contains ( r );
    }

    @Override
    public PathIterator getPathIterator ( final AffineTransform at )
    {
        return shape.getPathIterator ( at );
    }

    @Override
    public PathIterator getPathIterator ( final AffineTransform at, final double flatness )
    {
        return shape.getPathIterator ( at, flatness );
    }
}