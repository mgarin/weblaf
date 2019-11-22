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
import com.alee.utils.MathUtils;
import com.kitfox.svg.SVGElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * Simple adjustments for changing opacity of all colors in {@link SvgIcon}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "SvgColorOpacity" )
public class SvgColorOpacity extends AbstractSvgColorAdjustment
{
    /**
     * {@link Color} opacity value between {@code 0} and {@code 1}.
     * By default (if set to {@code null}) it will use {@code 1} as opacity value.
     */
    @Nullable
    @XStreamAsAttribute
    protected Double opacity;

    /**
     * Constructs new {@link SvgColorOpacity}.
     */
    public SvgColorOpacity ()
    {
        this ( null );
    }

    /**
     * Constructs new {@link SvgColorOpacity}.
     *
     * @param opacity {@link Color} opacity value between {@code 0} and {@code 1}
     */
    public SvgColorOpacity ( @Nullable final Double opacity )
    {
        this.opacity = opacity;
    }

    @NotNull
    @Override
    protected Color adjustColor ( @NotNull final SvgIcon icon, @NotNull final SVGElement element, @NotNull final String attribute,
                                  @NotNull final Color color )
    {
        final int offeredAlpha = opacity != null ? ( int ) Math.round ( color.getAlpha () * opacity ) : 255;
        return ColorUtils.transparent ( color, MathUtils.limit ( 0, offeredAlpha, 255 ) );
    }
}