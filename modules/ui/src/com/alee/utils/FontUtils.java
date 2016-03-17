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

public final class FontUtils
{
    /**
     * Derived fonts cache map.
     */
    private static final Map<DerivedFontAttributes, Font> derivedFontsCache = new SoftHashMap<DerivedFontAttributes, Font> ();

    /**
     * Referenced by code in the JDK which wants to test for the
     * minimum char code for which layout may be required.
     * Note that even basic latin text can benefit from ligatures,
     * eg "ffi" but we presently apply those only if explicitly
     * requested with TextAttribute.LIGATURES_ON.
     * The value here indicates the lowest char code for which failing
     * to invoke layout would prevent acceptable rendering.
     */
    public static final int MIN_LAYOUT_CHARCODE = 0x0300;

    /**
     * Referenced by code in the JDK which wants to test for the
     * maximum char code for which layout may be required.
     * Note this does not account for supplementary characters
     * where the caller interprets 'layout' to mean any case where
     * one 'char' (ie the java type char) does not map to one glyph
     */
    public static final int MAX_LAYOUT_CHARCODE = 0x206F;

    /**
     * Temporary constants moved from CharToGlyphMapper.
     */
    public static final int HI_SURROGATE_START = 55296;
    public static final int LO_SURROGATE_END = 57343;

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

    /**
     * checks whether TextLayout is required to handle characters.
     *
     * @param text  characters to be tested
     * @param start start
     * @param limit limit
     * @return <tt>true</tt>  if TextLayout is required
     * <tt>false</tt> if TextLayout is not required
     */
    public static boolean isComplexLayout ( final char[] text, final int start, final int limit )
    {
        return isComplexText ( text, start, limit );
    }

    /**
     * If there is anything in the text which triggers a case
     * where char-&gt;glyph does not map 1:1 in straightforward
     * left-&gt;right ordering, then this method returns true.
     * Scripts which might require it but are not treated as such
     * due to JDK implementations will not return true.
     * ie a 'true' return is an indication of the treatment by
     * the implementation.
     * Whether supplementary characters should be considered is dependent
     * on the needs of the caller. Since this method accepts the 'char' type
     * then such chars are always represented by a pair. From a rendering
     * perspective these will all (in the cases I know of) still be one
     * unicode character -&gt; one glyph. But if a caller is using this to
     * discover any case where it cannot make naive assumptions about
     * the number of chars, and how to index through them, then it may
     * need the option to have a 'true' return in such a case.
     */
    @SuppressWarnings ( "JavaDoc" )
    public static boolean isComplexText ( final char[] chs, final int start, final int limit )
    {

        for ( int i = start; i < limit; i++ )
        {
            if ( chs[ i ] < MIN_LAYOUT_CHARCODE )
            {
                //noinspection UnnecessaryContinue
                continue;
            }
            else if ( isNonSimpleChar ( chs[ i ] ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * This is almost the same as the method above, except it takes a
     * char which means it may include undecoded surrogate pairs.
     * The distinction is made so that code which needs to identify all
     * cases in which we do not have a simple mapping from
     * char-&gt;unicode character-&gt;glyph can be be identified.
     * For example measurement cannot simply sum advances of 'chars',
     * the caret in editable text cannot advance one 'char' at a time, etc.
     * These callers really are asking for more than whether 'layout'
     * needs to be run, they need to know if they can assume 1-&gt;1
     * char-&gt;glyph mapping.
     */
    @SuppressWarnings ( "JavaDoc" )
    static boolean isNonSimpleChar ( final char ch )
    {
        return isComplexCharCode ( ch ) || ( ch >= HI_SURROGATE_START && ch <= LO_SURROGATE_END );
    }

    /**
     * If the character code falls into any of a number of unicode ranges
     * where we know that simple left-&gt;right layout mapping chars to glyphs
     * 1:1 and accumulating advances is going to produce incorrect results,
     * we want to know this so the caller can use a more intelligent layout
     * approach. A caller who cares about optimum performance may want to
     * check the first case and skip the method call if its in that range.
     * Although there's a lot of tests in here, knowing you can skip
     * CTL saves a great deal more. The rest of the checks are ordered
     * so that rather than checking explicitly if (&gt;= start & &lt;= end)
     * which would mean all ranges would need to be checked so be sure
     * CTL is not needed, the method returns as soon as it recognises
     * the code point is outside of a CTL ranges.
     * NOTE: Since this method accepts an 'int' it is assumed to properly
     * represent a CHARACTER. ie it assumes the caller has already
     * converted surrogate pairs into supplementary characters, and so
     * can handle this case and doesn't need to be told such a case is
     * 'complex'.
     */
    @SuppressWarnings ( "JavaDoc" )
    static boolean isComplexCharCode ( final int code )
    {

        if ( code < MIN_LAYOUT_CHARCODE || code > MAX_LAYOUT_CHARCODE )
        {
            return false;
        }
        else if ( code <= 0x036f )
        {
            // Trigger layout for combining diacriticals 0x0300->0x036f
            return true;
        }
        else if ( code < 0x0590 )
        {
            // No automatic layout for Greek, Cyrillic, Armenian.
            return false;
        }
        else if ( code <= 0x06ff )
        {
            // Hebrew 0590 - 05ff
            // Arabic 0600 - 06ff
            return true;
        }
        else if ( code < 0x0900 )
        {
            return false; // Syriac and Thaana
        }
        else if ( code <= 0x0e7f )
        {
            // if Indic, assume shaping for conjuncts, reordering:
            // 0900 - 097F Devanagari
            // 0980 - 09FF Bengali
            // 0A00 - 0A7F Gurmukhi
            // 0A80 - 0AFF Gujarati
            // 0B00 - 0B7F Oriya
            // 0B80 - 0BFF Tamil
            // 0C00 - 0C7F Telugu
            // 0C80 - 0CFF Kannada
            // 0D00 - 0D7F Malayalam
            // 0D80 - 0DFF Sinhala
            // 0E00 - 0E7F if Thai, assume shaping for vowel, tone marks
            return true;
        }
        else if ( code < 0x1780 )
        {
            return false;
        }
        else if ( code <= 0x17ff )
        { // 1780 - 17FF Khmer
            return true;
        }
        else if ( code < 0x200c )
        {
            return false;
        }
        else if ( code <= 0x200d )
        { //  zwj or zwnj
            return true;
        }
        else if ( code >= 0x202a && code <= 0x202e )
        { // directional control
            return true;
        }
        else if ( code >= 0x206a && code <= 0x206f )
        { // directional control
            return true;
        }
        return false;
    }
}