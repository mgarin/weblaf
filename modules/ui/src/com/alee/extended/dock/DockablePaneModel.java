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
import com.alee.api.annotations.Nullable;
import com.alee.extended.dock.data.DockableContainer;
import com.alee.extended.dock.data.DockableElement;
import com.alee.extended.dock.data.ResizeData;
import com.alee.extended.dock.drag.FrameDropData;
import com.alee.laf.window.WebDialog;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Model used by {@link WebDockablePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 * @see WebDockablePaneModel
 */
public interface DockablePaneModel extends LayoutManager, Serializable
{
    /**
     * Returns root {@link DockableContainer}.
     *
     * @return root {@link DockableContainer}
     */
    @NotNull
    public DockableContainer getRoot ();

    /**
     * Sets root {@link DockableContainer}.
     *
     * @param root root {@link DockableContainer}
     */
    public void setRoot ( @NotNull DockableContainer root );

    /**
     * Returns {@link DockableElement} with the specified identifier.
     *
     * @param id  element ID
     * @param <T> element type
     * @return {@link DockableElement} with the specified identifier
     */
    @NotNull
    public <T extends DockableElement> T getElement ( @NotNull String id );

    /**
     * Ensures specified {@link WebDockableFrame} data exists in the model.
     *
     * @param dockablePane {@link WebDockablePane} frame is added to
     * @param frame        {@link WebDockableFrame} to process
     */
    public void updateFrame ( @NotNull WebDockablePane dockablePane, @NotNull WebDockableFrame frame );

    /**
     * Removes specified {@link WebDockableFrame} from the model.
     *
     * @param dockablePane {@link WebDockablePane} frame is added to
     * @param frame        {@link WebDockableFrame} to remove
     */
    public void removeFrame ( @NotNull WebDockablePane dockablePane, @NotNull WebDockableFrame frame );

    /**
     * Returns {@link FrameDropData} containing information about possible drop location.
     *
     * @param dockablePane {@link WebDockablePane} frame is added to
     * @param support      transfer operation data
     * @return {@link FrameDropData} containing information about possible drop location
     */
    @Nullable
    public FrameDropData dropData ( @NotNull WebDockablePane dockablePane, @NotNull TransferHandler.TransferSupport support );

    /**
     * Performs drop operation and returns whether or not drop operation was completed successfully.
     *
     * @param dockablePane {@link WebDockablePane} frame is added to
     * @param support      transfer operation data
     * @return {@code true} if drop operation was completed successfully, {@code false} otherwise
     */
    public boolean drop ( @NotNull WebDockablePane dockablePane, @NotNull TransferHandler.TransferSupport support );

    /**
     * Returns {@link WebDockablePane} outer bounds.
     * These bounds include sidebars and {@link DockableElement}s.
     *
     * @param dockablePane {@link WebDockablePane}
     * @return {@link WebDockablePane} outer bounds
     */
    @NotNull
    public Rectangle getOuterBounds ( @NotNull WebDockablePane dockablePane );

    /**
     * Returns {@link WebDockablePane} inner bounds.
     * These bounds include only {@link DockableElement}s.
     *
     * @param dockablePane {@link WebDockablePane}
     * @return {@link WebDockablePane} inner bounds
     */
    @NotNull
    public Rectangle getInnerBounds ( @NotNull WebDockablePane dockablePane );

    /**
     * Returns bounds for the {@link WebDockableFrame}'s {@link WebDialog} created in {@link DockableFrameState#floating} state.
     * These bounds are requested when frame is being switched into {@link DockableFrameState#floating}.
     *
     * @param dockablePane {@link WebDockablePane} frame is added to
     * @param frame        {@link WebDockableFrame} to returns bounds for
     * @param dialog       {@link com.alee.laf.window.WebDialog} used to display floating frame
     * @return bounds for the {@link WebDockableFrame}'s {@link WebDialog} created in {@link DockableFrameState#floating} state
     */
    @NotNull
    public Rectangle getFloatingBounds ( @NotNull WebDockablePane dockablePane, @NotNull WebDockableFrame frame,
                                         @NotNull WebDialog dialog );

    /**
     * Returns {@link ResizeData} under specified coordinate or {@code null}.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return {@link ResizeData} under specified coordinate or {@code null}
     */
    @Nullable
    public ResizeData getResizeData ( int x, int y );
}