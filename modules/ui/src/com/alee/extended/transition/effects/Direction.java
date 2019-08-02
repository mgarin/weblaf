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

package com.alee.extended.transition.effects;

/**
 * @author Mikle Garin
 */
public enum Direction
{
    // Any random direction each time (always first in enum)
    random,

    // Any random horizontal or vertical direction
    horizontal,
    vertical,

    // Specific directions
    up,
    down,
    left,
    right;

    public boolean isHorizontal ()
    {
        return Direction.this.equals ( horizontal ) || Direction.this.equals ( left ) || Direction.this.equals ( right );
    }

    public boolean isVertical ()
    {
        return Direction.this.equals ( vertical ) || Direction.this.equals ( up ) || Direction.this.equals ( down );
    }
}