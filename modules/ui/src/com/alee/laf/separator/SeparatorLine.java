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

package com.alee.laf.separator;

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
 * Single separator line data.
 *
 * @author Mikle Garin
 * @see SeparatorLines
 * @see com.alee.laf.separator.AbstractSeparatorPainter
 */

@XStreamAlias ("SeparatorLine")
public class SeparatorLine implements Serializable
{
    /**
     * Separator line stroke.
     * Might not be specified.
     */
    @XStreamAsAttribute
    private Stroke stroke;

    /**
     * Separator line colors.
     * Must always be provided to properly render separator.
     */
    @XStreamImplicit (itemFieldName = "color")
    private List<GradientColor> colors;

    /**
     * Returns separator line stroke.
     *
     * @return separator line stroke
     */
    public Stroke getStroke ()
    {
        return stroke;
    }

    /**
     * Sets separator line stroke.
     *
     * @param stroke separator line stroke
     */
    public void setStroke ( final Stroke stroke )
    {
        this.stroke = stroke;
    }

    /**
     * Returns separator line colors.
     *
     * @return separator line colors
     */
    public List<GradientColor> getColors ()
    {
        return colors;
    }

    /**
     * Sets separator line colors.
     *
     * @param colors separator line colors
     */
    public void setColors ( final List<GradientColor> colors )
    {
        this.colors = colors;
    }

    /**
     * Returns separator line paint.
     *
     * @param x1 line start X coordinate
     * @param y1 line start Y coordinate
     * @param x2 line end X coordinate
     * @param y2 line end Y coordinate
     * @return separator line paint
     */
    public Paint getPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        return DecorationUtils.getPaint ( GradientType.linear, colors, x1, y1, x2, y2 );
    }
}