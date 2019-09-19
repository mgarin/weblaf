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

import com.alee.api.annotations.NotNull;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;

/**
 * This class provides a set of utilities to work with HTML.
 *
 * @author Mikle Garin
 */
public final class HtmlUtils
{
    /**
     * Default line separator.
     */
    @NotNull
    public static final String DEFAULT_LINE_SEPARATOR = "\n";

    /**
     * Private constructor to avoid instantiation.
     */
    private HtmlUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns plain text extracted from the specified HTML.
     *
     * @param html HTML
     * @return plain text
     */
    @NotNull
    public static String getPlainText ( @NotNull final String html )
    {
        return getPlainText ( html, DEFAULT_LINE_SEPARATOR );
    }

    /**
     * Returns plain text extracted from the specified HTML.
     *
     * @param html          HTML
     * @param lineSeparator line separator
     * @return plain text
     */
    @NotNull
    public static String getPlainText ( @NotNull final String html, @NotNull final String lineSeparator )
    {
        final String plain;
        final Source source = new Source ( html );
        final Tag[] tags = source.fullSequentialParse ();
        if ( tags.length > 0 )
        {
            final Renderer renderer = source.getRenderer ();
            renderer.setIncludeHyperlinkURLs ( false );
            renderer.setIncludeAlternateText ( false );
            renderer.setDecorateFontStyles ( false );
            renderer.setMaxLineLength ( Integer.MAX_VALUE );
            renderer.setHRLineLength ( 40 );
            renderer.setBlockIndentSize ( 4 );
            renderer.setConvertNonBreakingSpaces ( false );
            renderer.setNewLine ( lineSeparator );
            plain = renderer.toString ();
        }
        else
        {
            plain = html;
        }
        return plain;
    }
}