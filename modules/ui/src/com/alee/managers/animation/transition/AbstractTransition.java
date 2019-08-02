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

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.managers.animation.AnimationException;
import com.alee.managers.animation.AnimationManager;
import com.alee.managers.animation.event.EventHandler;
import com.alee.managers.animation.framerate.FixedFrameRate;
import com.alee.managers.animation.framerate.FrameRate;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Abstract {@link Transition} implementation providing basic settings for transition object.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public abstract class AbstractTransition<V> implements Transition<V>
{
    /**
     * Default frame rate.
     */
    protected static final FrameRate DEFAULT_FRAME_RATE = new FixedFrameRate ( 60 );

    /**
     * Current {@link TransitionState}.
     */
    protected TransitionState state;

    /**
     * Transition listeners.
     */
    protected final Set<TransitionListener<V>> listeners;

    /**
     * Frames which should be processed per second for this transition.
     */
    protected FrameRate frameRate;

    /**
     * Custom {@link com.alee.managers.animation.event.EventHandler} for transition events.
     */
    protected EventHandler eventHandler;

    /**
     * Whether or not should try to filter out unnecessary events.
     *
     * A pratical example: animation might generate a lot of frames each second and not all of those frames will recieve a distinct value
     * from the transition - that becomes more obvious with transitions using non-linear easing - in that case any further operations with
     * the frames could be trimmed on the transition level instead of sending events further on. That could help us, as an example, to
     * avoiding unnecessary UI repaints and value updates.
     *
     * Even though this is set to {@code true} by default you might need to change it to {@code false} at some point if this would consume
     * some of the animation frames you want to recieve events for.
     *
     * @see #fireAdjusted(Object)
     */
    protected boolean optimizeEvents;

    /**
     * Latest value that was available within this transition.
     * It is {@code null} in case transition have not started or have been reset.
     */
    protected V latest;

    /**
     * Constructs new transition.
     */
    public AbstractTransition ()
    {
        this ( DEFAULT_FRAME_RATE );
    }

    /**
     * Constructs new transition.
     *
     * @param frameRate frames which should be processed per second for this transition
     */
    public AbstractTransition ( final FrameRate frameRate )
    {
        super ();
        this.state = TransitionState.constructed;
        this.listeners = new ConcurrentSkipListSet<TransitionListener<V>> ( new TransitionListenerComparator<V> () );
        setFrameRate ( frameRate != null ? frameRate : DEFAULT_FRAME_RATE );
        setEventHandler ( null );
        setOptimizeEvents ( true );
    }

    @Override
    public FrameRate getFrameRate ()
    {
        return frameRate;
    }

    /**
     * Sets transition {@link FrameRate}.
     *
     * @param frameRate transition {@link FrameRate}
     */
    public void setFrameRate ( final FrameRate frameRate )
    {
        this.frameRate = frameRate;
    }

    /**
     * Returns {@link EventHandler} for {@link #listeners} events.
     *
     * @return {@link EventHandler} for {@link #listeners} events
     */
    public EventHandler getEventHandler ()
    {
        return eventHandler != null ? eventHandler : AnimationManager.getEventHandler ();
    }

    /**
     * Sets {@link EventHandler} for {@link #listeners} events.
     *
     * @param eventHandler new {@link EventHandler} for {@link #listeners} events
     */
    public void setEventHandler ( final EventHandler eventHandler )
    {
        this.eventHandler = eventHandler;
    }

    /**
     * Returns whether or not should try to filter out unnecessary events.
     *
     * @return {@code true} if should try to filter out unnecessary events, {@code false} otherwise
     */
    public boolean isOptimizeEvents ()
    {
        return optimizeEvents;
    }

    /**
     * Sets whether or not should try to filter out unnecessary events.
     *
     * @param optimizeEvents whether or not should try to filter out unnecessary events
     */
    public void setOptimizeEvents ( final boolean optimizeEvents )
    {
        this.optimizeEvents = optimizeEvents;
    }

    @Override
    public TransitionState getState ()
    {
        return state;
    }

    /**
     * Sets current {@link TransitionState}.
     *
     * @param state {@link TransitionState}
     */
    protected void setState ( final TransitionState state )
    {
        synchronized ( AbstractTransition.this )
        {
            if ( this.state != state )
            {
                // Ensure state change is valid
                if ( !TransitionState.isValid ( this.state, state ) )
                {
                    throw new AnimationException ( "Invalid state change performed: " + this.state + " -> " + state +
                            " for transition: " + AbstractTransition.this );
                }

                // Updating state
                final TransitionState previous = this.state;
                this.state = state;

                // Informing state listeners
                final V value = getValue ();
                fireStateChanged ( value, previous, state );

                // Informing other listeners
                if ( state == TransitionState.playing )
                {
                    fireStarted ( getStart () );
                }
                else if ( state == TransitionState.finished )
                {
                    fireFinished ( getGoal () );
                }
                else if ( state == TransitionState.aborted )
                {
                    fireAborted ( value );
                }
                else if ( state == TransitionState.ready &&
                        ( previous == TransitionState.finished || previous == TransitionState.aborted ) )
                {
                    fireReset ( value );
                }
            }
        }
    }

    /**
     * Returns latest value that was available.
     *
     * @return latest value that was available
     */
    public V getLatest ()
    {
        return latest;
    }

    @Override
    public int compareTo ( @NotNull final Object object )
    {
        return this == object ? 0 : -1;
    }

    @Override
    public void addListener ( final TransitionListener<V> listener )
    {
        listeners.add ( listener );
    }

    @Override
    public void removeListener ( final TransitionListener<V> listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Informs about transition being started.
     *
     * @param value initial transition value
     */
    public void fireStarted ( final V value )
    {
        latest = value;
        submit ( new Runnable ()
        {
            @Override
            public void run ()
            {
                for ( final TransitionListener<V> listener : listeners )
                {
                    listener.started ( AbstractTransition.this, value );
                }
            }
        } );
    }

    /**
     * Informs about value being adjusted by transition.
     *
     * @param value current transition value
     */
    public void fireAdjusted ( final V value )
    {
        // Filter out events for same values if event optimization is enabled
        if ( !isOptimizeEvents () || Objects.notEquals ( value, latest ) )
        {
            latest = value;
            submit ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    for ( final TransitionListener<V> listener : listeners )
                    {
                        listener.adjusted ( AbstractTransition.this, value );
                    }
                }
            } );
        }
    }

    /**
     * Informs about transition being finished.
     *
     * @param value goal transition value
     */
    public void fireFinished ( final V value )
    {
        latest = value;
        submit ( new Runnable ()
        {
            @Override
            public void run ()
            {
                for ( final TransitionListener<V> listener : listeners )
                {
                    listener.finished ( AbstractTransition.this, value );
                }
            }
        } );
    }

    /**
     * Informs about transition being aborted.
     *
     * @param value intermediate transition value
     */
    public void fireAborted ( final V value )
    {
        submit ( new Runnable ()
        {
            @Override
            public void run ()
            {
                for ( final TransitionListener<V> listener : listeners )
                {
                    listener.aborted ( AbstractTransition.this, value );
                }
            }
        } );
    }

    /**
     * Informs about transition being reset.
     *
     * @param value initial transition value
     */
    public void fireReset ( final V value )
    {
        latest = null;
        submit ( new Runnable ()
        {
            @Override
            public void run ()
            {
                for ( final TransitionListener<V> listener : listeners )
                {
                    listener.reset ( AbstractTransition.this, value );
                }
            }
        } );
    }

    /**
     * Informs about transition state change.
     *
     * @param value    intermediate transition value
     * @param previous previous {@link TransitionState}
     * @param current  current {@link TransitionState}
     */
    public void fireStateChanged ( final V value, final TransitionState previous, final TransitionState current )
    {
        submit ( new Runnable ()
        {
            @Override
            public void run ()
            {
                for ( final TransitionListener<V> listener : listeners )
                {
                    listener.stateChanged ( AbstractTransition.this, value, previous, current );
                }
            }
        } );
    }

    /**
     * Submit transition event to a separate executor service.
     *
     * @param event transition event
     */
    protected void submit ( final Runnable event )
    {
        getEventHandler ().handle ( event );
    }

    /**
     * Asks {@link AnimationManager} to play this transition on preferred {@link com.alee.managers.animation.pipeline.AnimationPipeline}.
     * This is a simple bridge method added for convenient transition and {@link AnimationManager} usage.
     */
    public final void play ()
    {
        AnimationManager.play ( this );
    }

    /**
     * Asks {@link AnimationManager} to stop playing this transition.
     * This is a simple bridge method added for convenient transition and {@link AnimationManager} usage.
     */
    public final void stop ()
    {
        AnimationManager.stop ( this );
    }
}