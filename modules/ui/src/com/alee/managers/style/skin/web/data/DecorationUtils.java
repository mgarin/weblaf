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

package com.alee.managers.style.skin.web.data;

import com.alee.managers.style.skin.web.AbstractDecorationPainter;
import com.alee.managers.style.skin.web.data.background.GradientColor;
import com.alee.managers.style.skin.web.data.background.GradientType;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
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
    public static Paint getPaint ( final GradientType type, final List<GradientColor> colors, final int x1, final int y1, final int x2,
                                   final int y2 )
    {
        if ( colors.size () == 1 )
        {
            return colors.get ( 0 ).getColor ();
        }
        else
        {
            final float[] f = new float[ colors.size () ];
            final Color[] c = new Color[ colors.size () ];
            final boolean even = colors.get ( 0 ).getFraction () == null;
            for ( int i = 0; i < colors.size (); i++ )
            {
                final GradientColor color = colors.get ( i );
                f[ i ] = even ? i * 1f / ( colors.size () - 1 ) : color.getFraction ();
                c[ i ] = color.getColor ();
            }
            if ( type == GradientType.linear )
            {
                return new LinearGradientPaint ( x1, y1, x2, y2, f, c );
            }
            else if ( type == GradientType.radial )
            {
                final float r = ( float ) Point.distance ( x1, y1, x2, y2 );
                return new RadialGradientPaint ( x1, y1, r, f, c );
            }
            else
            {
                throw new RuntimeException ( "Unknown gradient type provided" );
            }
        }
    }

    /**
     * Informs about decoratable state changes.
     *
     * @param component component states changed for
     * @param oldStates old custom decoratable states
     * @param newStates new custom decoratable states
     */
    public static void fireDecoratableStatesChanged ( final JComponent component )
    {
        SwingUtils.firePropertyChanged ( component, AbstractDecorationPainter.DECORATABLE_STATES_PROPERTY, null, null );
    }
}