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
 * User: mgarin Date: 26.03.12 Time: 15:08
 * <p/>
 * This layout allows you to align components inside the container using the standart Swing constants - left/center/right and
 * top/center/bottom
 */

public class AlignLayout implements LayoutManager, SwingConstants
{
    // Default separator
    public static final String SEPARATOR = ",";
    public static final List<Integer> horizontals = Arrays.asList ( LEFT, CENTER, RIGHT );
    public static final List<Integer> verticals = Arrays.asList ( TOP, CENTER, BOTTOM );

    // Saved layout constraints
    private Map<Component, String> constraints = new HashMap<Component, String> ();

    // Gaps betwen components
    private int hgap = 0;
    private int vgap = 0;

    // Should fill horizontal or vertical container space
    private boolean hfill = false;
    private boolean vfill = false;

    /**
     * Some extended constructors
     */

    public AlignLayout ()
    {
        super ();
    }

    /**
     * Gap between components
     */

    public int getHgap ()
    {
        return hgap;
    }

    public void setHgap ( int hgap )
    {
        this.hgap = hgap;
    }

    public int getVgap ()
    {
        return vgap;
    }

    public void setVgap ( int vgap )
    {
        this.vgap = vgap;
    }

    /**
     * Should fill horizontal or vertical container space
     */

    public boolean isHfill ()
    {
        return hfill;
    }

    public void setHfill ( boolean hfill )
    {
        this.hfill = hfill;
    }

    public boolean isVfill ()
    {
        return vfill;
    }

    public void setVfill ( boolean vfill )
    {
        this.vfill = vfill;
    }

    /**
     * Standard LayoutManager methods
     */

    @Override
    public void addLayoutComponent ( String name, Component comp )
    {
        if ( name != null && !name.trim ().equals ( "" ) )
        {
            try
            {
                // Checking halign for validity
                int halign = getHalign ( name );
                if ( !horizontals.contains ( halign ) )
                {
                    illegalArgument ();
                }

                // Checking valign for validity
                int valign = getValign ( name );
                if ( !verticals.contains ( valign ) )
                {
                    illegalArgument ();
                }
            }
            catch ( Throwable ex )
            {
                illegalArgument ();
            }
        }
        else
        {
            // Default position
            name = CENTER + SEPARATOR + CENTER;
        }
        constraints.put ( comp, name );
    }

    private void illegalArgument ()
    {
        throw new IllegalArgumentException ( "Cannot add to layout: please specify proper alignment constraints" );
    }

    private int getHalign ( String name )
    {
        return name == null ? CENTER : Integer.parseInt ( name.substring ( 0, name.indexOf ( SEPARATOR ) ) );
    }

    private int getValign ( String name )
    {
        return name == null ? CENTER : Integer.parseInt ( name.substring ( name.indexOf ( SEPARATOR ) + SEPARATOR.length () ) );
    }

    @Override
    public void removeLayoutComponent ( Component comp )
    {
        constraints.remove ( comp );
    }

    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        Dimension ps;
        if ( parent.getComponentCount () > 1 )
        {
            // Counting size for each block
            Map<Integer, Integer> widths = new HashMap<Integer, Integer> ();
            Map<Integer, Integer> heights = new HashMap<Integer, Integer> ();
            for ( int halign : horizontals )
            {
                for ( int valign : verticals )
                {
                    Dimension size = getSideSize ( parent, halign, valign );
                    if ( size != null )
                    {
                        if ( !hfill )
                        {
                            int width = widths.containsKey ( halign ) ? widths.get ( halign ) : 0;
                            widths.put ( halign, Math.max ( width, size.width ) );
                        }
                        if ( !vfill )
                        {
                            int height = widths.containsKey ( valign ) ? widths.get ( valign ) : 0;
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
                for ( Integer width : widths.values () )
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
                for ( Integer height : heights.values () )
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
        Insets insets = parent.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    private Dimension getSideSize ( Container parent, int halign, int valign )
    {
        Dimension size = new Dimension ( 0, 0 );
        for ( Component component : parent.getComponents () )
        {
            // Component constraints and size
            String align = constraints.get ( component );
            if ( getHalign ( align ) == halign && getValign ( align ) == valign )
            {
                size = SwingUtils.max ( size, component.getPreferredSize () );
            }
        }
        return size.width > 0 || size.height > 0 ? size : null;
    }

    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        return preferredLayoutSize ( parent );
    }

    @Override
    public void layoutContainer ( Container parent )
    {
        Insets insets = parent.getInsets ();
        for ( Component component : parent.getComponents () )
        {
            // Component constraints and size
            String align = constraints.get ( component );
            int halign = getHalign ( align );
            int valign = getValign ( align );
            Dimension ps = component.getPreferredSize ();

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
                switch ( halign )
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
            component.setBounds ( x, y, hfill ? parent.getWidth () - insets.left - insets.right : ps.width,
                    vfill ? parent.getHeight () - insets.top - insets.bottom : ps.height );
        }
    }
}