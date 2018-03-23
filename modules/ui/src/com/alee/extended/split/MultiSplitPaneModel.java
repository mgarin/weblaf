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

package com.alee.extended.split;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;

/**
 * Model used by {@link WebMultiSplitPane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPaneModel
 * @see WebMultiSplitPane
 */

public interface MultiSplitPaneModel extends LayoutManager, Serializable
{
    /**
     * Installs this {@link MultiSplitPaneModel} into specified {@link WebMultiSplitPane}.
     *
     * @param multiSplitPane {@link WebMultiSplitPane} this model should be installed into
     * @param views          initial {@link MultiSplitView}s
     * @param dividers       initial {@link WebMultiSplitPaneDivider}s
     */
    public void install ( WebMultiSplitPane multiSplitPane, List<MultiSplitView> views, List<WebMultiSplitPaneDivider> dividers );

    /**
     * Uninstalls this {@link MultiSplitPaneModel} from the specified {@link WebMultiSplitPane}.
     *
     * @param multiSplitPane {@link WebMultiSplitPane} this model should be uninstalled from
     */
    public void uninstall ( WebMultiSplitPane multiSplitPane );

    /**
     * Adds {@link MultiSplitView} based on specified {@link Component} and constraints.
     *
     * @param component   {@link MultiSplitView} {@link Component}
     * @param constraints {@link MultiSplitView} constraints
     */
    public void addComponent ( Component component, Object constraints );

    /**
     * Moves {@link MultiSplitView} containing specified {@link Component} to the specified index.
     *
     * @param component {@link MultiSplitView} {@link Component}
     * @param index     index to move {@link MultiSplitView} to
     */
    public void moveComponent ( Component component, int index );

    /**
     * Removes {@link MultiSplitView} containing specified {@link Component}.
     *
     * @param component {@link MultiSplitView} {@link Component}
     */
    public void removeComponent ( Component component );

    /**
     * Returns amount of all {@link MultiSplitView}s contained in {@link MultiSplitPaneModel}.
     *
     * @return amount of all {@link MultiSplitView}s contained in {@link MultiSplitPaneModel}
     */
    public int getViewCount ();

    /**
     * Returns copy of the {@link List} of all {@link MultiSplitView}s contained in {@link MultiSplitPaneModel}.
     *
     * @return copy of the {@link List} of all {@link MultiSplitView}s contained in {@link MultiSplitPaneModel}
     */
    public List<MultiSplitView> getViews ();

    /**
     * Returns index of the {@link MultiSplitView} that contains specified {@link Component}.
     *
     * @param component {@link Component} contained in {@link MultiSplitView}
     * @return index of the {@link MultiSplitView} that contains specified {@link Component}
     */
    public int getViewIndex ( Component component );

    /**
     * Returns {@link Component} from {@link MultiSplitView} at the specified index.
     *
     * @param index {@link MultiSplitView} index
     * @return {@link Component} from {@link MultiSplitView} at the specified index
     */
    public Component getViewComponent ( int index );

    /**
     * Returns copy of the {@link List} of all {@link Component}s from {@link MultiSplitView}s in {@link MultiSplitPaneModel}.
     *
     * @return copy of the {@link List} of all {@link Component}s from {@link MultiSplitView}s in {@link MultiSplitPaneModel}
     */
    public List<Component> getViewComponents ();

    /**
     * Returns copy of the {@link List} of all {@link WebMultiSplitPaneDivider}s used by {@link WebMultiSplitPane}.
     *
     * @return copy of the {@link List} of all {@link WebMultiSplitPaneDivider}s used by {@link WebMultiSplitPane}
     */
    public List<WebMultiSplitPaneDivider> getDividers ();

    /**
     * Returns index of the specified {@link WebMultiSplitPaneDivider}.
     *
     * @param divider {@link WebMultiSplitPaneDivider} to find
     * @return index of the specified {@link WebMultiSplitPaneDivider}
     */
    public int getDividerIndex ( WebMultiSplitPaneDivider divider );

    /**
     * Returns {@link MultiSplitState} describing current {@link WebMultiSplitPane} state.
     *
     * @return {@link MultiSplitState} describing current {@link WebMultiSplitPane} state
     */
    public MultiSplitState getMultiSplitState ();

    /**
     * Updates {@link WebMultiSplitPane} using specified {@link MultiSplitState}.
     *
     * @param state{@link MultiSplitState} to update {@link WebMultiSplitPane} with
     */
    public void setMultiSplitState ( MultiSplitState state );

    /**
     * Resets {@link MultiSplitViewState}s to the initial ones dictated by {@link MultiSplitConstraints}.
     * Such reset always takes place at layout initialization to assign {@link MultiSplitViewState}s but normally isn't required later on.
     */
    public void resetViewStates ();

    /**
     * Returns whether or not any view is currently expanded in {@link WebMultiSplitPane}.
     *
     * @return {@code true} if any view is currently expanded in {@link WebMultiSplitPane}, {@code false} otherwise
     */
    public boolean isAnyViewExpanded ();

    /**
     * Returns index of currently expanded view in {@link WebMultiSplitPane}.
     * Might return {@code -1} if there are currently no expanded view.
     *
     * @return index of currently expanded view in {@link WebMultiSplitPane}
     */
    public int getExpandedViewIndex ();

    /**
     * Expands view positioned in {@link WebMultiSplitPane} at the specified index.
     *
     * @param index view index
     */
    public void expandView ( int index );

    /**
     * Collapses expanded view if any view is currently expanded in {@link WebMultiSplitPane}.
     */
    public void collapseExpandedView ();

    /**
     * Toggles expansion state for the view positioned in {@link WebMultiSplitPane} at the specified index.
     *
     * @param index view index
     */
    public void toggleViewExpansion ( int index );

    /**
     * Toggles expansion state for the view positioned to the left of the {@link WebMultiSplitPaneDivider}.
     *
     * @param divider {@link WebMultiSplitPaneDivider} to determine toggle index
     */
    public void toggleViewToLeft ( WebMultiSplitPaneDivider divider );

    /**
     * Toggles expansion state for the view positioned to the right of the {@link WebMultiSplitPaneDivider}.
     *
     * @param divider {@link WebMultiSplitPaneDivider} to determine toggle index
     */
    public void toggleViewToRight ( WebMultiSplitPaneDivider divider );

    /**
     * Returns whether or not drag for the specified {@link WebMultiSplitPaneDivider} is available.
     *
     * @param divider {@link WebMultiSplitPaneDivider} to check drag availability for
     * @return {@code true} if drag for the specified {@link WebMultiSplitPaneDivider} is available, {@code false} otherwise
     */
    public boolean isDragAvailable ( WebMultiSplitPaneDivider divider );

    /**
     * Returns currently dragged {@link WebMultiSplitPaneDivider} or {@code null} if no drag operation is happening at the time.
     *
     * @return currently dragged {@link WebMultiSplitPaneDivider} or {@code null} if no drag operation is happening at the time
     */
    public WebMultiSplitPaneDivider getDraggedDivider ();

    /**
     * Performs preparations for {@link WebMultiSplitPaneDivider} drag.
     *
     * @param divider {@link WebMultiSplitPaneDivider} that is being dragged
     * @param e       {@link MouseEvent}
     */
    public void dividerDragStarted ( WebMultiSplitPaneDivider divider, MouseEvent e );

    /**
     * Performs {@link WebMultiSplitPaneDivider} drag.
     *
     * @param divider {@link WebMultiSplitPaneDivider} that is being dragged
     * @param e       {@link MouseEvent}
     */
    public void dividerDragged ( WebMultiSplitPaneDivider divider, MouseEvent e );

    /**
     * Cleans up resources used for {@link WebMultiSplitPaneDivider} drag.
     *
     * @param divider {@link WebMultiSplitPaneDivider} that is being dragged
     * @param e       {@link MouseEvent}
     */
    public void dividerDragEnded ( WebMultiSplitPaneDivider divider, MouseEvent e );
}