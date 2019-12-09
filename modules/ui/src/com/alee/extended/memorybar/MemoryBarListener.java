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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;

import java.util.EventListener;

/**
 * {@link WebMemoryBar} GC action listener.
 *
 * @author Mikle Garin
 */
public interface MemoryBarListener extends EventListener
{
    /**
     * Informs about GC action being invoked.
     * At this point actual {@link System#gc()} is not yet called.
     *
     * @param memoryBar {@link WebMemoryBar}
     */
    public void gcCalled ( @NotNull WebMemoryBar memoryBar );

    /**
     * Informs about GC action completion.
     * At this point actual {@link System#gc()} is completed.
     *
     * @param memoryBar {@link WebMemoryBar}
     */
    public void gcCompleted ( @NotNull WebMemoryBar memoryBar );
}