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

package com.alee.extended.svg;

import com.alee.api.jdk.Objects;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.collection.ImmutableSet;
import com.kitfox.svg.SVGElement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Special object that parses {@link SvgIcon} selectors and performs lookup.
 * Here are a few examples of its usage:
 *
 * 1. Element by its ID, example: {@code "#id"}
 * All SVG elements with the specified ID selected.
 *
 * 2. Element by its class, example: {@code ".name"}
 * All SVG elements with the specified class name selected.
 *
 * 3. Element by its name, example: {@code "svg"}, {@code "path"}
 * All SVG elements of the specified type selected.
 *
 * 4. All elements, example: {@code "*"}
 * All SVG elements selected.
 *
 * 5. By attributes, example: {@code "[stroke]"}, {@code "*[fill]"}, {@code ".name[stroke,fill,transform]"}
 * Can be used as standalone selector - in that case all SVG elements will be checked (equal to {@code "*"} element selector).
 * Can be used in addition to one of previously mentioned selectors - that will limit SVG elements to be checked.
 *
 * @author Mikle Garin
 * @see SvgIcon
 */
public final class SvgSelector implements Serializable
{
    /**
     * Opening brace for attribute conditions.
     */
    private static final String OPEN_BRACE = "[";

    /**
     * Closing brace for attribute conditions.
     */
    private static final String CLOSE_BRACE = "]";

    /**
     * Attribute conditions separator.
     */
    private static final String ATTRIBUTE_SEPARATOR = ",";

    /**
     * Element selector.
     */
    private final String element;

    /**
     * Element attribute filter.
     */
    private final Set<String> attributes;

    /**
     * Constructs new {@link SvgSelector} based on its text representation.
     *
     * @param selector {@link String} representation of {@link SvgSelector}
     */
    public SvgSelector ( final String selector )
    {
        super ();

        // Checking raw selector
        if ( TextUtils.isEmpty ( selector ) )
        {
            final String msg = "SVG element selector cannot be empty";
            throw new RuntimeException ( msg );
        }

        // Checking syntax
        final int openBrace = selector.indexOf ( OPEN_BRACE );
        final int closeBrace = selector.indexOf ( CLOSE_BRACE );
        if ( openBrace != -1 && closeBrace == -1 || openBrace == -1 && closeBrace != -1 )
        {
            final String msg = "Incorrect attributes selector syntax";
            throw new RuntimeException ( msg );
        }

        // Parsing selector
        final String rawElement = openBrace != -1 ? selector.substring ( 0, openBrace ) : selector;
        this.element = rawElement.trim ().toLowerCase ( Locale.ROOT );

        // Parsing extra information
        final String bracedText = openBrace != -1 ? selector.substring ( openBrace + OPEN_BRACE.length (), closeBrace ) : null;
        final List<String> conditions = TextUtils.stringToList ( bracedText, ATTRIBUTE_SEPARATOR );
        this.attributes = new ImmutableSet<String> ( conditions );
    }

    /**
     * Returns whether or not specified {@link SVGElement} fits this {@link SvgSelector} conditions.
     *
     * @param icon    {@link SvgIcon} icon to check element
     * @param element {@link SVGElement}
     * @return {@code true} if specified {@link SVGElement} fits this {@link SvgSelector} conditions, {@code false} otherwise
     */
    public boolean isApplicable ( final SvgIcon icon, final SVGElement element )
    {
        return checkSelector ( icon, element ) && checkAttributes ( icon, element );
    }

    /**
     * Returns whether or not specified {@link SVGElement} fits this {@link SvgSelector} selector conditions.
     *
     * @param icon    {@link SvgIcon} icon to check element
     * @param element {@link SVGElement}
     * @return {@code true} if specified {@link SVGElement} fits this {@link SvgSelector} selector conditions, {@code false} otherwise
     */
    private boolean checkSelector ( final SvgIcon icon, final SVGElement element )
    {
        if ( TextUtils.isEmpty ( this.element ) || this.element.equals ( "*" ) )
        {
            return true;
        }
        else if ( this.element.startsWith ( "#" ) )
        {
            final String id = this.element.substring ( 1 );
            final boolean exist = icon.hasAttribute ( element, SvgElements.ID );
            return exist && Objects.equals ( id, icon.getAttribute ( element, SvgElements.ID ).getStringValue () );
        }
        else if ( this.element.startsWith ( "." ) )
        {
            final String style = this.element.substring ( 1 );
            final boolean exist = icon.hasAttribute ( element, SvgElements.CLAZZ );
            return exist && Objects.equals ( style, icon.getAttribute ( element, SvgElements.CLAZZ ).getStringValue () );
        }
        else
        {
            return element.getClass () == SvgElements.CLASSES.get ( this.element );
        }
    }

    /**
     * Returns whether or not specified {@link SVGElement} fits this {@link SvgSelector} attribute conditions.
     *
     * @param icon    {@link SvgIcon} icon to check element
     * @param element {@link SVGElement}
     * @return {@code true} if specified {@link SVGElement} fits this {@link SvgSelector} attribute conditions, {@code false} otherwise
     */
    private boolean checkAttributes ( final SvgIcon icon, final SVGElement element )
    {
        if ( CollectionUtils.isEmpty ( attributes ) )
        {
            return true;
        }
        else
        {
            for ( final String attribute : attributes )
            {
                if ( icon.hasAttribute ( element, attribute ) )
                {
                    return true;
                }
            }
            return false;
        }
    }
}