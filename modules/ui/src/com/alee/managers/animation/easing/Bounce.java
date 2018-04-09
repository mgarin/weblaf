/*
 * Copyright © 2008 George McGinley Smith
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the author nor the names of contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.alee.managers.animation.easing;

/**
 * Bounce easing implementation.
 *
 * @author George McGinley Smith
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 * @see <a href="http://gsgd.co.uk/sandbox/jquery/easing/">jQuery Easing Plugin</a>
 */
public abstract class Bounce extends AbstractEasing
{
    @Override
    public String getTitle ()
    {
        return "Bounce";
    }

    /**
     * Bounce at the start.
     *
     * @param start    starting value for the transition
     * @param distance distance that should be covered to reach the end of the transition
     * @param current  current transition progress
     * @param total    total progress required to finish transition
     * @return current value
     */
    protected final double in ( final double start, final double distance, final double current, final double total )
    {
        return distance - out ( 0, distance, total - current, total ) + start;
    }

    /**
     * Bounce at the end.
     *
     * @param start    starting value for the transition
     * @param distance distance that should be covered to reach the end of the transition
     * @param current  current transition progress
     * @param total    total progress required to finish transition
     * @return current value
     */
    protected final double out ( final double start, final double distance, double current, final double total )
    {
        if ( ( current /= total ) < 1 / 2.75 )
        {
            return distance * ( 7.5625 * current * current ) + start;
        }
        else if ( current < 2 / 2.75 )
        {
            return distance * ( 7.5625 * ( current -= 1.5 / 2.75 ) * current + .75 ) + start;
        }
        else if ( current < 2.5 / 2.75 )
        {
            return distance * ( 7.5625 * ( current -= 2.25 / 2.75 ) * current + .9375 ) + start;
        }
        else
        {
            return distance * ( 7.5625 * ( current -= 2.625 / 2.75 ) * current + .984375 ) + start;
        }
    }

    /**
     * Accelerating from zero velocity.
     */
    public static final class In extends Bounce
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " In";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, final double current, final double total )
        {
            return in ( start, distance, current, total );
        }
    }

    /**
     * Decelerating to zero velocity.
     */
    public static final class Out extends Bounce
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " Out";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, final double current, final double total )
        {
            return out ( start, distance, current, total );
        }
    }

    /**
     * Accelerating until halfway, then decelerating.
     */
    public static final class InOut extends Bounce
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " InOut";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, final double current, final double total )
        {
            if ( current < total / 2 )
            {
                return in ( 0, distance, current * 2, total ) * 0.5 + start;
            }
            else
            {
                return out ( 0, distance, current * 2 - total, total ) * 0.5 + distance * 0.5 + start;
            }
        }
    }
}