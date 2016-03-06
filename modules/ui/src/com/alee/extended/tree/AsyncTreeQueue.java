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

import com.alee.utils.concurrent.DaemonThreadFactory;

import java.util.Map;
import java.util.WeakHashMap;
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
     * Maximum threads amount to run all asynchronous trees requests in.
     * You can set this to zero to disable thread limit.
     */
    public static int threadsAmount = 4;

    /**
     * Whether to use threads mount limit for each separate asynchronous tree or for all existing asynchronous trees.
     */
    public static boolean separateLimitForEachTree = true;

    /**
     * Currently cached queues list.
     */
    private static final Map<WebAsyncTree, AsyncTreeQueue> queues = new WeakHashMap<WebAsyncTree, AsyncTreeQueue> ();

    /**
     * Lock for ExecutorService calls synchronization.
     */
    private final Object lock = new Object ();

    /**
     * ExecutorService to limit simultaneously running threads.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool ( threadsAmount, new DaemonThreadFactory () );

    /**
     * Sets maximum threads amount for the specified asynchronous tree.
     *
     * @param asyncTree asynchronous tree to process
     * @param amount    new maximum threads amount
     */
    public static void setMaximumThreadsAmount ( final WebAsyncTree asyncTree, final int amount )
    {
        getInstance ( asyncTree ).setMaximumThreadsAmount ( amount );
    }

    /**
     * Executes runnable using queue for the specified asynchronous tree.
     *
     * @param asyncTree asynchronous tree to process
     * @param runnable  runnable to execute
     */
    public static void execute ( final WebAsyncTree asyncTree, final Runnable runnable )
    {
        getInstance ( asyncTree ).execute ( runnable );
    }

    /**
     * Returns an instance of queue for the specified asynchronous tree.
     * This method might return the same queue for all trees depending on "separateLimitForEachTree" variable value.
     *
     * @param asyncTree asynchronous tree to process
     * @return an instance of queue for the specified asynchronous tree
     */
    public static AsyncTreeQueue getInstance ( final WebAsyncTree asyncTree )
    {
        if ( separateLimitForEachTree )
        {
            return getInstanceImpl ( asyncTree );
        }
        else
        {
            return getInstanceImpl ( null );
        }
    }

    /**
     * Returns an instance of queue for the specified asynchronous tree.
     *
     * @param asyncTree asynchronous tree to process
     * @return an instance of queue for the specified asynchronous tree
     */
    private static AsyncTreeQueue getInstanceImpl ( final WebAsyncTree asyncTree )
    {
        AsyncTreeQueue queue = queues.get ( asyncTree );
        if ( queue == null )
        {
            // Shutting down all tree-specific queues since the queue generation rule has changed
            if ( asyncTree == null )
            {
                shutdownAllQueues ();
            }

            // Creating new queue
            queue = new AsyncTreeQueue ();
            queues.put ( asyncTree, queue );
        }
        return queue;
    }

    /**
     * Forces all queues to shutdown.
     */
    private static void shutdownAllQueues ()
    {
        for ( final Map.Entry<WebAsyncTree, AsyncTreeQueue> queueEntry : queues.entrySet () )
        {
            queueEntry.getValue ().shutdown ();
        }
    }

    /**
     * Constructs new queue.
     */
    private AsyncTreeQueue ()
    {
        super ();
    }

    /**
     * Sets maximum threads amount for this queue.
     *
     * @param amount maximum threads amount for this queue
     */
    public void setMaximumThreadsAmount ( final int amount )
    {
        synchronized ( lock )
        {
            if ( executorService != null )
            {
                executorService.shutdown ();
            }
            if ( amount > 0 )
            {
                executorService = Executors.newFixedThreadPool ( amount, new DaemonThreadFactory () );
            }
            else
            {
                executorService = null;
            }
        }
    }

    /**
     * Shutdowns this queue.
     */
    public void shutdown ()
    {
        synchronized ( lock )
        {
            if ( executorService != null )
            {
                executorService.shutdown ();
            }
        }
    }

    /**
     * Executes runnable using this queue.
     *
     * @param runnable runnable to execute
     */
    public void execute ( final Runnable runnable )
    {
        synchronized ( lock )
        {
            if ( executorService != null )
            {
                executorService.execute ( runnable );
            }
            else
            {
                new Thread ( runnable, "AsyncTreeQueue" ).start ();
            }
        }
    }
}