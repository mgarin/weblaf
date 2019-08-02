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

package com.alee.utils.font;

import java.awt.*;

/**
 * This represents information about derived font.
 *
 * @author Mikle Garin
 */
public class DerivedFontAttributes
{
    /**
     * Original font.
     */
    private final Font font;

    /**
     * New font style.
     */
    private final int style;

    /**
     * New font size.
     */
    private final float size;

    /**
     * Constructs new information about derived font.
     *
     * @param font  original font
     * @param style new font style
     * @param size  new font size
     */
    public DerivedFontAttributes ( final Font font, final int style, final float size )
    {
        this.font = font;
        this.style = style;
        this.size = size;
    }

    /**
     * Returns original font.
     *
     * @return original font
     */
    public Font getFont ()
    {
        return font;
    }

    /**
     * Returns new font style.
     *
     * @return new font style
     */
    public int getStyle ()
    {
        return style;
    }

    /**
     * Returns new font size.
     *
     * @return new font size
     */
    public float getSize ()
    {
        return size;
    }

    @Override
    public boolean equals ( final Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( !( o instanceof DerivedFontAttributes ) )
        {
            return false;
        }

        final DerivedFontAttributes fa = ( DerivedFontAttributes ) o;
        return Float.compare ( fa.size, size ) == 0 && style == fa.style && !( font == null || !font.equals ( fa.font ) );
    }

    @Override
    public int hashCode ()
    {
        int result;
        result = font.hashCode ();
        result = 31 * result + style;
        result = 31 * result + ( size != 0.0f ? Float.floatToIntBits ( size ) : 0 );
        return result;
    }
}