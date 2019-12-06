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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.Clone;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.api.resource.Resource;
import com.alee.api.ui.DisabledCopySupplier;
import com.alee.api.ui.TransparentCopySupplier;
import com.alee.managers.icon.data.IconAdjustment;
import com.kitfox.svg.*;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.xml.StyleAttribute;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Slightly customized SvgSalamander library {@link SVGIcon} implementation.
 * This extension provides convenient constructors and methods for diagram modification.
 *
 * Note that distinct {@link SVGUniverse} should be provided in case you want to reconfigure the same icon differently.
 * Otherwise default {@link SVGUniverse} is used and changes will be applied to all icons coming from the same source.
 *
 * When you want to modify some SVG settings you will have to find specific SVG elements within {@link SVGDiagram}.
 * This is where css-like selectors will help you a lot, check out {@link SvgSelector} JavaDoc for more information on syntax.
 *
 * @author Mikle Garin
 * @see SvgSelector
 */
public class SvgIcon extends SVGIcon implements DisabledCopySupplier<SvgIcon>, TransparentCopySupplier<SvgIcon>, Cloneable
{
    /**
     * Cached raster image.
     */
    @OmitOnClone
    @OmitOnMerge
    @Nullable
    protected transient BufferedImage cache;

    /**
     * Constructs new {@link SvgIcon} based on {@link Resource}.
     *
     * @param resource SVG icon {@link Resource}
     */
    public SvgIcon ( @NotNull final Resource resource )
    {
        this ( resource, 16, 16 );
    }

    /**
     * Constructs new {@link SvgIcon} based on {@link Resource}.
     *
     * @param resource SVG icon {@link Resource}
     * @param width    preferred icon width
     * @param height   preferred icon height
     */
    public SvgIcon ( @NotNull final Resource resource, final int width, final int height )
    {
        try
        {
            // Loading SVG icon
            final SVGUniverse universe = new SVGUniverse ();
            final URI uri = universe.loadSVG ( resource.getInputStream (), "SvgIcon", true );

            // Checking diagram
            checkDiagram ( universe, uri );

            // Updating settings
            setSvgUniverse ( universe );
            setSvgURI ( uri );
            setAntiAlias ( true );
            setAutosize ( AUTOSIZE_STRETCH );
            setPreferredSize ( width, height );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to load SVG from resource: %s";
            throw new RuntimeException ( String.format ( msg, resource ), e );
        }
    }

    /**
     * Checks {@link SVGDiagram} existence in {@link SVGUniverse} for the specified {@link URI}.
     *
     * @param universe {@link SVGUniverse}
     * @param uri      {@link URI}
     */
    protected void checkDiagram ( @NotNull final SVGUniverse universe, @NotNull final URI uri )
    {
        if ( universe.getDiagram ( uri ) == null )
        {
            final String msg = "Unable to load SVG file: %s";
            throw new RuntimeException ( String.format ( msg, getSvgURI () ) );
        }
    }

    /**
     * Applies all specified {@link IconAdjustment}s to this {@link SvgIcon}.
     *
     * @param adjustments {@link IconAdjustment}s to apply
     */
    public void apply ( @NotNull final IconAdjustment<SvgIcon>... adjustments )
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
    public void apply ( @NotNull final List<? extends IconAdjustment<SvgIcon>> adjustments )
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
    @NotNull
    protected SVGDiagram getDiagram ()
    {
        return getSvgUniverse ().getDiagram ( getSvgURI () );
    }

    /**
     * Returns SVG diagram root.
     *
     * @return SVG diagram root
     */
    @NotNull
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
    @NotNull
    public List<SVGElement> find ( @NotNull final String selector )
    {
        return find ( selector, getRoot () );
    }

    /**
     * Returns list of {@link SVGElement} for the specified {@link SvgSelector}.
     *
     * @param selector {@link SvgSelector}
     * @return list of {@link SVGElement} for the specified {@link SvgSelector}
     */
    @NotNull
    public List<SVGElement> find ( @NotNull final SvgSelector selector )
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
    @NotNull
    public List<SVGElement> find ( @NotNull final String selector, @NotNull final SVGElement element )
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
    @NotNull
    public List<SVGElement> find ( @NotNull final SvgSelector selector, @NotNull final SVGElement element )
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
    @NotNull
    public List<SVGElement> find ( @NotNull final String selector, @NotNull final SVGElement element,
                                   @NotNull final List<SVGElement> result )
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
    @NotNull
    public List<SVGElement> find ( @NotNull final SvgSelector selector, @NotNull final SVGElement element,
                                   @NotNull final List<SVGElement> result )
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
    public boolean hasAttribute ( @NotNull final SVGElement element, @NotNull final String attribute )
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
    @Nullable
    public StyleAttribute getAttribute ( @NotNull final SVGElement element, @NotNull final String attribute )
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
    public void setAttribute ( @NotNull final SVGElement element, @NotNull final String attribute, @Nullable final String value )
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
     * Removes element attribute.
     *
     * @param element   SVG element
     * @param attribute attribute name
     */
    public void removeAttribute ( @NotNull final SVGElement element, @NotNull final String attribute )
    {
        if ( !attribute.equals ( SvgElements.ID ) )
        {
            if ( hasAttribute ( element, attribute ) )
            {
                element.getPresentationAttributes ().remove ( attribute );
            }
            update ( element );
        }
        else
        {
            final String msg = "SVG element identifier attribute cannot be removed: %s";
            throw new RuntimeException ( String.format ( msg, element ) );
        }
    }

    /**
     * Updates specified element data.
     *
     * @param element SVG element
     */
    protected void update ( @NotNull final SVGElement element )
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
    public void paintIcon ( @NotNull final Component component, @NotNull final Graphics g, final int x, final int y )
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
    @NotNull
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
    @NotNull
    public BufferedImage asBufferedImage ( @NotNull final Dimension size )
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
    @NotNull
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

    /**
     * Returns copy of this {@link SvgIcon} with adjustments making it look disabled.
     * Note that disabled version will use separate {@link SVGUniverse} to avoid causing adjustments in other {@link SvgIcon}s.
     *
     * @return copy of this {@link SvgIcon} with adjustments making it look disabled
     */
    @NotNull
    @Override
    public SvgIcon createDisabledCopy ()
    {
        final SvgIcon svgIcon = clone ();
        svgIcon.apply ( new SvgGrayscale () );
        svgIcon.apply ( new SvgOpacity ( 0.7d ) );
        return svgIcon;
    }

    /**
     * Returns copy of this {@link SvgIcon} with adjustments making it semi-transparent.
     * Note that semi-transparent version will use separate {@link SVGUniverse} to avoid causing adjustments in other {@link SvgIcon}s.
     *
     * @param opacity opacity value, must be between 0 and 1
     * @return copy of this {@link SvgIcon} with adjustments making it semi-transparent
     */
    @NotNull
    @Override
    public SvgIcon createTransparentCopy ( final float opacity )
    {
        final SvgIcon svgIcon = clone ();
        svgIcon.apply ( new SvgOpacity ( ( double ) opacity ) );
        return svgIcon;
    }

    @NotNull
    @Override
    protected SvgIcon clone ()
    {
        // todo Preserve Resource & adjustments instead and simply create new icon
        return Clone.reflective ().nonNullClone ( this );
    }
}