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

package com.alee.utils.swing;

import com.alee.api.merge.Overwriting;
import com.alee.utils.xml.ScaleConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * @author Mikle Garin
 */
@XStreamAlias ( "Scale" )
@XStreamConverter ( ScaleConverter.class )
public class Scale implements Overwriting, Cloneable, Serializable
{
    /**
     * X coordinate scale.
     */
    public double x;

    /**
     * Y coordinate scale.
     */
    public double y;

    /**
     * Constructs new {@link Scale}.
     */
    public Scale ()
    {
        this ( 1.0d, 1.0d );
    }

    /**
     * Constructs new {@link Scale}.
     *
     * @param x X coordinate scale
     * @param y Y coordinate scale
     */
    public Scale ( final double x, final double y )
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns X coordinate scale.
     *
     * @return X coordinate scale
     */
    public double getX ()
    {
        return x;
    }

    /**
     * Returns Y coordinate scale.
     *
     * @return Y coordinate scale
     */
    public double getY ()
    {
        return y;
    }

    /**
     * Adjusts X and Y coordinate scaling.
     *
     * @param x X coordinate scale
     * @param y Y coordinate scale
     */
    public void setScale ( final double x, final double y )
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    @Override
    public String toString ()
    {
        return "Scale[" + x + "," + y + "]";
    }
}