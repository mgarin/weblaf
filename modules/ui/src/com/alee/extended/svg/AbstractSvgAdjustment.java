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
     * {@link SVGElement} selector.
     * By default (if set to {@code null}) it will use {@code "svg"} selector for picking root SVG element.
     *
     * @see SvgSelector
     */
    @Nullable
    @XStreamAsAttribute
    protected String selector;

    /**
     * Constructs new {@link AbstractSvgAdjustment}.
     */
    public AbstractSvgAdjustment ()
    {
        this ( null );
    }

    /**
     * Constructs new {@link AbstractSvgAdjustment}.
     *
     * @param selector {@link SVGElement} selector
     */
    public AbstractSvgAdjustment ( @Nullable final String selector )
    {
        this.selector = selector;
    }

    /**
     * Returns {@link SVGElement} selector value.
     *
     * @return {@link SVGElement} selector value
     */
    @NotNull
    protected String getSelector ()
    {
        return selector != null ? selector : "svg";
    }

    @Override
    public void apply ( @NotNull final SvgIcon icon )
    {
        apply ( icon, icon.find ( getSelector () ) );
    }

    /**
     * Applies this adjustment to the specified {@link SvgIcon} elements.
     *
     * @param icon     {@link SvgIcon} to adjust
     * @param elements list of {@link SVGElement}s to adjust
     */
    protected abstract void apply ( @NotNull SvgIcon icon, @NotNull List<SVGElement> elements );
}