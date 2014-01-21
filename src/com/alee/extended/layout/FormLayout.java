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

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mikle Garin
 */

public class FormLayout extends AbstractLayoutManager
{
    /**
     * Form elements constraint constants.
     */
    public static final String LEFT = "left";
    public static final String CENTER = "center";
    public static final String RIGHT = "right";
    public static final String FILL = "fill";

    /**
     * Whether left side of the form should fill all the free space given by container or not.
     */
    protected boolean fillLeftSide;

    /**
     * Whether right side of the form should fill all the free space given by container or not.
     */
    protected boolean fillRightSide;

    /**
     * Horizontal gap between columns.
     */
    protected int horizontalGap;

    /**
     * Vertical gap between rows.
     */
    protected int verticalGap;

    /**
     * Added component constraints.
     */
    protected Map<Component, String> layoutConstraints = new WeakHashMap<Component, String> ();

    /**
     * Constructs new FormLayout with zero gaps and filled right side.
     */
    public FormLayout ()
    {
        this ( false, true, 0, 0 );
    }

    /**
     * Constructs new FormLayout with zero gaps and specified left/right side fills.
     *
     * @param fillLeftSide  whether left side of the form should fill all the free space given by container or not
     * @param fillRightSide whether right side of the form should fill all the free space given by container or not
     */
    public FormLayout ( final boolean fillLeftSide, final boolean fillRightSide )
    {
        this ( fillLeftSide, fillRightSide, 0, 0 );
    }

    /**
     * Constructs new FormLayout with specified gaps and filled right side.
     *
     * @param horizontalGap horizontal gap between columns
     * @param verticalGap   vertical gap between rows
     */
    public FormLayout ( final int horizontalGap, final int verticalGap )
    {
        this ( false, true, horizontalGap, verticalGap );
    }

    /**
     * Constructs new FormLayout with specified gaps and left/right side fills.
     *
     * @param fillLeftSide  whether left side of the form should fill all the free space given by container or not
     * @param fillRightSide whether right side of the form should fill all the free space given by container or not
     * @param horizontalGap horizontal gap between columns
     * @param verticalGap   vertical gap between rows
     */
    public FormLayout ( final boolean fillLeftSide, final boolean fillRightSide, final int horizontalGap, final int verticalGap )
    {
        super ();
        this.fillLeftSide = fillLeftSide;
        this.fillRightSide = fillRightSide;
        this.horizontalGap = horizontalGap;
        this.verticalGap = verticalGap;
    }

    /**
     * Returns whether left side of the form should fill all the free space given by container or not.
     *
     * @return true if left side of the form should fill all the free space given by container, false otherwise
     */
    public boolean isFillLeftSide ()
    {
        return fillLeftSide;
    }

    /**
     * Sets whether left side of the form should fill all the free space given by container or not.
     *
     * @param fillLeftSide whether left side of the form should fill all the free space given by container or not
     */
    public void setFillLeftSide ( final boolean fillLeftSide )
    {
        this.fillLeftSide = fillLeftSide;
    }

    /**
     * Returns whether right side of the form should fill all the free space given by container or not.
     *
     * @return true if right side of the form should fill all the free space given by container, false otherwise
     */
    public boolean isFillRightSide ()
    {
        return fillRightSide;
    }

    /**
     * Sets whether right side of the form should fill all the free space given by container or not.
     *
     * @param fillRightSide whether right side of the form should fill all the free space given by container or not
     */
    public void setFillRightSide ( final boolean fillRightSide )
    {
        this.fillRightSide = fillRightSide;
    }

    /**
     * Returns horizontal gap between columns.
     *
     * @return horizontal gap between columns
     */
    public int getHorizontalGap ()
    {
        return horizontalGap;
    }

    /**
     * Sets horizontal gap between columns.
     *
     * @param horizontalGap horizontal gap between columns
     */
    public void setHorizontalGap ( final int horizontalGap )
    {
        this.horizontalGap = horizontalGap;
    }

    /**
     * Returns vertical gap between rows.
     *
     * @return vertical gap between rows
     */
    public int getVerticalGap ()
    {
        return verticalGap;
    }

    /**
     * Sets vertical gap between rows.
     *
     * @param verticalGap vertical gap between rows
     */
    public void setVerticalGap ( final int verticalGap )
    {
        this.verticalGap = verticalGap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String halign = constraints != null ? "" + constraints : ( layoutConstraints.size () % 2 == 0 ? RIGHT : FILL );
        layoutConstraints.put ( component, halign );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent ( final Component component )
    {
        layoutConstraints.remove ( component );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final int cc = parent.getComponentCount ();
        final Insets i = parent.getInsets ();
        if ( cc > 0 )
        {
            // Pre-calculating childs preferred sizes
            final Map<Component, Dimension> cps = SwingUtils.getChildPreferredSizes ( parent );

            int lpw = 0;
            int rpw = 0;
            int ph = 0;
            for ( int j = 0; j < cc; j++ )
            {
                final Dimension ps = cps.get ( parent.getComponent ( j ) );
                if ( j % 2 == 0 )
                {
                    // First column
                    lpw = Math.max ( lpw, ps.width );

                    // Row preferred height
                    final int next = cc > j + 1 ? cps.get ( parent.getComponent ( j + 1 ) ).height : 0;
                    ph += Math.max ( ps.height, next );
                }
                else
                {
                    // Second column
                    rpw = Math.max ( rpw, ps.width );
                }
            }
            if ( fillLeftSide && fillRightSide )
            {
                lpw = Math.max ( lpw, rpw );
                rpw = lpw;
            }
            return new Dimension ( i.left + lpw + horizontalGap + rpw + i.right, i.top + ph + verticalGap * ( ( cc - 1 ) / 2 ) + i.bottom );
        }
        else
        {
            return new Dimension ( i.left + i.right, i.top + i.bottom );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( final Container parent )
    {
        final int cc = parent.getComponentCount ();
        if ( cc > 0 )
        {
            // Pre-calculating childs preferred sizes
            final Map<Component, Dimension> cps = SwingUtils.getChildPreferredSizes ( parent );

            // Calculating preferred column widths
            int lpw = 0;
            int rpw = 0;
            for ( int j = 0; j < cc; j++ )
            {
                final Dimension ps = cps.get ( parent.getComponent ( j ) );
                if ( j % 2 == 0 )
                {
                    // First column
                    lpw = Math.max ( lpw, ps.width );
                }
                else
                {
                    // Second column
                    rpw = Math.max ( lpw, ps.width );
                }
            }

            // Post-calculating actual column widths
            final Dimension parentSize = parent.getSize ();
            final Insets i = parent.getInsets ();
            if ( fillLeftSide && fillRightSide )
            {
                if ( cc > 1 )
                {
                    lpw = ( parentSize.width - i.left - i.right - horizontalGap ) / 2;
                    rpw = lpw;
                }
                else
                {
                    lpw = parentSize.width - i.left - i.right;
                }
            }
            else if ( fillLeftSide )
            {
                if ( cc > 1 )
                {
                    lpw = parentSize.width - i.left - i.right - horizontalGap - rpw;
                }
                else
                {
                    lpw = parentSize.width - i.left - i.right;
                }
            }
            else if ( fillRightSide )
            {
                if ( cc > 1 )
                {
                    rpw = parentSize.width - i.left - i.right - horizontalGap - lpw;
                }
            }

            // Layouting components
            final int x1 = i.left;
            final int x2 = i.left + lpw + horizontalGap;
            int y = i.top;
            for ( int j = 0; j < cc; j++ )
            {
                final Component component = parent.getComponent ( j );
                final Dimension ps = cps.get ( component );
                final String pos = layoutConstraints.get ( component );
                if ( j % 2 == 0 )
                {
                    // Row preferred height
                    final int next = cc > j + 1 ? cps.get ( parent.getComponent ( j + 1 ) ).height : 0;
                    final int rh = Math.max ( ps.height, next );

                    // First column
                    if ( pos.equals ( LEFT ) )
                    {
                        component.setBounds ( x1, y + rh / 2 - ps.height / 2, ps.width, ps.height );
                    }
                    else if ( pos.equals ( CENTER ) )
                    {
                        component.setBounds ( x1 + lpw / 2 - ps.width / 2, y + rh / 2 - ps.height / 2, ps.width, ps.height );
                    }
                    else if ( pos.equals ( RIGHT ) )
                    {
                        component.setBounds ( x1 + lpw - ps.width, y + rh / 2 - ps.height / 2, ps.width, ps.height );
                    }
                    else if ( pos.equals ( FILL ) )
                    {
                        component.setBounds ( x1, y + rh / 2 - ps.height / 2, lpw, ps.height );
                    }
                }
                else
                {
                    // Row preferred height
                    final int prev = cps.get ( parent.getComponent ( j - 1 ) ).height;
                    final int rh = Math.max ( ps.height, prev );

                    // Second column
                    if ( pos.equals ( LEFT ) )
                    {
                        component.setBounds ( x2, y + rh / 2 - ps.height / 2, ps.width, ps.height );
                    }
                    else if ( pos.equals ( CENTER ) )
                    {
                        component.setBounds ( x2 + rpw / 2 - ps.width / 2, y + rh / 2 - ps.height / 2, ps.width, ps.height );
                    }
                    else if ( pos.equals ( RIGHT ) )
                    {
                        component.setBounds ( x2 + rpw - ps.width, y + rh / 2 - ps.height / 2, ps.width, ps.height );
                    }
                    else if ( pos.equals ( FILL ) )
                    {
                        component.setBounds ( x2, y + rh / 2 - ps.height / 2, rpw, ps.height );
                    }

                    // Incrementing Y position
                    y += rh + verticalGap;
                }
            }
        }
    }
}