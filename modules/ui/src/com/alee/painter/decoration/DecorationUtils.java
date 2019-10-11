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

package com.alee.painter.decoration;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.background.GradientColor;
import com.alee.painter.decoration.background.GradientType;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * Various utilities used by decoration data.
 *
 * @author Mikle Garin
 */
public final class DecorationUtils
{
    /**
     * Returns gradient paint.
     *
     * @param type   gradient type
     * @param colors gradient colors
     * @param x1     gradient start X coordinate
     * @param y1     gradient start Y coordinate
     * @param x2     gradient end X coordinate
     * @param y2     gradient end Y coordinate
     * @return separator line paint
     */
    @NotNull
    public static Paint getPaint ( @NotNull final GradientType type, @NotNull final List<GradientColor> colors,
                                   final int x1, final int y1, final int x2, final int y2 )
    {
        final Paint result;
        if ( colors.size () == 1 )
        {
            result = colors.get ( 0 ).getColor ();
        }
        else if ( colors.size () > 1 )
        {
            final float[] f = new float[ colors.size () ];
            final Color[] c = new Color[ colors.size () ];
            final boolean even = colors.get ( 0 ).getFraction () == null;
            for ( int i = 0; i < colors.size (); i++ )
            {
                final GradientColor color = colors.get ( i );
                if ( even )
                {
                    f[ i ] = i * 1f / ( colors.size () - 1 );
                }
                else if ( color.getFraction () > 1f )
                {
                    final int length = MathUtils.sqrt ( MathUtils.sqr ( x2 - x1 ) + MathUtils.sqr ( y2 - y1 ) );
                    f[ i ] = MathUtils.limit ( 0, color.getFraction (), length ) / length;
                }
                else
                {
                    f[ i ] = color.getFraction ();
                }
                c[ i ] = color.getColor ();
            }
            final boolean fits = x1 != x2 || y1 != y2;
            if ( type == GradientType.linear )
            {
                if ( fits )
                {
                    result = new LinearGradientPaint ( x1, y1, x2, y2, f, c );
                }
                else
                {
                    result = colors.get ( 0 ).getColor ();
                }
            }
            else if ( type == GradientType.radial )
            {
                if ( fits )
                {
                    final float r = ( float ) Point.distance ( x1, y1, x2, y2 );
                    result = new RadialGradientPaint ( x1, y1, r, f, c );
                }
                else
                {
                    result = colors.get ( 0 ).getColor ();
                }
            }
            else
            {
                throw new RuntimeException ( "Unknown GradientType provided" );
            }
        }
        else
        {
            throw new RuntimeException ( "At least one GradientColor must be provided" );
        }
        return result;
    }

    /**
     * Returns sides or lines descriptor string representation.
     *
     * @param top    whether or not top should be painted
     * @param left   whether or not left should be painted
     * @param bottom whether or not bottom should be painted
     * @param right  whether or not right should be painted
     * @return sides or lines descriptor string representation
     */
    @NotNull
    public static String toString ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        return ( top ? 1 : 0 ) + "," + ( left ? 1 : 0 ) + "," + ( bottom ? 1 : 0 ) + "," + ( right ? 1 : 0 );
    }

    /**
     * Returns {@link List} of extra decoration states from {@link Object} based on its {@link Stateful} implementation.
     * It performs all necessary checks and always returns non-{@code null} {@link List} of states, though it might be empty.
     *
     * @param object {@link Object} to retrieve {@link List} of extra decoration states from
     * @return {@link List} of extra decoration states from {@link Object} based on its {@link Stateful} implementation
     */
    @NotNull
    public static List<String> getExtraStates ( @Nullable final Object object )
    {
        final List<String> states;
        if ( object instanceof Stateful )
        {
            final List<String> extra = ( ( Stateful ) object ).getStates ();
            states = CollectionUtils.notEmpty ( extra ) ? extra : Collections.<String>emptyList ();
        }
        else
        {
            states = Collections.emptyList ();
        }
        return states;
    }

    /**
     * Informs about {@link Component} decoration states changes.
     *
     * @param component {@link Component} decoration states changed for
     */
    public static void fireStatesChanged ( @Nullable final Component component )
    {
        if ( component instanceof JComponent )
        {
            SwingUtils.firePropertyChanged ( component, AbstractDecorationPainter.DECORATION_STATES_PROPERTY, null, null );
        }
    }

    /**
     * Informs about {@link Component} decoration border changes.
     *
     * @param component {@link Component} decoration border changed for
     */
    public static void fireBorderChanged ( @Nullable final Component component )
    {
        if ( component instanceof JComponent )
        {
            SwingUtils.firePropertyChanged ( component, AbstractDecorationPainter.DECORATION_BORDER_PROPERTY, null, null );
        }
    }
}