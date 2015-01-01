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

/**
 * This class represents part of the multi-styled text with its own style.
 * Created for internal usage within WebStyledLabelUI to split the styled text into separate renderable pieces.
 *
 * @author Mikle Garin
 */

public class TextRange
{
    /**
     * Text part.
     */
    public String text;

    /**
     * Text part style.
     */
    public StyleRange styleRange;

    /**
     * Constructs new TextRange with empty style.
     *
     * @param text text part
     */
    public TextRange ( final String text )
    {
        this ( text, null );
    }

    /**
     * Constructs new TextRange with the specified style.
     *
     * @param text       text part
     * @param styleRange text part style
     */
    public TextRange ( final String text, final StyleRange styleRange )
    {
        super ();
        this.text = text;
        this.styleRange = styleRange;
    }

    /**
     * Returns text part.
     *
     * @return text part
     */
    public String getText ()
    {
        return text;
    }

    /**
     * Returns text part style.
     *
     * @return text part style
     */
    public StyleRange getStyleRange ()
    {
        return styleRange;
    }
}