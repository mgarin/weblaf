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

package com.alee.extended.svg;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.ColorUtils;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.xml.StyleAttribute;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * Adds or replaces existing stroke color on the target {@link com.kitfox.svg.SVGElement}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "SvgStroke" )
public class SvgStroke extends AbstractSvgAttributeAdjustment
{
    /**
     * Stroke color.
     * By default (if set to {@code null}) it will use {@code "none"} color value.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Constructs new {@link SvgStroke}.
     */
    public SvgStroke ()
    {
        this ( null, null );
    }

    /**
     * Constructs new {@link SvgStroke}.
     *
     * @param color stroke {@link Color}
     */
    public SvgStroke ( @Nullable final Color color )
    {
        this ( null, color );
    }

    /**
     * Constructs new {@link SvgStroke}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     */
    public SvgStroke ( @Nullable final String selector )
    {
        this ( selector, null );
    }

    /**
     * Constructs new {@link SvgStroke}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     * @param color    stroke {@link Color}
     */
    public SvgStroke ( @Nullable final String selector, @Nullable final Color color )
    {
        super ( selector );
        this.color = color;
    }

    @NotNull
    @Override
    protected String getAttribute ( @NotNull final SvgIcon icon )
    {
        return SvgElements.STROKE;
    }

    @Nullable
    @Override
    protected String getValue ( @NotNull final SvgIcon icon, @NotNull final SVGElement element, @Nullable final StyleAttribute attribute )
    {
        return color != null ? ColorUtils.toHex ( color ) : "none";
    }
}