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

package com.alee.managers.animation.pipeline;

import com.alee.managers.animation.transition.Transition;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TimeUtils;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * {@link com.alee.managers.animation.pipeline.AnimationPipeline} implementation that performs animation frames at irregular time intervals
 * based on the delays returned by transitions.
 *
 * A few important notes:
 * 1. Single instance of this pipeline can process any amount of transitions with different frame rate starting at different times
 * 2. This pipeline does not guarantee that each transition frame will be executed exactly in time, but it tries to do so
 * 3. This pipeline do guarantee that each transition frame will be executed sooner or later depending on queue load and other factors
 * 4. Nanoseconds are used as unit of measurement as a more precise way to determine frames position on the timeline
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class TimedAnimationPipeline extends AbstractAnimationPipeline implements Runnable
{
    /**
     * Currently active transition.
     */
    private final Set<Transition> transitions;

    /**
     * Active transitions animator.
     */
    private final Thread animator;

    /**
     * Constructs new {@link TimedAnimationPipeline}.
     */
    public TimedAnimationPipeline ()
    {
        super ();

        // Concurrent transitions set
        transitions = new ConcurrentSkipListSet<Transition> ();

        // Active transitions animator
        animator = new Thread ( TimedAnimationPipeline.this );
        animator.setName ( ReflectUtils.getClassName ( TimedAnimationPipeline.this.getClass () ) );
        animator.setDaemon ( true );
        animator.start ();
    }

    @Override
    public void run ()
    {
        try
        {
            // Variable used to pass previous cycle start nano time
            // For the first cycle it is always {@code -1}
            long previousFrame = -1;

            // Continue running until pipeline is terminated
            while ( !isTerminated () )
            {
                // Saving current iteration start time
                // It is done first in cycle and outside of synchronization to be more precise
                final long currentFrame = System.nanoTime ();

                // Delay until next closest frame from the current frame time
                // In case no active transitions are encountered delay will be left undefined
                long delay = -1;

                // Synchronized by the pipeline instance to keep next transitions integrity and notify thread
                synchronized ( TimedAnimationPipeline.this )
                {
                    // First frame is always ignored
                    if ( previousFrame > 0 )
                    {
                        // Calculating new list of transitions that should be processed next
                        for ( final Transition transition : transitions )
                        {
                            // Performing next transition step
                            // It is up to specific transition implementation to proceed
                            final long untilNextFrame = transition.proceed ( previousFrame, currentFrame );

                            // Checking time until next transition frame
                            if ( untilNextFrame <= 0 )
                            {
                                // Removing finished or aborted transition from pipeline
                                transitions.remove ( transition );
                            }
                            else if ( delay <= 0 || untilNextFrame < delay )
                            {
                                // Updating next delay based on current transition if it is still running
                                delay = untilNextFrame;
                            }
                        }
                    }

                    // Updating previous time with current cycle start time
                    // This is necessary to avoid skipping any frames at any point
                    // We always check the timeline between previous cycle and current cycle for possible frames
                    previousFrame = currentFrame;

                    // Wait until next animation tick
                    // We will either wait until someone wakes up animator or delay ends
                    if ( delay > 0 )
                    {
                        // We need to limit delay to 1 millisecond minimum to avoid zero delay here
                        // No frames will be skipped in any case so we shouldn't worry about waiting extra time here
                        final long nextDelayMs = Math.round ( delay / TimeUtils.nsInMillisecond );
                        TimedAnimationPipeline.this.wait ( Math.max ( 1L, nextDelayMs ) );
                    }
                    else
                    {
                        // No specific delay, just wait until something wakes us up
                        TimedAnimationPipeline.this.wait ();
                    }
                }
            }
        }
        catch ( final InterruptedException ignored )
        {
            // Interrupted on shutdown
        }
    }

    @Override
    public void play ( final Transition transition )
    {
        try
        {
            // Informing transition about start
            transition.start ( System.nanoTime () );

            // Adding transition into active set
            transitions.add ( transition );

            // Reset animator
            reset ();
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to play transition: %s";
            LoggerFactory.getLogger ( TimedAnimationPipeline.class ).error ( String.format ( msg, transition ), e );
        }
    }

    @Override
    public void stop ( final Transition transition )
    {
        try
        {
            // Aborting transition
            transition.abort ();

            // Removing transition from active set
            transitions.remove ( transition );

            // Reset animator
            reset ();
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to stop transition: %s";
            LoggerFactory.getLogger ( TimedAnimationPipeline.class ).error ( String.format ( msg, transition ), e );
        }
    }

    @Override
    public void shutdown ()
    {
        try
        {
            // Shutdown tasks executor
            super.shutdown ();

            // Cleaning up resources
            synchronized ( TimedAnimationPipeline.this )
            {
                transitions.clear ();
                animator.interrupt ();
            }
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to shutdown pipeline";
            LoggerFactory.getLogger ( TimedAnimationPipeline.class ).error ( msg, e );
        }
    }

    /**
     * Resets animator thread.
     * This will update next tick time and also reset transitions which were supposed to be procesed next.
     */
    private void reset ()
    {
        // Synchronized by the pipeline instance to keep next transitions integrity and notify thread
        synchronized ( TimedAnimationPipeline.this )
        {
            // Notifying animator to update next tick time
            TimedAnimationPipeline.this.notifyAll ();
        }
    }
}