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
import com.alee.managers.icon.data.IconAdjustment;
import com.alee.utils.ColorUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.collection.ImmutableList;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.xml.StyleAttribute;

import java.awt.*;
import java.util.List;

/**
 * Simple adjustments for changing {@link SvgIcon} colors.
 *
 * @author Mikle Garin
 */
public abstract class AbstractSvgColorAdjustment implements IconAdjustment<SvgIcon>
{
    /**
     * todo 1. Adjust "style" attribute settings as well
     */

    /**
     * Color attributes used for adjustment.
     */
    protected static final List<String> ATTRIBUTES = new ImmutableList<String> (
            SvgElements.STROKE,
            SvgElements.FILL,
            SvgElements.STOP_COLOR
    );

    @Override
    public void apply ( @NotNull final SvgIcon icon )
    {
        // Searching for elements containig color attributes
        final List<SVGElement> elements = icon.find ( "*[" + TextUtils.listToString ( ATTRIBUTES, "," ) + "]" );

        // Iterating through all found elements and adjusting color attributes
        for ( final SVGElement element : elements )
        {
            // Modifying attributes for elements
            for ( final String attribute : ATTRIBUTES )
            {
                apply ( icon, element, attribute );
            }
        }
    }

    /**
     * Changes {@link SVGElement} attribute color value to grayscale.
     *
     * @param icon      {@link SvgIcon} to modify element for
     * @param element   {@link SVGElement} to modify attribute for
     * @param attribute {@link SVGElement} attribute
     */
    protected void apply ( @NotNull final SvgIcon icon, @NotNull final SVGElement element, @NotNull final String attribute )
    {
        if ( icon.hasAttribute ( element, attribute ) )
        {
            final StyleAttribute hexColor = icon.getAttribute ( element, attribute );
            if ( hexColor != null )
            {
                final Color color = hexColor.getColorValue ();
                if ( color != null )
                {
                    icon.setAttribute (
                            element,
                            attribute,
                            ColorUtils.toHex ( adjustColor ( icon, element, attribute, color ) )
                    );
                }
            }
        }
    }

    /**
     * Returns adjusted attribute {@link Color} value.
     *
     * @param icon      {@link SvgIcon} to modify element for
     * @param element   {@link SVGElement} to modify attribute for
     * @param attribute {@link SVGElement} attribute
     * @param color     atribute {@link Color} value
     * @return adjusted attribute {@link Color} value
     */
    @NotNull
    protected abstract Color adjustColor ( @NotNull SvgIcon icon, @NotNull SVGElement element, @NotNull String attribute,
                                           @NotNull Color color );
}