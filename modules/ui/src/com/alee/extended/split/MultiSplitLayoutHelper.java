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

package com.alee.extended.split;

import com.alee.utils.swing.SizeType;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link WebMultiSplitPaneModel} layout helper.
 * It calculates and provides easy access to some of the basic parameters used to layout {@link WebMultiSplitPane} contents.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPaneModel
 * @see WebMultiSplitPane
 */

public class MultiSplitLayoutHelper
{
    /**
     * Preferred sizes of all components.
     */
    protected Map<Component, Dimension> preferredSizes;

    /**
     * Minimum sizes of all components.
     */
    protected Map<Component, Dimension> minimumSizes;

    /**
     * Maximum sizes of all components.
     */
    protected Map<Component, Dimension> maximumSizes;

    /**
     * Returns {@link Component} size for the specified {@link SizeType}.
     *
     * @param component {@link Component}
     * @param sizeType  {@link SizeType}
     * @return {@link Component} size for the specified {@link SizeType}
     */
    public Dimension size ( final Component component, final SizeType sizeType )
    {
        final Dimension size;
        switch ( sizeType )
        {
            case minimum:
            {
                if ( minimumSizes == null )
                {
                    minimumSizes = new HashMap<Component, Dimension> ( 1 );
                }
                if ( !minimumSizes.containsKey ( component ) )
                {
                    minimumSizes.put ( component, component.getMinimumSize () );
                }
                size = minimumSizes.get ( component );
                break;
            }
            case maximum:
            {
                if ( maximumSizes == null )
                {
                    maximumSizes = new HashMap<Component, Dimension> ( 1 );
                }
                if ( !maximumSizes.containsKey ( component ) )
                {
                    maximumSizes.put ( component, component.getMaximumSize () );
                }
                size = maximumSizes.get ( component );
                break;
            }
            default:
            case preferred:
            {
                if ( preferredSizes == null )
                {
                    preferredSizes = new HashMap<Component, Dimension> ( 1 );
                }
                if ( !preferredSizes.containsKey ( component ) )
                {
                    preferredSizes.put ( component, component.getPreferredSize () );
                }
                size = preferredSizes.get ( component );
            }
        }
        return size;
    }

    /**
     * Helper used for calculating initial {@link MultiSplitViewState}s.
     * Also used for calculating {@link WebMultiSplitPane} minimum, preferred and maximum sizes.
     * Calculations in this helper rely only on {@link MultiSplitConstraints} and do not take {@link MultiSplitViewState} into account.
     */
    public static class Static extends MultiSplitLayoutHelper
    {
        /**
         * Whether or not {@link WebMultiSplitPane}s are placed horizontally.
         */
        public final boolean horizontal;

        /**
         * Space available for {@link MultiSplitView}s.
         * This doesn't include insets and divider sizes.
         */
        public final int space;

        /**
         * Space available for percentage {@link MultiSplitView}s only.
         * It always has {@code 0} or larger value.
         */
        public final int spaceForPercentViews;

        /**
         * Sum of all percent based {@link MultiSplitView}s.
         * It also includes {@link MultiSplitView}s added under {@link MultiSplitConstraints#FILL} constraints.
         */
        public final double totalPercentViews;

        /**
         * Sum of all weights for {@link MultiSplitView}s.
         */
        public final double totalWeights;

        /**
         * Calculated percent representation for each of {@link MultiSplitConstraints#FILL} constraints.
         */
        public final double fillViewSize;

        /**
         * Constructs new {@link Static}.
         *
         * @param multiSplitPane {@link WebMultiSplitPane}
         * @param views          {@link List} containing {@link MultiSplitView}s
         * @param sizeType       {@link SizeType} describing method requesting help
         */
        public Static ( final WebMultiSplitPane multiSplitPane, final List<MultiSplitView> views, final SizeType sizeType )
        {
            // Basic settings
            this.horizontal = multiSplitPane.getOrientation ().isHorizontal ();

            // Space available for the views
            final Insets insets = multiSplitPane.getInsets ();
            final int dividersCount = views.size () - 1;
            final int dividersSpace = multiSplitPane.getDividerSize () * dividersCount;
            this.space = Math.max ( 0, horizontal ?
                    multiSplitPane.getWidth () - insets.left - insets.right - dividersSpace :
                    multiSplitPane.getHeight () - insets.top - insets.bottom - dividersSpace );

            // Views sizes
            int totalPixelViews = 0;
            double totalPercentViews = 0.0;
            int fillViewsCount = 0;
            double totalWeights = 0.0;
            for ( final MultiSplitView view : views )
            {
                final Component component = view.component ();
                final MultiSplitConstraints constraints = view.constraints ();

                // Views sizes
                if ( constraints.isPixels () )
                {
                    totalPixelViews += constraints.pixels ();
                }
                else if ( constraints.isPercents () )
                {
                    totalPercentViews += constraints.percents ();
                }
                else if ( constraints.isFill () )
                {
                    fillViewsCount++;
                }
                else if ( constraints.isPreferred () )
                {
                    final Dimension ps = size ( component, SizeType.preferred );
                    totalPixelViews += horizontal ? ps.width : ps.height;
                }
                else if ( constraints.isMinimum () )
                {
                    final Dimension ms = size ( component, SizeType.minimum );
                    totalPixelViews += horizontal ? ms.width : ms.height;
                }
                else
                {
                    throw new IllegalArgumentException ( "Unknown view size value: " + constraints.size () );
                }

                // Weights
                totalWeights += constraints.weight ();
            }
            this.totalWeights = totalWeights;

            // Variables for normalizing percentage and fill views
            this.fillViewSize = totalPercentViews > 0.0 ? totalPercentViews : 1.0;
            this.totalPercentViews = totalPercentViews + fillViewSize * fillViewsCount;

            // Space available for percentage views
            spaceForPercentViews = Math.max ( 0, space - totalPixelViews );
        }
    }

    /**
     * Helper used for calculating runtime variables for {@link WebMultiSplitPane} layout.
     * Calculations in this helper rely only on both {@link MultiSplitViewState} and {@link MultiSplitConstraints}.
     */
    public static class Runtime
    {
        /**
         * Space available for {@link MultiSplitView}s.
         * This doesn't include insets and divider sizes.
         */
        public final int space;

        /**
         * Sum of all {@link MultiSplitView}s sizes.
         */
        public final int totalSizes;

        /**
         * Sum of all {@link MultiSplitView}s weights.
         */
        public final double totalWeights;

        /**
         * Amount of {@link MultiSplitView}s with non-zero size.
         */
        public final int nonZeroViews;

        /**
         * Sum of all weights for {@link MultiSplitView}s with non-zero size.
         */
        public final double nonZeroViewsWeights;

        /**
         * Constructs new {@link Static}.
         *
         * @param multiSplitPane {@link WebMultiSplitPane}
         * @param views          {@link List} containing {@link MultiSplitView}s
         */
        public Runtime ( final WebMultiSplitPane multiSplitPane, final List<MultiSplitView> views )
        {
            // Space available for the views
            final Insets insets = multiSplitPane.getInsets ();
            final int dividersCount = views.size () - 1;
            final int dividersSpace = multiSplitPane.getDividerSize () * dividersCount;
            this.space = Math.max ( 0, multiSplitPane.getOrientation ().isHorizontal () ?
                    multiSplitPane.getWidth () - insets.left - insets.right - dividersSpace :
                    multiSplitPane.getHeight () - insets.top - insets.bottom - dividersSpace );

            // Total sizes
            int totalSizes = 0;
            double totalWeights = 0.0;
            int nonZeroViews = 0;
            double nonZeroWeights = 0.0;
            for ( final MultiSplitView view : views )
            {
                final MultiSplitViewState viewState = view.state ();
                final int size = viewState.size ();

                // Total size
                totalSizes += size;

                // Total weight
                totalWeights += view.constraints ().weight ();

                // Stats for non-zero size views
                if ( size > 0 )
                {
                    // Count
                    nonZeroViews++;

                    // Non-zero weights
                    nonZeroWeights += view.constraints ().weight ();
                }
            }
            this.totalSizes = totalSizes;
            this.totalWeights = totalWeights;
            this.nonZeroViews = nonZeroViews;
            this.nonZeroViewsWeights = nonZeroWeights;
        }
    }
}