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
import com.alee.utils.ColorUtils;
import com.kitfox.svg.SVGElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.*;

/**
 * Simple adjustments for making {@link SvgIcon} colors grayscale.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "SvgGrayscale" )
public class SvgGrayscale extends AbstractSvgColorAdjustment
{
    @NotNull
    @Override
    protected Color adjustColor ( @NotNull final SvgIcon icon, @NotNull final SVGElement element, @NotNull final String attribute,
                                  @NotNull final Color color )
    {
        return ColorUtils.grayscale ( color );
    }
}