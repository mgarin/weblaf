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
 * {@link Transition} events listener.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface TransitionListener<V>
{
    /**
     * Informs about transition being started.
     * Provides initial transition value that is being adjusted to reach goal value.
     *
     * @param transition event source transition
     * @param value      initial transition value
     */
    public void started ( Transition transition, V value );

    /**
     * Informs about value being adjusted by transition.
     * Provides value adjusted by the transition on the last state change.
     *
     * @param transition event source transition
     * @param value      current transitionvalue
     */
    public void adjusted ( Transition transition, V value );

    /**
     * Informs about transition being finished.
     * Provides the goal value that has been reached upon transition completion.
     *
     * @param transition event source transition
     * @param value      goal transition value
     */
    public void finished ( Transition transition, V value );

    /**
     * Informs about transition being aborted.
     * Provides intermediate value that was the last value reached within the transition.
     *
     * @param transition event source transition
     * @param value      intermediate transition value
     */
    public void aborted ( Transition transition, V value );

    /**
     * Informs about transition being reset.
     * Provides initial transition value since it was reset.
     *
     * @param transition event source transition
     * @param value      initial transition value
     */
    public void reset ( Transition transition, V value );

    /**
     * Informs about transition state change.
     *
     * @param transition event source transition
     * @param value      current transition value
     * @param previous   previous {@link TransitionState}
     * @param current    current {@link TransitionState}
     */
    public void stateChanged ( Transition transition, V value, TransitionState previous, TransitionState current );
}