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
import com.alee.managers.animation.framerate.FrameRate;
import com.alee.utils.CollectionUtils;

import java.util.*;

/**
 * {@link Transition} implementation that allows convenient grouping of consecutive transitions.
 * Grouped transitions have a strict order and could be looped indefinitely until this transition is aborted.
 * To add delays {@link IdleTransition}
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public class QueueTransition<V> extends AbstractTransition<V>
{
    /**
     * Whether or not transitions should be repeated in loop until aborted.
     */
    protected boolean looped;

    /**
     * Queue transitions.
     */
    protected final List<Transition<V>> elements;

    /**
     * Separate lock for transition elements list.
     */
    protected final Object elementsLock;

    /**
     * Transitions queue.
     */
    protected Queue<Transition<V>> queue;

    /**
     * Constructs new queue transition.
     *
     * @param transitions transitions to queue
     */
    public QueueTransition ( final Transition<V>... transitions )
    {
        this ( false, transitions );
    }

    /**
     * Constructs new queue transition.
     *
     * @param transitions transitions to queue
     */
    public QueueTransition ( final List<Transition<V>> transitions )
    {
        this ( false, transitions );
    }

    /**
     * Constructs new queue transition.
     *
     * @param looped      whether or not transitions should be repeated in loop until aborted
     * @param transitions transitions to queue
     */
    public QueueTransition ( final boolean looped, final Transition<V>... transitions )
    {
        this ( looped, CollectionUtils.asList ( transitions ) );
    }

    /**
     * Constructs new queue transition.
     *
     * @param looped      whether or not transitions should be repeated in loop until aborted
     * @param transitions transitions to queue
     */
    public QueueTransition ( final boolean looped, final List<Transition<V>> transitions )
    {
        super ();
        this.looped = looped;
        this.elements = new ArrayList<Transition<V>> ( transitions.size () );
        this.elementsLock = new Object ();
        add ( transitions );
    }

    @Override
    public void setFrameRate ( final FrameRate frameRate )
    {
        // Ensure that proper frame rate is set
        super.setFrameRate ( new QueueFrameRate () );
    }

    /**
     * Returns whether or not transitions should be repeated in loop until aborted.
     *
     * @return {@code true} if transitions should be repeated in loop until aborted, {@code false} otherwise
     */
    public boolean isLooped ()
    {
        return looped;
    }

    /**
     * Sets whether or not transitions should be repeated in loop until aborted.
     *
     * @param looped whether or not transitions should be repeated in loop until aborted
     */
    public void setLooped ( final boolean looped )
    {
        this.looped = looped;
    }

    /**
     * Adds all specified transitions into queue.
     *
     * @param transitions transitions to add
     */
    public void add ( final Transition<V>... transitions )
    {
        synchronized ( elementsLock )
        {
            Collections.addAll ( elements, transitions );
        }
    }

    /**
     * Adds all specified transitions into queue.
     *
     * @param transitions transitions to add
     */
    public void add ( final List<Transition<V>> transitions )
    {
        synchronized ( elementsLock )
        {
            elements.addAll ( transitions );
        }
    }

    @Override
    public FrameRate getFrameRate ()
    {
        return frameRate;
    }

    @Override
    public long getStartTime ()
    {
        synchronized ( QueueTransition.this )
        {
            switch ( getState () )
            {
                case playing:
                    return queue.element ().getStartTime ();

                case finished:
                    return getLastTransition ().getStartTime ();

                case aborted:
                    // todo Should return last transition used before got aborted

                case ready:
                default:
                    return getFirstTransition ().getStartTime ();
            }
        }
    }

    @Override
    public V getStart ()
    {
        return getFirstTransition ().getStart ();
    }

    @Override
    public V getGoal ()
    {
        return getLastTransition ().getGoal ();
    }

    @Override
    public long start ( final long currentFrame )
    {
        synchronized ( QueueTransition.this )
        {
            // Resetting transition first
            reset ();

            // Starting first queue step
            final long untilNextFrame = queue.element ().start ( currentFrame );

            // Updating state
            setState ( TransitionState.playing );

            // Time until next frame in nanoseconds
            return untilNextFrame;
        }
    }

    @Override
    public long proceed ( final long previousFrame, final long currentFrame )
    {
        synchronized ( QueueTransition.this )
        {
            if ( getState () == TransitionState.playing )
            {
                // Retrieving current queue element
                final Transition<V> current = queue.element ();

                // Performing current transition step
                final long untilNextFrame = current.proceed ( previousFrame, currentFrame );

                // Informing about adjusted value
                fireAdjusted ( getValue () );

                // Transition has finished or was aborted
                if ( untilNextFrame <= 0 )
                {
                    // Ensure transition has finished or was aborted
                    final long adjustedFrame = currentFrame + untilNextFrame;
                    if ( current.getState () == TransitionState.playing )
                    {
                        // Process additional transition frames
                        return proceed ( adjustedFrame, currentFrame );
                    }
                    else
                    {
                        // Removing finished transition step
                        final Transition<V> done = queue.remove ();

                        // Adding step to end if looped
                        if ( isLooped () )
                        {
                            queue.add ( done );
                        }

                        // Checking next step availability
                        if ( !queue.isEmpty () )
                        {
                            // Starting next transition step
                            final long untilStartedNextFrame = queue.element ().start ( adjustedFrame );

                            // Updating value according to new transition
                            fireAdjusted ( getValue () );

                            // Check whether or not we need to process additional transition frames
                            if ( adjustedFrame + untilStartedNextFrame < currentFrame )
                            {
                                // Process additional transition frames
                                return proceed ( adjustedFrame, currentFrame );
                            }
                            else
                            {
                                // Time until next frame in nanoseconds
                                return untilStartedNextFrame;
                            }
                        }
                        else
                        {
                            // Emptying queue
                            queue = null;

                            // Updating state
                            setState ( TransitionState.finished );

                            // Transition queue has finished
                            return untilNextFrame;
                        }
                    }
                }
                else
                {
                    // Time until next frame in nanoseconds
                    return untilNextFrame;
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
        synchronized ( QueueTransition.this )
        {
            if ( getState () == TransitionState.playing )
            {
                // Aborting current transition
                queue.element ().abort ();

                // Emptying queue
                queue = null;

                // Updating state
                setState ( TransitionState.aborted );
            }
        }
    }

    @Override
    public void reset ()
    {
        synchronized ( QueueTransition.this )
        {
            if ( getState () != TransitionState.ready )
            {
                // Ensures that transition is properly configured
                checkConfiguration ();

                // Copying queue elements
                // This is made to avoid modifying initial list and allow resetting it on restart
                queue = new ArrayDeque<Transition<V>> ( elements );

                // Updating state
                setState ( TransitionState.ready );
            }
        }
    }

    @Override
    public V getValue ()
    {
        synchronized ( QueueTransition.this )
        {
            switch ( getState () )
            {
                case playing:
                    return queue.element ().getValue ();

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

    @Override
    public int compareTo ( final Object object )
    {
        return this == object ? 0 : -1;
    }

    /**
     * Specific {@link FrameRate} implementation for {@link QueueTransition}.
     */
    protected class QueueFrameRate implements FrameRate
    {
        @Override
        public double value ()
        {
            synchronized ( QueueTransition.this )
            {
                synchronized ( elementsLock )
                {
                    switch ( getState () )
                    {
                        case playing:
                            return queue.element ().getFrameRate ().value ();

                        case finished:
                        case aborted:
                            return elements.get ( elements.size () - 1 ).getFrameRate ().value ();

                        case ready:
                        default:
                            return elements.get ( 0 ).getFrameRate ().value ();
                    }
                }
            }
        }

        @Override
        public boolean isFixed ()
        {
            synchronized ( elementsLock )
            {
                double frameRate = -1;
                for ( final Transition<V> element : elements )
                {
                    if ( !element.getFrameRate ().isFixed () )
                    {
                        return false;
                    }
                    else if ( frameRate == -1 )
                    {
                        frameRate = element.getFrameRate ().value ();
                    }
                    else if ( frameRate != element.getFrameRate ().value () )
                    {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    /**
     * Returns first available transition.
     *
     * @return first available transition
     */
    protected Transition<V> getFirstTransition ()
    {
        synchronized ( elementsLock )
        {
            // Ensures that transition is properly configured
            checkConfiguration ();

            // Return first available transition
            return elements.get ( 0 );
        }
    }

    /**
     * Returns last available transition.
     *
     * @return last available transition
     */
    protected Transition<V> getLastTransition ()
    {
        synchronized ( elementsLock )
        {
            // Ensures that transition is properly configured
            checkConfiguration ();

            // Return last available transition
            return elements.get ( elements.size () - 1 );
        }
    }

    /**
     * Ensures that transition is properly configured.
     */
    protected void checkConfiguration ()
    {
        synchronized ( elementsLock )
        {
            if ( CollectionUtils.isEmpty ( elements ) )
            {
                throw new AnimationException ( "QueueTransition must contain at least one Transition" );
            }
        }
    }
}