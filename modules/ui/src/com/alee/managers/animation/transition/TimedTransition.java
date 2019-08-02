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

import com.alee.managers.animation.AnimationException;
import com.alee.managers.animation.AnimationManager;
import com.alee.managers.animation.easing.Cubic;
import com.alee.managers.animation.easing.Easing;
import com.alee.managers.animation.framerate.FrameRate;
import com.alee.managers.animation.types.TransitionType;
import com.alee.utils.TimeUtils;

/**
 * {@link Transition} implementation providing time-based transition.
 * This is the most basic transition that can be used for almost any type of transition between two value states.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public class TimedTransition<V> extends AbstractTransition<V>
{
    /**
     * Default easing algorithm.
     */
    protected static final Easing DEFAULT_EASING = new Cubic.InOut ();

    /**
     * Default transition duration in milliseconds.
     */
    protected static final long DEFAULT_DURATION = 360L;

    /**
     * Type transition for the value.
     */
    protected final TransitionType<V> transitionType;

    /**
     * Initial value.
     */
    protected final V start;

    /**
     * Goal value.
     */
    protected final V goal;

    /**
     * Used easing algorithm.
     */
    protected final Easing easing;

    /**
     * Transition duration in milliseconds.
     */
    protected final long duration;

    /**
     * Transition start time in nanoseconds.
     */
    protected Long startTime;

    /**
     * Constructs new timed transition.
     *
     * @param start initial value
     * @param goal  goal value
     */
    public TimedTransition ( final V start, final V goal )
    {
        this ( start, goal, DEFAULT_FRAME_RATE, DEFAULT_EASING, DEFAULT_DURATION );
    }

    /**
     * Constructs new timed transition.
     *
     * @param start     initial value
     * @param goal      goal value
     * @param frameRate frames which should be processed per second for this transition
     */
    public TimedTransition ( final V start, final V goal, final FrameRate frameRate )
    {
        this ( start, goal, frameRate, DEFAULT_EASING, DEFAULT_DURATION );
    }

    /**
     * Constructs new timed transition.
     *
     * @param start  initial value
     * @param goal   goal value
     * @param easing used easing algorithm
     */
    public TimedTransition ( final V start, final V goal, final Easing easing )
    {
        this ( start, goal, DEFAULT_FRAME_RATE, easing, DEFAULT_DURATION );
    }

    /**
     * Constructs new timed transition.
     *
     * @param start    initial value
     * @param goal     goal value
     * @param duration transition duration in milliseconds
     */
    public TimedTransition ( final V start, final V goal, final Long duration )
    {
        this ( start, goal, DEFAULT_FRAME_RATE, DEFAULT_EASING, duration );
    }

    /**
     * Constructs new timed transition.
     *
     * @param start    initial value
     * @param goal     goal value
     * @param easing   used easing algorithm
     * @param duration transition duration in milliseconds
     */
    public TimedTransition ( final V start, final V goal, final Easing easing, final Long duration )
    {
        this ( start, goal, DEFAULT_FRAME_RATE, easing, duration );
    }

    /**
     * Constructs new timed transition.
     *
     * @param start     initial value
     * @param goal      goal value
     * @param frameRate frames which should be processed per second for this transition
     * @param easing    used easing algorithm
     * @param duration  transition duration in milliseconds
     */
    public TimedTransition ( final V start, final V goal, final FrameRate frameRate, final Easing easing, final Long duration )
    {
        super ( frameRate );
        if ( start == null )
        {
            throw new AnimationException ( "Transition start value cannot be null" );
        }
        if ( goal == null )
        {
            throw new AnimationException ( "Transition goal value cannot be null" );
        }
        this.transitionType = AnimationManager.getTransitionType ( ( Class<V> ) start.getClass () );
        this.start = start;
        this.goal = goal;
        this.easing = easing != null ? easing : DEFAULT_EASING;
        this.duration = duration != null ? duration : DEFAULT_DURATION;
    }

    @Override
    public long getStartTime ()
    {
        return startTime;
    }

    @Override
    public V getStart ()
    {
        return start;
    }

    @Override
    public V getGoal ()
    {
        return goal;
    }

    @Override
    public long start ( final long currentFrame )
    {
        synchronized ( TimedTransition.this )
        {
            // Resetting transition first
            reset ();

            // Resetting start time
            startTime = currentFrame;

            // Updating state
            setState ( TransitionState.playing );

            // Time until next frame in nanoseconds
            return Math.min ( getFrameDelay (), getDuration () );
        }
    }

    @Override
    public long proceed ( final long previousFrame, final long currentFrame )
    {
        synchronized ( TimedTransition.this )
        {
            if ( getState () == TransitionState.playing )
            {
                // Recalculating start and delay, it might have changed since we made a frame
                final long transitionStart = getStartTime ();
                final long frameDelay = getFrameDelay ();

                // Calculating frame time
                final long frame;
                if ( transitionStart < previousFrame )
                {
                    // Previous frame was after transition start
                    final long passedSincePrevStart = previousFrame - transitionStart;
                    if ( passedSincePrevStart >= frameDelay )
                    {
                        frame = previousFrame + frameDelay - Math.round ( passedSincePrevStart % frameDelay );
                    }
                    else
                    {
                        frame = previousFrame + frameDelay - passedSincePrevStart;
                    }
                }
                else
                {
                    // Previous frame was before transition start
                    frame = transitionStart + frameDelay;
                }

                // Performing frame if it happened between previous and current frame times
                if ( frame <= currentFrame )
                {
                    // Informing about value change on this frame
                    fireAdjusted ( getValue () );

                    // Returning time left until next frame
                    final long duration = getDuration ();
                    final long endFrame = transitionStart + duration;
                    if ( endFrame < currentFrame )
                    {
                        // Transition has finished
                        setState ( TransitionState.finished );

                        // All frames have been done, returning zero or negative time
                        return endFrame - currentFrame;
                    }
                    else
                    {
                        // There are frames left, returning time until closest one
                        final long nextFrame = frame + frameDelay;
                        if ( endFrame < nextFrame )
                        {
                            // Returning delay between current frame and final frame
                            return endFrame - currentFrame;
                        }
                        else if ( nextFrame < currentFrame )
                        {
                            // Multiple frames occured between pipeline frames
                            // We have already fired the frame closest to the previousFrame time, proceeding to next
                            return proceed ( frame, currentFrame );
                        }
                        else
                        {
                            // Returning delay between current frame and next frame
                            return nextFrame - currentFrame;
                        }
                    }
                }
                else
                {
                    // Frame will occur later
                    return frame - currentFrame;
                }
            }
            else
            {
                // Transition is not playing
                return 0;
            }
        }
    }

    @Override
    public void abort ()
    {
        synchronized ( TimedTransition.this )
        {
            if ( getState () == TransitionState.playing )
            {
                // Updating state
                setState ( TransitionState.aborted );
            }
        }
    }

    @Override
    public void reset ()
    {
        synchronized ( TimedTransition.this )
        {
            if ( getState () != TransitionState.ready )
            {
                // Updating state
                setState ( TransitionState.ready );
            }
        }
    }

    @Override
    public V getValue ()
    {
        synchronized ( TimedTransition.this )
        {
            switch ( getState () )
            {
                case playing:
                    return transitionType.value ( easing, start, goal, getProgress (), getTotal () );

                case finished:
                    return getGoal ();

                case aborted:
                    return getLatest ();

                case ready:
                default:
                    return getStart ();
            }
        }
    }

    /**
     * Returns current progress towards final value state.
     * It is always a double value within [0..1] range.
     *
     * @return current progress towards final value state
     */
    protected double getProgress ()
    {
        final long duration = getDuration ();
        final long elapsedTime = getElapsedTime ();
        return duration > elapsedTime ? ( double ) elapsedTime / duration : getTotal ();
    }

    /**
     * Returns maximum transition progress.
     * It is always exactly {@code 1d} as this transition progresses from {@code 0d} to {@code 1d}.
     *
     * @return maximum transition progress
     */
    protected double getTotal ()
    {
        return 1d;
    }

    /**
     * Returns current time in nanoseconds.
     *
     * @return current time in nanoseconds
     */
    protected long getTime ()
    {
        return System.nanoTime ();
    }

    /**
     * Returns transition duration in nanoseconds.
     *
     * @return transition duration in nanoseconds
     */
    protected long getDuration ()
    {
        return duration * TimeUtils.nsInMillisecond;
    }

    /**
     * Returns delay between frames in nanoseconds.
     *
     * @return delay between frames in nanoseconds
     */
    protected long getFrameDelay ()
    {
        return Math.round ( TimeUtils.nsInSecond / getFrameRate ().value () );
    }

    /**
     * Returns time elapsed since transition start in nanoseconds.
     *
     * @return time elapsed since transition start in nanoseconds
     */
    protected long getElapsedTime ()
    {
        return getTime () - getStartTime ();
    }
}