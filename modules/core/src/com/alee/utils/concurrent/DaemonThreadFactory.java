package com.alee.utils.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * A thread factory that only produces daemon threads.
 *
 * @author Adolph C.
 */

public class DaemonThreadFactory implements ThreadFactory
{
    /**
     * Constructs a new daemon thread.
     *
     * @param runnable a runnable to be executed by new thread instance
     * @return constructed thread.
     */
    @Override
    public Thread newThread ( final Runnable runnable )
    {
        final Thread thread = new Thread ( runnable );
        thread.setDaemon ( true );
        return thread;
    }
}