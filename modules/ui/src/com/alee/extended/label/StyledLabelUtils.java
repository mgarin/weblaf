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

import com.alee.utils.TextUtils;
import com.alee.utils.xml.ColorConverter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class provides a set of utilities for WebStyledLabel component.
 *
 * @author Mikle Garin
 */

public final class StyledLabelUtils implements SwingConstants
{
    public static String getPlainText ( final String text, final List<StyleRange> styles )
    {
        if ( TextUtils.isEmpty ( text ) )
        {
            return text;
        }

        String plainText = "";
        String trimmedText = text;
        int begin = nextUnescaped ( trimmedText, "{", 0 );
        if ( begin != -1 )
        {
            while ( begin != -1 )
            {
                final int end = nextUnescaped ( trimmedText, "}", begin + 1 );
                if ( end != -1 )
                {
                    // Parsing statement
                    final String statement = trimmedText.substring ( begin + 1, end );
                    final TextRange range = parseStatement ( statement );

                    // Including parsed statement or simple text
                    if ( range != null )
                    {
                        // We found some styles in the statement
                        // Adding text and style range and proceeding
                        plainText += trimmedText.substring ( 0, begin );
                        range.getStyleRange ().setStartIndex ( plainText.length () );
                        styles.add ( range.getStyleRange () );
                        plainText += range.getText ();
                    }
                    else
                    {
                        // We didn't find any style statements
                        if ( statement.equals ( "br" ) )
                        {
                            // Adding linebreak and proceeding
                            plainText += trimmedText.substring ( 0, begin ) + "\n";
                        }
                        else
                        {
                            // Adding plain text and proceeding
                            plainText += trimmedText.substring ( 0, begin ) + statement;
                        }
                    }

                    // Continue to next
                    trimmedText = trimmedText.substring ( end + 1 );
                    begin = nextUnescaped ( trimmedText, "{", 0 );
                }
                else
                {
                    break;
                }
            }
            plainText += trimmedText;
        }
        else
        {
            plainText = text;
        }
        return plainText;
    }

    private static int nextUnescaped ( final String trimmedText, final String pattern, final int from )
    {
        // todo
        return trimmedText.indexOf ( pattern, from );
    }

    private static TextRange parseStatement ( final String statement )
    {
        final int sep = statement.indexOf ( ":" );
        if ( sep != -1 )
        {
            try
            {
                final String text = statement.substring ( 0, sep );
                boolean p = false;
                boolean b = false;
                boolean i = false;
                Color fg = null;
                Color bg = null;
                final List<CustomStyle> customStyles = new ArrayList<CustomStyle> ();

                final String vars = statement.substring ( sep + 1 );
                final StringTokenizer st = new StringTokenizer ( vars, ";", false );
                int styles = 0;
                while ( st.hasMoreTokens () )
                {
                    final String token = st.nextToken ();
                    if ( token.equals ( "p" ) || token.equals ( "plain" ) )
                    {
                        p = true;
                        styles++;
                    }
                    else if ( token.equals ( "b" ) || token.equals ( "bold" ) )
                    {
                        b = true;
                        styles++;
                    }
                    else if ( token.equals ( "i" ) || token.equals ( "italic" ) )
                    {
                        i = true;
                        styles++;
                    }
                    else if ( token.equals ( "u" ) || token.equals ( "underlined" ) )
                    {
                        customStyles.add ( CustomStyle.underlined );
                        styles++;
                    }
                    else if ( token.equals ( "sp" ) || token.equals ( "sup" ) || token.equals ( "superscript" ) )
                    {
                        customStyles.add ( CustomStyle.superscript );
                        styles++;
                    }
                    else if ( token.equals ( "sb" ) || token.equals ( "sub" ) || token.equals ( "subscript" ) )
                    {
                        customStyles.add ( CustomStyle.subscript );
                        styles++;
                    }
                    else if ( token.equals ( "s" ) || token.equals ( "strike" ) )
                    {
                        customStyles.add ( CustomStyle.strikeThrough );
                        styles++;
                    }
                    else if ( token.equals ( "ds" ) || token.equals ( "doublestrike" ) )
                    {
                        customStyles.add ( CustomStyle.doubleStrikeThrough );
                        styles++;
                    }
                    else if ( token.startsWith ( "c" ) || token.startsWith ( "color" ) ||
                            token.startsWith ( "fg" ) || token.startsWith ( "foreground" ) )
                    {
                        final Color color = parseColor ( token );
                        if ( color != null )
                        {
                            fg = color;
                            styles++;
                        }
                    }
                    else if ( token.startsWith ( "bg" ) || token.startsWith ( "background" ) )
                    {
                        final Color color = parseColor ( token );
                        if ( color != null )
                        {
                            bg = color;
                            styles++;
                        }
                    }
                    // Other variables are simply ignored so far
                    // New possible variables might be added in future
                }

                // Create style range only if some style was actually found
                if ( styles > 0 )
                {
                    // Combining TextRange and StyleRange from retrieved data
                    final int style = b && i ? Font.BOLD | Font.ITALIC : b ? Font.BOLD : i ? Font.ITALIC : p ? Font.PLAIN : -1;
                    final CustomStyle[] cs = customStyles.toArray ( new CustomStyle[ customStyles.size () ] );
                    return new TextRange ( text, new StyleRange ( 0, text.length (), style, fg, bg, cs ) );
                }
                else
                {
                    return null;
                }
            }
            catch ( final Throwable e )
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    private static Color parseColor ( final String statement )
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
            catch ( final Throwable e )
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