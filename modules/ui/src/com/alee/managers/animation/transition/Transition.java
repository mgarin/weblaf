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

import com.alee.managers.animation.framerate.FrameRate;

/**
 * Base interface for any transition between two value states.
 * {@link com.alee.managers.animation.types.TransitionType} implementations define how exactly value state is being calculated.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface Transition<V> extends Comparable
{
    /**
     * Returns transition {@link FrameRate}.
     *
     * @return transition {@link FrameRate}
     */
    public FrameRate getFrameRate ();

    /**
     * Returns transition (or transition part related to frame rate) start time in nanoseconds.
     * It should be the actual time of transition (or transition part related to frame rate) start taken from {@link System#nanoTime()}.
     *
     * @return transition (or transition part related to frame rate) start time in nanoseconds
     */
    public long getStartTime ();

    /**
     * Returns initial transition value.
     *
     * @return initial transition value
     */
    public V getStart ();

    /**
     * Returns goal transition value.
     *
     * @return goal transition value
     */
    public V getGoal ();

    /**
     * Starts transition and changes state to {@link TransitionState#playing}.
     * If transition is not {@link TransitionState#ready} it should be {@link #reset()} first.
     * Returns time in nanoseconds left until next transition frame since the {@code currentFrame}, it should always be greater than zero.
     *
     * @param currentFrame current frame time in nanoseconds
     * @return time in nanoseconds left until next transition frame since the {@code currentFrame}, it should always be greater than zero
     */
    public long start ( long currentFrame );

    /**
     * Proceeds to next transition step, closer to the final value state.
     * Returns time in nanoseconds left until next transition frame since the {@code currentFrame}.
     * When goal value is reached it changes state to {@link TransitionState#finished} and returns zero or negative time.
     *
     * @param previousFrame previous frame time in nanoseconds
     * @param currentFrame  current frame time in nanoseconds
     * @return time in nanoseconds left until next transition frame since the {@code currentFrame}, zero or negative time if none left
     */
    public long proceed ( long previousFrame, long currentFrame );

    /**
     * Aborts transition.
     * This should also change state to {@link TransitionState#aborted}.
     */
    public void abort ();

    /**
     * Resets transition to initial values.
     * This should also change state to {@link TransitionState#ready}.
     */
    public void reset ();

    /**
     * Returns current value.
     *
     * @return current value
     */
    public V getValue ();

    /**
     * Returns current {@link TransitionState}.
     *
     * @return current {@link TransitionState}
     */
    public TransitionState getState ();

    /**
     * Adds transition listener.
     *
     * @param listener transition listener to add
     */
    public void addListener ( TransitionListener<V> listener );

    /**
     * Removes transition listener.
     *
     * @param listener transition listener to remove
     */
    public void removeListener ( TransitionListener<V> listener );
}