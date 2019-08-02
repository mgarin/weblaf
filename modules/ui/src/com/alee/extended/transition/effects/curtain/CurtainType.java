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
 * @author Mikle Garin
 */
public enum CurtainType
{
    /**
     * Any random curtain type each time (always first in enum).
     */
    random,

    /**
     * Fade-in curtain blocks in a specific direction.
     */
    fade,

    /**
     * Fill (and fade-in) curtain blocks in a specific direction.
     */
    fill,

    /**
     * Slide (and fade-in) curtain blocks from a specific side in a specific direction.
     */
    slide
}
