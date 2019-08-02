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

package com.alee.managers.animation.transition;

import com.alee.managers.animation.easing.Linear;
import com.alee.managers.animation.framerate.FixedFrameRate;
import com.alee.utils.TimeUtils;

/**
 * {@link Transition} implementation providing simple idle transition (aka delay).
 * Single value must still be provided for the case when someone would request start/current/goal value from this transition.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public class IdleTransition<V> extends TimedTransition<V>
{
    /**
     * todo 1. Make separate from TimedTransition?
     */

    /**
     * Constructs new idle transition.
     *
     * @param value    idle value returned as start/current/goal value at any time
     * @param duration idle duration (aka delay) in milliseconds
     */
    public IdleTransition ( final V value, final Long duration )
    {
        super ( value, value, new FixedFrameRate ( ( double ) TimeUtils.msInSecond / duration ), new Linear (), duration );
    }

    @Override
    public V getValue ()
    {
        return getStart ();
    }
}