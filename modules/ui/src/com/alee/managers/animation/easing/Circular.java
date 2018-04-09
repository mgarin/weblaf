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
 * Circular easing implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public abstract class Circular extends AbstractEasing
{
    @Override
    public String getTitle ()
    {
        return "Circular";
    }

    /**
     * Accelerating from zero velocity.
     */
    public static final class In extends Circular
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
            return -distance * ( Math.sqrt ( 1 - current * current ) - 1 ) + start;
        }
    }

    /**
     * Decelerating to zero velocity.
     */
    public static final class Out extends Circular
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
            current--;
            return distance * Math.sqrt ( 1 - current * current ) + start;
        }
    }

    /**
     * Accelerating until halfway, then decelerating.
     */
    public static final class InOut extends Circular
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
                return -distance / 2 * ( Math.sqrt ( 1 - current * current ) - 1 ) + start;
            }
            current -= 2;
            return distance / 2 * ( Math.sqrt ( 1 - current * current ) + 1 ) + start;
        }
    }
}