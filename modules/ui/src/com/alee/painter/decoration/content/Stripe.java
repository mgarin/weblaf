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

package com.alee.painter.decoration.content;

import com.alee.api.merge.Mergeable;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.background.GradientColor;
import com.alee.painter.decoration.background.GradientType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * Single stripe data.
 *
 * @author Mikle Garin
 * @see Stripes
 */
@XStreamAlias ( "Stripe" )
public class Stripe implements Mergeable, Cloneable, Serializable
{
    /**
     * Stripe stroke.
     * Might not be specified.
     */
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Stripe colors.
     * At least one color must always be provided.
     */
    @XStreamImplicit ( itemFieldName = "color" )
    protected List<GradientColor> colors;

    /**
     * Returns stripe stroke.
     *
     * @return stripe stroke
     */
    public Stroke getStroke ()
    {
        return stroke;
    }

    /**
     * Sets stripe stroke.
     *
     * @param stroke stripe stroke
     */
    public void setStroke ( final Stroke stroke )
    {
        this.stroke = stroke;
    }

    /**
     * Returns stripe colors.
     *
     * @return stripe colors
     */
    public List<GradientColor> getColors ()
    {
        if ( colors != null )
        {
            return colors;
        }
        else
        {
            throw new DecorationException ( "At least one stripe color must be specified" );
        }
    }

    /**
     * Sets stripe colors.
     *
     * @param colors stripe colors
     */
    public void setColors ( final List<GradientColor> colors )
    {
        this.colors = colors;
    }

    /**
     * Returns stripe paint.
     *
     * @param x1 stripe start X coordinate
     * @param y1 stripe start Y coordinate
     * @param x2 stripe end X coordinate
     * @param y2 stripe end Y coordinate
     * @return stripe paint
     */
    public Paint getPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        return DecorationUtils.getPaint ( GradientType.linear, getColors (), x1, y1, x2, y2 );
    }
}