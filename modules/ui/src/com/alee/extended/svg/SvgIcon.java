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

import com.alee.utils.CompareUtils;
import com.alee.utils.NetUtils;
import com.alee.utils.TextUtils;
import com.kitfox.svg.*;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.xml.StyleAttribute;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Slightly customized SvgSalamander library {@link com.kitfox.svg.app.beans.SVGIcon} implementation.
 * This extension provides a set of predefined convenient constructors and offers some additional customization methods.
 *
 * Note that distinct {@link com.kitfox.svg.SVGUniverse} should be provided in case you want to reconfigure the same icon differently.
 * Otherwise default universe is used and changes will be applied to all icons coming from the same source.
 *
 * When you want to modify some SVG settings you will have to reference SVG elements.
 * This implementation uses css-like selectors which could reference:
 *
 * 1. Element by its ID, example: {@code #id}
 * In that case all SVG elements with the specified ID will be adjusted.
 *
 * 2. Element by its class, example: {@code .name}
 * In that case all SVG elements with the specified class name will be adjusted.
 *
 * 3. Element by its name, example: {@code svg}, {@code path} or any other SVG element
 * In that case all SVG elements of the specified type will be adjusted.
 *
 * @author Mikle Garin
 */

public class SvgIcon extends SVGIcon
{
    /**
     * Constructs new empty SVG icon.
     */
    public SvgIcon ()
    {
        this ( SVGCache.getSVGUniverse () );
    }

    /**
     * Constructs new SVG icon based on SVG file.
     *
     * @param file path to SVG file
     */
    public SvgIcon ( final String file )
    {
        this ( SVGCache.getSVGUniverse (), file );
    }

    /**
     * Constructs new SVG icon based on SVG file with the specified width and height.
     *
     * @param file   path to SVG file
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final String file, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), file, width, height );
    }

    /**
     * Constructs new SVG icon based on SVG resource near class.
     *
     * @param clazz class near which SVG resource is located
     * @param path  SVG resource path
     */
    public SvgIcon ( final Class clazz, final String path )
    {
        this ( SVGCache.getSVGUniverse (), clazz, path );
    }

    /**
     * Constructs new SVG icon based on SVG resource near class with the specified width and height.
     *
     * @param clazz  class near which SVG resource is located
     * @param path   SVG resource path
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final Class clazz, final String path, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), clazz, path, width, height );
    }

    /**
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param url SVG file URL
     */
    public SvgIcon ( final URL url )
    {
        this ( SVGCache.getSVGUniverse (), url );
    }

    /**
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param url    SVG file URL
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final URL url, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), url, width, height );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param uri SVG file URI
     */
    public SvgIcon ( final URI uri )
    {
        this ( SVGCache.getSVGUniverse (), uri );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param uri    SVG file URI
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final URI uri, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), uri, width, height );
    }

    /**
     * Constructs new empty SVG icon.
     *
     * @param universe SVG Universe
     */
    public SvgIcon ( final SVGUniverse universe )
    {
        this ( universe, ( URI ) null );
    }

    /**
     * Constructs new SVG icon based on SVG file.
     *
     * @param universe SVG Universe
     * @param file     path to SVG file
     */
    public SvgIcon ( final SVGUniverse universe, final String file )
    {
        this ( universe, new File ( file ).toURI () );
    }

    /**
     * Constructs new SVG icon based on SVG file with the specified width and height.
     *
     * @param universe SVG Universe
     * @param file     path to SVG file
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final String file, final int width, final int height )
    {
        this ( universe, new File ( file ).toURI (), width, height );
    }

    /**
     * Constructs new SVG icon based on SVG resource near class.
     *
     * @param universe SVG Universe
     * @param clazz    class near which SVG resource is located
     * @param path     SVG resource path
     */
    public SvgIcon ( final SVGUniverse universe, final Class clazz, final String path )
    {
        this ( universe, clazz.getResource ( path ) );
    }

    /**
     * Constructs new SVG icon based on SVG resource near class with the specified width and height.
     *
     * @param universe SVG Universe
     * @param clazz    class near which SVG resource is located
     * @param path     SVG resource path
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final Class clazz, final String path, final int width, final int height )
    {
        this ( universe, clazz.getResource ( path ), width, height );
    }

    /**
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param universe SVG Universe
     * @param url      SVG file URL
     */
    public SvgIcon ( final SVGUniverse universe, final URL url )
    {
        this ( universe, NetUtils.toURI ( url ) );
    }

    /**
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param universe SVG Universe
     * @param url      SVG file URL
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final URL url, final int width, final int height )
    {
        this ( universe, NetUtils.toURI ( url ), width, height );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param universe SVG Universe
     * @param uri      SVG file URI
     */
    public SvgIcon ( final SVGUniverse universe, final URI uri )
    {
        this ( universe, uri, 16, 16 );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param universe SVG Universe
     * @param uri      SVG file URI
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final URI uri, final int width, final int height )
    {
        super ();
        setSvgUniverse ( universe );
        setSvgURI ( uri );
        checkSVGDiagram ();
        setAntiAlias ( true );
        setScaleToFit ( true );
        setPreferredSize ( width, height );
    }

    /**
     * Returns SVG diagram.
     *
     * @return SVG diagram
     */
    protected SVGDiagram getDiagram ()
    {
        return getSvgUniverse ().getDiagram ( getSvgURI () );
    }

    /**
     * Checks SVG diagram existence.
     */
    protected void checkSVGDiagram ()
    {
        if ( getDiagram () == null )
        {
            throw new RuntimeException ( "Unable to load SVG file: " + getSvgURI () );
        }
    }

    /**
     * Returns SVG diagram root.
     *
     * @return SVG diagram root
     */
    public SVGRoot getRoot ()
    {
        return getDiagram ().getRoot ();
    }

    /**
     * Returns list of {@link SVGElement} for the specified selector.
     *
     * @param selector css-like selector
     * @return list of {@link SVGElement} for the specified selector
     */
    public List<SVGElement> find ( final String selector )
    {
        return find ( selector, getRoot () );
    }

    /**
     * Returns list of {@link SVGElement} for the specified selector.
     *
     * @param selector css-like selector
     * @param element  root {@link SVGElement} to start search from
     * @return list of {@link SVGElement} for the specified selector
     */
    public List<SVGElement> find ( final String selector, final SVGElement element )
    {
        return find ( selector, element, new ArrayList<SVGElement> ( 1 ) );
    }

    /**
     * Returns list of {@link SVGElement} for the specified selector.
     *
     * @param selector css-like selector
     * @param element  root {@link SVGElement} to start search from
     * @param result   list to place results into
     * @return list of {@link SVGElement} for the specified selector
     */
    public List<SVGElement> find ( final String selector, final SVGElement element, final List<SVGElement> result )
    {
        if ( isApplicable ( selector, element ) )
        {
            result.add ( element );
        }
        for ( int i = 0; i < element.getNumChildren (); i++ )
        {
            find ( selector, element.getChild ( i ), result );
        }
        return result;
    }

    /**
     * Returns whether or not specified {@link SVGElement} fits selector conditions.
     *
     * @param selector css-like selector
     * @param element  {@link SVGElement}
     * @return true if specified {@link SVGElement} fits selector conditions, false otherwise
     */
    protected boolean isApplicable ( final String selector, final SVGElement element )
    {
        if ( !TextUtils.isEmpty ( selector ) )
        {
            if ( selector.startsWith ( "#" ) )
            {
                final String id = selector.substring ( 1 );
                final boolean exist = hasAttribute ( element, SvgElements.ID );
                return exist && CompareUtils.equals ( id, getAttribute ( element, SvgElements.ID ).getStringValue () );
            }
            else if ( selector.startsWith ( "." ) )
            {
                final String style = selector.substring ( 1 );
                final boolean exist = hasAttribute ( element, SvgElements.CLAZZ );
                return exist && CompareUtils.equals ( style, getAttribute ( element, SvgElements.CLAZZ ).getStringValue () );
            }
            else
            {
                return element.getClass () == SvgElements.CLASSES.get ( selector );
            }
        }
        else
        {
            throw new RuntimeException ( "SVG element selector cannot be empty" );
        }
    }

    /**
     * Returns whether or not element has specified attribute.
     *
     * @param element   SVG element
     * @param attribute attribute name
     * @return true if element has specified attribute, false otherwise
     */
    public boolean hasAttribute ( final SVGElement element, final String attribute )
    {
        try
        {
            return element.hasAttribute ( attribute, AnimationElement.AT_XML );
        }
        catch ( final SVGElementException e )
        {
            throw new RuntimeException ( "Unable to check attribte \"" + attribute + "\" existence for element: " + element );
        }
    }

    /**
     * Returns element attribute for the specified attribute name.
     *
     * @param element   SVG element
     * @param attribute attribute name
     * @return element attribute for the specified attribute name
     */
    public StyleAttribute getAttribute ( final SVGElement element, final String attribute )
    {
        return element.getPresAbsolute ( attribute );
    }

    /**
     * Adds or replaces element attribute value.
     *
     * @param element   SVG element
     * @param attribute attribute name
     * @param value     new attribute value
     */
    public void setAttribute ( final SVGElement element, final String attribute, final String value )
    {
        try
        {
            if ( hasAttribute ( element, attribute ) )
            {
                element.setAttribute ( attribute, AnimationElement.AT_XML, value );
            }
            else
            {
                element.addAttribute ( attribute, AnimationElement.AT_XML, value );
            }
            update ( element );
        }
        catch ( final SVGElementException e )
        {
            throw new RuntimeException (
                    "Unable to set SVG attribute \"" + attribute + "\" with value \"" + value + "\" for element: " + element );
        }
    }

    /**
     * Updates specified element data.
     *
     * @param element SVG element
     */
    protected void update ( final SVGElement element )
    {
        try
        {
            element.updateTime ( 0 );
        }
        catch ( final SVGException e )
        {
            throw new RuntimeException ( "Unable to set update element: " + element );
        }
    }

    /**
     * Sets SVG icon preferred size.
     *
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public void setPreferredSize ( final int width, final int height )
    {
        setPreferredSize ( new Dimension ( width, height ) );
    }
}