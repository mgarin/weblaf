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

package com.alee.extended.dock;

import com.alee.api.data.CompassDirection;

import java.util.EventListener;

/**
 * {@link WebDockableFrame} events listener.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockableFrame
 */
public interface DockableFrameListener extends EventListener
{
    /**
     * Called when frame is added to {@link WebDockablePane}.
     *
     * @param frame        {@link WebDockableFrame} which was added
     * @param dockablePane {@link WebDockablePane} where frame was added
     */
    public void frameAdded ( WebDockableFrame frame, WebDockablePane dockablePane );

    /**
     * Called when frame state is changed.
     *
     * @param frame    {@link WebDockableFrame}
     * @param oldState previous frame state
     * @param newState current frame state
     */
    public void frameStateChanged ( WebDockableFrame frame, DockableFrameState oldState, DockableFrameState newState );

    /**
     * Called when frame is moved within {@link WebDockablePane}.
     *
     * @param frame    {@link WebDockableFrame}
     * @param position current frame position relative to content
     */
    public void frameMoved ( WebDockableFrame frame, CompassDirection position );

    /**
     * Called when frame is removed from {@link WebDockablePane}.
     *
     * @param frame        {@link WebDockableFrame} which was removed
     * @param dockablePane {@link WebDockablePane} where frame was removed from
     */
    public void frameRemoved ( WebDockableFrame frame, WebDockablePane dockablePane );
}