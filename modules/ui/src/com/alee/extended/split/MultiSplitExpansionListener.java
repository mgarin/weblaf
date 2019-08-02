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

import java.awt.*;
import java.util.EventListener;

/**
 * Special listener for tracking {@link WebMultiSplitPane} view expansion states.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public interface MultiSplitExpansionListener extends EventListener
{
    /**
     * Informs about {@link Component} becoming an expanded view within {@link WebMultiSplitPane}.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}
     * @param view           expanded view {@link Component}
     */
    public void viewExpanded ( @NotNull WebMultiSplitPane multiSplitPane, @NotNull Component view );

    /**
     * Informs about previously expanded {@link Component} view becoming collapsed within {@link WebMultiSplitPane}.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}
     * @param view           collapsed view {@link Component}
     */
    public void viewCollapsed ( @NotNull WebMultiSplitPane multiSplitPane, @NotNull Component view );
}