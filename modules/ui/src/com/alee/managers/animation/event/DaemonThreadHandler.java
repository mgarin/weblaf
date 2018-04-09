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

package com.alee.managers.animation.event;

import com.alee.utils.concurrent.DaemonThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link EventHandler} that sends tasks into separate daemon thread.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class DaemonThreadHandler implements EventHandler
{
    /**
     * Executor service that handles events.
     */
    private final ExecutorService executor;

    /**
     * Constructs new {@link DaemonThreadHandler}.
     */
    public DaemonThreadHandler ()
    {
        super ();
        executor = Executors.newSingleThreadExecutor ( new DaemonThreadFactory ( "DaemonThreadHandler" ) );
    }

    @Override
    public void handle ( final Runnable event )
    {
        executor.submit ( event );
    }
}