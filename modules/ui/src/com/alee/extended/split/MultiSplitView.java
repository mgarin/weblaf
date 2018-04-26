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

import com.alee.api.clone.behavior.PreserveOnClone;

import java.awt.*;
import java.io.Serializable;

/**
 * {@link MultiSplitConstraints} and {@link MultiSplitViewState} for a single {@link Component} within {@link WebMultiSplitPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see MultiSplitConstraints
 * @see MultiSplitViewState
 * @see WebMultiSplitPaneModel
 * @see WebMultiSplitPane
 */
public class MultiSplitView implements Cloneable, Serializable
{
    /**
     * Immutable {@link Component} which this {@link MultiSplitView} describes.
     */
    @PreserveOnClone
    protected final Component component;

    /**
     * Immutable {@link MultiSplitConstraints}.
     */
    protected final MultiSplitConstraints constraints;

    /**
     * Mutable {@link MultiSplitViewState}.
     */
    protected MultiSplitViewState state;

    /**
     * Constructs new {@link MultiSplitView}.
     *
     * @param component   {@link Component} which this {@link MultiSplitView} describes
     * @param constraints {@link MultiSplitConstraints}
     */
    public MultiSplitView ( final Component component, final MultiSplitConstraints constraints )
    {
        this.component = component;
        this.constraints = constraints;
        this.state = new MultiSplitViewState ();
    }

    /**
     * Returns {@link Component} which this {@link MultiSplitView} describes.
     *
     * @return {@link Component} which this {@link MultiSplitView} describes
     */
    public Component component ()
    {
        return component;
    }

    /**
     * Returns {@link MultiSplitConstraints}.
     *
     * @return {@link MultiSplitConstraints}
     */
    public MultiSplitConstraints constraints ()
    {
        return constraints;
    }

    /**
     * Returns {@link MultiSplitViewState}.
     *
     * @return {@link MultiSplitViewState}
     */
    public MultiSplitViewState state ()
    {
        return state;
    }

    /**
     * Modifies {@link MultiSplitViewState}.
     *
     * @param state {@link MultiSplitViewState}
     */
    public void setState ( final MultiSplitViewState state )
    {
        this.state = state;
    }
}