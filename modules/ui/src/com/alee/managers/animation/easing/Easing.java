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

package com.alee.managers.animation.easing;

/**
 * Base interface for all kinds of transition easing.
 * For calculation convenience all values have `double` type.
 *
 * Here is an example the value combination options:
 * int `start` - X coordinate we want to start visual transition from
 * int `distance` - length we want to cover using our transition
 * long `current` - time passed since the transition has started
 * long `total` - time gate we want transition to be finished within
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface Easing
{
    /**
     * Returns easing implementation title.
     *
     * @return easing implementation title
     */
    public String getTitle ();

    /**
     * Returns value eased according to implementation algorithm, usually between {@code start} and {@code start + distance}.
     * To understand how provided values are used you can look at the most simple implementation - {@link Linear} easing.
     *
     * @param start    starting value for the transition
     * @param distance distance that should be covered to reach the end of the transition
     * @param current  current transition progress, should always be in [0..total] range
     * @param total    total progress required to finish transition
     * @return value eased according to implementation algorithm, usually between {@code start} and {@code start + distance}.
     */
    public double calculate ( double start, double distance, double current, double total );
}