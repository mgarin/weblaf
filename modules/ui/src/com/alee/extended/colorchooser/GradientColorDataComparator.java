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

package com.alee.extended.colorchooser;

import java.util.Comparator;

/**
 * GradientColorData location comparator.
 *
 * @author Mikle Garin
 */
public class GradientColorDataComparator implements Comparator<GradientColorData>
{
    /**
     * Compares two GradientColorData by their locations.
     *
     * @param o1 first GradientColorData to be compared
     * @param o2 second GradientColorData to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
     */
    @Override
    public int compare ( GradientColorData o1, GradientColorData o2 )
    {
        return Float.compare ( o1.getLocation (), o2.getLocation () );
    }
}