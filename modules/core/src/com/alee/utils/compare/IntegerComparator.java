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

package com.alee.utils.compare;

import java.util.Comparator;

/**
 * {@link Comparator} implementation for {@link Integer}s.
 *
 * @author Mikle Garin
 */
public final class IntegerComparator implements Comparator<Integer>
{
    /**
     * Static {@link IntegerComparator} instance.
     */
    private static IntegerComparator instance;

    /**
     * Returns static {@link IntegerComparator} instance.
     *
     * @return static {@link IntegerComparator} instance
     */
    public static IntegerComparator instance ()
    {
        if ( instance == null )
        {
            instance = new IntegerComparator ();
        }
        return instance;
    }

    @Override
    public int compare ( final Integer o1, final Integer o2 )
    {
        final int result;
        if ( o1 == null && o2 == null )
        {
            result = 0;
        }
        else if ( o2 == null )
        {
            result = 1;
        }
        else if ( o1 == null )
        {
            result = -1;
        }
        else
        {
            result = o1.compareTo ( o2 );
        }
        return result;
    }
}