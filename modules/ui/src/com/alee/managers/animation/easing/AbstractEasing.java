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
 * Abstract {@link Easing} implementation providing borderline values check.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public abstract class AbstractEasing implements Easing
{
    @Override
    public double calculate ( final double start, final double distance, final double current, final double total )
    {
        if ( current <= 0 )
        {
            return start;
        }
        else if ( current >= total )
        {
            return start + distance;
        }
        else
        {
            return calculateImpl ( start, distance, current, total );
        }
    }

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
    protected abstract double calculateImpl ( double start, double distance, double current, double total );

    @Override
    public String toString ()
    {
        return getTitle ();
    }
}