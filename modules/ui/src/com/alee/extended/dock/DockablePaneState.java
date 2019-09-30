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
import com.alee.api.merge.Mergeable;
import com.alee.extended.dock.data.DockableContainer;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * {@link WebDockablePane} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see DockablePaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "DockablePaneState" )
public class DockablePaneState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link WebDockablePane} root element.
     */
    @Nullable
    @XStreamAsAttribute
    protected final DockableContainer root;

    /**
     * Constructs default {@link DockablePaneState}.
     */
    public DockablePaneState ()
    {
        this ( ( DockableContainer ) null );
    }

    /**
     * Constructs new {@link DockablePaneState} with settings from {@link WebDockablePane}.
     *
     * @param dockablePane {@link WebDockablePane} to retrieve settings from
     */
    public DockablePaneState ( @NotNull final WebDockablePane dockablePane )
    {
        this ( dockablePane.getState () );
    }

    /**
     * Constructs new {@link DockablePaneState} with specified settings.
     *
     * @param root {@link WebDockablePane} root element
     */
    public DockablePaneState ( @Nullable final DockableContainer root )
    {
        this.root = root;
    }

    /**
     * Returns {@link WebDockablePane} root element.
     *
     * @return {@link WebDockablePane} root element
     */
    @Nullable
    public DockableContainer root ()
    {
        return root;
    }

    /**
     * Applies this {@link DockablePaneState} to the specified {@link WebDockablePane}.
     *
     * @param dockablePane {@link WebDockablePane} to apply this {@link DockablePaneState} to
     */
    public void apply ( @NotNull final WebDockablePane dockablePane )
    {
        if ( root != null )
        {
            dockablePane.setState ( root );
        }
    }
}