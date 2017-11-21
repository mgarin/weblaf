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

package com.alee.extended.tree;

import com.alee.api.jdk.Function;
import com.alee.api.jdk.Supplier;
import com.alee.utils.concurrent.DaemonThreadFactory;
import com.alee.utils.swing.WeakComponentData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Asynchronous tree children loading queue.
 *
 * @author Mikle Garin
 */

public final class AsyncTreeQueue
{
    /**
     * Maximum number of threads to run all asynchronous tree requests in.
     * You can set this to zero to disable hardcoded thread limit.
     */
    private static final int threadsNumber = 4;

    /**
     * Queue {@link Thread} number.
     */
    private static int queueNumber = 0;

    /**
     * Cached tree queues.
     */
    private static final WeakComponentData<WebAsyncTree, AsyncTreeQueue> queues =
            new WeakComponentData<WebAsyncTree, AsyncTreeQueue> ( "AsyncTreeQueue", 3 );

    /**
     * ExecutorService to limit simultaneously running threads.
     */
    private ExecutorService executorService;

    /**
     * Returns an instance of queue for the specified asynchronous tree.
     * This method might return the same queue for all trees depending on "separateLimitForEachTree" variable value.
     *
     * @param asyncTree asynchronous tree to process
     * @return an instance of queue for the specified asynchronous tree
     */
    public static AsyncTreeQueue getInstance ( final WebAsyncTree asyncTree )
    {
        return queues.get ( asyncTree, new Function<WebAsyncTree, AsyncTreeQueue> ()
        {
            @Override
            public AsyncTreeQueue apply ( final WebAsyncTree webAsyncTree )
            {
                return new AsyncTreeQueue ();
            }
        } );
    }

    /**
     * Constructs new queue.
     */
    private AsyncTreeQueue ()
    {
        super ();
        restartService ( threadsNumber );
    }

    /**
     * Sets maximum number of threads for this queue.
     *
     * @param threadsNumber maximum number of threads for this queue
     */
    public void setMaximumThreadsAmount ( final int threadsNumber )
    {
        restartService ( threadsNumber );
    }

    /**
     * Restarts this queue service.
     *
     * @param threadsNumber maximum number of threads for this queue
     */
    public synchronized void restartService ( final int threadsNumber )
    {
        // Shutting down previous service
        shutdownService ();

        // Thread factory for the service
        final DaemonThreadFactory thread = new DaemonThreadFactory ( new Supplier<String> ()
        {
            @Override
            public String get ()
            {
                synchronized ( AsyncTreeQueue.class )
                {
                    return "AsyncTreeQueue-" + queueNumber++;
                }
            }
        } );

        // Creating new service
        if ( threadsNumber > 0 )
        {
            // Fixed thread pool
            executorService = Executors.newFixedThreadPool ( threadsNumber, thread );
        }
        else
        {
            // Unlimited thread pool
            executorService = Executors.newCachedThreadPool ( thread );
        }
    }

    /**
     * Shutdowns this queue.
     */
    public synchronized void shutdownService ()
    {
        if ( executorService != null )
        {
            executorService.shutdown ();
        }
    }

    /**
     * Executes runnable using this queue.
     *
     * @param runnable runnable to execute
     */
    public synchronized void execute ( final Runnable runnable )
    {
        executorService.execute ( runnable );
    }
}