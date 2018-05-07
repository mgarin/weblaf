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

import com.alee.managers.icon.data.IconAdjustment;
import com.alee.utils.NetUtils;
import com.kitfox.svg.*;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.xml.StyleAttribute;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Slightly customized SvgSalamander library {@link SVGIcon} implementation.
 * This extension provides a set of predefined convenient constructors and offers some additional customization methods.
 *
 * Note that distinct {@link SVGUniverse} should be provided in case you want to reconfigure the same icon differently.
 * Otherwise default universe is used and changes will be applied to all icons coming from the same source.
 *
 * When you want to modify some SVG settings you will have to find specific SVG elements within {@link SVGDiagram}.
 * This is where css-like selectors will help you a lot, check out {@link SvgSelector} JavaDoc for more information on syntax.
 *
 * @author Mikle Garin
 * @see SvgSelector
 */
public class SvgIcon extends SVGIcon
{
    /**
     * Cached raster image.
     */
    protected transient BufferedImage cache;

    /**
     * Constructs new empty {@link SvgIcon}.
     */
    public SvgIcon ()
    {
        this ( SVGCache.getSVGUniverse () );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file.
     *
     * @param file path to SVG file
     */
    public SvgIcon ( final String file )
    {
        this ( SVGCache.getSVGUniverse (), file );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file with the specified width and height.
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
     * Constructs new {@link SvgIcon} based on SVG resource near {@link Class}.
     *
     * @param clazz {@link Class} near which SVG resource is located
     * @param path  SVG resource path
     */
    public SvgIcon ( final Class clazz, final String path )
    {
        this ( SVGCache.getSVGUniverse (), clazz, path );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG resource near {@link Class} with the specified width and height.
     *
     * @param clazz  {@link Class} near which SVG resource is located
     * @param path   SVG resource path
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final Class clazz, final String path, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), clazz, path, width, height );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URL}.
     *
     * @param url SVG file {@link URL}
     */
    public SvgIcon ( final URL url )
    {
        this ( SVGCache.getSVGUniverse (), url );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URL}.
     *
     * @param url    SVG file {@link URL}
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final URL url, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), url, width, height );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URI}.
     *
     * @param uri SVG file {@link URI}
     */
    public SvgIcon ( final URI uri )
    {
        this ( SVGCache.getSVGUniverse (), uri );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URI}.
     *
     * @param uri    SVG file {@link URI}
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public SvgIcon ( final URI uri, final int width, final int height )
    {
        this ( SVGCache.getSVGUniverse (), uri, width, height );
    }

    /**
     * Constructs new empty {@link SvgIcon}.
     *
     * @param universe {@link SVGUniverse}
     */
    public SvgIcon ( final SVGUniverse universe )
    {
        this ( universe, ( URI ) null );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file.
     *
     * @param universe {@link SVGUniverse}
     * @param file     path to SVG file
     */
    public SvgIcon ( final SVGUniverse universe, final String file )
    {
        this ( universe, new File ( file ).toURI () );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file with the specified width and height.
     *
     * @param universe {@link SVGUniverse}
     * @param file     path to SVG file
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final String file, final int width, final int height )
    {
        this ( universe, new File ( file ).toURI (), width, height );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG resource near {@link Class}.
     *
     * @param universe {@link SVGUniverse}
     * @param clazz    {@link Class} near which SVG resource is located
     * @param path     SVG resource path
     */
    public SvgIcon ( final SVGUniverse universe, final Class clazz, final String path )
    {
        this ( universe, clazz.getResource ( path ) );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG resource near {@link Class} with the specified width and height.
     *
     * @param universe {@link SVGUniverse}
     * @param clazz    {@link Class} near which SVG resource is located
     * @param path     SVG resource path
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final Class clazz, final String path, final int width, final int height )
    {
        this ( universe, clazz.getResource ( path ), width, height );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URL}.
     *
     * @param universe {@link SVGUniverse}
     * @param url      SVG file {@link URL}
     */
    public SvgIcon ( final SVGUniverse universe, final URL url )
    {
        this ( universe, NetUtils.toURI ( url ) );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URL}.
     *
     * @param universe {@link SVGUniverse}
     * @param url      SVG file {@link URL}
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( final SVGUniverse universe, final URL url, final int width, final int height )
    {
        this ( universe, NetUtils.toURI ( url ), width, height );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URI}.
     *
     * @param universe {@link SVGUniverse}
     * @param uri      SVG file {@link URI}
     */
    public SvgIcon ( final SVGUniverse universe, final URI uri )
    {
        this ( universe, uri, 16, 16 );
    }

    /**
     * Constructs new {@link SvgIcon} based on SVG file {@link URI}.
     *
     * @param universe {@link SVGUniverse}
     * @param uri      SVG file {@link URI}
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
     * Applies all specified {@link IconAdjustment}s to this {@link SvgIcon}.
     *
     * @param adjustments {@link IconAdjustment}s to apply
     */
    public void apply ( final IconAdjustment<SvgIcon>... adjustments )
    {
        for ( final IconAdjustment<SvgIcon> adjustment : adjustments )
        {
            adjustment.apply ( this );
        }
    }

    /**
     * Applies all specified {@link IconAdjustment}s to this {@link SvgIcon}.
     *
     * @param adjustments {@link IconAdjustment}s to apply
     */
    public void apply ( final List<? extends IconAdjustment<SvgIcon>> adjustments )
    {
        for ( final IconAdjustment<SvgIcon> adjustment : adjustments )
        {
            adjustment.apply ( this );
        }
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
            final String msg = "Unable to load SVG file: %s";
            throw new RuntimeException ( String.format ( msg, getSvgURI () ) );
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
     * Returns list of {@link SVGElement} for the specified {@link SvgSelector}.
     *
     * @param selector {@link SvgSelector}
     * @return list of {@link SVGElement} for the specified {@link SvgSelector}
     */
    public List<SVGElement> find ( final SvgSelector selector )
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
     * Returns list of {@link SVGElement} for the specified {@link SvgSelector}.
     *
     * @param selector {@link SvgSelector}
     * @param element  root {@link SVGElement} to start search from
     * @return list of {@link SVGElement} for the specified {@link SvgSelector}
     */
    public List<SVGElement> find ( final SvgSelector selector, final SVGElement element )
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
        return find ( new SvgSelector ( selector ), element, result );
    }

    /**
     * Returns list of {@link SVGElement} for the specified {@link SvgSelector}.
     *
     * @param selector {@link SvgSelector}
     * @param element  root {@link SVGElement} to start search from
     * @param result   list to place results into
     * @return list of {@link SVGElement} for the specified {@link SvgSelector}
     */
    public List<SVGElement> find ( final SvgSelector selector, final SVGElement element, final List<SVGElement> result )
    {
        if ( selector.isApplicable ( this, element ) )
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
            final String msg = "Unable to check attribute %s existence for element: %s";
            throw new RuntimeException ( String.format ( msg, attribute, element ) );
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
            final String msg = "Unable to set SVG attribute %s with value %s for element: %s";
            throw new RuntimeException ( String.format ( msg, attribute, value, element ) );
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
            // Updating SVG diagram
            element.updateTime ( 0 );

            // Cleaning up cache
            cache = null;
        }
        catch ( final SVGException e )
        {
            final String msg = "Unable to update element: %s";
            throw new RuntimeException ( String.format ( msg, element ) );
        }
    }

    @Override
    public void paintIcon ( final Component component, final Graphics g, final int x, final int y )
    {
        // Validating cache
        final Dimension size = getPreferredSize ();
        if ( cache == null || cache.getWidth () != size.width || cache.getHeight () != size.height )
        {
            // Flushing previous icon cache
            if ( cache != null )
            {
                cache.flush ();
            }

            // Create new cached image
            cache = asBufferedImage ( size );
        }

        // Painting SVG icon from raster cache image
        g.drawImage ( cache, x, y, null );
    }

    /**
     * Sets {@link SvgIcon} preferred size.
     *
     * @param width  preferred icon width
     * @param height preferred icon height
     */
    public void setPreferredSize ( final int width, final int height )
    {
        setPreferredSize ( new Dimension ( width, height ) );
    }

    /**
     * Returns this {@link SvgIcon} painted on {@link BufferedImage}.
     * Preferred {@link SvgIcon} size will be used to determine {@link BufferedImage} size.
     *
     * @return this {@link SvgIcon} painted on {@link BufferedImage}
     */
    public BufferedImage asBufferedImage ()
    {
        return asBufferedImage ( getPreferredSize () );
    }

    /**
     * Returns this {@link SvgIcon} painted on {@link BufferedImage} of the specified size.
     *
     * @param size resulting {@link BufferedImage} size
     * @return this {@link SvgIcon} painted on {@link BufferedImage} of the specified size
     */
    public BufferedImage asBufferedImage ( final Dimension size )
    {
        return asBufferedImage ( size.width, size.height );
    }

    /**
     * Returns this {@link SvgIcon} painted on {@link BufferedImage} of the specified size.
     *
     * @param width  resulting {@link BufferedImage} width
     * @param height resulting {@link BufferedImage} height
     * @return this {@link SvgIcon} painted on {@link BufferedImage} of the specified size
     */
    public BufferedImage asBufferedImage ( final int width, final int height )
    {
        // Save initial preferred size
        final Dimension ps = getPreferredSize ();

        // Setup temporary preferred size
        setPreferredSize ( width, height );

        // Create image
        final BufferedImage image = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D g2d = image.createGraphics ();
        super.paintIcon ( null, g2d, 0, 0 );
        g2d.dispose ();

        // Restoring initial preferred size
        setPreferredSize ( ps );

        return image;
    }
}