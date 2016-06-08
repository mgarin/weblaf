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

import com.alee.utils.ColorUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.NetUtils;
import com.alee.utils.TextUtils;
import com.kitfox.svg.*;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.xml.StyleAttribute;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Slightly customized SvgSalamander library {@link com.kitfox.svg.app.beans.SVGIcon} implementation.
 * This extension provides a set of predefined convenient constructors and offers some additional customization methods.
 *
 * Note that distinct {@link com.kitfox.svg.SVGUniverse} should be provided in case you want to reconfigure the same icon differently.
 * Otherwise default universe is used and changes will be applied to all icons coming from the same source.
 *
 * @author Mikle Garin
 */

public class SvgIcon extends SVGIcon
{
    /**
     * Runtime variables.
     */
    protected Color fill;
    protected Point2D translate;
    protected Point2D scale;
    protected Double rotate;

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
     * Constructs new SVG icon based on SVG file with the specified width and height.
     *
     * @param file   path to SVG file
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final String file, final int width, final int height, final Color color )
    {
        this ( SVGCache.getSVGUniverse (), file, width, height, color );
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
        this ( SVGCache.getSVGUniverse (), clazz, path, width, height, color );
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
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param url    SVG file URL
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final URL url, final int width, final int height, final Color color )
    {
        this ( SVGCache.getSVGUniverse (), url, width, height, color );
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
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param uri    SVG file URI
     * @param width  preferred icon width
     * @param height preferred icon height
     * @param color  fill color
     */
    public SvgIcon ( final URI uri, final int width, final int height, final Color color )
    {
        this ( SVGCache.getSVGUniverse (), uri, width, height, color );
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
     * Constructs new SVG icon based on SVG file with the specified width and height.
     *
     * @param universe SVG Universe
     * @param file     path to SVG file
     * @param width    preferred icon width
     * @param height   preferred icon height
     * @param color    fill color
     */
    public SvgIcon ( final SVGUniverse universe, final String file, final int width, final int height, final Color color )
    {
        this ( universe, new File ( file ).toURI (), width, height, color );
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
     * Constructs new SVG icon based on SVG resource near class with the specified width and height.
     *
     * @param universe SVG Universe
     * @param clazz    class near which SVG resource is located
     * @param path     SVG resource path
     * @param width    preferred icon width
     * @param height   preferred icon height
     * @param color    fill color
     */
    public SvgIcon ( final SVGUniverse universe, final Class clazz, final String path, final int width, final int height,
                     final Color color )
    {
        this ( universe, clazz.getResource ( path ), width, height, color );
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
     * Constructs new SVG icon based on SVG file URL.
     *
     * @param universe SVG Universe
     * @param url      SVG file URL
     * @param width    preferred icon width
     * @param height   preferred icon height
     * @param color    fill color
     */
    public SvgIcon ( final SVGUniverse universe, final URL url, final int width, final int height, final Color color )
    {
        this ( universe, NetUtils.toURI ( url ), width, height, color );
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
        this ( universe, uri, width, height, null );
    }

    /**
     * Constructs new SVG icon based on SVG file URI.
     *
     * @param universe SVG Universe
     * @param uri      SVG file URI
     * @param width    preferred icon width
     * @param height   preferred icon height
     * @param color    fill color
     */
    public SvgIcon ( final SVGUniverse universe, final URI uri, final int width, final int height, final Color color )
    {
        super ();
        setSvgUniverse ( universe );
        setSvgURI ( uri );
        checkSVGDiagram ();
        setAntiAlias ( true );
        setScaleToFit ( true );
        setPreferredSize ( width, height );
        fill ( color );
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
     * Checks SVG diagram existance.
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
    protected SVGRoot getRoot ()
    {
        return getDiagram ().getRoot ();
    }

    /**
     * Returns whether or not element has specified attribute.
     *
     * @param element   SVG element
     * @param attribute attribute name
     * @return true if element has specified attribute, false otherwise
     */
    protected boolean hasAttribute ( final SVGElement element, final String attribute )
    {
        try
        {
            return element.hasAttribute ( attribute, AnimationElement.AT_XML );
        }
        catch ( final SVGElementException e )
        {
            throw new RuntimeException ( "Unable to check attribte \"" + attribute + "\" existance for element: " + element );
        }
    }

    /**
     * Returns element attribute for the specified attribute name.
     *
     * @param element   SVG element
     * @param attribute attribute name
     * @return element attribute for the specified attribute name
     */
    protected StyleAttribute getAttribute ( final SVGElement element, final String attribute )
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
    protected void setAttribute ( final SVGElement element, final String attribute, final String value )
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
     * Changes all fill colors for the SVG icon to the specified one.
     * Note that this method is intended only for use against single-colored SVG icons.
     * It might have an unwanted effect on icons which use multiple colors.
     *
     * @param fill fill color
     * @return this SVG icon
     */
    public SvgIcon fill ( final Color fill )
    {
        if ( !CompareUtils.equals ( this.fill, fill ) )
        {
            this.fill = fill;
            fill ( getRoot (), fill );
        }
        return this;
    }

    /**
     * Sets SVG icon fill color recursively.
     * It will either set global fill attribute or replace existing ones.
     *
     * @param element SVG element
     * @param color   fill color
     */
    protected void fill ( final SVGElement element, final Color color )
    {
        if ( !replaceFill ( element, color ) )
        {
            setAttribute ( element, SvgAttributes.FILL, ColorUtils.getHexColor ( color ) );
        }
    }

    /**
     * Replaces SVG icon fill color recursively and returns whether or not some color was actually modified.
     *
     * @param element SVG element
     * @param color   fill color
     * @return true if some color was actually modified, false otherwise
     */
    protected boolean replaceFill ( final SVGElement element, final Color color )
    {
        boolean modified = false;
        if ( hasAttribute ( element, SvgAttributes.FILL ) )
        {
            setAttribute ( element, SvgAttributes.FILL, ColorUtils.getHexColor ( color ) );
            modified = true;
        }
        for ( int i = 0; i < element.getNumChildren (); i++ )
        {
            modified = replaceFill ( element.getChild ( i ), color ) || modified;
        }
        return modified;
    }

    /**
     * Applies provided transformations to this icon.
     *
     * @param translate icon X and Y translation
     * @param scale     icon X and Y scaling
     * @param rotate    icon rotation in degrees
     * @return this SVG icon
     */
    public SvgIcon transform ( final Point2D translate, final Point2D scale, final Double rotate )
    {
        if ( !CompareUtils.equals ( this.translate, translate ) ||
                !CompareUtils.equals ( this.scale, scale ) ||
                !CompareUtils.equals ( this.rotate, rotate ) )
        {
            this.translate = translate;
            this.scale = scale;
            this.rotate = rotate;
            final SVGRoot element = getRoot ();
            transform ( element, translate, scale, rotate );
            update ( element );
        }
        return this;
    }

    /**
     * Applies translation to this icon.
     *
     * @param translate icon X and Y translation
     * @return this SVG icon
     */
    public SvgIcon translate ( final Point2D translate )
    {
        if ( !CompareUtils.equals ( this.translate, translate ) )
        {
            this.translate = translate;
            final SVGRoot element = getRoot ();
            transform ( element, translate, scale, rotate );
            update ( element );
        }
        return this;
    }

    /**
     * Applies scaling to this icon.
     *
     * @param scale icon X and Y scaling
     * @return this SVG icon
     */
    public SvgIcon scale ( final Point2D scale )
    {
        if ( !CompareUtils.equals ( this.scale, scale ) )
        {
            this.scale = scale;
            final SVGRoot element = getRoot ();
            transform ( element, translate, scale, rotate );
            update ( element );
        }
        return this;
    }

    /**
     * Applies rotation to this icon.
     *
     * @param rotate icon rotation in degrees
     * @return this SVG icon
     */
    public SvgIcon rotate ( final Double rotate )
    {
        if ( !CompareUtils.equals ( this.rotate, rotate ) )
        {
            this.rotate = rotate;
            final SVGRoot element = getRoot ();
            transform ( element, translate, scale, rotate );
            update ( element );
        }
        return this;
    }

    /**
     * Applies provided transformations to this icon.
     *
     * @param element   SVG element
     * @param translate icon X and Y translation
     * @param scale     icon X and Y scaling
     * @param rotate    icon rotation in degrees
     */
    protected void transform ( final SVGElement element, final Point2D translate, final Point2D scale, final Double rotate )
    {
        if ( translate != null || scale != null || rotate != null )
        {
            String transform = "";
            if ( translate != null )
            {
                transform += "translate(" + translate.getX () + " " + translate.getY () + ")";
            }
            if ( scale != null )
            {
                if ( !TextUtils.isEmpty ( transform ) )
                {
                    transform += " ";
                }
                final Dimension ps = getPreferredSize ();
                final double x = ( ps.width - ps.width * scale.getX () ) / 2;
                final double y = ( ps.height - ps.height * scale.getY () ) / 2;
                transform += "translate(" + x + " " + y + ") scale(" + scale.getX () + " " + scale.getY () + ")";
            }
            if ( rotate != null )
            {
                if ( !TextUtils.isEmpty ( transform ) )
                {
                    transform += " ";
                }
                final Dimension ps = getPreferredSize ();
                transform += "rotate(" + rotate + " " + ps.width / 2 + " " + ps.height / 2 + ")";
            }
            setAttribute ( element, SvgAttributes.TRANSFORM, transform );
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