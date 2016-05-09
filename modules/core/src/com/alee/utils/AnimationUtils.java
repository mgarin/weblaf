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

package com.alee.utils;

/**
 * This class provides a set of utilities to simplify UI animation code.
 *
 * @author Mikle Garin
 */

public final class AnimationUtils
{
    /**
     * Returns move animation speed depending on the animated object location.
     * Speed provided by this method will ensure smooth movement animation with spring effect.
     * <p>
     * Specified location must be a floating point number in the inclusive range [0.0, 1.0].
     * 0.0 location = object is at the start of its path.
     * 1.0 location = object has reached the end of its path.
     *
     * @param location object location on its path
     * @param minSpeed minimum desired object speed, used when object is close to path start and end
     * @param maxSpeed maximum desired object speed, used when object is in the middle of its path
     * @return move animation speed
     */
    public static float calculateSpeed ( final float location, final float minSpeed, final float maxSpeed )
    {
        return Math.max ( minSpeed, maxSpeed * ( 1f - MathUtils.sqr ( ( location - 0.5f ) * 2 ) ) );
    }
}