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

package com.alee.painter.decoration.layout;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.IContent;
import com.alee.utils.collection.ImmutableList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link com.alee.extended.layout.AlignLayout} implementation of {@link IContentLayout}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 * @see com.alee.extended.layout.AlignLayout
 * @see AbstractContentLayout
 * @see IContentLayout
 */
@XStreamAlias ( "AlignLayout" )
public class AlignLayout<C extends JComponent, D extends IDecoration<C, D>, I extends AlignLayout<C, D, I>>
        extends AbstractContentLayout<C, D, I>
{
    /**
     * todo 1. Take orientation into account
     * todo 2. Center element doesn't take side ones into account when placed
     */

    /**
     * Layout constraints.
     */
    protected static final String LEFT = "left";
    protected static final String RIGHT = "right";
    protected static final String TOP = "top";
    protected static final String BOTTOM = "bottom";
    protected static final String CENTER = "center";

    /**
     * Horizontal alignment constraints.
     */
    public static final List<String> horizontals = new ImmutableList<String> ( LEFT, CENTER, RIGHT );

    /**
     * Vertical alignment constraints.
     */
    public static final List<String> verticals = new ImmutableList<String> ( TOP, CENTER, BOTTOM );

    /**
     * Layout constraints separator.
     */
    public static final String SEPARATOR = ",";

    /**
     * Horizontal gap between content elements.
     */
    @XStreamAsAttribute
    protected Integer hgap;

    /**
     * Vertical gap between content elements.
     */
    @XStreamAsAttribute
    protected Integer vgap;

    /**
     * Whether or not content elements should fill all available horizontal space.
     */
    @XStreamAsAttribute
    protected Boolean hfill;

    /**
     * Whether or not content elements should fill all available vertical space.
     */
    @XStreamAsAttribute
    protected Boolean vfill;

    /**
     * Returns horizontal gap between content elements.
     *
     * @return horizontal gap between content elements
     */
    public int getHorizontalGap ()
    {
        return hgap != null ? hgap : 0;
    }

    /**
     * Returns vertical gap between content elements.
     *
     * @return vertical gap between content elements
     */
    public int getVerticalGap ()
    {
        return vgap != null ? vgap : 0;
    }

    /**
     * Returns whether content elements should fill all available horizontal space or not.
     *
     * @return {@code true} if content elements should fill all available horizontal space, {@code false} otherwise
     */
    public boolean isHorizontalFill ()
    {
        return hfill != null && hfill;
    }

    /**
     * Returns whether content elements should fill all available vertical space or not.
     *
     * @return {@code true} if content elements should fill all available vertical space, {@code false} otherwise
     */
    public boolean isVerticalFill ()
    {
        return vfill != null && vfill;
    }

    @Override
    public ContentLayoutData layoutContent ( final C c, final D d, final Rectangle bounds )
    {
        final ContentLayoutData layoutData = new ContentLayoutData ( 2 );
        final boolean hfill = isHorizontalFill ();
        final boolean vfill = isHorizontalFill ();
        for ( final String halign : horizontals )
        {
            for ( final String valign : verticals )
            {
                final String constraints = constraints ( halign, valign );
                if ( !isEmpty ( c, d, constraints ) )
                {
                    // Component size
                    final Dimension ps = getPreferredSize ( c, d, constraints, bounds.getSize () );
                    ps.width = Math.min ( ps.width, bounds.width );
                    ps.height = Math.min ( ps.height, bounds.height );

                    // Determining x coordinate
                    final int x;
                    if ( hfill )
                    {
                        x = bounds.x;
                    }
                    else
                    {
                        if ( halign.equals ( LEFT ) )
                        {
                            x = bounds.x;
                        }
                        else if ( halign.equals ( CENTER ) )
                        {
                            x = bounds.x + bounds.width / 2 - ps.width / 2;
                        }
                        else if ( halign.equals ( RIGHT ) )
                        {
                            x = bounds.x + bounds.width - ps.width;
                        }
                        else
                        {
                            throw new IllegalArgumentException ( "Unknown horizontal alignment: " + halign );
                        }
                    }

                    // Determining y coordinate
                    final int y;
                    if ( vfill )
                    {
                        y = bounds.y;
                    }
                    else
                    {
                        if ( valign.equals ( TOP ) )
                        {
                            y = bounds.y;
                        }
                        else if ( valign.equals ( CENTER ) )
                        {
                            y = bounds.y + bounds.height / 2 - ps.height / 2;
                        }
                        else if ( valign.equals ( BOTTOM ) )
                        {
                            y = bounds.y + bounds.height - ps.height;
                        }
                        else
                        {
                            throw new IllegalArgumentException ( "Unknown vertical alignment: " + valign );
                        }
                    }

                    // Determining width and height
                    final int width = hfill ? bounds.width : ps.width;
                    final int height = vfill ? bounds.height : ps.height;

                    // Saving data for constraints
                    layoutData.put ( constraints, new Rectangle ( x, y, width, height ) );
                }
            }
        }
        return layoutData;
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        final Dimension ps;
        final List<IContent> contents = getContents ( c, d );
        if ( contents.size () > 1 )
        {
            final int hgap = getHorizontalGap ();
            final int vgap = getVerticalGap ();
            final boolean hfill = isHorizontalFill ();
            final boolean vfill = isHorizontalFill ();

            // Counting size for each block
            final Map<String, Integer> widths = new HashMap<String, Integer> ();
            final Map<String, Integer> heights = new HashMap<String, Integer> ();
            if ( !hfill || !vfill )
            {
                for ( final String halign : horizontals )
                {
                    for ( final String valign : verticals )
                    {
                        final Dimension size = getAreaSize ( c, d, available, halign, valign );
                        if ( size != null )
                        {
                            if ( !hfill )
                            {
                                final int width = widths.containsKey ( halign ) ? widths.get ( halign ) : 0;
                                widths.put ( halign, Math.max ( width, size.width ) );
                            }
                            if ( !vfill )
                            {
                                final int height = widths.containsKey ( valign ) ? widths.get ( valign ) : 0;
                                heights.put ( valign, Math.max ( height, size.height ) );
                            }
                        }
                    }
                }
            }

            // Summing up blocks
            ps = new Dimension ( 0, 0 );
            if ( hfill )
            {
                ps.width = maxWidth ( c, d, available, contents );
            }
            else
            {
                for ( final Integer width : widths.values () )
                {
                    ps.width += ps.width > 0 ? hgap + width : width;
                }
            }
            if ( vfill )
            {
                ps.height = maxHeight ( c, d, available, contents );
            }
            else
            {
                for ( final Integer height : heights.values () )
                {
                    ps.height += ps.height > 0 ? vgap + height : height;
                }
            }
        }
        else if ( contents.size () == 1 )
        {
            // Separate case for single component
            ps = contents.get ( 0 ).getPreferredSize ( c, d, available );
        }
        else
        {
            // Separate case for empty container
            ps = new Dimension ( 0, 0 );
        }
        return ps;
    }

    /**
     * Returns size for the area specified by horizontal and vertical alignments.
     *
     * @param c          painted component
     * @param d          painted decoration state
     * @param available  available space
     * @param horizontal horizontal position
     * @param vertical   vertical position
     * @return size for the area specified by horizontal and vertical alignments
     */
    protected Dimension getAreaSize ( final C c, final D d, final Dimension available, final String horizontal, final String vertical )
    {
        final Dimension size = new Dimension ( 0, 0 );
        for ( final IContent content : getContents ( c, d, constraints ( horizontal, vertical ) ) )
        {
            final Dimension preferredSize = content.getPreferredSize ( c, d, available );
            size.width = Math.max ( size.width, preferredSize.width );
            size.height = Math.max ( size.height, preferredSize.height );
        }
        return size.width > 0 || size.height > 0 ? size : null;
    }

    /**
     * Returns maximum content elements width.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available available space
     * @param contents  content elements to process
     * @return maximum content elements width
     */
    protected int maxWidth ( final C c, final D d, final Dimension available, final List<IContent> contents )
    {
        int max = 0;
        for ( final IContent content : contents )
        {
            max = Math.max ( max, content.getPreferredSize ( c, d, available ).width );
        }
        return max;
    }

    /**
     * Returns maximum content elements height.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available available space
     * @param contents  content elements to process
     * @return maximum content elements height
     */
    protected int maxHeight ( final C c, final D d, final Dimension available, final List<IContent> contents )
    {
        int max = 0;
        for ( final IContent content : contents )
        {
            max = Math.max ( max, content.getPreferredSize ( c, d, available ).height );
        }
        return max;
    }

    /**
     * Returns complete constraints for specified horizontal and vertical positions.
     *
     * @param horizontal horizontal position
     * @param vertical   vertical position
     * @return complete constraints for specified horizontal and vertical positions
     */
    protected String constraints ( final String horizontal, final String vertical )
    {
        return horizontal + SEPARATOR + vertical;
    }
}