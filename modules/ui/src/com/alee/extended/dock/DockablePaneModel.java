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

import com.alee.extended.dock.data.ResizeData;
import com.alee.extended.dock.data.StructureContainer;
import com.alee.extended.dock.data.StructureElement;
import com.alee.extended.dock.drag.FrameDropData;

import javax.swing.*;
import java.awt.*;

/**
 * Model used by {@link com.alee.extended.dock.WebDockablePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 * @see com.alee.extended.dock.WebDockablePaneModel
 */

public interface DockablePaneModel extends LayoutManager
{
    /**
     * Returns root structure container element.
     *
     * @return root structure container element
     */
    public StructureContainer getRoot ();

    /**
     * Returns element with the specified ID.
     *
     * @param id element ID
     * @return element with the specified ID
     */
    public StructureElement getElement ( String id );

    /**
     * Adds specified {@link com.alee.extended.dock.WebDockableFrame} into model.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame} to add
     */
    public void addFrame ( WebDockableFrame frame );

    /**
     * Removes specified {@link com.alee.extended.dock.WebDockableFrame} from model.
     *
     * @param frame {@link com.alee.extended.dock.WebDockableFrame} to remove
     */
    public void removeFrame ( WebDockableFrame frame );

    /**
     * Returns information on possible drop location.
     *
     * @param dockablePane dockable pane
     * @param support      transfer operation data
     * @return information on possible drop location
     */
    public FrameDropData dropData ( WebDockablePane dockablePane, TransferHandler.TransferSupport support );

    /**
     * Performs drop operation and returns whether or not drop operation was completed successfully.
     *
     * @param dockablePane dockable pane
     * @param support      transfer operation data
     * @return true if drop operation was completed successfully, false otherwise
     */
    public boolean drop ( WebDockablePane dockablePane, TransferHandler.TransferSupport support );

    /**
     * Returns resize data under specified coordinate or {@code null}.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return resize data under specified coordinate or {@code null}
     */
    public ResizeData getResizeData ( int x, int y );
}