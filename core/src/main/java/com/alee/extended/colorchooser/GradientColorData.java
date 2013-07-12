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

package com.alee.extended.colorchooser;

import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * Single color data for GradientData.
 *
 * @author Mikle Garin
 * @since 1.4
 */

@XStreamAlias ( "GradientColorData" )
public class GradientColorData implements Serializable, Cloneable
{
    /**
     * GradientData location.
     */
    @XStreamAsAttribute
    private float location;

    /**
     * GradientData color.
     */
    @XStreamAsAttribute
    private Color color;

    /**
     * Constructs GradientColorData with zero location and white color.
     */
    public GradientColorData ()
    {
        super ();
        this.location = 0f;
        this.color = Color.WHITE;
    }

    /**
     * Constructs GradientColorData with the specified location and color.
     *
     * @param location GradientColorData location
     * @param color    GradientColorData color
     */
    public GradientColorData ( float location, Color color )
    {
        super ();
        this.location = location;
        this.color = color;
    }

    /**
     * Returns GradientColorData color.
     *
     * @return GradientColorData color
     */
    public Color getColor ()
    {
        return color;
    }

    /**
     * Sets GradientColorData color.
     *
     * @param color new GradientColorData color
     */
    public void setColor ( Color color )
    {
        this.color = color;
    }

    /**
     * Returns GradientColorData location.
     *
     * @return GradientColorData location
     */
    public float getLocation ()
    {
        return location;
    }

    /**
     * Sets GradientColorData location.
     *
     * @param location new GradientColorData location
     */
    public void setLocation ( float location )
    {
        this.location = location;
    }

    /**
     * Returns whether this GradientColorData is equal to the specified object or not.
     *
     * @param obj object to compare with
     * @return true if this GradientColorData is equal to the specified object, false otherwise
     */
    public boolean equals ( Object obj )
    {
        if ( obj == null || !( obj instanceof GradientColorData ) )
        {
            return false;
        }

        GradientColorData other = ( GradientColorData ) obj;
        return Float.compare ( getLocation (), other.getLocation () ) == 0 && getColor ().equals ( other.getColor () );
    }

    /**
     * Returns cloned GradientColorData.
     *
     * @return cloned GradientColorData
     */
    public GradientColorData clone ()
    {
        return new GradientColorData ( location, SwingUtils.copy ( color ) );
    }
}