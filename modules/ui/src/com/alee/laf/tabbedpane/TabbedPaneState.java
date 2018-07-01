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

package com.alee.laf.tabbedpane;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link JTabbedPane} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see TabbedPaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "TabbedPaneState" )
public class TabbedPaneState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link JTabbedPane} selected tab index.
     */
    @XStreamAsAttribute
    protected final Integer selectedIndex;

    /**
     * Constructs default {@link TabbedPaneState}.
     */
    public TabbedPaneState ()
    {
        this ( ( Integer ) null );
    }

    /**
     * Constructs new {@link TabbedPaneState} with settings from {@link JTabbedPane}.
     *
     * @param tabbedPane {@link JTabbedPane} to retrieve settings from
     */
    public TabbedPaneState ( final JTabbedPane tabbedPane )
    {
        this ( tabbedPane.getSelectedIndex () );
    }

    /**
     * Constructs new {@link TabbedPaneState} with specified settings.
     *
     * @param selectedIndex {@link JTabbedPane} selected tab index
     */
    public TabbedPaneState ( final Integer selectedIndex )
    {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Returns {@link JTabbedPane} selected tab index.
     *
     * @return {@link JTabbedPane} selected tab index
     */
    public Integer selectedIndex ()
    {
        return selectedIndex;
    }

    /**
     * Applies this {@link TabbedPaneState} to the specified {@link JTabbedPane}.
     *
     * @param tabbedPane {@link JTabbedPane} to apply this {@link TabbedPaneState} to
     */
    public void apply ( final JTabbedPane tabbedPane )
    {
        if ( selectedIndex != null && selectedIndex >= 0 && selectedIndex < tabbedPane.getTabCount () &&
                selectedIndex != tabbedPane.getSelectedIndex () )
        {
            tabbedPane.setSelectedIndex ( selectedIndex );
        }
    }
}