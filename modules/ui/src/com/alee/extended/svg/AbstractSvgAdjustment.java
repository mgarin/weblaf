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

import com.alee.managers.icon.data.IconAdjustment;
import com.kitfox.svg.SVGElement;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Base class for any selector-based adjustments for {@link SvgIcon}.
 *
 * @author Mikle Garin
 */
public abstract class AbstractSvgAdjustment implements IconAdjustment<SvgIcon>
{
    /**
     * SVG element selector.
     *
     * @see SvgSelector
     */
    @XStreamAsAttribute
    protected String selector;

    @Override
    public void apply ( final SvgIcon icon )
    {
        apply ( icon, icon.find ( selector ) );
    }

    /**
     * Applies this adjustment to the specified {@link SvgIcon} elements.
     *
     * @param icon     {@link SvgIcon} to adjust
     * @param elements list of {@link SVGElement}s to adjust
     */
    protected abstract void apply ( SvgIcon icon, List<SVGElement> elements );
}