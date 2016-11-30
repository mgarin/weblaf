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

package com.alee.graphics.data;

import java.awt.*;
import java.io.Serializable;

/**
 * This class represents a simple line data.
 *
 * @author Mikle Garin
 */

public class Line implements Serializable
{
    /**
     * First X coordinate.
     */
    public int x1;

    /**
     * First Y coordinate.
     */
    public int y1;

    /**
     * Second X coordinate.
     */
    public int x2;

    /**
     * Second Y coordinate.
     */
    public int y2;

    /**
     * Conscturcts a line using empty coordinates.
     */
    public Line ()
    {
        super ();
    }

    /**
     * Constructs a line using the specifed coordinates.
     *
     * @param x1 first X coordinate
     * @param y1 first Y coordinate
     * @param x2 second X coordinate
     * @param y2 second Y coordinate
     */
    public Line ( final int x1, final int y1, final int x2, final int y2 )
    {
        super ();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Constructs a line using the specifed points.
     *
     * @param p1 first point
     * @param p2 second point
     */
    public Line ( final Point p1, final Point p2 )
    {
        super ();
        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
    }

    /**
     * Constructs a line using coordinates from the specified line.
     *
     * @param line line to process
     */
    public Line ( final Line line )
    {
        super ();
        this.x1 = line.x1;
        this.y1 = line.y1;
        this.x2 = line.x2;
        this.y2 = line.y2;
    }

    /**
     * Returns first X coordinate.
     *
     * @return first X coordinate
     */
    public double getX1 ()
    {
        return x1;
    }

    /**
     * Sets first X coordinate.
     *
     * @param x1 first X coordinate
     */
    public void setX1 ( final int x1 )
    {
        this.x1 = x1;
    }

    /**
     * Returns first Y coordinate.
     *
     * @return first Y coordinate
     */
    public double getY1 ()
    {
        return y1;
    }

    /**
     * Sets first Y coordinate.
     *
     * @param y1 first Y coordinate
     */
    public void setY1 ( final int y1 )
    {
        this.y1 = y1;
    }

    /**
     * Returns second X coordinate.
     *
     * @return second X coordinate
     */
    public double getX2 ()
    {
        return x2;
    }

    /**
     * Sets second X coordinate.
     *
     * @param x2 second X coordinate
     */
    public void setX2 ( final int x2 )
    {
        this.x2 = x2;
    }

    /**
     * Returns second Y coordinate.
     *
     * @return second Y coordinate
     */
    public double getY2 ()
    {
        return y2;
    }

    /**
     * Sets second Y coordinate.
     *
     * @param y2 second Y coordinate
     */
    public void setY2 ( final int y2 )
    {
        this.y2 = y2;
    }

    /**
     * Sets first point.
     *
     * @param p1 first point
     */
    public void setPoint1 ( final Point p1 )
    {
        this.x1 = p1.x;
        this.y1 = p1.y;
    }

    /**
     * Sets second point.
     *
     * @param p2 second point
     */
    public void setPoint2 ( final Point p2 )
    {
        this.x2 = p2.x;
        this.y2 = p2.y;
    }
}