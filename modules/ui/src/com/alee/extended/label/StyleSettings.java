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

import com.alee.utils.xml.ColorConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Base implementation of style settings.
 * It provides a convenient way of parsing text with custom style settings.
 *
 * Here is a full list of style settings supported:
 * - p, plain - Reverts text style to plain
 * - b, bold - Bold text
 * - i, italic - Italic text
 * - u, underlined - Underlined text
 * - w, waved - Underlined by a wavy line text
 * - s, strike - Striked text
 * - ds, doublestrike - Double-striked text
 * - sp, sup, superscript - Superscript text
 * - sb, sub, subscript - Subscript text
 * - c, color, fg, foreground - Text color
 * - bg, background - Text background color
 *
 * Here are a few usage examples of this style settings implementation:
 * - {Bold text:b}
 * - {Bold:b} and {underlined:u} text
 * - {Italic red text:i;c(red)}
 * - Text with {superscript:sp} and {subscript:sb}
 * - {Multiline:b}{br}{styled:u}{br}{text:i}
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */
public class StyleSettings implements IStyleSettings
{
    /**
     * {@link StyleRange} start index.
     */
    protected final int startIndex;

    /**
     * {@link StyleRange} length.
     */
    protected final int length;

    /**
     * Style settings.
     */
    protected final String settings;

    /**
     * @param startIndex {@link StyleRange} start index
     * @param length     {@link StyleRange} length
     * @param settings   style settings
     */
    public StyleSettings ( final int startIndex, final int length, final String settings )
    {
        super ();
        this.startIndex = startIndex;
        this.length = length;
        this.settings = settings;
    }

    @Override
    public StyleRange getStyleRange ()
    {
        // Parsing style settings one by one
        boolean p = false;
        boolean b = false;
        boolean i = false;
        Color fg = null;
        Color bg = null;
        final List<CustomStyle> customStyles = new ArrayList<CustomStyle> ();
        final StringTokenizer st = new StringTokenizer ( settings, ";", false );
        while ( st.hasMoreTokens () )
        {
            final String token = st.nextToken ();
            if ( token.equals ( "p" ) || token.equals ( "plain" ) )
            {
                p = true;
            }
            else if ( token.equals ( "b" ) || token.equals ( "bold" ) )
            {
                b = true;
            }
            else if ( token.equals ( "i" ) || token.equals ( "italic" ) )
            {
                i = true;
            }
            else if ( token.equals ( "u" ) || token.equals ( "underlined" ) )
            {
                customStyles.add ( CustomStyle.underlined );
            }
            else if ( token.equals ( "w" ) || token.equals ( "waved" ) )
            {
                customStyles.add ( CustomStyle.waved );
            }
            else if ( token.equals ( "s" ) || token.equals ( "strike" ) )
            {
                customStyles.add ( CustomStyle.strikeThrough );
            }
            else if ( token.equals ( "ds" ) || token.equals ( "doublestrike" ) )
            {
                customStyles.add ( CustomStyle.doubleStrikeThrough );
            }
            else if ( token.equals ( "sp" ) || token.equals ( "sup" ) || token.equals ( "superscript" ) )
            {
                customStyles.add ( CustomStyle.superscript );
            }
            else if ( token.equals ( "sb" ) || token.equals ( "sub" ) || token.equals ( "subscript" ) )
            {
                customStyles.add ( CustomStyle.subscript );
            }
            else if ( token.startsWith ( "c" ) || token.startsWith ( "color" ) ||
                    token.startsWith ( "fg" ) || token.startsWith ( "foreground" ) )
            {
                final Color color = parseColor ( token );
                if ( color != null )
                {
                    fg = color;
                }
            }
            else if ( token.startsWith ( "bg" ) || token.startsWith ( "background" ) )
            {
                final Color color = parseColor ( token );
                if ( color != null )
                {
                    bg = color;
                }
            }
            // Other variables are simply ignored so far
            // New possible variables might be added in future
        }

        // Checking styles existence
        if ( p || b || i || fg != null || bg != null || customStyles.size () > 0 )
        {
            // Creating style
            final int style = b && i ? Font.BOLD | Font.ITALIC : b ? Font.BOLD : i ? Font.ITALIC : p ? Font.PLAIN : -1;
            final CustomStyle[] cs = customStyles.toArray ( new CustomStyle[ customStyles.size () ] );
            return new StyleRange ( startIndex, length, style, fg, bg, cs );
        }
        else
        {
            // No style available
            return null;
        }
    }

    /**
     * Returns {@link Color} parsed from statement.
     *
     * @param statement {@link String} statement
     * @return {@link Color} parsed from statement
     */
    protected Color parseColor ( final String statement )
    {
        final int i1 = statement.indexOf ( "(" );
        final int i2 = statement.lastIndexOf ( ")" );
        if ( i1 != -1 && i2 != -1 )
        {
            try
            {
                final String colorString = statement.substring ( i1 + 1, i2 );
                return ColorConverter.colorFromString ( colorString );
            }
            catch ( final Exception e )
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}