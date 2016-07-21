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

package com.alee.managers.icon.data;

import com.alee.extended.svg.SvgIcon;
import com.kitfox.svg.SVGElement;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Base class for any adjustments for {@link SvgIcon}.
 *
 * @author Mikle Garin
 */

public abstract class SvgAdjustment implements IconAdjustment<SvgIcon>
{
    /**
     * SVG element selector.
     */
    @XStreamAsAttribute
    protected String selector;

    @Override
    public void apply ( final SvgIcon icon )
    {
        apply ( icon, icon.find ( selector ) );
    }

    /**
     * Applies this adjustment to the specified SVG icon elements.
     *
     * @param icon    SVG icon to adjust
     * @param elements SVG elements to adjust
     */
    protected abstract void apply ( SvgIcon icon, List<SVGElement> elements );
}