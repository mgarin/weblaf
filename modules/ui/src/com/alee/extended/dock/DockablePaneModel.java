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

import com.alee.extended.dock.data.DockableContainer;
import com.alee.extended.dock.data.DockableElement;
import com.alee.extended.dock.data.ResizeData;
import com.alee.extended.dock.drag.FrameDropData;
import com.alee.laf.window.WebDialog;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Model used by {@link com.alee.extended.dock.WebDockablePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 * @see com.alee.extended.dock.WebDockablePaneModel
 */

public interface DockablePaneModel extends LayoutManager, Serializable
{
    /**
     * Returns root structure container element.
     *
     * @return root structure container element
     */
    public DockableContainer getRoot ();

    /**
     * Sets root structure container element.
     *
     * @param root root structure container element
     */
    public void setRoot ( DockableContainer root );

    /**
     * Returns element with the specified ID.
     *
     * @param id  element ID
     * @param <T> element type
     * @return element with the specified ID
     */
    public <T extends DockableElement> T getElement ( String id );

    /**
     * Ensures specified {@link com.alee.extended.dock.WebDockableFrame} data exists in the model.
     *
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} frame is added to
     * @param frame        {@link WebDockableFrame} to process
     */
    public void updateFrame ( WebDockablePane dockablePane, WebDockableFrame frame );

    /**
     * Removes specified {@link com.alee.extended.dock.WebDockableFrame} from model.
     *
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} frame is added to
     * @param frame        {@link WebDockableFrame} to remove
     */
    public void removeFrame ( WebDockablePane dockablePane, WebDockableFrame frame );

    /**
     * Returns information on possible drop location.
     *
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} frame is added to
     * @param support      transfer operation data
     * @return information on possible drop location
     */
    public FrameDropData dropData ( WebDockablePane dockablePane, TransferHandler.TransferSupport support );

    /**
     * Performs drop operation and returns whether or not drop operation was completed successfully.
     *
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} frame is added to
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

    /**
     * Returns bounds for the frame dialog created in {@link com.alee.extended.dock.DockableFrameState#floating} state.
     * These bounds are requested when frame is being switched into {@link com.alee.extended.dock.DockableFrameState#floating}.
     *
     * @param dockablePane {@link WebDockablePane} frame is added to
     * @param frame        {@link com.alee.extended.dock.WebDockableFrame} to returns bounds for
     * @param dialog       {@link com.alee.laf.window.WebDialog} used to display floating frame
     * @return bounds for the frame dialog created in floating state
     */
    public Rectangle getFloatingBounds ( WebDockablePane dockablePane, WebDockableFrame frame, WebDialog dialog );
}