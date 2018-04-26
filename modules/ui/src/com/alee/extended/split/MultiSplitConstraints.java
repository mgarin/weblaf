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

import com.alee.utils.TextUtils;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * Constraints for a single {@link Component} within {@link WebMultiSplitPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see MultiSplitView
 * @see WebMultiSplitPaneModel
 * @see WebMultiSplitPane
 */
public class MultiSplitConstraints implements Cloneable, Serializable
{
    /**
     * Indicates that the row/column should be taking all of the remaining space in the split pane.
     * If there are two or more component placed under {@link #FILL} they will always take equal space.
     */
    public static final double FILL = -1.0;

    /**
     * Indicates that the row/column should be taking just enough space to accomidate the
     * {@link Component#getPreferredSize()} of all components contained within this row/column.
     */
    public static final double PREFERRED = -2.0;

    /**
     * Indicates that the row/column should be taking just enough space to accomidate the
     * {@link Component#getMinimumSize()} of all components contained within this row/column.
     */
    public static final double MINIMUM = -3.0;

    /**
     * Initial size of the {@link MultiSplitView} placed under these constraints.
     * Anything between {@code 0.0} and {@code 1.0} inclusive is considered as percentage of the total available size minus pixel sizes.
     * Anything greater than {@code 1.0} is considered as pixel size and will be rounded.
     * It can also be set to {@link #FILL}, {@link #PREFERRED} or {@link #MINIMUM} for some advanced cases.
     */
    protected final double size;

    /**
     * Weight of the {@link MultiSplitView} placed under these constraints.
     * It affects how space is distributed between {@link MultiSplitView}s whenever {@link WebMultiSplitPane} size changes.
     * It can be set to anything greater or equal to {@code 0.0}.
     */
    protected final double weight;

    /**
     * Constructs new {@link MultiSplitConstraints}.
     */
    public MultiSplitConstraints ()
    {
        this ( null, null );
    }

    /**
     * Constructs new {@link MultiSplitConstraints}.
     *
     * @param size initial size of the {@link MultiSplitView} placed under these constraints
     */
    public MultiSplitConstraints ( final Number size )
    {
        this ( size.doubleValue (), null );
    }

    /**
     * Constructs new {@link MultiSplitConstraints}.
     *
     * @param size   initial size of the {@link MultiSplitView} placed under these constraints
     * @param weight weight of the {@link MultiSplitView} placed under these constraints
     */
    public MultiSplitConstraints ( final Double size, final Double weight )
    {
        this.size = validSize ( size );
        this.weight = validWeight ( weight );
    }

    /**
     * Constructs new {@link MultiSplitConstraints}.
     *
     * @param constraints {@link MultiSplitConstraints} text representation
     */
    public MultiSplitConstraints ( final String constraints )
    {
        try
        {
            final Double size;
            final Double weight;
            final List<String> values = TextUtils.stringToList ( constraints, "," );
            if ( values.size () == 1 )
            {
                size = Double.parseDouble ( values.get ( 0 ) );
                weight = null;

            }
            else if ( values.size () == 2 )
            {
                size = Double.parseDouble ( values.get ( 0 ) );
                weight = Double.parseDouble ( values.get ( 1 ) );
            }
            else
            {
                final String msg = "Unknown layout constraints: %s";
                throw new IllegalArgumentException ( String.format ( msg, constraints ) );
            }
            this.size = validSize ( size );
            this.weight = validWeight ( weight );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to parse layout constraints: %s";
            throw new IllegalArgumentException ( String.format ( msg, constraints ) );
        }
    }

    /**
     * Returns validated and adjusted size value.
     *
     * @param size initial size of the {@link MultiSplitView} placed under these constraints
     * @return validated and adjusted size value
     */
    protected double validSize ( final Double size )
    {
        if ( size != null && size < 0.0 && size != FILL && size != PREFERRED && size != MINIMUM )
        {
            throw new IllegalArgumentException ( "Unknown size value: " + size () );
        }
        return size != null ? size : 1.0;
    }

    /**
     * Returns validated and adjusted weight value.
     *
     * @param weight weight of the {@link MultiSplitView} placed under these constraints
     * @return validated and adjusted weight value
     */
    protected double validWeight ( final Double weight )
    {
        final double defaultWeight;
        if ( weight != null )
        {
            if ( weight < 0.0 )
            {
                throw new IllegalArgumentException ( "Weight cannot be less than zero: " + weight );
            }
            defaultWeight = weight;
        }
        else if ( isPercents () )
        {
            // For percentage size we can use the percentage itself
            defaultWeight = percents ();
        }
        else if ( isFill () )
        {
            // We can't calculate exact weight without having other constraints, so just leave it at 1.0
            defaultWeight = 1.0;
        }
        else
        {
            // For pixel-preceise types we don't need weight
            defaultWeight = 0.0;
        }
        return defaultWeight;
    }

    /**
     * Returns initial size of the {@link MultiSplitView} placed under these constraints.
     *
     * @return initial size of the {@link MultiSplitView} placed under these constraints
     */
    public double size ()
    {
        return size;
    }

    /**
     * Returns whether or not {@link MultiSplitView} placed under these constraints contains pixel size.
     *
     * @return {@code true} if {@link MultiSplitView} placed under these constraints contains pixel size, {@code false} otherwise
     */
    public boolean isPixels ()
    {
        return size > 1.0;
    }

    /**
     * Returns initial size in pixels of the {@link MultiSplitView} placed under these constraints.
     *
     * @return initial size in pixels of the {@link MultiSplitView} placed under these constraints
     */
    public int pixels ()
    {
        if ( isPixels () )
        {
            return ( int ) size;
        }
        else
        {
            throw new IllegalArgumentException ( "View size is not pixel" );
        }
    }

    /**
     * Returns whether or not {@link MultiSplitView} placed under these constraints contains percent size.
     *
     * @return {@code true} if {@link MultiSplitView} placed under these constraints contains percent size, {@code false} otherwise
     */
    public boolean isPercents ()
    {
        return 0.0 <= size && size <= 1.0;
    }

    /**
     * Returns initial size in percents of the {@link MultiSplitView} placed under these constraints.
     *
     * @return initial size in percents of the {@link MultiSplitView} placed under these constraints
     */
    public double percents ()
    {
        if ( isPercents () )
        {
            return size;
        }
        else
        {
            throw new IllegalArgumentException ( "View size is not percents" );
        }
    }

    /**
     * Returns whether or not {@link MultiSplitView} placed under these constraints should fill remaining space.
     *
     * @return {@code true} if {@link MultiSplitView} placed under these constraints should fill remaining space,
     * {@code false} otherwise
     */
    public boolean isFill ()
    {
        return size == FILL;
    }

    /**
     * Returns whether or not {@link MultiSplitView} placed under these constraints refers to preferred component size.
     *
     * @return {@code true} if {@link MultiSplitView} placed under these constraints refers to preferred component size,
     * {@code false} otherwise
     */
    public boolean isPreferred ()
    {
        return size == PREFERRED;
    }

    /**
     * Returns whether or not {@link MultiSplitView} placed under these constraints refers to minimum component size.
     *
     * @return {@code true} if {@link MultiSplitView} placed under these constraints refers to minimum component size,
     * {@code false} otherwise
     */
    public boolean isMinimum ()
    {
        return size == MINIMUM;
    }

    /**
     * Returns weight of the {@link MultiSplitView} placed under these constraints.
     *
     * @return weight of the {@link MultiSplitView} placed under these constraints
     */
    public double weight ()
    {
        return weight;
    }

    /**
     * Returns whether or not {@link MultiSplitView} placed under these constraints has weight.
     *
     * @return {@code true} if {@link MultiSplitView} placed under these constraints has weight, {@code false} otherwise
     */
    public boolean hasWeight ()
    {
        return weight > 0.0;
    }
}