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

package com.alee.extended.label;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

/**
 * This class represents part of the multi-styled text with its own style.
 * Used within {@link AbstractStyledTextContent} to split styled text into simple renderable pieces.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */
public class TextRange
{
    /**
     * Text part.
     */
    @NotNull
    public final String text;

    /**
     * Text part style.
     */
    @Nullable
    public final StyleRange styleRange;

    /**
     * Constructs new TextRange with empty style.
     *
     * @param text text part
     */
    public TextRange ( @NotNull final String text )
    {
        this ( text, null );
    }

    /**
     * Constructs new TextRange with the specified style.
     *
     * @param text       text part
     * @param styleRange text part style
     */
    public TextRange ( @NotNull final String text, @Nullable final StyleRange styleRange )
    {
        this.text = text;
        this.styleRange = styleRange;
    }

    /**
     * Returns text part.
     *
     * @return text part
     */
    @NotNull
    public String getText ()
    {
        return text;
    }

    /**
     * Returns text part style.
     *
     * @return text part style
     */
    @Nullable
    public StyleRange getStyleRange ()
    {
        return styleRange;
    }
}