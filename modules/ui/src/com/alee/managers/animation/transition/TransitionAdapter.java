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

/**
 * Adapter for {@link TransitionListener}.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public abstract class TransitionAdapter<V> implements TransitionListener<V>
{
    @Override
    public void started ( final Transition transition, final V value )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void adjusted ( final Transition transition, final V value )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void finished ( final Transition transition, final V value )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void aborted ( final Transition transition, final V value )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void reset ( final Transition transition, final V value )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void stateChanged ( final Transition transition, final V value, final TransitionState previous, final TransitionState current )
    {
        /**
         * Do nothing by default.
         */
    }
}