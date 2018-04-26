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

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * {@link Serializable} state object for {@link WebMultiSplitPane}.
 * It can be used to save and restore {@link MultiSplitView}s state in runtime.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see MultiSplitPaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "MultiSplitState" )
public class MultiSplitState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link List} of {@link MultiSplitViewState}.
     */
    @XStreamImplicit
    protected List<MultiSplitViewState> states;

    /**
     * Returns {@link List} of {@link MultiSplitViewState}.
     *
     * @return {@link List} of {@link MultiSplitViewState}
     */
    public List<MultiSplitViewState> states ()
    {
        return states;
    }

    /**
     * Sets {@link List} of {@link MultiSplitViewState}.
     *
     * @param states {@link List} of {@link MultiSplitViewState}
     */
    public void setStates ( final List<MultiSplitViewState> states )
    {
        this.states = states;
    }
}