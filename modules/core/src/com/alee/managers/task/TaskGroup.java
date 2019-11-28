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

package com.alee.managers.task;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.concurrent.DaemonThreadFactory;

import java.util.concurrent.*;

/**
 * This manager provides API for convenient usage of {@link Thread} groups.
 * You can register and shutdown {@link TaskGroup}s within {@link TaskManager} at will.
 *
 * @author Vyacheslav Ivanov
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-ConcurrencyManager">How to use ConcurrencyManager</a>
 * @see TaskManager
 */
public class TaskGroup implements Identifiable
{
    /**
     * This {@link TaskGroup} identifier.
     */
    @NotNull
    protected final String id;

    /**
     * {@link ThreadFactory} used to spawn {@link Thread}s for executing tasks.
     */
    @NotNull
    protected final ThreadFactory threadFactory;

    /**
     * Maximum parallel {@link Thread}s count in this {@link TaskGroup}.
     */
    protected final int maxThreadsCount;

    /**
     * {@link ExecutorService} used for executing tasks.
     */
    @Nullable
    protected ExecutorService executorService;

    /**
     * Constructs new {@link TaskGroup}.
     *
     * @param id this {@link TaskGroup} identifier
     */
    public TaskGroup ( @NotNull final String id )
    {
        this ( id, new DaemonThreadFactory ( id ), 0 );
    }

    /**
     * Constructs new {@link TaskGroup}.
     *
     * @param id              this {@link TaskGroup} identifier
     * @param maxThreadsCount maximum parallel {@link Thread}s count in this {@link TaskGroup}
     */
    public TaskGroup ( @NotNull final String id, final int maxThreadsCount )
    {
        this ( id, new DaemonThreadFactory ( id ), maxThreadsCount );
    }

    /**
     * Constructs new {@link TaskGroup}.
     *
     * @param id            this {@link TaskGroup} identifier
     * @param threadFactory {@link ThreadFactory} used to spawn {@link Thread}s for executing tasks
     */
    public TaskGroup ( @NotNull final String id, @NotNull final ThreadFactory threadFactory )
    {
        this ( id, threadFactory, 0 );
    }

    /**
     * Constructs new {@link TaskGroup}.
     *
     * @param id              this {@link TaskGroup} identifier
     * @param threadFactory   {@link ThreadFactory} used to spawn {@link Thread}s for executing tasks
     * @param maxThreadsCount maximum parallel {@link Thread}s count in this {@link TaskGroup}
     */
    public TaskGroup ( @NotNull final String id, @NotNull final ThreadFactory threadFactory, final int maxThreadsCount )
    {
        this.id = Objects.requireNonNull ( id, "ThreadGroup identifier must be specified" );
        this.threadFactory = Objects.requireNonNull ( threadFactory, "ThreadFactory must be specified" );
        this.maxThreadsCount = maxThreadsCount;
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Executes specified {@link Runnable}.
     * Returns {@link Future} of the execured task.
     *
     * @param runnable {@link Runnable} to execute
     * @return {@link Future} of the execured task
     */
    @NotNull
    public Future<?> execute ( @NotNull final Runnable runnable )
    {
        return executorService ().submit ( runnable );
    }

    /**
     * Executes specified {@link Callable}.
     * Returns {@link Future} of the execured task.
     *
     * @param callable {@link Callable} to execute
     * @param <V>      computed result type
     * @return {@link Future} of the execured task
     */
    @NotNull
    public <V> Future<V> execute ( @NotNull final Callable<V> callable )
    {
        return executorService ().submit ( callable );
    }

    /**
     * Shutdowns this {@link TaskGroup} after finishing all remaining submitted tasks.
     */
    public void shutdown ()
    {
        if ( executorService != null )
        {
            synchronized ( TaskGroup.this )
            {
                if ( executorService != null )
                {
                    executorService.shutdown ();
                    executorService = null;
                }
            }
        }
    }

    /**
     * Shutdowns this {@link TaskGroup} without finishing any of remaining submitted tasks.
     */
    public void shutdownNow ()
    {
        if ( executorService != null )
        {
            synchronized ( TaskGroup.this )
            {
                if ( executorService != null )
                {
                    executorService.shutdownNow ();
                    executorService = null;
                }
            }
        }
    }

    /**
     * Returns {@link ExecutorService} that can be used to execute tasks according to this {@link TaskGroup} settings.
     * In case {@link ExecutorService} doesn't exist or was shutdown it will be recreated.
     *
     * @return {@link ExecutorService} that can be used to execute tasks according to this {@link TaskGroup} settings
     */
    @NotNull
    protected ExecutorService executorService ()
    {
        if ( executorService == null )
        {
            synchronized ( TaskGroup.this )
            {
                if ( executorService == null )
                {
                    switch ( maxThreadsCount )
                    {
                        case 0:
                        {
                            executorService = Executors.newCachedThreadPool ( threadFactory );
                            break;
                        }
                        case 1:
                        {
                            executorService = Executors.newSingleThreadExecutor ( threadFactory );
                            break;
                        }
                        default:
                        {
                            executorService = Executors.newFixedThreadPool ( maxThreadsCount, threadFactory );
                            break;
                        }
                    }
                }
            }
        }
        return executorService;
    }

    @Override
    public int hashCode ()
    {
        return id.hashCode ();
    }
}