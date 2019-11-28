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

import com.alee.api.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * This manager provides API for convenient usage of {@link TaskGroup}s.
 * You can register, reconfigure and shutdown separate {@link TaskGroup}s.
 * Usig {@link TaskGroup} identifier you can execute {@link Runnable} or {@link Callable} tasks.
 *
 * @author Vyacheslav Ivanov
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-ConcurrencyManager">How to use ConcurrencyManager</a>
 * @see TaskGroup
 * @see TaskException
 */
public final class TaskManager
{
    /**
     * Identifier of predefined {@link TaskGroup} for file system requests.
     * By default it uses maximum 4 threads and {@link com.alee.utils.concurrent.DaemonThreadFactory}.
     * It can be configured through providing new {@link ThreadGroup} with same identifier in {@link #configureGroup(TaskGroup)}.
     */
    public static final String FILE_SYSTEM = "FileSystem";

    /**
     * Identifier of predefined {@link TaskGroup} for remote requests.
     * By default it uses maximum 4 threads and {@link com.alee.utils.concurrent.DaemonThreadFactory}.
     * It can be configured through providing new {@link ThreadGroup} with same identifier in {@link #configureGroup(TaskGroup)}.
     */
    public static final String REMOTE_REQUEST = "RemoteRequest";

    /**
     * Identifier of predefined {@link TaskGroup} for computation requests.
     * By default it uses maximum 8 threads and {@link com.alee.utils.concurrent.DaemonThreadFactory}.
     * It can be configured through providing new {@link ThreadGroup} with same identifier in {@link #configureGroup(TaskGroup)}.
     */
    public static final String COMPUTATION = "Computation";

    /**
     * {@link TaskGroup}s mapped by their identifiers.
     */
    private static Map<String, TaskGroup> groups;

    /**
     * Whether or not {@link TaskManager} is initialized.
     */
    private static boolean initialized;

    /**
     * Initializes {@link TaskManager} settings.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            // Map that kees all registered task groups
            groups = new HashMap<String, TaskGroup> ();

            // Default task groups
            registerGroup ( new TaskGroup ( FILE_SYSTEM, 4 ) );
            registerGroup ( new TaskGroup ( REMOTE_REQUEST, 4 ) );
            registerGroup ( new TaskGroup ( COMPUTATION, 8 ) );

            // Updating initialization mark
            initialized = true;
        }
    }

    /**
     * Returns whether or not specified {@link TaskGroup} already exists.
     *
     * @param group {@link TaskGroup} to look for
     * @return {@code true} if specified {@link TaskGroup} already exists, {@code false} otherwise
     */
    public static boolean exists ( @NotNull final TaskGroup group )
    {
        return exists ( group.getId () );
    }

    /**
     * Returns whether or not {@link TaskGroup} with the specified identifier already exists.
     *
     * @param groupId identifier of {@link TaskGroup} to look for
     * @return {@code true} if {@link TaskGroup} with the specified identifier already exists, {@code false} otherwise
     */
    public static boolean exists ( @NotNull final String groupId )
    {
        return groups.containsKey ( groupId );
    }

    /**
     * Returns {@link TaskGroup} with the specified identifier.
     * Throws {@link TaskException} if such {@link TaskGroup} is not yet registered.
     *
     * @param groupId identifier of {@link TaskGroup} to look for
     * @return {@link TaskGroup} with the specified identifier
     */
    @NotNull
    public static TaskGroup getGroup ( @NotNull final String groupId )
    {
        if ( exists ( groupId ) )
        {
            return groups.get ( groupId );
        }
        else
        {
            throw new TaskException ( "ThreadGroup(" + groupId + ") doesn't exist" );
        }
    }

    /**
     * Registers new {@link TaskGroup}.
     * Throws {@link TaskException} if such {@link TaskGroup} was already registered.
     *
     * @param group {@link TaskGroup} to register
     */
    public static void registerGroup ( @NotNull final TaskGroup group )
    {
        if ( !exists ( group ) )
        {
            groups.put ( group.getId (), group );
        }
        else
        {
            throw new TaskException ( "Use ConcurrencyManager.configure(...) to reconfigure ThreadGroup(" + group.getId () + ")" );
        }
    }

    /**
     * Configures existing {@link TaskGroup}.
     * Throws {@link TaskException} if such {@link TaskGroup} is not yet registered.
     *
     * @param group {@link TaskGroup} to configure
     */
    public static void configureGroup ( @NotNull final TaskGroup group )
    {
        if ( exists ( group ) )
        {
            groups.put ( group.getId (), group );
        }
        else
        {
            throw new TaskException ( "ThreadGroup(" + group.getId () + ") doesn't exist" );
        }
    }

    /**
     * Executes specified {@link Runnable} on the {@link TaskGroup} with the specified identifier.
     * Returns {@link Future} of the execured task.
     *
     * @param groupId  identifier of {@link TaskGroup} to execute {@link Runnable} on
     * @param runnable {@link Runnable} to execute
     * @return {@link Future} of the execured task
     */
    @NotNull
    public static Future<?> execute ( @NotNull final String groupId, @NotNull final Runnable runnable )
    {
        return getGroup ( groupId ).execute ( runnable );
    }

    /**
     * Executes specified {@link Callable} on the {@link TaskGroup} with the specified identifier.
     * Returns {@link Future} of the execured task.
     *
     * @param groupId  identifier of {@link TaskGroup} to execute {@link Callable} on
     * @param callable {@link Callable} to execute
     * @param <V>      computed result type
     * @return {@link Future} of the execured task
     */
    @NotNull
    public static <V> Future<V> execute ( @NotNull final String groupId, @NotNull final Callable<V> callable )
    {
        return getGroup ( groupId ).execute ( callable );
    }

    /**
     * Shutdowns {@link TaskGroup} with the specified identifier after finishing all remaining submitted tasks.
     *
     * @param groupId identifier of {@link TaskGroup} to shutdown
     */
    public static void shutdown ( @NotNull final String groupId )
    {
        getGroup ( groupId ).shutdown ();
    }

    /**
     * Shutdowns {@link TaskGroup} with the specified identifier without finishing any of remaining submitted tasks.
     *
     * @param groupId identifier of {@link TaskGroup} to shutdown
     */
    public static void shutdownNow ( @NotNull final String groupId )
    {
        getGroup ( groupId ).shutdownNow ();
    }
}