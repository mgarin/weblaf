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

package com.alee.utils;

import com.alee.utils.font.DerivedFontAttributes;
import com.alee.utils.map.SoftHashMap;

import java.awt.*;
import java.util.Map;

/**
 * This class provides a set of utilities to work with fonts.
 *
 * @author Mikle Garin
 */
public class FontUtils
{
    /**
     * Derived fonts cache map.
     */
    private static final Map<DerivedFontAttributes, Font> derivedFontsCache = new SoftHashMap<DerivedFontAttributes, Font> ();

    /**
     * Clears derived fonts cache.
     */
    public static void clearDerivedFontsCache ()
    {
        if ( derivedFontsCache != null )
        {
            derivedFontsCache.clear ();
        }
    }

    /**
     * Get derived font by font, style and size. At first it will get the derived font from cache. If it cannot hit the
     * derived font, it will invoke font.deriveFont to derive a font.
     *
     * @param font  original font
     * @param style new font style
     * @param size  new font size
     * @return the derived font.
     */
    public static Font getCachedDerivedFont ( final Font font, final int style, final int size )
    {
        final DerivedFontAttributes attribute = getFontAttribute ( font, style, size );
        Font derivedFont = derivedFontsCache.get ( attribute );
        if ( derivedFont == null )
        {
            derivedFont = font.deriveFont ( style, size );
            derivedFontsCache.put ( attribute, derivedFont );
        }
        return derivedFont;
    }

    /**
     * Returns derived font attributes object.
     *
     * @param font  original font
     * @param style new font style
     * @param size  new font size
     * @return font attributes object
     */
    protected static DerivedFontAttributes getFontAttribute ( final Font font, final int style, final int size )
    {
        return new DerivedFontAttributes ( font, style, size );
    }
}