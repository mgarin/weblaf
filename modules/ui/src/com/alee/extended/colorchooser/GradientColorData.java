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

import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * Single color data for {@link GradientData}.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "GradientColorData" )
public class GradientColorData implements Serializable, Cloneable
{
    /**
     * Color location.
     */
    @XStreamAsAttribute
    private float location;

    /**
     * Color.
     */
    @XStreamAsAttribute
    private Color color;

    /**
     * Constructs {@link GradientColorData} with zero location and white color.
     */
    public GradientColorData ()
    {
        super ();
        this.location = 0f;
        this.color = Color.WHITE;
    }

    /**
     * Constructs {@link GradientColorData} with the specified location and color.
     *
     * @param location color location
     * @param color    color
     */
    public GradientColorData ( final float location, final Color color )
    {
        super ();
        this.location = location;
        this.color = color;
    }

    /**
     * Returns color.
     *
     * @return color
     */
    public Color getColor ()
    {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color new color
     */
    public void setColor ( final Color color )
    {
        this.color = color;
    }

    /**
     * Returns color location.
     *
     * @return color location
     */
    public float getLocation ()
    {
        return location;
    }

    /**
     * Sets color location.
     *
     * @param location new color location
     */
    public void setLocation ( final float location )
    {
        this.location = location;
    }

    /**
     * Returns whether this {@link GradientColorData} is equal to the specified object or not.
     *
     * @param obj object to compare with
     * @return {@code true} if this {@link GradientColorData} is equal to the specified object, {@code false} otherwise
     */
    @Override
    public boolean equals ( final Object obj )
    {
        if ( obj == null || !( obj instanceof GradientColorData ) )
        {
            return false;
        }

        final GradientColorData other = ( GradientColorData ) obj;
        return Float.compare ( getLocation (), other.getLocation () ) == 0 && getColor ().equals ( other.getColor () );
    }

    /**
     * Returns cloned {@link GradientColorData}.
     *
     * @return cloned {@link GradientColorData}
     */
    @Override
    public GradientColorData clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}