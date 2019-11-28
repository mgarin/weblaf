package com.alee.utils.concurrent;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Supplier;

import java.util.concurrent.ThreadFactory;

/**
 * A thread factory that only produces daemon threads.
 *
 * @author Adolph C.
 * @author Mikle Garin
 */
public class DaemonThreadFactory implements ThreadFactory
{
    /**
     * {@link Thread} name supplier.
     */
    @NotNull
    private final Supplier<String> nameSupplier;

    /**
     * Constructs new {@link DaemonThreadFactory}.
     */
    public DaemonThreadFactory ()
    {
        this ( "DaemonThread" );
    }

    /**
     * Constructs new {@link DaemonThreadFactory}.
     *
     * @param name {@link Thread} name
     */
    public DaemonThreadFactory ( @NotNull final String name )
    {
        this ( new Supplier<String> ()
        {
            /**
             * {@link Thread} number.
             */
            private int number = 0;

            @Override
            public synchronized String get ()
            {
                return name + "-" + number++;
            }
        } );
    }

    /**
     * Constructs new {@link DaemonThreadFactory}.
     *
     * @param nameSupplier {@link Thread} name supplier
     */
    public DaemonThreadFactory ( @NotNull final Supplier<String> nameSupplier )
    {
        this.nameSupplier = nameSupplier;
    }

    /**
     * Constructs a new daemon thread.
     *
     * @param runnable a runnable to be executed by new thread instance
     * @return constructed thread.
     */
    @NotNull
    @Override
    public Thread newThread ( @NotNull final Runnable runnable )
    {
        final Thread thread = new Thread ( runnable );
        thread.setName ( nameSupplier.get () );
        thread.setDaemon ( true );
        return thread;
    }
}