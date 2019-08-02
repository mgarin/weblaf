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

/**
 * Adapter for {@link MultiSplitResizeListener}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public abstract class MultiSplitResizeAdapter implements MultiSplitResizeListener
{
    @Override
    public void viewResizeStarted ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final WebMultiSplitPaneDivider divider )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void viewResized ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final WebMultiSplitPaneDivider divider )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void viewResizeEnded ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final WebMultiSplitPaneDivider divider )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void viewSizeAdjusted ( @NotNull final WebMultiSplitPane multiSplitPane )
    {
        /**
         * Do nothing by default.
         */
    }
}