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

package com.alee.demo.svg;

import com.alee.managers.log.Log;
import com.alee.utils.ColorUtils;
import com.alee.utils.NetUtils;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGRoot;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * @author Mikle Garin
 */

public class SvgIcon extends SVGIcon
{
    /**
     * Constructs new empty SVG icon.
     */
    public SvgIcon ()
    {
        this ( ( URI ) null );
    }

    /**
     * Constructs new SVG icon based on SVG file.
     *
     * @param file path to SVG file
     */
    public SvgIcon ( final String file )
    {
        this ( new File ( file ).toURI () );
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
        this ( new File ( file ).toURI (), width, height );
    }

    /**
     * Constructs new SVG icon based on SVG file with the specified width and height.
     *
     * @param file   path to SVG file
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final String file, final int width, final int height, final Color color )
    {
        this ( new File ( file ).toURI (), width, height, color );
    }

    /**
     * Constructs new SVG icon based on SVG resource near class.
     *
     * @param clazz class near which SVG resource is located
     * @param path  SVG resource path
     */
    public SvgIcon ( final Class clazz, final String path )
    {
        this ( clazz.getResource ( path ) );
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
        this ( clazz.getResource ( path ), width, height );
    }

    /**
     * Constructs new SVG icon based on SVG resource near class with the specified width and height.
     *
     * @param clazz  class near which SVG resource is located
     * @param path   SVG resource path
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final Class clazz, final String path, final int width, final int height, final Color color )
    {
        this ( clazz.getResource ( path ), width, height, color );
    }

    /**
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param url SVG file URL
     */
    public SvgIcon ( final URL url )
    {
        this ( NetUtils.toURI ( url ) );
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
        this ( NetUtils.toURI ( url ), width, height );
    }

    /**
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param url    SVG file URL
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final URL url, final int width, final int height, final Color color )
    {
        this ( NetUtils.toURI ( url ), width, height, color );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param uri SVG file URI
     */
    public SvgIcon ( final URI uri )
    {
        this ( uri, 16, 16 );
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
        this ( uri, width, height, null );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param uri    SVG file URI
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final URI uri, final int width, final int height, final Color color )
    {
        super ();
        setSvgURI ( uri );
        checkSVGDiagram ();
        setAntiAlias ( true );
        setScaleToFit ( true );
        setPreferredSize ( width, height );
        setColor ( color );
    }

    /**
     * Returns SVG diagram.
     *
     * @return SVG diagram
     */
    protected SVGDiagram getSVGDiagram ()
    {
        return getSvgUniverse ().getDiagram ( getSvgURI () );
    }

    /**
     * Checks SVG diagram existance.
     */
    protected void checkSVGDiagram ()
    {
        if ( getSVGDiagram () == null )
        {
            throw new RuntimeException ( "Unable to load SVG file: " + getSvgURI () );
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

    /**
     * Changes all fill colors for the SVG icon to the specified one.
     * Note that this method is intended only for use against single-colored SVG icons.
     * It might have an unwanted effect on icons which use multiple colors.
     *
     * @param color fill color
     * @return this SVG icon
     */
    public SvgIcon setColor ( final Color color )
    {
        if ( color != null )
        {
            try
            {
                final SVGDiagram diagram = getSVGDiagram ();
                final SVGRoot root = diagram.getRoot ();
                if ( !setColor ( root, color ) )
                {
                    addFill ( root, color );
                }
            }
            catch ( final SVGElementException e )
            {
                Log.error ( e );
            }
        }
        return this;
    }

    /**
     * Sets SVG icon color recursively and returns whether or not some color was actually modified.
     *
     * @param element SVG element
     * @param color   fill color
     * @return true if some color was actually modified, false otherwise
     * @throws com.kitfox.svg.SVGElementException if something went wrong
     */
    protected boolean setColor ( final SVGElement element, final Color color ) throws SVGElementException
    {
        boolean modified = false;
        if ( hasFill ( element ) )
        {
            setFill ( element, color );
            modified = true;
        }
        for ( int i = 0; i < element.getNumChildren (); i++ )
        {
            modified = setColor ( element.getChild ( i ), color ) || modified;
        }
        return modified;
    }

    /**
     * Returns whether or not element has fill attribute specified.
     *
     * @param element SVG element
     * @return true if element has fill attribute specified, false otherwise
     * @throws com.kitfox.svg.SVGElementException if something went wrong
     */
    protected boolean hasFill ( final SVGElement element ) throws SVGElementException
    {
        return element.hasAttribute ( SvgAttributes.FILL, AnimationElement.AT_XML );
    }

    /**
     * Sets SVG element fill attribute to the specified color.
     *
     * @param element SVG element
     * @param color   fill color
     * @throws com.kitfox.svg.SVGElementException if something went wrong
     */
    protected void setFill ( final SVGElement element, final Color color ) throws SVGElementException
    {
        element.setAttribute ( SvgAttributes.FILL, AnimationElement.AT_XML, ColorUtils.getHexColor ( color ) );
    }

    /**
     * Adds SVG element fill attribute with the specified color.
     *
     * @param element SVG element
     * @param color   fill color
     * @throws com.kitfox.svg.SVGElementException if something went wrong
     */
    protected void addFill ( final SVGElement element, final Color color ) throws SVGElementException
    {
        element.addAttribute ( SvgAttributes.FILL, AnimationElement.AT_XML, ColorUtils.getHexColor ( color ) );
    }
}