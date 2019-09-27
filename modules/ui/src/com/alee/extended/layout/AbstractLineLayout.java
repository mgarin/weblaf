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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.swing.ZOrderComparator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom {@link LayoutManager}
 *
 * @author Mikle Garin
 */
public abstract class AbstractLineLayout extends AbstractLayoutManager implements SwingConstants
{
    /**
     * Positions component at the leading side of the container.
     */
    @NotNull
    public static final String START = "START";

    /**
     * Positions component in the middle between leading and trailing sides.
     * Note that this constraint overlaps with {@link #FILL} constraint.
     */
    @NotNull
    public static final String MIDDLE = "MIDDLE";

    /**
     * Forces component to fill all the space left between leading and trailing sides.
     * Note that this constraint overlaps with {@link #MIDDLE} constraint.
     */
    @NotNull
    public static final String FILL = "FILL";

    /**
     * Positions component at the trailing side of the container.
     */
    @NotNull
    public static final String END = "END";

    /**
     * Special position for an element displayed at the end of the container whenever its size is less than preferred size.
     */
    @NotNull
    public static final String TRIM = "TRIM";

    /**
     * Lazy instance of {@link ZOrderComparator}.
     */
    @NotNull
    protected static final ZOrderComparator COMPONENTS_COMPARATOR = new ZOrderComparator ();

    /**
     * Bounds used to hide components.
     */
    @NotNull
    protected static final Rectangle HIDE_BOUNDS = new Rectangle ( -1, -1, 0, 0 );

    /**
     * Spacing between layout elements.
     */
    protected int spacing;

    /**
     * Spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts.
     */
    protected int partsSpacing;

    /**
     * Saved layout constraints.
     */
    @NotNull
    protected transient Map<Component, String> constraints;

    /**
     * Mapped components.
     */
    @NotNull
    protected transient Map<String, List<Component>> components;

    /**
     * Constructs new {@link AbstractLineLayout}.
     *
     * @param spacing      spacing between layout elements
     * @param partsSpacing spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts
     */
    public AbstractLineLayout ( final int spacing, final int partsSpacing )
    {
        this.spacing = spacing;
        this.partsSpacing = partsSpacing;
        this.constraints = new HashMap<Component, String> ( 10 );
        this.components = new HashMap<String, List<Component>> ( 4 );
    }

    /**
     * Returns spacing between layout elements.
     *
     * @return spacing between layout elements
     */
    public int getSpacing ()
    {
        return spacing;
    }

    /**
     * Sets spacing between layout elements
     *
     * @param spacing spacing between layout elements
     */
    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
    }

    /**
     * Returns spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts.
     *
     * @return spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts
     */
    public int getPartsSpacing ()
    {
        return partsSpacing;
    }

    /**
     * Sets spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts.
     *
     * @param partsSpacing spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts
     */
    public void setPartsSpacing ( final int partsSpacing )
    {
        this.partsSpacing = partsSpacing;
    }

    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( !TextUtils.isBlank ( value ) && Objects.notEquals ( value, START, MIDDLE, FILL, END, TRIM ) )
        {
            final String msg = "Unsupported layout constraints: %s";
            throw new IllegalArgumentException ( String.format ( msg, value ) );
        }

        /**
         * Resolving proper constraints.
         */
        final String actualConstraints = value == null || value.trim ().equals ( "" ) ? START : value;

        /**
         * Checking intersection to ensure layout doesn't get messy.
         */
        if ( Objects.equals ( actualConstraints, MIDDLE ) &&
                this.components.containsKey ( FILL ) )
        {
            throw new RuntimeException ( "Layout already contains element under `FILL` constraints" );
        }
        else if ( Objects.equals ( actualConstraints, FILL ) &&
                ( this.components.containsKey ( MIDDLE ) || this.components.containsKey ( FILL ) ) )
        {
            throw new RuntimeException ( "Layout already contains element under `MIDDLE` or `FILL` constraints" );
        }
        else if ( Objects.equals ( actualConstraints, TRIM ) &&
                this.components.containsKey ( TRIM ) )
        {
            throw new RuntimeException ( "Layout already contains element under `TRIM` constraints" );
        }

        /**
         * Saving constraints per component.
         */
        this.constraints.put ( component, actualConstraints );

        /**
         * Saving components per constraints.
         */
        List<Component> components = this.components.get ( actualConstraints );
        if ( components == null )
        {
            components = new ArrayList<Component> ( 1 );
        }
        components.add ( component );
        this.components.put ( actualConstraints, components );
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        final String constraints = this.constraints.get ( component );

        /**
         * Removing constraints for component.
         */
        this.constraints.remove ( component );

        /**
         * Removing component for constraints.
         */
        final List<Component> components = this.components.get ( constraints );
        components.remove ( component );
        if ( components.isEmpty () )
        {
            this.components.remove ( constraints );
        }
    }

    @Override
    public void migrate ( @NotNull final Container container, @Nullable final LayoutManager oldLayout )
    {
        if ( oldLayout != null && oldLayout instanceof AbstractLineLayout )
        {
            final AbstractLineLayout oldLineLayout = ( AbstractLineLayout ) oldLayout;
            for ( int i = 0; i < container.getComponentCount (); i++ )
            {
                final Component component = container.getComponent ( i );
                if ( oldLineLayout.constraints.containsKey ( component ) )
                {
                    addComponent ( component, oldLineLayout.constraints.get ( component ) );
                }
            }
        }
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
    {
        final int orientation = getOrientation ( container );
        final boolean horizontal = orientation == HORIZONTAL;
        final Insets insets = container.getInsets ();
        final Dimension size = container.getSize ();
        final boolean ltr = container.getComponentOrientation ().isLeftToRight ();
        final int lineWidth = horizontal ? size.height - insets.top - insets.bottom : size.width - insets.left - insets.right;

        /**
         * Calculating preferred size of the whole layout.
         * We will need this to decide how components should be placed.
         * We also cache all component preferred sizes to avoid overhead calculations.
         */
        final Map<Component, Dimension> cache = new HashMap<Component, Dimension> ();
        final Dimension preferredSize = preferredLayoutSize ( container, cache );

        /**
         * Calculating {@link #TRIM} size separately.
         * We will need this later for various calculations.
         */
        final Dimension trimSize = preferredPartSize ( TRIM, orientation, ltr, cache );

        /**
         * Bounds available within the container.
         */
        final Rectangle available = new Rectangle (
                insets.left,
                insets.top,
                size.width - insets.left - insets.right,
                size.height - insets.top - insets.bottom
        );

        /**
         * There are two different placement cases.
         * One is when we have enough space to place all elements in their preferred sizes.
         * The other one is when we don't have enough space and need to make sure components don't overlap.
         */
        final boolean fit = horizontal ? preferredSize.width <= size.width : preferredSize.height <= size.height;
        if ( fit )
        {
            /**
             * There is enough space to place all components.
             * We can simplify calculations and assume positions.
             */

            /**
             * Placing START components.
             */
            final Dimension startSize = preferredPartSize ( START, orientation, ltr, cache );
            if ( startSize.width > 0 )
            {
                final Point startPoint = new Point ( insets.left, insets.top );
                placeComponents ( START, startPoint, orientation, ltr, lineWidth, available, cache );
            }

            /**
             * Placing END components.
             */
            final Dimension endSize = preferredPartSize ( END, orientation, ltr, cache );
            if ( endSize.width > 0 )
            {
                final Point endPoint = new Point (
                        horizontal ? size.width - insets.right - endSize.width : insets.left,
                        horizontal ? insets.top : size.height - insets.bottom - endSize.height
                );
                placeComponents ( END, endPoint, orientation, ltr, lineWidth, available, cache );
            }

            /**
             * Placing components at the middle.
             */
            final Dimension middleSize = preferredPartSize ( MIDDLE, orientation, ltr, cache );
            final Dimension fillSize = preferredPartSize ( FILL, orientation, ltr, cache );
            if ( middleSize.width > 0 || fillSize.width > 0 )
            {
                final int xStartLength = horizontal ? startSize.width + ( startSize.width > 0 ? partsSpacing : 0 ) : 0;
                final int yStartLength = horizontal ? 0 : startSize.height + ( startSize.height > 0 ? partsSpacing : 0 );
                final int xEndLength = horizontal ? endSize.width + ( endSize.width > 0 ? partsSpacing : 0 ) : 0;
                final int yEndLength = horizontal ? 0 : endSize.height + ( endSize.height > 0 ? partsSpacing : 0 );
                final Rectangle fillBounds = new Rectangle (
                        insets.left + xStartLength,
                        insets.top + yStartLength,
                        size.width - insets.left - xStartLength - xEndLength - insets.right,
                        size.height - insets.top - yStartLength - yEndLength - insets.bottom
                );
                if ( middleSize.width > 0 )
                {
                    /**
                     * Placing MIDDLE components.
                     */
                    final Point middlePoint = new Point (
                            horizontal ? fillBounds.x + fillBounds.width / 2 - middleSize.width / 2 : fillBounds.x,
                            horizontal ? fillBounds.y : fillBounds.y + fillBounds.height / 2 - middleSize.height / 2
                    );
                    placeComponents ( MIDDLE, middlePoint, orientation, ltr, lineWidth, available, cache );
                }
                else
                {
                    /**
                     * Placing FILL component.
                     */
                    placeFillComponent ( fillBounds, available );
                }
            }
        }
        else
        {
            /**
             * There is not enough space to place all components.
             * We have to carefully position them one by one from left to right or from right to left (depending on component orientation).
             */

            /**
             * Bounds available for all container elements except {@link #TRIM}.
             * It is necessary to hide all components that are staying outside of the bounds.
             */
            final Rectangle trimmed;
            if ( trimSize.width > 0 )
            {
                /**
                 * Only calculating trimmed size when {@link #TRIM} component is available.
                 * This will slightly shrink available bounds by the {@link #TRIM} component size plus {@link #spacing}.
                 */
                trimmed = new Rectangle (
                        available.x + ( horizontal && !ltr ? trimSize.width + spacing : 0 ),
                        available.y,
                        available.width - ( horizontal ? trimSize.width + spacing : 0 ),
                        available.height - ( !horizontal ? trimSize.height + spacing : 0 )
                );
            }
            else
            {
                trimmed = available;
            }

            /**
             * Starting point for all components.
             */
            final Point point = new Point (
                    insets.left + ( horizontal && !ltr ? size.width - preferredSize.width : 0 ),
                    insets.top
            );

            /**
             * Placing START components.
             */
            final Dimension startSize = preferredPartSize ( START, orientation, ltr, cache );
            if ( startSize.width > 0 )
            {
                placeComponents ( START, point, orientation, ltr, lineWidth, trimmed, cache );
                point.x += horizontal ? partsSpacing : 0;
                point.y += horizontal ? 0 : partsSpacing;
            }

            /**
             * Placing MIDDLE components.
             */
            final Dimension middleSize = preferredPartSize ( MIDDLE, orientation, ltr, cache );
            if ( middleSize.width > 0 )
            {
                placeComponents ( MIDDLE, point, orientation, ltr, lineWidth, trimmed, cache );
                point.x += horizontal ? partsSpacing : 0;
                point.y += horizontal ? 0 : partsSpacing;
            }

            /**
             * Placing FILL components.
             */
            final Dimension fillSize = preferredPartSize ( FILL, orientation, ltr, cache );
            if ( fillSize.width > 0 )
            {
                placeComponents ( FILL, point, orientation, ltr, lineWidth, trimmed, cache );
                point.x += horizontal ? partsSpacing : 0;
                point.y += horizontal ? 0 : partsSpacing;
            }

            /**
             * Placing END components.
             */
            final Dimension endSize = preferredPartSize ( END, orientation, ltr, cache );
            if ( endSize.width > 0 )
            {
                placeComponents ( END, point, orientation, ltr, lineWidth, trimmed, cache );
            }
        }

        /**
         * Placing TRIM component.
         */
        if ( trimSize.width > 0 )
        {
            placeTrimComponent ( trimSize, orientation, ltr, available, fit );
        }
    }

    /**
     * Places {@link Component}s under specified constraints in a line starting at the specified {@link Point}.
     *
     * @param c           layout constraints
     * @param point       starting {@link Point}
     * @param orientation layout orientation
     * @param ltr         layout component orientation
     * @param lineWidth   layout line width
     * @param available   available space bounds
     * @param cache       {@link Map} containing {@link Component} size caches
     */
    protected void placeComponents ( @NotNull final String c, @NotNull final Point point,
                                     final int orientation, final boolean ltr, final int lineWidth,
                                     @NotNull final Rectangle available, @NotNull final Map<Component, Dimension> cache )
    {
        final String constraints = constraints ( c, orientation, ltr );
        final boolean horizontal = orientation == HORIZONTAL;

        /**
         * Retrieving components for constraints.
         * We need to sort them by Z-order to place them correctly.
         */
        final List<Component> components = this.components.get ( constraints );
        CollectionUtils.sort ( components, COMPONENTS_COMPARATOR );

        /**
         * Placing components.
         */
        if ( ltr || !horizontal )
        {
            /**
             * Direct components placement order for horizontal or vertical layout.
             */
            for ( final Component component : components )
            {
                final Dimension cps = preferredSize ( component, cache );
                final Rectangle bounds = new Rectangle (
                        point.x,
                        point.y,
                        horizontal ? cps.width : lineWidth,
                        horizontal ? lineWidth : cps.height
                );
                if ( available.contains ( bounds ) )
                {
                    component.setBounds ( bounds );
                }
                else
                {
                    component.setBounds ( HIDE_BOUNDS );
                }
                point.x += horizontal ? bounds.width + spacing : 0;
                point.y += horizontal ? 0 : bounds.height + spacing;
            }
        }
        else
        {
            /**
             * Reversed (RTL) components placement order for horizontal layout.
             */
            for ( int i = components.size () - 1; i >= 0; i-- )
            {
                final Component component = components.get ( i );
                final Dimension cps = preferredSize ( component, cache );
                final Rectangle bounds = new Rectangle (
                        point.x,
                        point.y,
                        cps.width,
                        lineWidth
                );
                if ( available.contains ( bounds ) )
                {
                    component.setBounds ( bounds );
                }
                else
                {
                    component.setBounds ( HIDE_BOUNDS );
                }
                point.x += bounds.width + spacing;
            }
        }
        point.x -= horizontal ? spacing : 0;
        point.y -= horizontal ? 0 : spacing;
    }

    /**
     * Places {@link #FILL} component at the specified bounds.
     *
     * @param bounds    {@link #FILL} bounds
     * @param available available space bounds
     */
    protected void placeFillComponent ( @NotNull final Rectangle bounds, @NotNull final Rectangle available )
    {
        /**
         * Placing single {@link #FILL} component.
         * It will always be only one component and we will never call this method if there is none.
         */
        if ( available.contains ( bounds ) )
        {
            components.get ( FILL ).get ( 0 ).setBounds ( bounds );
        }
        else
        {
            components.get ( FILL ).get ( 0 ).setBounds ( HIDE_BOUNDS );
        }
    }

    /**
     * Places {@link #TRIM} component.
     *
     * @param trimSize    size of the {@link #TRIM} component
     * @param orientation layout orientation
     * @param ltr         layout component orientation
     * @param available   available space bounds
     * @param fit         whether or not layout size is larger or equal to its preferred
     */
    protected void placeTrimComponent ( @NotNull final Dimension trimSize, final int orientation, final boolean ltr,
                                        @NotNull final Rectangle available, final boolean fit )
    {
        if ( !fit )
        {
            final boolean horizontal = orientation == HORIZONTAL;
            final Rectangle trimBounds = new Rectangle (
                    horizontal && ltr ? available.x + available.width - trimSize.width : available.x,
                    horizontal ? available.y : available.y + available.height - trimSize.height,
                    horizontal ? trimSize.width : available.width,
                    horizontal ? available.height : trimSize.height
            );
            components.get ( TRIM ).get ( 0 ).setBounds ( trimBounds );
        }
        else
        {
            components.get ( TRIM ).get ( 0 ).setBounds ( HIDE_BOUNDS );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        return preferredLayoutSize ( container, new HashMap<Component, Dimension> () );
    }

    /**
     * Returns preferred layout size.
     *
     * @param container layout container
     * @param cache     {@link Map} containing {@link Component} size caches
     * @return preferred layout size
     */
    protected Dimension preferredLayoutSize ( @NotNull final Container container, @NotNull final Map<Component, Dimension> cache )
    {
        final Dimension ps = new Dimension ( 0, 0 );

        /**
         * Calculating content size.
         * We don't need to differentiate {@link #MIDDLE} and {@link #FILL} constraints here.
         * Whatever doesn't exist will simply be ignored by having zero sizes.
         * Also {@link #TRIM} part is not included into preferred size at all as it only appears when size is not enough.
         */
        final int orientation = getOrientation ( container );
        final boolean ltr = container.getComponentOrientation ().isLeftToRight ();
        expandSizeFromPart ( ps, START, partsSpacing, orientation, ltr, cache );
        expandSizeFromPart ( ps, MIDDLE, partsSpacing, orientation, ltr, cache );
        expandSizeFromPart ( ps, FILL, partsSpacing, orientation, ltr, cache );
        expandSizeFromPart ( ps, END, partsSpacing, orientation, ltr, cache );

        /**
         * Adding insets afterwards.
         */
        final Insets insets = container.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    /**
     * Expands provided {@link Dimension} by preferred size of {@link Component}s under specified constraints.
     *
     * @param size        {@link Dimension} to expand
     * @param constraints layout constraints
     * @param spacing     extra spacing between this and previous parts
     * @param orientation layout orientation
     * @param ltr         layout component orientation
     * @param cache       {@link Map} containing {@link Component} size caches
     */
    protected void expandSizeFromPart ( @NotNull final Dimension size, @NotNull final String constraints, final int spacing,
                                        final int orientation, final boolean ltr, @NotNull final Map<Component, Dimension> cache )
    {
        final Dimension partSize = preferredPartSize ( constraints, orientation, ltr, cache );
        if ( partSize.width > 0 )
        {
            final boolean horizontal = orientation == HORIZONTAL;
            size.width = horizontal ? size.width + ( size.width > 0 ? spacing : 0 ) + partSize.width :
                    Math.max ( size.width, partSize.width );
            size.height = horizontal ? Math.max ( size.height, partSize.height ) :
                    size.height + ( size.height > 0 ? spacing : 0 ) + partSize.height;
        }
    }

    /**
     * Returns preferred size of {@link Component}s under the specified constraints.
     *
     * @param c           layout constraints
     * @param orientation layout orientation
     * @param ltr         layout component orientation
     * @param cache       {@link Map} containing {@link Component} size caches
     * @return preferred size of {@link Component}s under the specified constraints
     */
    @NotNull
    protected Dimension preferredPartSize ( @NotNull final String c, final int orientation, final boolean ltr,
                                            @NotNull final Map<Component, Dimension> cache )
    {
        final Dimension ps = new Dimension ( 0, 0 );
        final String constraints = constraints ( c, orientation, ltr );

        /**
         * Retrieving components for constraints.
         * We do not need to sort them for preferred size calculations.
         */
        final List<Component> components = this.components.get ( constraints );

        /**
         * Collecting component preferred sizes.
         */
        if ( CollectionUtils.notEmpty ( components ) )
        {
            final boolean horizontal = orientation == HORIZONTAL;
            for ( final Component component : components )
            {
                final Dimension cps = preferredSize ( component, cache );
                ps.width = horizontal ? ps.width + cps.width + spacing : Math.max ( ps.width, cps.width );
                ps.height = horizontal ? Math.max ( ps.height, cps.height ) : ps.height + cps.height + spacing;
            }
            ps.width -= horizontal ? spacing : 0;
            ps.height -= horizontal ? 0 : spacing;
        }

        return ps;
    }

    /**
     * Returns cached {@link Component} preferred size.
     *
     * @param component {@link Component}
     * @param cache     {@link Map} containing {@link Component} size caches
     * @return cached {@link Component} preferred size
     */
    @NotNull
    protected Dimension preferredSize ( @NotNull final Component component, @NotNull final Map<Component, Dimension> cache )
    {
        Dimension ps = cache.get ( component );
        if ( ps == null )
        {
            ps = component.getPreferredSize ();
            cache.put ( component, ps );
        }
        return ps;
    }

    /**
     * Returns actual constraints based on the layout orientation and component orientation.
     *
     * @param constraints layout constraints
     * @param orientation layout orientation
     * @param ltr         layout component orientation
     * @return actual constraints based on the layout orientation and component orientation
     */
    @NotNull
    protected String constraints ( @NotNull final String constraints, final int orientation, final boolean ltr )
    {
        final String c;
        if ( Objects.equals ( constraints, START ) )
        {
            c = ltr || orientation != HORIZONTAL ? START : END;
        }
        else if ( Objects.equals ( constraints, END ) )
        {
            c = ltr || orientation != HORIZONTAL ? END : START;
        }
        else
        {
            c = constraints;
        }
        return c;
    }

    /**
     * Returns either {@link #HORIZONTAL} or {@link #VERTICAL} orientation for {@link Container}.
     *
     * @param container {@link Container} to retrieve orientation for
     * @return either {@link #HORIZONTAL} or {@link #VERTICAL} orientation for {@link Container}
     */
    public abstract int getOrientation ( @NotNull Container container );
}