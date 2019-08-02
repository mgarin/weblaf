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

import com.alee.utils.ColorUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * Adds or replaces existing fill color on the target {@link com.kitfox.svg.SVGElement}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "SvgFill" )
public class SvgFill extends AbstractSvgAttributeAdjustment
{
    /**
     * Fill color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    protected String getAttribute ( final SvgIcon icon )
    {
        return SvgElements.FILL;
    }

    @Override
    protected String getValue ( final SvgIcon icon )
    {
        return color != null ? ColorUtils.toHex ( color ) : "none";
    }
}