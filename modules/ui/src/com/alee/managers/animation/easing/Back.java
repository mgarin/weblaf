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
public abstract class Back extends AbstractEasing
{
    @Override
    public String getTitle ()
    {
        return "Back";
    }

    /**
     * Accelerating from zero velocity.
     */
    public static final class In extends Back
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " In";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, double current, final double total )
        {
            final double mod = 1.70158;
            return distance * ( current /= total ) * current * ( ( mod + 1 ) * current - mod ) + start;
        }
    }

    /**
     * Decelerating to zero velocity.
     */
    public static final class Out extends Back
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " Out";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, double current, final double total )
        {
            final double mod = 1.70158;
            return distance * ( ( current = current / total - 1 ) * current * ( ( mod + 1 ) * current + mod ) + 1 ) + start;
        }
    }

    /**
     * Accelerating until halfway, then decelerating.
     */
    public static final class InOut extends Back
    {
        @Override
        public String getTitle ()
        {
            return super.getTitle () + " InOut";
        }

        @Override
        protected double calculateImpl ( final double start, final double distance, double current, final double total )
        {
            double mod = 1.70158;
            if ( ( current /= total / 2 ) < 1 )
            {
                return distance / 2 * ( current * current * ( ( ( mod *= 1.525 ) + 1 ) * current - mod ) ) + start;
            }
            return distance / 2 * ( ( current -= 2 ) * current * ( ( ( mod *= 1.525 ) + 1 ) * current + mod ) + 2 ) + start;
        }
    }
}