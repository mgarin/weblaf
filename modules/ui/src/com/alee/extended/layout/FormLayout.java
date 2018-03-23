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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class FormLayout extends AbstractLayoutManager
{
    /**
     * todo 1. Fix {@link #LINE} and {@link RIGHT} constraints behavior
     */

    /**
     * Form elements constraint constants.
     */
    public static final String LEFT = "left";
    public static final String CENTER = "center";
    public static final String RIGHT = "right";
    public static final String FILL = "fill";
    public static final String LINE = "line";

    /**
     * Whether left side of the form should fill all the free space given by container or not.
     */
    protected boolean fillLeftSide;

    /**
     * Whether right side of the form should fill all the free space given by container or not.
     */
    protected boolean fillRightSide;

    /**
     * Whether component in each form row on the left side of the form should fill given height or not by default.
     */
    protected boolean fillLeftSideHeight;

    /**
     * Whether component in each form row on the right side of the form should fill given height or not by default.
     */
    protected boolean fillRightSideHeight;

    /**
     * Default vertical alignment of components on the left side of the form.
     * This is applied only if left side components do not fill the whole row height.
     */
    protected int leftVerticalAlignment;

    /**
     * Default vertical alignment of components on the right side of the form.
     * This is applied only if right side components do not fill the whole row height.
     */
    protected int rightVerticalAlignment;

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
    protected Map<Component, String> layoutConstraints = new HashMap<Component, String> ();

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
        this.fillLeftSideHeight = false;
        this.fillRightSideHeight = false;
        this.leftVerticalAlignment = SwingConstants.CENTER;
        this.rightVerticalAlignment = SwingConstants.CENTER;
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
     * Returns whether component in each form row on the left side of the form should fill given height or not.
     *
     * @return true if component in each form row on the left side of the form should fill given height, false otherwise
     */
    public boolean isFillLeftSideHeight ()
    {
        return fillLeftSideHeight;
    }

    /**
     * Sets whether component in each form row on the left side of the form should fill given height or not.
     *
     * @param fillLeftSideHeight whether component in each form row on the left side of the form should fill given height or not
     */
    public void setFillLeftSideHeight ( final boolean fillLeftSideHeight )
    {
        this.fillLeftSideHeight = fillLeftSideHeight;
    }

    /**
     * Returns whether component in each form row on the right side of the form should fill given height or not.
     *
     * @return true if component in each form row on the right side of the form should fill given height, false otherwise
     */
    public boolean isFillRightSideHeight ()
    {
        return fillRightSideHeight;
    }

    /**
     * Sets whether component in each form row on the right side of the form should fill given height or not.
     *
     * @param fillRightSideHeight whether component in each form row on the right side of the form should fill given height or not
     */
    public void setFillRightSideHeight ( final boolean fillRightSideHeight )
    {
        this.fillRightSideHeight = fillRightSideHeight;
    }

    /**
     * Returns default vertical alignment of components on the left side of the form.
     *
     * @return default vertical alignment of components on the left side of the form
     */
    public int getLeftVerticalAlignment ()
    {
        return leftVerticalAlignment;
    }

    /**
     * Sets default vertical alignment of components on the left side of the form.
     *
     * @param leftVerticalAlignment default vertical alignment of components on the left side of the form
     */
    public void setLeftVerticalAlignment ( final int leftVerticalAlignment )
    {
        this.leftVerticalAlignment = leftVerticalAlignment;
    }

    /**
     * Returns default vertical alignment of components on the right side of the form.
     *
     * @return default vertical alignment of components on the right side of the form
     */
    public int getRightVerticalAlignment ()
    {
        return rightVerticalAlignment;
    }

    /**
     * Sets default vertical alignment of components on the right side of the form.
     *
     * @param rightVerticalAlignment default vertical alignment of components on the right side of the form
     */
    public void setRightVerticalAlignment ( final int rightVerticalAlignment )
    {
        this.rightVerticalAlignment = rightVerticalAlignment;
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

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        // Adding default constraints if needed (left side components aligned to right, right side components fill the space)
        final Container container = component.getParent ();
        final int cc = container.getComponentCount ();
        boolean wasFirstColumn = false;
        for ( int j = 0; j < cc; j++ )
        {
            final Component c = container.getComponent ( j );
            final String lc = layoutConstraints.get ( c );
            if ( lc != null )
            {
                wasFirstColumn = !wasFirstColumn && !lc.equals ( LINE );
            }
            if ( c == component )
            {
                final String halign = constraints != null ? "" + constraints : wasFirstColumn ? FILL : RIGHT;
                layoutConstraints.put ( component, halign );
                break;
            }
        }
    }

    @Override
    public void removeComponent ( final Component component )
    {
        layoutConstraints.remove ( component );
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        final int cc = container.getComponentCount ();
        if ( cc > 0 )
        {
            // Pre-calculating children preferred sizes
            final Map<Component, Dimension> cps = SwingUtils.getChildPreferredSizes ( container );

            // Calculating preferred column widths
            int lpw = 0;
            int rpw = 0;
            boolean wasFirstColumn = false;
            for ( int j = 0; j < cc; j++ )
            {
                final Component component = container.getComponent ( j );
                final Dimension ps = cps.get ( component );
                if ( !wasFirstColumn )
                {
                    if ( layoutConstraints.get ( component ).equals ( LINE ) )
                    {
                        // Skipping to next line
                        wasFirstColumn = false;
                    }
                    else
                    {
                        // First column
                        lpw = Math.max ( lpw, ps.width );

                        // Taking second column into account
                        wasFirstColumn = true;
                    }
                }
                else
                {
                    // Second column
                    rpw = Math.max ( lpw, ps.width );

                    // Going to next line
                    wasFirstColumn = false;
                }
            }

            // Post-calculating actual column widths
            final Dimension parentSize = container.getSize ();
            final Insets i = container.getInsets ();
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
            else
            {
                if ( cc > 1 )
                {
                    rpw = parentSize.width - i.left - i.right - horizontalGap - lpw;
                }
            }

            // Layouting components
            final int x1 = i.left;
            final int x2 = i.left + lpw + horizontalGap;
            final int w = parentSize.width - i.left - i.right;
            int y = i.top;
            wasFirstColumn = false;
            for ( int j = 0; j < cc; j++ )
            {
                final Component component = container.getComponent ( j );
                final Dimension ps = cps.get ( component );
                final String pos = layoutConstraints.get ( component );
                if ( !wasFirstColumn )
                {
                    if ( pos.equals ( LINE ) )
                    {
                        // Full line column
                        final int rh = ps.height;
                        component.setBounds ( x1, y, w, rh );

                        // Incrementing Y position
                        y += rh + verticalGap;

                        // Skipping to next line
                        wasFirstColumn = false;
                    }
                    else
                    {
                        // Row preferred height
                        final int next = cc > j + 1 ? cps.get ( container.getComponent ( j + 1 ) ).height : 0;
                        final int rh = Math.max ( ps.height, next );

                        // First column
                        final int cy = fillLeftSideHeight ? y : getSideY ( true, y, rh, ps.height );
                        final int ch = fillLeftSideHeight ? rh : ps.height;
                        final int aw = Math.min ( ps.width, lpw );
                        if ( pos.equals ( LEFT ) )
                        {
                            component.setBounds ( x1, cy, aw, ch );
                        }
                        else if ( pos.equals ( CENTER ) )
                        {
                            component.setBounds ( x1 + lpw / 2 - aw / 2, cy, aw, ch );
                        }
                        else if ( pos.equals ( RIGHT ) )
                        {
                            component.setBounds ( x1 + lpw - aw, cy, aw, ch );
                        }
                        else if ( pos.equals ( FILL ) )
                        {
                            component.setBounds ( x1, cy, lpw, ch );
                        }

                        // Going for second column
                        wasFirstColumn = true;
                    }
                }
                else
                {
                    // Row preferred height
                    final int prev = cps.get ( container.getComponent ( j - 1 ) ).height;
                    final int rh = Math.max ( ps.height, prev );

                    // Second column
                    final int cy = fillRightSideHeight ? y : getSideY ( false, y, rh, ps.height );
                    final int ch = fillRightSideHeight ? rh : ps.height;
                    final int aw = Math.min ( ps.width, rpw );
                    if ( pos.equals ( LEFT ) )
                    {
                        component.setBounds ( x2, cy, aw, ch );
                    }
                    else if ( pos.equals ( CENTER ) )
                    {
                        component.setBounds ( x2 + rpw / 2 - aw / 2, cy, aw, ch );
                    }
                    else if ( pos.equals ( RIGHT ) )
                    {
                        component.setBounds ( x2 + rpw - aw, cy, aw, ch );
                    }
                    else if ( pos.equals ( FILL ) )
                    {
                        component.setBounds ( x2, cy, rpw, ch );
                    }

                    // Incrementing Y position
                    y += rh + verticalGap;

                    // Going to next line
                    wasFirstColumn = false;
                }
            }
        }
    }

    /**
     * Returns component Y coordinate for the specified side and other settings.
     *
     * @param leftSide        whether component is on the left side of the form or not
     * @param rowY            row Y coordinate
     * @param rowHeight       row height
     * @param componentHeight component preferred height
     * @return component Y coordinate
     */
    protected int getSideY ( final boolean leftSide, final int rowY, final int rowHeight, final int componentHeight )
    {
        if ( leftSide ? leftVerticalAlignment == SwingConstants.CENTER : rightVerticalAlignment == SwingConstants.CENTER )
        {
            return rowY + rowHeight / 2 - componentHeight / 2;
        }
        else if ( leftSide ? leftVerticalAlignment == SwingConstants.TOP : rightVerticalAlignment == SwingConstants.TOP )
        {
            return rowY;
        }
        else if ( leftSide ? leftVerticalAlignment == SwingConstants.BOTTOM : rightVerticalAlignment == SwingConstants.BOTTOM )
        {
            return rowY + rowHeight - componentHeight;
        }
        return rowY;
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        final int cc = container.getComponentCount ();
        final Insets i = container.getInsets ();
        if ( cc > 0 )
        {
            // Pre-calculating children preferred sizes
            final Map<Component, Dimension> cps = SwingUtils.getChildPreferredSizes ( container );

            int pw = 0;
            int lpw = 0;
            int rpw = 0;
            int ph = 0;
            boolean wasFirstColumn = false;
            for ( int j = 0; j < cc; j++ )
            {
                final Component thisComponent = container.getComponent ( j );
                final Dimension ps = cps.get ( thisComponent );
                if ( !wasFirstColumn )
                {
                    if ( layoutConstraints.get ( thisComponent ).equals ( LINE ) )
                    {
                        // Single column line
                        pw = Math.max ( pw, ps.width );

                        // Row preferred height
                        ph += ps.height + verticalGap;

                        // Skipping to next row
                        wasFirstColumn = false;
                    }
                    else
                    {
                        // First column
                        lpw = Math.max ( lpw, ps.width );

                        // Row preferred height
                        final int next = cc > j + 1 ? cps.get ( container.getComponent ( j + 1 ) ).height : 0;
                        ph += Math.max ( ps.height, next ) + verticalGap;

                        // Remembering that this was first column
                        wasFirstColumn = true;
                    }
                }
                else
                {
                    // Second column
                    rpw = Math.max ( rpw, ps.width );

                    // Going to next row
                    wasFirstColumn = false;
                }
            }
            ph -= verticalGap;
            if ( fillLeftSide && fillRightSide )
            {
                lpw = Math.max ( lpw, rpw );
                rpw = lpw;
            }
            return new Dimension ( i.left + Math.max ( lpw + horizontalGap + rpw, pw ) + i.right, i.top + ph + i.bottom );
        }
        else
        {
            return new Dimension ( i.left + i.right, i.top + i.bottom );
        }
    }
}