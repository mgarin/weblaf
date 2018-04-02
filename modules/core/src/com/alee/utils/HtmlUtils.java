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

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;

import java.util.Locale;

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
    public static String getPlainText ( final String html )
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
    public static String getPlainText ( final String html, final String lineSeparator )
    {
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
            return renderer.toString ();
        }
        else
        {
            return html;
        }
    }

    /**
     * Returns HTML with specified text made bold.
     *
     * @param text text to process
     * @return HTML with specified text made bold
     */
    public static String bold ( final String text )
    {
        return "<html><b>" + text + "</b></html>";
    }

    /**
     * Returns whether the specified text contains HTML tags or not.
     *
     * @param text text to process
     * @return true if the specified text contains HTML tags, false otherwise
     */
    public static boolean hasTags ( final String text )
    {
        return text != null && text.trim ().length () > 0 && new Source ( text ).fullSequentialParse ().length > 0;
    }

    /**
     * Returns whether specified text contains HTML tag or not.
     *
     * @param text text to process
     * @return true if specified text contains HTML tag, false otherwise
     */
    public static boolean hasHtml ( final String text )
    {
        return hasTag ( text, HTMLElementName.HTML );
    }

    /**
     * Returns whether text contains the specified tag or not.
     *
     * @param text text to process
     * @param tag  tag to find
     * @return true if text contains the specified tag, false otherwise
     */
    public static boolean hasTag ( final String text, final String tag )
    {
        if ( text != null && text.trim ().length () > 0 )
        {
            final Source source = new Source ( text );
            source.fullSequentialParse ();
            return source.getFirstElement ( tag ) != null;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns HTML content between body or html tags.
     *
     * @param text text to process
     * @return HTML content between body or html tags
     */
    public static String getContent ( final String text )
    {
        final String lowerCaseText = text.toLowerCase ( Locale.ROOT );

        final String bodyTag = "<body>";
        final int body = lowerCaseText.indexOf ( bodyTag );
        final int bodyEnd = lowerCaseText.indexOf ( "</body>" );
        if ( body != -1 && bodyEnd != -1 )
        {
            return text.substring ( body + bodyTag.length (), bodyEnd );
        }

        final String htmlTag = "<html>";
        final int html = lowerCaseText.indexOf ( bodyTag );
        final int htmlEnd = lowerCaseText.indexOf ( "</html>" );
        if ( html != -1 && htmlEnd != -1 )
        {
            return text.substring ( html + htmlTag.length (), htmlEnd );
        }

        return text;
    }

    /**
     * Returns text converted into multiline HTML.
     *
     * @param text      text to convert
     * @param lineBreak line break text
     * @return text converted into multiline HTML
     */
    public static String convertToMultilineHtml ( final String text, final String... lineBreak )
    {
        String body = text;
        for ( final String divider : lineBreak )
        {
            body = body.replaceAll ( divider, "<br>" );
        }
        return "<html>" + body + "</html>";
    }
}