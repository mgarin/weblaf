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

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
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
     */

    /**
     * Layout constraints separator.
     */
    public static final String SEPARATOR = ",";

    /**
     * Horizontal alignment constraints.
     */
    public static final List<Integer> horizontals = Arrays.asList ( LEFT, CENTER, RIGHT );

    /**
     * Vertical alignment constraints.
     */
    public static final List<Integer> verticals = Arrays.asList ( TOP, CENTER, BOTTOM );

    /**
     * Constraints cache for added components.
     */
    protected final Map<Component, String> constraints = new HashMap<Component, String> ();

    /**
     * Horizontal gap between components.
     */
    protected int hgap = 0;

    /**
     * Vertical gap between components.
     */
    protected int vgap = 0;

    /**
     * Whether components should fill all available horizontal space or not.
     */
    protected boolean hfill = false;

    /**
     * Whether components should fill all available vertical space or not.
     */
    protected boolean vfill = false;

    /**
     * Constructs new align layout.
     */
    public AlignLayout ()
    {
        super ();
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
     * @return true if components should fill all available horizontal space, false otherwise
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
     * @return true if components should fill all available vertical space, false otherwise
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
        String name = ( String ) constraints;
        if ( name != null && !name.trim ().equals ( "" ) )
        {
            try
            {
                // Checking halign for validity
                final int halign = getHalign ( name );
                if ( !horizontals.contains ( halign ) )
                {
                    illegalArgument ();
                }

                // Checking valign for validity
                final int valign = getValign ( name );
                if ( !verticals.contains ( valign ) )
                {
                    illegalArgument ();
                }
            }
            catch ( final Throwable ex )
            {
                illegalArgument ();
            }
        }
        else
        {
            // Default position
            name = CENTER + SEPARATOR + CENTER;
        }
        this.constraints.put ( component, name );
    }

    /**
     * Throws illegal argument (constraint) exception.
     */
    protected void illegalArgument ()
    {
        throw new IllegalArgumentException ( "Cannot add to layout: please specify proper alignment constraints" );
    }

    /**
     * Returns horizontal alignment for the specified constraint.
     *
     * @param name constraint
     * @return horizontal alignment
     */
    protected int getHalign ( final String name )
    {
        return name == null ? CENTER : Integer.parseInt ( name.substring ( 0, name.indexOf ( SEPARATOR ) ) );
    }

    /**
     * Returns vertical alignment for the specified constraint.
     *
     * @param name constraint
     * @return vertical alignment
     */
    protected int getValign ( final String name )
    {
        return name == null ? CENTER : Integer.parseInt ( name.substring ( name.indexOf ( SEPARATOR ) + SEPARATOR.length () ) );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        constraints.remove ( component );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Dimension ps;
        if ( parent.getComponentCount () > 1 )
        {
            // Counting size for each block
            final Map<Integer, Integer> widths = new HashMap<Integer, Integer> ();
            final Map<Integer, Integer> heights = new HashMap<Integer, Integer> ();
            for ( final int halign : horizontals )
            {
                for ( final int valign : verticals )
                {
                    final Dimension size = getSideSize ( parent, halign, valign );
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

            // Summing up blocks
            ps = new Dimension ( 0, 0 );
            if ( hfill )
            {
                ps.width = SwingUtils.maxWidth ( parent.getComponents () );
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
                ps.height = SwingUtils.maxHeight ( parent.getComponents () );
            }
            else
            {
                for ( final Integer height : heights.values () )
                {
                    ps.height += ps.height > 0 ? vgap + height : height;
                }
            }
        }
        else if ( parent.getComponentCount () == 1 )
        {
            // Separate case for single component
            ps = parent.getComponent ( 0 ).getPreferredSize ();
        }
        else
        {
            // Separate case for empty container
            ps = new Dimension ( 0, 0 );
        }

        // Adding insets
        final Insets insets = parent.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    /**
     * Returns size for the side specified by horizontal and vertical alignments.
     *
     * @param parent container
     * @param halign horizontal alignment
     * @param valign vertical alignment
     * @return size for the side specified by horizontal and vertical alignments
     */
    protected Dimension getSideSize ( final Container parent, final int halign, final int valign )
    {
        Dimension size = new Dimension ( 0, 0 );
        for ( final Component component : parent.getComponents () )
        {
            // Component constraints and size
            final String align = constraints.get ( component );
            if ( getHalign ( align ) == halign && getValign ( align ) == valign )
            {
                size = SwingUtils.max ( size, component.getPreferredSize () );
            }
        }
        return size.width > 0 || size.height > 0 ? size : null;
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final Insets insets = parent.getInsets ();
        final int cw = parent.getWidth () - insets.left - insets.right;
        final int ch = parent.getHeight () - insets.top - insets.bottom;
        for ( final Component component : parent.getComponents () )
        {
            // Component constraints and size
            final String align = constraints.get ( component );
            final int halign = getHalign ( align );
            final int valign = getValign ( align );
            final Dimension ps = component.getPreferredSize ();

            // Determining x coordinate
            int x = 0;
            if ( hfill )
            {
                x = insets.left;
            }
            else
            {
                switch ( halign )
                {
                    case LEFT:
                    {
                        x = insets.left;
                        break;
                    }
                    case CENTER:
                    {
                        x = parent.getWidth () / 2 - ps.width / 2;
                        break;
                    }
                    case RIGHT:
                    {
                        x = parent.getWidth () - ps.width - insets.right;
                        break;
                    }
                }
            }

            // Determining y coordinate
            int y = 0;
            if ( vfill )
            {
                y = insets.top;
            }
            else
            {
                switch ( valign )
                {
                    case TOP:
                    {
                        y = insets.top;
                        break;
                    }
                    case CENTER:
                    {
                        y = parent.getHeight () / 2 - ps.height / 2;
                        break;
                    }
                    case BOTTOM:
                    {
                        y = parent.getHeight () - ps.height - insets.bottom;
                        break;
                    }
                }
            }

            // Placing component
            component.setBounds ( x, y, hfill ? cw : ps.width, vfill ? ch : ps.height );
        }
    }
}