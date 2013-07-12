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

package com.alee.extended.transition.effects.curtain;

/**
 * User: mgarin Date: 20.11.12 Time: 17:34
 * <p/>
 * Slide curtain blocks to specific side relative to curtain appearance direction
 */

public enum CurtainSlideDirection
{
    // Random each time (always first in enum)
    random,

    // Left side relative to direction
    left,

    // Right side relative to direction
    right,

    // Both sides relative to direction
    both
}
