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

import com.alee.api.annotations.NotNull;
import com.alee.managers.task.TaskManager;

/**
 * {@link EventHandler} that sends tasks into separate {@link com.alee.managers.task.TaskGroup}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class ThreadGroupHandler implements EventHandler
{
    /**
     * Identifier of {@link com.alee.managers.task.TaskGroup} to execute {@link Runnable} on
     */
    @NotNull
    private final String groupId;

    /**
     * Constructs new {@link ThreadGroupHandler}.
     *
     * @param groupId identifier of {@link com.alee.managers.task.TaskGroup} to execute {@link Runnable} on
     */
    public ThreadGroupHandler ( @NotNull final String groupId )
    {
        this.groupId = groupId;
    }

    @Override
    public void handle ( @NotNull final Runnable event )
    {
        TaskManager.execute ( groupId, event );
    }
}