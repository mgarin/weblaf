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
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.xml.StyleAttribute;

import java.util.List;

/**
 * Base class for any attribute adjustments for {@link SvgIcon}.
 *
 * @author Mikle Garin
 */
public abstract class AbstractSvgAttributeAdjustment extends AbstractSvgAdjustment
{
    /**
     * Constructs new {@link AbstractSvgAttributeAdjustment}.
     */
    public AbstractSvgAttributeAdjustment ()
    {
        this ( null );
    }

    /**
     * Constructs new {@link AbstractSvgAttributeAdjustment}.
     *
     * @param selector {@link SVGElement} selector
     */
    public AbstractSvgAttributeAdjustment ( @Nullable final String selector )
    {
        super ( selector );
    }

    @Override
    protected void apply ( @NotNull final SvgIcon icon, @NotNull final List<SVGElement> elements )
    {
        final String attribute = getAttribute ( icon );
        for ( final SVGElement element : elements )
        {
            final StyleAttribute styleAttribute = icon.getAttribute ( element, attribute );
            final String value = getValue ( icon, element, styleAttribute );
            if ( value != null )
            {
                icon.setAttribute ( element, attribute, value );
            }
            else
            {
                icon.removeAttribute ( element, attribute );
            }
        }
    }

    /**
     * Returns {@link SVGElement} attribute to adjust.
     *
     * @param icon {@link SvgIcon} to adjust
     * @return {@link SVGElement} attribute to adjust
     */
    @NotNull
    protected abstract String getAttribute ( @NotNull SvgIcon icon );

    /**
     * Returns {@link SVGElement} attribute value to apply.
     *
     * @param icon     {@link SvgIcon} to adjust
     * @param element  {@link SVGElement} attribute will be adjusted for
     * @param attribute old attribute value
     * @return {@link SVGElement} attribute value to apply
     */
    @Nullable
    protected abstract String getValue ( @NotNull SvgIcon icon, @NotNull SVGElement element, @Nullable StyleAttribute attribute );
}