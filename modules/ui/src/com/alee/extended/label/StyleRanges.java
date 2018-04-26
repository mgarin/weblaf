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

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of style ranges.
 * It provides a convenient way of parsing text with custom style syntax.
 * Plain text is kept within {@link #styledText} field and styles list is kept within {@link #styleRanges} field.
 * Supported syntax settings depend on the {@link IStyleSettings} implementation used.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 * @see IStyleSettings
 */
public class StyleRanges implements IStyleRanges
{
    /**
     * todo 1. Add multi-level styling support (sub-styles)
     */

    /**
     * Text containing style syntax.
     */
    protected final String styledText;

    /**
     * Cached plain text extracted from {@link #styledText}.
     */
    protected String plainText;

    /**
     * Cached style ranges extracted from {@link #styledText}.
     */
    protected List<StyleRange> styleRanges;

    /**
     * Constructs new style ranges.
     *
     * @param styledText text containing style syntax
     */
    public StyleRanges ( final String styledText )
    {
        super ();
        this.styledText = styledText;
    }

    @Override
    public String getPlainText ()
    {
        return parseStyledText ().plainText;
    }

    @Override
    public List<StyleRange> getStyleRanges ()
    {
        return parseStyledText ().styleRanges;
    }

    /**
     * Parses text containing style syntax.
     * As a result {@link #plainText} and {@link #styleRanges} fields will be filled-in.
     *
     * @return resulting {@link StyleRanges}
     */
    protected StyleRanges parseStyledText ()
    {
        // Parse only if it is needed and it wasn't already completed
        if ( styleRanges == null )
        {
            styleRanges = new ArrayList<StyleRange> ();
            if ( !TextUtils.isEmpty ( styledText ) )
            {
                int begin = nextUnescaped ( styledText, "{", 0 );
                if ( begin != -1 )
                {
                    plainText = "";
                    String trimmedText = styledText;
                    while ( begin != -1 )
                    {
                        final int end = nextUnescaped ( trimmedText, "}", begin + 1 );
                        if ( end != -1 )
                        {
                            // Clipping statement
                            final String statement = trimmedText.substring ( begin + 1, end );
                            if ( statement.equals ( "br" ) )
                            {
                                // Adding linebreak and proceeding
                                plainText += trimmedText.substring ( 0, begin ) + "\n";
                            }
                            else
                            {
                                // Parsing possible style syntax
                                final TextRange range = parseStatement ( plainText.length () + begin, statement );
                                if ( range != null && range.getStyleRange () != null )
                                {
                                    // Adding text and style range
                                    plainText += trimmedText.substring ( 0, begin ) + range.getText ();
                                    styleRanges.add ( range.getStyleRange () );
                                }
                                else
                                {
                                    // Adding plain text
                                    plainText += trimmedText.substring ( 0, begin ) + statement;
                                }
                            }

                            // Continue to next
                            trimmedText = trimmedText.substring ( end + 1 );
                            begin = nextUnescaped ( trimmedText, "{", 0 );
                        }
                        else
                        {
                            // Something wrong with the syntax
                            // Abort parsing and add the rest as plain text
                            break;
                        }
                    }
                    plainText += trimmedText;
                }
                else
                {
                    plainText = styledText;
                }
            }
            else
            {
                plainText = null;
            }
        }
        return this;
    }

    /**
     * Returns next unescaped text that fits specified pattern.
     *
     * @param text    text to look for the pattern in
     * @param pattern pattern to find
     * @param from    starting search index
     * @return next unescaped text that fits specified pattern
     */
    protected int nextUnescaped ( final String text, final String pattern, final int from )
    {
        // todo Enhance this syntax with an escape recognition, probably an "\\" one
        return text.indexOf ( pattern, from );
    }

    /**
     * Returns {@link TextRange} parsed from statement.
     *
     * @param startIndex {@link StyleRange} start index
     * @param statement  {@link String} statement
     * @return {@link TextRange} parsed from statement
     */
    protected TextRange parseStatement ( final int startIndex, final String statement )
    {
        TextRange textRange = null;
        try
        {
            // Looking for the settings separator
            final int sep = statement.lastIndexOf ( ":" );
            if ( sep != -1 )
            {
                // Parsing style range
                final String text = statement.substring ( 0, sep );
                final String settings = statement.substring ( sep + 1 );
                final IStyleSettings styleSettings = getStyleSettings ( startIndex, text.length (), settings );
                final StyleRange styleRange = styleSettings.getStyleRange ();
                if ( styleRange != null )
                {
                    textRange = new TextRange ( text, styleRange );
                }
            }
        }
        catch ( final Exception ignored )
        {
            // Ignoring any exceptions in style parsing
        }
        return textRange;
    }

    /**
     * Returns {@link IStyleSettings} implementation which will resolve style settings.
     *
     * @param startIndex {@link StyleRange} start index
     * @param length     {@link StyleRange} length
     * @param settings   {@link String} style settings
     * @return {@link IStyleSettings} implementation which will resolve style settings
     */
    protected IStyleSettings getStyleSettings ( final int startIndex, final int length, final String settings )
    {
        return new StyleSettings ( startIndex, length, settings );
    }
}