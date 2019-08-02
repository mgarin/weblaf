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
 * {@link Transition} possible states.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public enum TransitionState
{
    /**
     * Transition has been constucted.
     * You should never receive events about state being changed to this one as it is only set on construction.
     * It simply indicates that transition haven't been performed even once yet and it should be prepared first.
     */
    constructed,

    /**
     * Transition is ready to play.
     * It's value is set to starting one.
     */
    ready,

    /**
     * Transition is playing.
     * It's value is constantly being updated.
     */
    playing,

    /**
     * Transition has finished.
     * It's value is set to goal one.
     */
    finished,

    /**
     * Transition was aborted.
     * It's value is set to the lastest available one.
     */
    aborted;

    /**
     * Returns whether or not specified state change is valid.
     *
     * @param previous previous {@link TransitionState}
     * @param next     next {@link TransitionState}
     * @return {@code true} if specified state change is valid, {@code false} otherwise
     */
    public static boolean isValid ( final TransitionState previous, final TransitionState next )
    {
        return previous == constructed && next == ready ||
                previous == ready && next == playing ||
                previous == playing && ( next == finished || next == aborted ) ||
                ( previous == finished || previous == aborted ) && next == ready;
    }
}