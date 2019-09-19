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

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;

/**
 * Adapter for {@link DockableFrameListener}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockableFrame
 */
public abstract class DockableFrameAdapter implements DockableFrameListener
{
    @Override
    public void frameAdded ( @NotNull final WebDockableFrame frame, @NotNull final WebDockablePane dockablePane )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void frameStateChanged ( @NotNull final WebDockableFrame frame, @NotNull final DockableFrameState oldState,
                                    @NotNull final DockableFrameState newState )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void frameMoved ( @NotNull final WebDockableFrame frame, @NotNull final CompassDirection position )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void frameRemoved ( @NotNull final WebDockableFrame frame, @NotNull final WebDockablePane dockablePane )
    {
        /**
         * Do nothing by default.
         */
    }
}