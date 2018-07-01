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

package com.alee.laf.splitpane;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link JSplitPane} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SplitPaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "SplitPaneState" )
public class SplitPaneState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link JSplitPane} divider location.
     */
    @XStreamAsAttribute
    protected final Integer dividerLocation;

    /**
     * Constructs default {@link SplitPaneState}.
     */
    public SplitPaneState ()
    {
        this ( ( Integer ) null );
    }

    /**
     * Constructs new {@link SplitPaneState} with settings from {@link JSplitPane}.
     *
     * @param splitPane {@link JSplitPane} to retrieve settings from
     */
    public SplitPaneState ( final JSplitPane splitPane )
    {
        this ( splitPane.getDividerLocation () );
    }

    /**
     * Constructs new {@link SplitPaneState} with specified settings.
     *
     * @param dividerLocation {@link JSplitPane} divider location
     */
    public SplitPaneState ( final Integer dividerLocation )
    {
        this.dividerLocation = dividerLocation;
    }

    /**
     * Returns {@link JSplitPane} divider location.
     *
     * @return {@link JSplitPane} divider location
     */
    public Integer dividerLocation ()
    {
        return dividerLocation;
    }

    /**
     * Applies this {@link SplitPaneState} to the specified {@link JSplitPane}.
     *
     * @param splitPane {@link JSplitPane} to apply this {@link SplitPaneState} to
     */
    public void apply ( final JSplitPane splitPane )
    {
        if ( dividerLocation != null )
        {
            splitPane.setDividerLocation ( dividerLocation );
        }
    }
}