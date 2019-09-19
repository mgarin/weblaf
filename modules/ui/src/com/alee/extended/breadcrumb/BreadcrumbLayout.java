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

package com.alee.extended.breadcrumb;

import com.alee.api.annotations.NotNull;
import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * Special {@link LayoutManager} for {@link WebBreadcrumb}.
 * It allows {@link WebBreadcrumb} elements to overlap each other.
 * Paired with unique styling of {@link WebBreadcrumb} elements that creates breadcrumb component.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "BreadcrumbLayout" )
public class BreadcrumbLayout extends AbstractLayoutManager implements Mergeable, Cloneable, Serializable
{
    /**
     * Layout {@link Strategy}.
     *
     * @see Strategy
     */
    @XStreamAsAttribute
    protected Strategy strategy;

    /**
     * Length of the overlap.
     */
    @XStreamAsAttribute
    protected Integer overlap;

    /**
     * Constructs new {@link BreadcrumbLayout} with default overlap length.
     */
    public BreadcrumbLayout ()
    {
        this ( Strategy.fillLast, 0 );
    }

    /**
     * Constructs new {@link BreadcrumbLayout} with the specified overlap length.
     *
     * @param strategy layout {@link Strategy}
     * @param overlap  overlap length
     */
    public BreadcrumbLayout ( final Strategy strategy, final int overlap )
    {
        this.strategy = strategy;
        this.overlap = overlap;
    }

    /**
     * Returns layout {@link Strategy}.
     *
     * @return layout {@link Strategy}
     */
    public Strategy strategy ()
    {
        return strategy != null ? strategy : Strategy.fillLast;
    }

    /**
     * Sets layout {@link Strategy}.
     *
     * @param strategy new layout {@link Strategy}
     */
    public void setStrategy ( final Strategy strategy )
    {
        this.strategy = strategy;
    }

    /**
     * Returns overlap length.
     *
     * @return overlap length
     */
    public int overlap ()
    {
        return overlap != null ? overlap : 0;
    }

    /**
     * Sets overlap length.
     *
     * @param overlap new overlap length
     */
    public void setOverlap ( final int overlap )
    {
        this.overlap = overlap;
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
    {
        final Insets insets = container.getInsets ();
        final boolean ltr = container.getComponentOrientation ().isLeftToRight ();
        final int componentCount = container.getComponentCount ();
        switch ( strategy () )
        {
            case allPreferred:
            case fillLast:
            {
                if ( ltr )
                {
                    int x = insets.left;
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Component component = container.getComponent ( i );
                        final Dimension ps = component.getPreferredSize ();
                        if ( strategy () != Strategy.fillLast || i != componentCount - 1 )
                        {
                            component.setBounds ( x, insets.top, ps.width, container.getHeight () - insets.top - insets.bottom );
                        }
                        else
                        {
                            final int w = Math.max ( ps.width, container.getWidth () - x - insets.right );
                            component.setBounds ( x, insets.top, w, container.getHeight () - insets.top - insets.bottom );
                        }
                        x += ps.width - overlap;
                    }
                }
                else
                {
                    int x = container.getWidth () - insets.right;
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Component component = container.getComponent ( i );
                        final Dimension ps = component.getPreferredSize ();
                        if ( strategy () != Strategy.fillLast || i != componentCount - 1 )
                        {
                            component.setBounds ( x - ps.width, insets.top, ps.width, container.getHeight () - insets.top - insets.bottom );
                        }
                        else
                        {
                            final int w = Math.max ( ps.width, x - insets.left );
                            component.setBounds ( x - w, insets.top, w, container.getHeight () - insets.top - insets.bottom );
                        }
                        x += overlap - ps.width;
                    }
                }
                break;
            }
            case allEqual:
            {
                final Dimension max = maxComponentSize ( container );
                if ( ltr )
                {
                    int x = insets.left;
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Component component = container.getComponent ( i );
                        component.setBounds ( x, insets.top, max.width, container.getHeight () - insets.top - insets.bottom );
                        x += max.width - overlap;
                    }
                }
                else
                {
                    int x = container.getWidth () - insets.right;
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Component component = container.getComponent ( i );
                        component.setBounds ( x - max.width, insets.top, max.width, container.getHeight () - insets.top - insets.bottom );
                        x += overlap - max.width;
                    }
                }
                break;
            }
            case fillAll:
            {
                final int available = container.getWidth () - insets.left - insets.right;
                final int part = componentCount > 1 ? available / componentCount + overlap : available;
                int left = available;
                if ( ltr )
                {
                    int x = insets.left;
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Component component = container.getComponent ( i );
                        final int w = i < componentCount - 1 ? part : left;
                        component.setBounds ( x, insets.top, w, container.getHeight () - insets.top - insets.bottom );
                        x += w - overlap;
                        left -= part - overlap;
                    }
                }
                else
                {
                    int x = container.getWidth () - insets.right;
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Component component = container.getComponent ( i );
                        final int w = i < componentCount - 1 ? part : left;
                        component.setBounds ( x - w, insets.top, w, container.getHeight () - insets.top - insets.bottom );
                        x += overlap - w;
                        left -= part - overlap;
                    }
                }
                break;
            }
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        final Dimension ps = new Dimension ( 0, 0 );

        final int componentCount = container.getComponentCount ();
        if ( componentCount > 0 )
        {
            switch ( strategy () )
            {
                case allPreferred:
                case fillLast:
                    for ( int i = 0; i < componentCount; i++ )
                    {
                        final Dimension cps = container.getComponent ( i ).getPreferredSize ();
                        ps.width += cps.width - ( i < componentCount - 1 ? overlap : 0 );
                        ps.height = Math.max ( ps.height, cps.height );
                    }
                    break;

                case allEqual:
                case fillAll:
                    final Dimension max = maxComponentSize ( container );
                    ps.width = max.width * componentCount - overlap * ( componentCount - 1 );
                    ps.height = max.height;
                    break;

            }
        }

        final Insets insets = container.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    /**
     * Returns {@link Dimension} containing width and height of largest components in the {@link Container}.
     *
     * @param container {@link Container}
     * @return {@link Dimension} containing width and height of largest components in the {@link Container}
     */
    protected Dimension maxComponentSize ( final Container container )
    {
        final Dimension max = new Dimension ( 0, 0 );
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Dimension cps = container.getComponent ( i ).getPreferredSize ();
            max.width = Math.max ( max.width, cps.width );
            max.height = Math.max ( max.height, cps.height );
        }
        return max;
    }

    /**
     * Enumeration representing supported {@link BreadcrumbLayout} strategies.
     */
    public static enum Strategy
    {
        /**
         * Elements should remain at their preferred sizes.
         */
        allPreferred,

        /**
         * Last element should fill all remaining {@link WebBreadcrumb} space.
         */
        fillLast,

        /**
         * All elements should take space of the same length equal to maximum length of a single element within the {@link WebBreadcrumb}.
         */
        allEqual,

        /**
         * All elements should take space of the same length equal to {@code 1/elementCount} of total {@link WebBreadcrumb} length.
         */
        fillAll
    }

    /**
     * The UI resource version of {@link BreadcrumbLayout}.
     */
    @XStreamAlias ( "BreadcrumbLayout$UIResource" )
    public static final class UIResource extends BreadcrumbLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link BreadcrumbLayout}.
         */
    }
}