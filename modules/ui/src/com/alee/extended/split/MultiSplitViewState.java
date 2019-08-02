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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * State settings for single {@link MultiSplitView}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see MultiSplitView
 * @see WebMultiSplitPaneModel
 * @see WebMultiSplitPane
 */
@XStreamAlias ( "MultiSplitViewState" )
public class MultiSplitViewState implements Cloneable, Serializable
{
    /**
     * Current size of the {@link MultiSplitView} within {@link WebMultiSplitPane}.
     * Unlike {@link MultiSplitConstraints} this size is always in pixels as it is quite inconvenient to work with two formats at once.
     * Percentage size from {@link MultiSplitConstraints} is only used for initial size calculations.
     */
    @XStreamAsAttribute
    protected int size;

    /**
     * Whether or not {@link MultiSplitView} is expanded.
     * Expanded {@link MultiSplitView} always fills all available space in the {@link WebMultiSplitPane}.
     * Expansion state has no effect on the {@link WebMultiSplitPane} preferred size.
     */
    @XStreamAsAttribute
    protected boolean expanded;

    /**
     * Constructs new {@link MultiSplitViewState}.
     */
    public MultiSplitViewState ()
    {
        this.size = 0;
        this.expanded = false;
    }

    /**
     * Returns current size of the {@link MultiSplitView}.
     *
     * @return current size of the {@link MultiSplitView}
     */
    public int size ()
    {
        return size;
    }

    /**
     * Modifies current size of the {@link MultiSplitView}.
     *
     * @param size current size of the {@link MultiSplitView}
     */
    public void setSize ( final int size )
    {
        this.size = size;
    }

    /**
     * Returns {@link MultiSplitView} is expansion state.
     *
     * @return {@code true} if {@link MultiSplitView} is expanded, {@code false} otherwise
     */
    public boolean isExpanded ()
    {
        return expanded;
    }

    /**
     * Modifies {@link MultiSplitView} expansion state.
     *
     * @param expanded whether or not {@link MultiSplitView} is expanded
     */
    public void setExpanded ( final boolean expanded )
    {
        this.expanded = expanded;
    }
}