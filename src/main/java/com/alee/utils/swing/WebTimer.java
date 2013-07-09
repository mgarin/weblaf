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

package com.alee.utils.swing;

import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TimeUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * This timer is a small extension for standart javax.swing.Timer. Instead of running in a single queue it creates separate Threads for
 * each timer and does not affect event-dispatching thread, until events are dispatched. This basically means that you can use any number
 * of Timer instances and you can run them alltogether without having any issues.
 * <p/>
 * Also this Timer implementation offers a variety of additional features and improvements which standard timer doesn't have (for example
 * you can dispatch events in a separate non-EDT thread and as a result avoid using EDT at all where it is not necessary).
 *
 * @author Mikle Garin
 * @since 1.3
 */

public class WebTimer
{
    /**
     * Default name for timer thread.
     */
    public static String defaultThreadName = "WebTimer";

    /**
     * Timer event listeners list.
     */
    private List<ActionListener> listeners = new ArrayList<ActionListener> ();

    /**
     * Unique (within one timer instance) ID of currently running thread.
     */
    private int id = 0;

    /**
     * ID of previously executed thread.
     */
    private int lastId;

    /**
     * Map of marks for currently active threads.
     */
    private Map<Integer, Boolean> running = new Hashtable<Integer, Boolean> ();

    /**
     * Last timer cycle start time.
     */
    private long sleepStart = 0;

    /**
     * Last timer cycle delay time.
     */
    private long sleepTime = 0;

    /**
     * Last timer thread.
     */
    private Thread exec = null;

    /**
     * Delay between timer cycles in milliseconds.
     */
    private long delay;

    /**
     * Delay before the first timer cycle run in milliseconds.
     */
    private long initialDelay;

    /**
     * Whether timer repeat its cycles or not.
     */
    private boolean repeats = true;

    /**
     * Whether each action should be fired from a separate invoke and wait call or not.
     * This might be useful if you are going to use multiply action listeners and make some interface changes on each action.
     */
    private boolean coalesce = true;

    /**
     * Whether actions should be fired from Event Dispatch Thread or not.
     * This might be useful if operations you want to perform within timer cycles have nothing to do with Event Dispatch Thread.
     */
    private boolean useEventDispatchThread = true;

    /**
     * Action command for fired events.
     */
    private String actionCommand = "";

    /**
     * Internal timer thread name.
     */
    private String name = null;

    /**
     * Timer cycles execution limit.
     * Zero and less = unlimited amount of execution cycles.
     */
    private int cyclesLimit = 0;

    /**
     * Constructs timer with specified delay.
     *
     * @param delay delay between timer cycles in milliseconds
     */
    public WebTimer ( long delay )
    {
        this ( defaultThreadName, delay );
    }

    /**
     * Constructs timer with specified internal thread name and delay.
     *
     * @param name  internal thread name
     * @param delay delay between timer cycles in milliseconds
     */
    public WebTimer ( String name, long delay )
    {
        this ( name, delay, null );
    }

    /**
     * Constructs timer with specified delay and initial delay.
     *
     * @param delay        delay between timer cycles in milliseconds
     * @param initialDelay delay before the first timer cycle run in milliseconds
     */
    public WebTimer ( long delay, long initialDelay )
    {
        this ( defaultThreadName, delay, initialDelay );
    }

    /**
     * Constructs timer with specified internal thread name, delay and initial delay.
     *
     * @param name         internal thread name
     * @param delay        delay between timer cycles in milliseconds
     * @param initialDelay delay before the first timer cycle run in milliseconds
     */
    public WebTimer ( String name, long delay, long initialDelay )
    {
        this ( name, delay, initialDelay, null );
    }

    /**
     * Constructs timer with specified delay and action listener.
     *
     * @param delay    delay between timer cycles in milliseconds
     * @param listener action listener
     */
    public WebTimer ( long delay, ActionListener listener )
    {
        this ( defaultThreadName, delay, listener );
    }

    /**
     * Constructs timer with specified internal thread name, delay and action listener.
     *
     * @param name     internal thread name
     * @param delay    delay between timer cycles in milliseconds
     * @param listener action listener
     */
    public WebTimer ( String name, long delay, ActionListener listener )
    {
        this ( name, delay, -1, listener );
    }

    /**
     * Constructs timer with specified delay, initial delay and action listener.
     *
     * @param delay        delay between timer cycles in milliseconds
     * @param initialDelay delay before the first timer cycle run in milliseconds
     * @param listener     action listener
     */
    public WebTimer ( long delay, long initialDelay, ActionListener listener )
    {
        this ( defaultThreadName, delay, initialDelay, listener );
    }

    /**
     * Constructs timer with specified internal thread name, delay, initial delay and action listener.
     *
     * @param name         internal thread name
     * @param delay        delay between timer cycles in milliseconds
     * @param initialDelay delay before the first timer cycle run in milliseconds
     * @param listener     action listener
     */
    public WebTimer ( String name, long delay, long initialDelay, ActionListener listener )
    {
        super ();
        setName ( name );
        setDelay ( delay );
        setInitialDelay ( initialDelay );
        addActionListener ( listener );
    }

    /**
     * Returns delay before the first timer cycle run in milliseconds.
     *
     * @return delay before the first timer cycle run in milliseconds
     */
    public long getInitialDelay ()
    {
        return initialDelay;
    }

    /**
     * Sets delay before the first timer cycle run in milliseconds.
     *
     * @param initialDelay delay before the first timer cycle run in milliseconds
     */
    public void setInitialDelay ( long initialDelay )
    {
        if ( initialDelay != -1 && initialDelay < 0 )
        {
            throw new IllegalArgumentException ( "Invalid initial delay: " + initialDelay );
        }
        else
        {
            this.initialDelay = initialDelay;
        }
    }

    /**
     * Returns delay between timer cycles in milliseconds.
     *
     * @return delay between timer cycles in milliseconds
     */
    public long getDelay ()
    {
        return delay;
    }

    /**
     * Sets delay between timer cycles in milliseconds.
     *
     * @param delay delay between timer cycles in milliseconds
     */
    public void setDelay ( long delay )
    {
        if ( delay < 0 )
        {
            throw new IllegalArgumentException ( "Invalid delay: " + delay );
        }
        else
        {
            this.delay = delay;
        }
    }

    /**
     * Returns whether timer repeat its cycles or not.
     *
     * @return true if timer repeat its cycles, false otherwise
     */
    public boolean isRepeats ()
    {
        return repeats;
    }

    /**
     * Sets whether timer should repeat its cycles or not.
     *
     * @param repeats whether timer should repeat its cycles or not
     */
    public void setRepeats ( boolean repeats )
    {
        this.repeats = repeats;
    }

    /**
     * Returns whether each action should be fired from a separate invoke and wait call or not.
     *
     * @return true if each action should be fired from a separate invoke and wait call, false otherwise
     */
    public boolean isCoalesce ()
    {
        return coalesce;
    }

    /**
     * Sets whether each action should be fired from a separate invoke and wait call or not.
     *
     * @param coalesce whether each action should be fired from a separate invoke and wait call or not
     */
    public void setCoalesce ( boolean coalesce )
    {
        this.coalesce = coalesce;
    }

    /**
     * Returns whether actions should be fired from Event Dispatch Thread or not.
     *
     * @return true if actions should be fired from Event Dispatch Thread, false otherwise
     */
    public boolean isUseEventDispatchThread ()
    {
        return useEventDispatchThread;
    }

    /**
     * Sets whether actions should be fired from Event Dispatch Thread or not.
     *
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     */
    public void setUseEventDispatchThread ( boolean useEventDispatchThread )
    {
        this.useEventDispatchThread = useEventDispatchThread;
    }

    /**
     * Returns action command for fired events.
     *
     * @return action command for fired events
     */
    public String getActionCommand ()
    {
        return actionCommand;
    }

    /**
     * Sets action command for fired events.
     *
     * @param actionCommand action command for fired events
     */
    public void setActionCommand ( String actionCommand )
    {
        this.actionCommand = actionCommand;
    }

    /**
     * Returns timer cycles execution limit.
     *
     * @return timer cycles execution limit
     */
    public int getCyclesLimit ()
    {
        return cyclesLimit;
    }

    /**
     * Sets timer cycles execution limit.
     * Zero and less = unlimited amount of execution cycles.
     *
     * @param cyclesLimit timer cycles execution limit
     */
    public void setCyclesLimit ( int cyclesLimit )
    {
        this.cyclesLimit = cyclesLimit;
    }

    /**
     * Returns internal timer thread name.
     *
     * @return internal timer thread name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets internal timer thread name.
     *
     * @param name internal timer thread name
     */
    public void setName ( String name )
    {
        this.name = name;
        if ( exec != null )
        {
            exec.setName ( name );
        }
    }

    /**
     * Returns time passed in milliseconds since curent cycle start.
     * Cycle includes its delay time and execution time.
     *
     * @return time passed in milliseconds since curent cycle start
     */
    public long getCycleTimePassed ()
    {
        return System.currentTimeMillis () - sleepStart;
    }

    /**
     * Returns time left in milliseconds until current cycle action exection.
     *
     * @return time left in milliseconds until current cycle action exection
     */
    public long getCycleTimeLeft ()
    {
        return sleepTime - getCycleTimePassed ();
    }

    /**
     * Starts timer execution.
     */
    public void start ()
    {
        startExec ();
    }

    /**
     * Stops timer execution.
     */
    public void stop ()
    {
        stopExec ();
    }

    /**
     * Restarts timer execution.
     */
    public void restart ()
    {
        stopExec ();
        startExec ();
    }

    /**
     * Returns whether this timer is running or not.
     *
     * @return true if this timer is running, false otherwise
     */
    public synchronized boolean isRunning ()
    {
        return exec != null && exec.isAlive ();
    }

    /**
     * Starts timer execution thread.
     */
    private synchronized void startExec ()
    {
        // Ignore if timer is already running
        if ( isRunning () )
        {
            return;
        }

        // Saving current thread unique id
        lastId = id;
        id++;

        // Starting new cycling thread
        final int currentId = lastId;
        exec = new Thread ( new Runnable ()
        {
            public void run ()
            {
                // Adding a live thread into map
                setAlive ( currentId, true );

                try
                {
                    // Initial delay
                    long actualInitialDelay = getInitialDelay () < 0 ? getDelay () : getInitialDelay ();
                    if ( actualInitialDelay > 0 )
                    {
                        sleepStart = System.currentTimeMillis ();
                        sleepTime = actualInitialDelay;
                        Thread.sleep ( actualInitialDelay );
                    }

                    // Checking if we sould stop execution
                    if ( shouldContinue ( -1, currentId ) )
                    {
                        // Starting cycles execution
                        if ( repeats )
                        {
                            // Repeated events
                            int cycle = 0;
                            while ( shouldContinue ( cycle, currentId ) )
                            {
                                // Firing events
                                fireEvent ();

                                // Incrementing cycles count
                                cycle++;

                                // Checking if we sould stop execution due to changes through events
                                if ( !shouldContinue ( cycle, currentId ) )
                                {
                                    break;
                                }

                                // Waiting for next execution
                                if ( getDelay () > 0 )
                                {
                                    long currentDelay = getDelay ();
                                    sleepStart = System.currentTimeMillis ();
                                    sleepTime = currentDelay;
                                    Thread.sleep ( currentDelay );
                                }
                            }
                        }
                        else
                        {
                            // Single event
                            fireEvent ();
                        }
                    }
                }
                catch ( InterruptedException e )
                {
                    // Execution interrupted
                }

                // Removing finished thread from map
                cleanUp ( currentId );
            }
        }, name );
        exec.start ();
    }

    /**
     * Returns whether thread with specified ID should continue execution or not.
     *
     * @param cycle cycle number
     * @param id    thread ID
     * @return true if thread with specified ID should continue execution, false otherwise
     */
    private boolean shouldContinue ( int cycle, int id )
    {
        return running.get ( id ) && !Thread.currentThread ().isInterrupted () && ( cyclesLimit <= 0 || cyclesLimit > cycle );
    }

    /**
     * Sets whether thread under specified ID is alive or not.
     *
     * @param id    thread ID
     * @param alive whether thread is alive or not
     */
    private void setAlive ( int id, boolean alive )
    {
        running.put ( id, alive );
    }

    /**
     * Cleans thread ID cache.
     *
     * @param id thread ID
     */
    private void cleanUp ( int id )
    {
        running.remove ( id );
    }

    /**
     * Stops timer execution.
     */
    private synchronized void stopExec ()
    {
        if ( exec != null )
        {
            // Interrupt thread
            exec.interrupt ();

            // Stop execution from inside
            setAlive ( lastId, false );

            // Wait for execution to stop
            try
            {
                exec.join ();
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace ();
            }
        }
    }

    /**
     * Adds new action listener.
     *
     * @param listener new action listener
     */
    public void addActionListener ( ActionListener listener )
    {
        if ( listener != null )
        {
            listeners.add ( listener );
        }
    }

    /**
     * Removes an action listener.
     *
     * @param listener action listener
     */
    public void removeActionListener ( ActionListener listener )
    {
        if ( listener != null )
        {
            listeners.remove ( listener );
        }
    }

    /**
     * Returns available action listeners list.
     *
     * @return available action listeners list
     */
    public List<ActionListener> getListeners ()
    {
        return listeners;
    }

    /**
     * Fires action events.
     */
    private void fireEvent ()
    {
        if ( listeners.size () > 0 )
        {
            // Event
            final ActionEvent actionEvent = createActionEvent ();

            // Working with local array
            final List<ActionListener> listenerList = CollectionUtils.copy ( listeners );

            // Dispatch event in chosen way
            if ( useEventDispatchThread )
            {
                if ( coalesce )
                {
                    // Merge all events into single call to event dispatch thread
                    SwingUtils.invokeAndWaitSafely ( new Runnable ()
                    {
                        public void run ()
                        {
                            for ( ActionListener listener : listenerList )
                            {
                                listener.actionPerformed ( actionEvent );
                            }
                        }
                    } );
                }
                else
                {
                    // Make separate event calls to event dispatch thread
                    for ( final ActionListener listener : listenerList )
                    {
                        SwingUtils.invokeAndWaitSafely ( new Runnable ()
                        {
                            public void run ()
                            {
                                listener.actionPerformed ( actionEvent );
                            }
                        } );
                    }
                }
            }
            else
            {
                // Execute events in the same thread with timer
                for ( ActionListener listener : listenerList )
                {
                    listener.actionPerformed ( actionEvent );
                }
            }
        }
    }

    /**
     * Returns action event.
     *
     * @return action event
     */
    private ActionEvent createActionEvent ()
    {
        return new ActionEvent ( WebTimer.this, 0, actionCommand, TimeUtils.currentTime (), 0 );
    }

    /**
     * Returns newly created and started timer that doesn't repeat and has the specified delay and action listener.
     *
     * @param delay    delay between timer cycles in milliseconds
     * @param listener action listener
     * @return newly created and started timer
     */
    public static WebTimer delay ( long delay, ActionListener listener )
    {
        return delay ( defaultThreadName, delay, listener );
    }

    /**
     * Returns newly created and started timer that doesn't repeat and has the specified delay and action listener.
     *
     * @param name     thread name
     * @param delay    delay between timer cycles in milliseconds
     * @param listener action listener
     * @return newly created and started timer
     */
    public static WebTimer delay ( String name, long delay, ActionListener listener )
    {
        return delay ( name, delay, true, listener );
    }

    /**
     * Returns newly created and started timer that doesn't repeat and has the specified delay and action listener.
     *
     * @param delay                  delay between timer cycles in milliseconds
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     * @param listener               action listener
     * @return newly created and started timer
     */
    public static WebTimer delay ( long delay, boolean useEventDispatchThread, ActionListener listener )
    {
        return delay ( defaultThreadName, delay, useEventDispatchThread, listener );
    }

    /**
     * Returns newly created and started timer that doesn't repeat and has the specified delay and action listener.
     *
     * @param name                   thread name
     * @param delay                  delay between timer cycles in milliseconds
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     * @param listener               action listener
     * @return newly created and started timer
     */
    public static WebTimer delay ( String name, long delay, boolean useEventDispatchThread, ActionListener listener )
    {
        WebTimer once = new WebTimer ( name, delay, listener );
        once.setRepeats ( false );
        once.start ();
        return once;
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay and action listener.
     *
     * @param delay    delay between timer cycles in milliseconds
     * @param listener action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( long delay, ActionListener listener )
    {
        return repeat ( defaultThreadName, delay, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay and action listener.
     *
     * @param name     thread name
     * @param delay    delay between timer cycles in milliseconds
     * @param listener action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( String name, long delay, ActionListener listener )
    {
        return repeat ( name, delay, delay, true, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay and action listener.
     *
     * @param delay                  delay between timer cycles in milliseconds
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     * @param listener               action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( long delay, boolean useEventDispatchThread, ActionListener listener )
    {
        return repeat ( defaultThreadName, delay, useEventDispatchThread, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay and action listener.
     *
     * @param name                   thread name
     * @param delay                  delay between timer cycles in milliseconds
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     * @param listener               action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( String name, long delay, boolean useEventDispatchThread, ActionListener listener )
    {
        return repeat ( name, delay, delay, useEventDispatchThread, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay, initial delay and action listener.
     *
     * @param delay        delay between timer cycles in milliseconds
     * @param initialDelay delay before the first timer cycle run in milliseconds
     * @param listener     action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( long delay, long initialDelay, ActionListener listener )
    {
        return repeat ( defaultThreadName, delay, initialDelay, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay, initial delay and action listener.
     *
     * @param name         thread name
     * @param delay        delay between timer cycles in milliseconds
     * @param initialDelay delay before the first timer cycle run in milliseconds
     * @param listener     action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( String name, long delay, long initialDelay, ActionListener listener )
    {
        return repeat ( name, delay, initialDelay, true, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay, initial delay and action listener.
     *
     * @param delay                  delay between timer cycles in milliseconds
     * @param initialDelay           delay before the first timer cycle run in milliseconds
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     * @param listener               action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( long delay, long initialDelay, boolean useEventDispatchThread, ActionListener listener )
    {
        return repeat ( defaultThreadName, delay, initialDelay, useEventDispatchThread, listener );
    }

    /**
     * Returns newly created and started timer that repeats and has the specified delay, initial delay and action listener.
     *
     * @param name                   thread name
     * @param delay                  delay between timer cycles in milliseconds
     * @param initialDelay           delay before the first timer cycle run in milliseconds
     * @param useEventDispatchThread whether actions should be fired from Event Dispatch Thread or not
     * @param listener               action listener
     * @return newly created and started timer
     */
    public static WebTimer repeat ( String name, long delay, long initialDelay, boolean useEventDispatchThread, ActionListener listener )
    {
        WebTimer repeat = new WebTimer ( name, delay, initialDelay, listener );
        repeat.setRepeats ( true );
        repeat.start ();
        return repeat;
    }
}