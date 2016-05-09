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
 * Compares style ranges by their positions.
 *
 * @author Mikle Garin
 * @see com.alee.extended.label.StyleRange
 */

public class StyleRangeComparator implements Comparator<StyleRange>
{
    @Override
    public int compare ( final StyleRange r1, final StyleRange r2 )
    {
        return r1.getStartIndex () < r2.getStartIndex () ? -1 : r1.getStartIndex () > r2.getStartIndex () ? 1 : 0;
    }
}