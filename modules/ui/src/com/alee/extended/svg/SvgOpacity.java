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
import com.alee.utils.MathUtils;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.xml.StyleAttribute;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Adds or replaces existing opacity on the target {@link com.kitfox.svg.SVGElement}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "SvgOpacity" )
public class SvgOpacity extends AbstractSvgAttributeAdjustment
{
    /**
     * Opacity value between {@code 0} and {@code 1}.
     * By default (if set to {@code null}) it will use {@code 1} as opacity value.
     */
    @Nullable
    @XStreamAsAttribute
    protected Double opacity;

    /**
     * Constructs new {@link SvgOpacity}.
     */
    public SvgOpacity ()
    {
        this ( null, null );
    }

    /**
     * Constructs new {@link SvgOpacity}.
     *
     * @param opacity opacity value between {@code 0} and {@code 1}
     */
    public SvgOpacity ( @Nullable final Double opacity )
    {
        this ( null, opacity );
    }

    /**
     * Constructs new {@link SvgOpacity}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     */
    public SvgOpacity ( @Nullable final String selector )
    {
        this ( selector, null );
    }

    /**
     * Constructs new {@link SvgOpacity}.
     *
     * @param selector {@link com.kitfox.svg.SVGElement} selector
     * @param opacity  opacity value between {@code 0} and {@code 1}
     */
    public SvgOpacity ( @Nullable final String selector, @Nullable final Double opacity )
    {
        super ( selector );
        this.opacity = opacity;
    }

    @NotNull
    @Override
    protected String getAttribute ( @NotNull final SvgIcon icon )
    {
        return SvgElements.OPACITY;
    }

    @Nullable
    @Override
    protected String getValue ( @NotNull final SvgIcon icon, @NotNull final SVGElement element, @Nullable final StyleAttribute attribute )
    {
        final double oldValue = attribute != null ? attribute.getDoubleValue () : 1d;
        return String.format (
                "%s",
                opacity != null
                        ? MathUtils.limit ( 0.0, opacity * oldValue, 1.0 )
                        : oldValue
        );
    }
}