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

package com.alee.laf.splitpane;

import javax.swing.*;
import java.awt.*;

/**
 * Custom {@link LayoutManager} for {@link JSplitPane}.
 * It is mainly created for internal usage within {@link WSplitPaneUI} and relies on having that UI instance in {@link JSplitPane}.
 *
 * @author Scott Violet
 * @author Steve Wilson
 * @author Ralph Kar
 * @author Mikle Garin
 */

public class SplitPaneLayout implements LayoutManager2
{
    /**
     * Left, right, divider (in this exact order).
     */
    protected int[] sizes;

    /**
     * Split components.
     */
    protected Component[] components;

    /**
     * Size of the splitpane the last time laid out.
     */
    protected int lastSplitPaneSize;

    /**
     * Set to {@code true} whenever {@link #resetToPreferredSizes()} has been invoked.
     */
    protected boolean doReset;

    /**
     * Layout axis.
     * Should be {@code 0} for {@link JSplitPane#HORIZONTAL_SPLIT} and {@code 1} for {@link JSplitPane#VERTICAL_SPLIT}.
     */
    protected final int axis;

    /**
     * Constructs new {@link SplitPaneLayout}.
     *
     * @param orientation {@link JSplitPane} orientation
     */
    public SplitPaneLayout ( final int orientation )
    {
        super ();
        this.axis = orientation == JSplitPane.HORIZONTAL_SPLIT ? 0 : 1;
        this.components = new Component[ 3 ];
        this.sizes = new int[ 3 ];
    }

    @Override
    public void addLayoutComponent ( final String constraints, final Component component )
    {
        boolean isValid = true;

        if ( constraints != null )
        {
            if ( constraints.equals ( JSplitPane.DIVIDER ) )
            {
                components[ 2 ] = component;
                sizes[ 2 ] = getSizeForPrimaryAxis ( component.getPreferredSize () );
            }
            else if ( constraints.equals ( JSplitPane.LEFT ) || constraints.equals ( JSplitPane.TOP ) )
            {
                components[ 0 ] = component;
                sizes[ 0 ] = 0;
            }
            else if ( constraints.equals ( JSplitPane.RIGHT ) || constraints.equals ( JSplitPane.BOTTOM ) )
            {
                components[ 1 ] = component;
                sizes[ 1 ] = 0;
            }
            else if ( !constraints.equals ( WSplitPaneUI.NON_CONTINUOUS_DIVIDER ) )
            {
                isValid = false;
            }
        }
        else
        {
            isValid = false;
        }
        if ( !isValid )
        {
            throw new IllegalArgumentException ( "cannot add to layout: unknown constraint: " + constraints );
        }
        doReset = true;
    }

    @Override
    public void addLayoutComponent ( final Component component, final Object constraints )
    {
        if ( constraints == null || constraints instanceof String )
        {
            addLayoutComponent ( ( String ) constraints, component );
        }
        else
        {
            throw new IllegalArgumentException ( "cannot add to layout: constraint must be a string (or null)" );
        }
    }

    @Override
    public void removeLayoutComponent ( final Component component )
    {
        for ( int counter = 0; counter < 3; counter++ )
        {
            if ( components[ counter ] == component )
            {
                components[ counter ] = null;
                sizes[ counter ] = 0;
                doReset = true;
            }
        }
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        final JSplitPane splitPane = ( JSplitPane ) container;
        final WSplitPaneUI splitPaneUI = ( WSplitPaneUI ) splitPane.getUI ();
        final Dimension containerSize = container.getSize ();

        /**
         * If the splitpane has a zero size then no op out of here.
         * If we execute this function now, we're going to cause ourselves much grief.
         */
        if ( containerSize.height <= 0 || containerSize.width <= 0 )
        {
            lastSplitPaneSize = 0;
            return;
        }

        final int spDividerLocation = splitPane.getDividerLocation ();
        final Insets insets = splitPane.getInsets ();
        final int availableSize = getAvailableSize ( containerSize, insets );
        final int dOffset = getSizeForPrimaryAxis ( insets, true );
        final Dimension dSize = components[ 2 ] == null ? null : components[ 2 ].getPreferredSize ();

        if ( doReset && !splitPaneUI.isDividerLocationSet () || spDividerLocation < 0 )
        {
            resetToPreferredSizes ( splitPane, availableSize );
        }
        else if ( lastSplitPaneSize <= 0 || availableSize == lastSplitPaneSize || !splitPaneUI.isPainted () ||
                dSize != null && getSizeForPrimaryAxis ( dSize ) != sizes[ 2 ] )
        {
            if ( dSize != null )
            {
                sizes[ 2 ] = getSizeForPrimaryAxis ( dSize );
            }
            else
            {
                sizes[ 2 ] = 0;
            }
            setDividerLocation ( spDividerLocation - dOffset, availableSize );
            splitPaneUI.setDividerLocationSet ( false );
        }
        else if ( availableSize != lastSplitPaneSize )
        {
            distributeSpace ( splitPane, availableSize - lastSplitPaneSize, splitPaneUI.isKeepHidden () );
        }
        doReset = false;
        splitPaneUI.setDividerLocationSet ( false );
        lastSplitPaneSize = availableSize;

        // Reset the bounds of each component
        int nextLocation = getInitialLocation ( insets );
        int counter = 0;

        while ( counter < 3 )
        {
            if ( components[ counter ] != null && components[ counter ].isVisible () )
            {
                setComponentToSize ( components[ counter ], sizes[ counter ], nextLocation, insets, containerSize );
                nextLocation += sizes[ counter ];
            }
            switch ( counter )
            {
                case 0:
                    counter = 2;
                    break;

                case 2:
                    counter = 1;
                    break;

                case 1:
                    counter = 3;
                    break;
            }
        }
        if ( splitPaneUI.isPainted () )
        {
            /**
             * This is tricky.
             * There is never a good time for us to push the value to the splitpane, painted appears to the best time to do it.
             * What is really needed is notification that layout has completed.
             */
            final int newLocation = splitPane.getUI ().getDividerLocation ( splitPane );
            if ( newLocation != spDividerLocation - dOffset )
            {
                try
                {
                    final int lastLocation = splitPane.getLastDividerLocation ();
                    splitPaneUI.setIgnoreDividerLocationChange ( true );

                    /**
                     * Updating divider location.
                     */
                    splitPane.setDividerLocation ( newLocation );

                    /**
                     * This is not always needed, but is rather tricky to determine when...
                     * The case this is needed for is if the user sets the divider location to some bogus value, say 0,
                     * and the actual value is 1, the call to {@code setDividerLocation(1)} will preserve the old value of 0,
                     * when we really want the dividerlocation value  before the call. This is needed for the one touch buttons.
                     */
                    splitPane.setLastDividerLocation ( lastLocation );
                }
                finally
                {
                    splitPaneUI.setIgnoreDividerLocationChange ( false );
                }
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        final JSplitPane splitPane = ( JSplitPane ) container;

        int minPrimary = 0;
        int minSecondary = 0;
        final Insets insets = splitPane.getInsets ();

        for ( int counter = 0; counter < 3; counter++ )
        {
            if ( components[ counter ] != null )
            {
                final Dimension minSize = components[ counter ].getMinimumSize ();
                final int secSize = getSizeForSecondaryAxis ( minSize );

                minPrimary += getSizeForPrimaryAxis ( minSize );
                if ( secSize > minSecondary )
                {
                    minSecondary = secSize;
                }
            }
        }
        if ( insets != null )
        {
            minPrimary += getSizeForPrimaryAxis ( insets, true ) + getSizeForPrimaryAxis ( insets, false );
            minSecondary += getSizeForSecondaryAxis ( insets, true ) + getSizeForSecondaryAxis ( insets, false );
        }
        if ( axis == 0 )
        {
            return new Dimension ( minPrimary, minSecondary );
        }
        return new Dimension ( minSecondary, minPrimary );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        final JSplitPane splitPane = ( JSplitPane ) container;

        int prePrimary = 0;
        int preSecondary = 0;
        final Insets insets = splitPane.getInsets ();

        for ( int counter = 0; counter < 3; counter++ )
        {
            if ( components[ counter ] != null )
            {
                final Dimension preSize = components[ counter ].getPreferredSize ();
                final int secSize = getSizeForSecondaryAxis ( preSize );

                prePrimary += getSizeForPrimaryAxis ( preSize );
                if ( secSize > preSecondary )
                {
                    preSecondary = secSize;
                }
            }
        }
        if ( insets != null )
        {
            prePrimary += getSizeForPrimaryAxis ( insets, true ) + getSizeForPrimaryAxis ( insets, false );
            preSecondary += getSizeForSecondaryAxis ( insets, true ) + getSizeForSecondaryAxis ( insets, false );
        }
        if ( axis == 0 )
        {
            return new Dimension ( prePrimary, preSecondary );
        }
        return new Dimension ( preSecondary, prePrimary );
    }

    @Override
    public Dimension maximumLayoutSize ( final Container container )
    {
        return new Dimension ( Integer.MAX_VALUE, Integer.MAX_VALUE );
    }

    @Override
    public float getLayoutAlignmentX ( final Container container )
    {
        return 0.0f;
    }

    @Override
    public float getLayoutAlignmentY ( final Container container )
    {
        return 0.0f;
    }

    @Override
    public void invalidateLayout ( final Container container )
    {
        /**
         * Does nothing by default.
         * If size of one of the views needs to change {@link JSplitPane#resetToPreferredSizes()} should be messaged.
         */
    }

    /**
     * Marks the receiver so that the next time this instance is laid out it'll ask for the preferred sizes.
     */
    public void resetToPreferredSizes ()
    {
        doReset = true;
    }

    /**
     * Sets the sizes to {@code newSizes}.
     *
     * @param newSizes new sizes
     */
    protected void setSizes ( final int[] newSizes )
    {
        System.arraycopy ( newSizes, 0, sizes, 0, 3 );
    }

    /**
     * Returns sizes of the components.
     *
     * @return sizes of the components
     */
    protected int[] getSizes ()
    {
        final int[] retSizes = new int[ 3 ];

        System.arraycopy ( sizes, 0, retSizes, 0, 3 );
        return retSizes;
    }

    /**
     * Returns preferred size of the specified {@link Component} based on current {@link #axis}.
     *
     * @param component {@link Component}
     * @return preferred size of the specified {@link Component} based on current {@link #axis}
     */
    protected int getPreferredSizeOfComponent ( final Component component )
    {
        return getSizeForPrimaryAxis ( component.getPreferredSize () );
    }

    /**
     * Returns minimum size of the specified {@link Component} based on current {@link #axis}.
     *
     * @param component {@link Component}
     * @return minimum size of the specified {@link Component} based on current {@link #axis}
     */
    protected int getMinimumSizeOfComponent ( final Component component )
    {
        return getSizeForPrimaryAxis ( component.getMinimumSize () );
    }

    /**
     * Returns available width based on the container size and {@link Insets}.
     *
     * @param containerSize container size
     * @param insets        container {@link Insets}
     * @return available width based on the container size and {@link Insets}
     */
    protected int getAvailableSize ( final Dimension containerSize, final Insets insets )
    {
        int size = getSizeForPrimaryAxis ( containerSize );
        if ( insets != null )
        {
            size -= getSizeForPrimaryAxis ( insets, true ) + getSizeForPrimaryAxis ( insets, false );
        }
        return size;
    }

    /**
     * Returns left inset unless the {@link Insets} are {@code null} in which case {@code 0} is returned.
     *
     * @param insets {@link Insets}
     * @return left inset unless the {@link Insets} are {@code null} in which case {@code 0} is returned
     */
    protected int getInitialLocation ( final Insets insets )
    {
        return insets != null ? getSizeForPrimaryAxis ( insets, true ) : 0;
    }

    /**
     * Sets the width of the specified {@link Component} to be {@code size}, placing its x location at location, y to the insets.top,
     * height to the containersize.height less the top and bottom insets.
     *
     * @param component     {@link Component}
     * @param size          size
     * @param location      location
     * @param insets        {@link Insets}
     * @param containerSize container size
     */
    protected void setComponentToSize ( final Component component, final int size, final int location, final Insets insets,
                                        final Dimension containerSize )
    {
        if ( insets != null )
        {
            if ( axis == 0 )
            {
                component.setBounds ( location, insets.top, size, containerSize.height - ( insets.top + insets.bottom ) );
            }
            else
            {
                component.setBounds ( insets.left, location, containerSize.width - ( insets.left + insets.right ), size );
            }
        }
        else
        {
            if ( axis == 0 )
            {
                component.setBounds ( location, 0, size, containerSize.height );
            }
            else
            {
                component.setBounds ( 0, location, containerSize.width, size );
            }
        }
    }

    /**
     * Returns width for zero axis, height otherwise.
     *
     * @param size size
     * @return width for zero axis, height otherwise
     */
    protected int getSizeForPrimaryAxis ( final Dimension size )
    {
        return axis == 0 ? size.width : size.height;
    }

    /**
     * Returns height for zero axis, width otherwise.
     *
     * @param size size
     * @return height for zero axis, width otherwise
     */
    protected int getSizeForSecondaryAxis ( final Dimension size )
    {
        return axis == 0 ? size.height : size.width;
    }

    /**
     * Returns a particular value of the specified {@link Insets} identified by the {@link #axis} and {@code isTop}
     *
     * @param insets {@link Insets}
     * @param isTop  whether should use top value
     * @return particular value of the specified {@link Insets} identified by the {@link #axis} and {@code isTop}
     */
    protected int getSizeForPrimaryAxis ( final Insets insets, final boolean isTop )
    {
        return axis == 0 ?
                isTop ? insets.left : insets.right :
                isTop ? insets.top : insets.bottom;
    }

    /**
     * Returns particular value of the specified {@link Insets} identified by the {@link #axis} and {@code isTop}.
     *
     * @param insets {@link Insets}
     * @param isTop  whether should use top value
     * @return particular value of the specified {@link Insets} identified by the {@link #axis} and {@code isTop}
     */
    protected int getSizeForSecondaryAxis ( final Insets insets, final boolean isTop )
    {
        return axis == 0 ?
                isTop ? insets.top : insets.bottom :
                isTop ? insets.left : insets.right;
    }

    /**
     * Determines the components.
     * This should be called whenever a new instance of this is installed into an existing SplitPane.
     *
     * @param splitPane {@link JSplitPane}
     */
    protected void updateComponents ( final JSplitPane splitPane )
    {
        final WSplitPaneUI splitPaneUI = ( WSplitPaneUI ) splitPane.getUI ();

        Component comp;

        comp = splitPane.getLeftComponent ();
        if ( components[ 0 ] != comp )
        {
            components[ 0 ] = comp;
            if ( comp == null )
            {
                sizes[ 0 ] = 0;
            }
            else
            {
                sizes[ 0 ] = -1;
            }
        }

        comp = splitPane.getRightComponent ();
        if ( components[ 1 ] != comp )
        {
            components[ 1 ] = comp;
            if ( comp == null )
            {
                sizes[ 1 ] = 0;
            }
            else
            {
                sizes[ 1 ] = -1;
            }
        }

        /**
         * Find the divider.
         */
        final Component[] children = splitPane.getComponents ();
        final Component oldDivider = components[ 2 ];

        components[ 2 ] = null;
        for ( int counter = children.length - 1; counter >= 0; counter-- )
        {
            if ( children[ counter ] != components[ 0 ] &&
                    children[ counter ] != components[ 1 ] &&
                    children[ counter ] != splitPaneUI.getNonContinuousLayoutDivider () )
            {
                if ( oldDivider != children[ counter ] )
                {
                    components[ 2 ] = children[ counter ];
                }
                else
                {
                    components[ 2 ] = oldDivider;
                }
                break;
            }
        }
        if ( components[ 2 ] == null )
        {
            sizes[ 2 ] = 0;
        }
        else
        {
            sizes[ 2 ] = getSizeForPrimaryAxis ( components[ 2 ].getPreferredSize () );
        }
    }

    /**
     * Resets the size of the first component to {@code leftSize}, and the right component to the remainder of the space.
     *
     * @param leftSize      size for the left component
     * @param availableSize available size
     */
    protected void setDividerLocation ( int leftSize, final int availableSize )
    {
        final boolean lValid = components[ 0 ] != null && components[ 0 ].isVisible ();
        final boolean rValid = components[ 1 ] != null && components[ 1 ].isVisible ();
        final boolean dValid = components[ 2 ] != null && components[ 2 ].isVisible ();
        int max = availableSize;

        if ( dValid )
        {
            max -= sizes[ 2 ];
        }
        leftSize = Math.max ( 0, Math.min ( leftSize, max ) );
        if ( lValid )
        {
            if ( rValid )
            {
                sizes[ 0 ] = leftSize;
                sizes[ 1 ] = max - leftSize;
            }
            else
            {
                sizes[ 0 ] = max;
                sizes[ 1 ] = 0;
            }
        }
        else if ( rValid )
        {
            sizes[ 1 ] = max;
            sizes[ 0 ] = 0;
        }
    }

    /**
     * Returns array of the preferred sizes of the components.
     *
     * @return array of the preferred sizes of the components
     */
    protected int[] getPreferredSizes ()
    {
        final int[] retValue = new int[ 3 ];
        for ( int counter = 0; counter < 3; counter++ )
        {
            if ( components[ counter ] != null && components[ counter ].isVisible () )
            {
                retValue[ counter ] = getPreferredSizeOfComponent ( components[ counter ] );
            }
            else
            {
                retValue[ counter ] = -1;
            }
        }
        return retValue;
    }

    /**
     * Returns array of the minimum sizes of the components.
     *
     * @return array of the minimum sizes of the components
     */
    protected int[] getMinimumSizes ()
    {
        final int[] retValue = new int[ 3 ];
        for ( int counter = 0; counter < 2; counter++ )
        {
            if ( components[ counter ] != null && components[ counter ].isVisible () )
            {
                retValue[ counter ] = getMinimumSizeOfComponent ( components[ counter ] );
            }
            else
            {
                retValue[ counter ] = -1;
            }
        }
        retValue[ 2 ] = components[ 2 ] != null ? getMinimumSizeOfComponent ( components[ 2 ] ) : -1;
        return retValue;
    }

    /**
     * Resets the components to their preferred sizes.
     *
     * @param splitPane     {@link JSplitPane}
     * @param availableSize available size
     */
    protected void resetToPreferredSizes ( final JSplitPane splitPane, final int availableSize )
    {
        /**
         * Set the sizes to the preferred sizes (if fits), otherwise set to min sizes and distribute any extra space.
         */
        int[] testSizes = getPreferredSizes ();
        int totalSize = 0;
        for ( int counter = 0; counter < 3; counter++ )
        {
            if ( testSizes[ counter ] != -1 )
            {
                totalSize += testSizes[ counter ];
            }
        }
        if ( totalSize > availableSize )
        {
            testSizes = getMinimumSizes ();

            totalSize = 0;
            for ( int counter = 0; counter < 3; counter++ )
            {
                if ( testSizes[ counter ] != -1 )
                {
                    totalSize += testSizes[ counter ];
                }
            }
        }
        setSizes ( testSizes );
        distributeSpace ( splitPane, availableSize - totalSize, false );
    }

    /**
     * Distributes {@code space} between the two components (divider won't get any extra space) based on the weighting.
     * This attempts to honor the min size of the components.
     *
     * @param splitPane  {@link JSplitPane}
     * @param space      space to distribute
     * @param keepHidden if true and one of the components is 0x0 it gets none of the extra space
     */
    protected void distributeSpace ( final JSplitPane splitPane, final int space, final boolean keepHidden )
    {
        boolean lValid = components[ 0 ] != null && components[ 0 ].isVisible ();
        boolean rValid = components[ 1 ] != null && components[ 1 ].isVisible ();
        if ( keepHidden )
        {
            if ( lValid && getSizeForPrimaryAxis ( components[ 0 ].getSize () ) == 0 )
            {
                // Both aren't valid, force them both to be valid
                lValid = rValid && getSizeForPrimaryAxis ( components[ 1 ].getSize () ) == 0;
            }
            else if ( rValid && getSizeForPrimaryAxis ( components[ 1 ].getSize () ) == 0 )
            {
                rValid = false;
            }
        }
        if ( lValid && rValid )
        {
            final double weight = splitPane.getResizeWeight ();
            final int lExtra = ( int ) ( weight * ( double ) space );
            final int rExtra = space - lExtra;

            sizes[ 0 ] += lExtra;
            sizes[ 1 ] += rExtra;

            final int lMin = getMinimumSizeOfComponent ( components[ 0 ] );
            final int rMin = getMinimumSizeOfComponent ( components[ 1 ] );
            final boolean lMinValid = sizes[ 0 ] >= lMin;
            final boolean rMinValid = sizes[ 1 ] >= rMin;

            if ( !lMinValid && !rMinValid )
            {
                if ( sizes[ 0 ] < 0 )
                {
                    sizes[ 1 ] += sizes[ 0 ];
                    sizes[ 0 ] = 0;
                }
                else if ( sizes[ 1 ] < 0 )
                {
                    sizes[ 0 ] += sizes[ 1 ];
                    sizes[ 1 ] = 0;
                }
            }
            else if ( !lMinValid )
            {
                if ( sizes[ 1 ] - ( lMin - sizes[ 0 ] ) < rMin )
                {
                    /**
                     * Both below min, just make sure > 0.
                     */
                    if ( sizes[ 0 ] < 0 )
                    {
                        sizes[ 1 ] += sizes[ 0 ];
                        sizes[ 0 ] = 0;
                    }
                }
                else
                {
                    sizes[ 1 ] -= lMin - sizes[ 0 ];
                    sizes[ 0 ] = lMin;
                }
            }
            else if ( !rMinValid )
            {
                if ( sizes[ 0 ] - ( rMin - sizes[ 1 ] ) < lMin )
                {
                    /**
                     * Both below min, just make sure > 0.
                     */
                    if ( sizes[ 1 ] < 0 )
                    {
                        sizes[ 0 ] += sizes[ 1 ];
                        sizes[ 1 ] = 0;
                    }
                }
                else
                {
                    sizes[ 0 ] -= rMin - sizes[ 1 ];
                    sizes[ 1 ] = rMin;
                }
            }
            if ( sizes[ 0 ] < 0 )
            {
                sizes[ 0 ] = 0;
            }
            if ( sizes[ 1 ] < 0 )
            {
                sizes[ 1 ] = 0;
            }
        }
        else if ( lValid )
        {
            sizes[ 0 ] = Math.max ( 0, sizes[ 0 ] + space );
        }
        else if ( rValid )
        {
            sizes[ 1 ] = Math.max ( 0, sizes[ 1 ] + space );
        }
    }
}