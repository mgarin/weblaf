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

package com.alee.painter.decoration.content;

import com.alee.extended.label.CustomStyle;
import com.alee.extended.label.StyleRange;
import com.alee.utils.TextUtils;
import com.alee.utils.xml.ColorConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class representing {@link com.alee.extended.label.WebStyledLabel} styled text.
 *
 * @author Mikle Garin
 */

public class StyledText
{
    /**
     * todo 1. Add multi-level styling support (sub-styles)
     */

    /**
     * Text containing style syntax.
     */
    protected final String text;

    /**
     * Plain text extracted from {@link #text}.
     */
    protected String plainText;

    /**
     * Style ranges extracted from {@link #text}.
     */
    protected List<StyleRange> styleRanges;

    /**
     * Constructs new styled text.
     *
     * @param text text containing style syntax
     */
    public StyledText ( final String text )
    {
        super ();
        this.text = text;
    }

    /**
     * Returns plain text extracted from {@link #text}.
     *
     * @return plain text extracted from {@link #text}
     */
    public String getPlainText ()
    {
        parseText ();
        return plainText;
    }

    /**
     * Returns style ranges extracted from {@link #text}.
     *
     * @return style ranges extracted from {@link #text}
     */
    public List<StyleRange> getStyleRanges ()
    {
        parseText ();
        return styleRanges;
    }

    /**
     * Parses text containing style syntax.
     * As a result {@link #plainText} and {@link #styleRanges} fields will be filled-in.
     */
    protected void parseText ()
    {
        // Skip parsing if it not needed or was already completed
        if ( TextUtils.isEmpty ( text ) || plainText != null )
        {
            return;
        }

        // Parsing text
        String plainText = "";
        final List<StyleRange> styleRanges = new ArrayList<StyleRange> ();
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
                        styleRanges.add ( range.getStyleRange () );
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

        // Updating parsed values
        this.plainText = plainText;
        this.styleRanges = styleRanges;
    }

    /**
     * Returns next unescaped text that fits specified pattern.
     * todo Enhance this syntax with an escape recognition
     *
     * @param text    text to look for the pattern in
     * @param pattern pattern to find
     * @param from    starting search index
     * @return next unescaped text that fits specified pattern
     */
    protected int nextUnescaped ( final String text, final String pattern, final int from )
    {
        return text.indexOf ( pattern, from );
    }

    /**
     * Returns single {@link com.alee.painter.decoration.content.TextRange} parsed from statement.
     *
     * @param statement {@link java.lang.String} statement
     * @return single {@link com.alee.painter.decoration.content.TextRange} parsed from statement
     */
    protected TextRange parseStatement ( final String statement )
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

    /**
     * Returns {@link java.awt.Color} parsed from statement.
     *
     * @param statement {@link java.lang.String} statement
     * @return {@link java.awt.Color} parsed from statement
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