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

package com.alee.painter.decoration.background;

import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import java.awt.*;
import java.io.Serializable;

/**
 * Gradient paint color.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("GradientColor")
@XStreamConverter (value = ToAttributedValueConverter.class, strings = { "color" })
public class GradientColor implements Serializable, Cloneable
{
    /**
     * Color fraction.
     * It is not used when gradient has only one color specified.
     * Also it might not be specified for multi-color separator to use even fractions.
     */
    @XStreamAsAttribute
    private Float fraction;

    /**
     * Fraction color.
     * Must always be provided to properly render separator.
     */
    private Color color;

    /**
     * Returns color fraction on the line.
     *
     * @return color fraction on the line
     */
    public Float getFraction ()
    {
        return fraction;
    }

    /**
     * Sets color fraction on the line.
     *
     * @param fraction color fraction on the line
     */
    public void setFraction ( final Float fraction )
    {
        this.fraction = fraction;
    }

    /**
     * Returns fracton color.
     *
     * @return fracton color
     */
    public Color getColor ()
    {
        return color;
    }

    /**
     * Sets fracton color.
     *
     * @param color fracton color
     */
    public void setColor ( final Color color )
    {
        this.color = color;
    }

    @Override
    public GradientColor clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}