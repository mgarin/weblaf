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

import com.alee.api.annotations.NotNull;

import java.util.EventListener;

/**
 * Special listener for tracking {@link WebMultiSplitPane} view resize.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public interface MultiSplitResizeListener extends EventListener
{
    /**
     * Informs about view resize being started due to the specified {@link WebMultiSplitPaneDivider} being pressed.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}
     * @param divider        {@link WebMultiSplitPaneDivider} that is being pressed
     */
    public void viewResizeStarted ( @NotNull WebMultiSplitPane multiSplitPane, @NotNull WebMultiSplitPaneDivider divider );

    /**
     * Informs about occurred view resize due to the specified {@link WebMultiSplitPaneDivider} being dragged.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}
     * @param divider        {@link WebMultiSplitPaneDivider} that is being dragged
     */
    public void viewResized ( @NotNull WebMultiSplitPane multiSplitPane, @NotNull WebMultiSplitPaneDivider divider );

    /**
     * Informs about view resize being finished due to the specified {@link WebMultiSplitPaneDivider} being released.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}
     * @param divider        {@link WebMultiSplitPaneDivider} that is being released
     */
    public void viewResizeEnded ( @NotNull WebMultiSplitPane multiSplitPane, @NotNull WebMultiSplitPaneDivider divider );

    /**
     * Informs about view size adjustments occurred due to {@link WebMultiSplitPane} size or settings changes.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}
     */
    public void viewSizeAdjusted ( @NotNull WebMultiSplitPane multiSplitPane );
}