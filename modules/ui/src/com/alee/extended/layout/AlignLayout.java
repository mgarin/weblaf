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

package com.alee.extended.layout;

import com.alee.utils.SwingUtils;
import com.alee.utils.collection.ImmutableList;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This layout allows you to align components inside the container using the standart Swing constants.
 *
 * @author Mikle Garin
 */
public class AlignLayout extends AbstractLayoutManager implements SwingConstants
{
    /**
     * todo 1. Take orientation into account
     * todo 2. Center element doesn't take side ones into account when placed
     */

    /**
     * Horizontal alignment constraints.
     */
    public static final List<Integer> horizontals = new ImmutableList<Integer> ( LEFT, CENTER, RIGHT );

    /**
     * Vertical alignment constraints.
     */
    public static final List<Integer> verticals = new ImmutableList<Integer> ( TOP, CENTER, BOTTOM );

    /**
     * Layout constraints separator.
     */
    public static final String SEPARATOR = ",";

    /**
     * Constraints cache for added components.
     */
    protected final Map<Component, String> constraints;

    /**
     * Horizontal gap between components.
     */
    protected int hgap;

    /**
     * Vertical gap between components.
     */
    protected int vgap;

    /**
     * Whether or not components should fill all available horizontal space.
     */
    protected boolean hfill;

    /**
     * Whether or not components should fill all available vertical space.
     */
    protected boolean vfill;

    /**
     * Constructs new {@link AlignLayout}.
     */
    public AlignLayout ()
    {
        this ( 0, 0, false, false );
    }

    /**
     * Constructs new align layout.
     *
     * @param hgap horizontal gap between components
     * @param vgap vertical gap between components
     */
    public AlignLayout ( final int hgap, final int vgap )
    {
        this ( hgap, vgap, false, false );
    }

    /**
     * Constructs new align layout.
     *
     * @param hgap  horizontal gap between components
     * @param vgap  vertical gap between components
     * @param hfill whether or not components should fill all available horizontal space
     * @param vfill whether or not components should fill all available vertical space
     */
    public AlignLayout ( final int hgap, final int vgap, final boolean hfill, final boolean vfill )
    {
        this.constraints = new HashMap<Component, String> ();
        this.hgap = 0;
        this.vgap = 0;
        this.hfill = false;
        this.vfill = false;
    }

    /**
     * Returns horizontal gap between components.
     *
     * @return horizontal gap between components
     */
    public int getHgap ()
    {
        return hgap;
    }

    /**
     * Sets horizontal gap between components.
     *
     * @param hgap new horizontal gap between components
     */
    public void setHgap ( final int hgap )
    {
        this.hgap = hgap;
    }

    /**
     * Returns vertical gap between components.
     *
     * @return vertical gap between components
     */
    public int getVgap ()
    {
        return vgap;
    }

    /**
     * Sets vertical gap between components.
     *
     * @param vgap new vertical gap between components
     */
    public void setVgap ( final int vgap )
    {
        this.vgap = vgap;
    }

    /**
     * Returns whether components should fill all available horizontal space or not.
     *
     * @return {@code true} if components should fill all available horizontal space, {@code false} otherwise
     */
    public boolean isHfill ()
    {
        return hfill;
    }

    /**
     * Sets whether components should fill all available horizontal space or not.
     *
     * @param hfill whether components should fill all available horizontal space or not
     */
    public void setHfill ( final boolean hfill )
    {
        this.hfill = hfill;
    }

    /**
     * Returns whether components should fill all available vertical space or not.
     *
     * @return {@code true} if components should fill all available vertical space, {@code false} otherwise
     */
    public boolean isVfill ()
    {
        return vfill;
    }

    /**
     * Sets whether components should fill all available vertical space or not.
     *
     * @param vfill whether components should fill all available vertical space or not
     */
    public void setVfill ( final boolean vfill )
    {
        this.vfill = vfill;
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        String align = ( String ) constraints;
        if ( align != null && !align.trim ().equals ( "" ) )
        {
            try
            {
                // Checking halign for validity
                final int halign = getHalign ( align );
                if ( !horizontals.contains ( halign ) )
                {
                    illegalArgument ();
                }

                // Checking valign for validity
                final int valign = getValign ( align );
                if ( !verticals.contains ( valign ) )
                {
                    illegalArgument ();
                }
            }
            catch ( final Exception ex )
            {
                illegalArgument ();
            }
        }
        else
        {
            // Default position
            align = CENTER + SEPARATOR + CENTER;
        }
        this.constraints.put ( component, align );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        constraints.remove ( component );
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        final Insets insets = container.getInsets ();
        final int cw = container.getWidth () - insets.left - insets.right;
        final int ch = container.getHeight () - insets.top - insets.bottom;
        for ( final Component component : container.getComponents () )
        {
            // Component constraints
            final String align = constraints.get ( component );
            final int halign = getHalign ( align );
            final int valign = getValign ( align );

            // Component size
            final Dimension ps = component.getPreferredSize ();
            ps.width = Math.min ( ps.width, cw );
            ps.height = Math.min ( ps.height, ch );

            // Determining x coordinate
            final int x;
            if ( isHfill () )
            {
                x = insets.left;
            }
            else
            {
                if ( halign == LEFT )
                {
                    x = insets.left;
                }
                else if ( halign == CENTER )
                {
                    x = insets.left + cw / 2 - Math.min ( ps.width / 2, cw / 2 );
                }
                else if ( halign == RIGHT )
                {
                    x = container.getWidth () - Math.min ( ps.width, cw ) - insets.right;
                }
                else
                {
                    throw new IllegalArgumentException ( "Unknown horizontal alignment: " + halign );
                }
            }

            // Determining y coordinate
            final int y;
            if ( isVfill () )
            {
                y = insets.top;
            }
            else
            {
                if ( valign == TOP )
                {
                    y = insets.top;
                }
                else if ( valign == CENTER )
                {
                    y = insets.top + ch / 2 - Math.min ( ps.width / 2, ch / 2 );
                }
                else if ( valign == BOTTOM )
                {
                    y = container.getHeight () - Math.min ( ps.height, ch ) - insets.bottom;
                }
                else
                {
                    throw new IllegalArgumentException ( "Unknown vertical alignment: " + valign );
                }
            }

            // Determining width and height
            final int width = isHfill () ? cw : Math.min ( ps.width, cw );
            final int height = isVfill () ? ch : Math.min ( ps.height, ch );

            // Placing component
            component.setBounds ( x, y, width, height );
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        final Dimension ps;
        if ( container.getComponentCount () > 1 )
        {
            // Counting size for each block
            final Map<Integer, Integer> widths = new HashMap<Integer, Integer> ();
            final Map<Integer, Integer> heights = new HashMap<Integer, Integer> ();
            if ( !isHfill () || !isVfill () )
            {
                for ( final int halign : horizontals )
                {
                    for ( final int valign : verticals )
                    {
                        final Dimension size = getAreaSize ( container, halign, valign );
                        if ( size != null )
                        {
                            if ( !isHfill () )
                            {
                                final int width = widths.containsKey ( halign ) ? widths.get ( halign ) : 0;
                                widths.put ( halign, Math.max ( width, size.width ) );
                            }
                            if ( !isVfill () )
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
            if ( isHfill () )
            {
                ps.width = SwingUtils.maxWidth ( container.getComponents () );
            }
            else
            {
                for ( final Integer width : widths.values () )
                {
                    ps.width += ps.width > 0 ? getHgap () + width : width;
                }
            }
            if ( isVfill () )
            {
                ps.height = SwingUtils.maxHeight ( container.getComponents () );
            }
            else
            {
                for ( final Integer height : heights.values () )
                {
                    ps.height += ps.height > 0 ? getVgap () + height : height;
                }
            }
        }
        else if ( container.getComponentCount () == 1 )
        {
            // Separate case for single component
            ps = container.getComponent ( 0 ).getPreferredSize ();
        }
        else
        {
            // Separate case for empty container
            ps = new Dimension ( 0, 0 );
        }

        // Adding insets
        final Insets insets = container.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    /**
     * Returns size for the area specified by horizontal and vertical alignments.
     *
     * @param container container
     * @param halign    horizontal alignment
     * @param valign    vertical alignment
     * @return size for the area specified by horizontal and vertical alignments
     */
    protected Dimension getAreaSize ( final Container container, final int halign, final int valign )
    {
        final Dimension size = new Dimension ( 0, 0 );
        for ( final Component component : container.getComponents () )
        {
            final String align = constraints.get ( component );
            if ( getHalign ( align ) == halign && getValign ( align ) == valign )
            {
                final Dimension preferredSize = component.getPreferredSize ();
                size.width = Math.max ( size.width, preferredSize.width );
                size.height = Math.max ( size.height, preferredSize.height );
            }
        }
        return size.width > 0 || size.height > 0 ? size : null;
    }

    /**
     * Returns horizontal alignment for the specified constraint.
     *
     * @param constraints constraints
     * @return horizontal alignment
     */
    protected int getHalign ( final String constraints )
    {
        return constraints == null ? CENTER :
                Integer.parseInt ( constraints.substring ( 0, constraints.indexOf ( SEPARATOR ) ) );
    }

    /**
     * Returns vertical alignment for the specified constraint.
     *
     * @param constraints constraints
     * @return vertical alignment
     */
    protected int getValign ( final String constraints )
    {
        return constraints == null ? CENTER :
                Integer.parseInt ( constraints.substring ( constraints.indexOf ( SEPARATOR ) + SEPARATOR.length () ) );
    }

    /**
     * Throws illegal argument (constraint) exception.
     */
    protected void illegalArgument ()
    {
        throw new IllegalArgumentException ( "Cannot add to layout: please specify proper alignment constraints" );
    }
}