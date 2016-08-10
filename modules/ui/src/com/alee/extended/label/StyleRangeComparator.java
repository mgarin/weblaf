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

package com.alee.extended.label;

import java.util.Comparator;

/**
 * Custom StyleRange comparator.
 * Ranges with lower starting index are placed first.
 * If starting index for ranges is the same the longest one will be placed first.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 * @see StyleRange
 */

public class StyleRangeComparator implements Comparator<StyleRange>
{
    @Override
    public int compare ( final StyleRange r1, final StyleRange r2 )
    {
        final int s1 = r1.getStartIndex ();
        final int s2 = r2.getStartIndex ();
        if ( s1 != s2 )
        {
            return s1 < s2 ? -1 : 1;
        }
        else
        {
            final int l1 = r1.getLength ();
            final int l2 = r2.getLength ();
            return l1 > l2 ? -1 : l1 < l2 ? 1 : 0;
        }
    }
}