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
 * Quadratic easing implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public abstract class Quadratic extends AbstractEasing
{
    @Override
    public String getTitle ()
    {
        return "Quadratic";
    }

    /**
     * Accelerating from zero velocity.
     */
    public static final class In extends Quadratic
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " In";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, double current, final double total )
        {
            current /= total;
            return distance * current * current + start;
        }
    }

    /**
     * Decelerating to zero velocity.
     */
    public static final class Out extends Quadratic
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " Out";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, double current, final double total )
        {
            current /= total;
            return -distance * current * ( current - 2 ) + start;
        }
    }

    /**
     * Accelerating until halfway, then decelerating.
     */
    public static final class InOut extends Quadratic
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " InOut";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, double current, final double total )
        {
            current /= total / 2;
            if ( current < 1 )
            {
                return distance / 2 * current * current + start;
            }
            current--;
            return -distance / 2 * ( current * ( current - 2 ) - 1 ) + start;
        }
    }
}