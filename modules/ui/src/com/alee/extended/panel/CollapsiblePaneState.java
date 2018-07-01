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

package com.alee.extended.panel;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * {@link WebCollapsiblePane} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see CollapsiblePaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "CollapsiblePaneState" )
public class CollapsiblePaneState implements Mergeable, Cloneable, Serializable
{
    /**
     * Whether or not {@link WebCollapsiblePane} is expanded.
     */
    @XStreamAsAttribute
    protected final Boolean expanded;

    /**
     * Constructs default {@link CollapsiblePaneState}.
     */
    public CollapsiblePaneState ()
    {
        this ( ( Boolean ) null );
    }

    /**
     * Constructs new {@link CollapsiblePaneState} with settings from {@link WebCollapsiblePane}.
     *
     * @param collapsiblePane {@link WebCollapsiblePane} to retrieve settings from
     */
    public CollapsiblePaneState ( final WebCollapsiblePane collapsiblePane )
    {
        this ( collapsiblePane.isExpanded () );
    }

    /**
     * Constructs new {@link CollapsiblePaneState} with specified settings.
     *
     * @param expanded whether or not {@link WebCollapsiblePane} is expanded
     */
    public CollapsiblePaneState ( final Boolean expanded )
    {
        this.expanded = expanded;
    }

    /**
     * Returns whether or not {@link WebCollapsiblePane} is expanded.
     *
     * @return {@code true} if {@link WebCollapsiblePane} is expanded, {@code false} otherwise
     */
    public Boolean isExpanded ()
    {
        return expanded != null && expanded;
    }

    /**
     * Applies this {@link CollapsiblePaneState} to the specified {@link WebCollapsiblePane}.
     *
     * @param collapsiblePane {@link WebCollapsiblePane} to apply this {@link CollapsiblePaneState} to
     */
    public void apply ( final WebCollapsiblePane collapsiblePane )
    {
        if ( expanded != null && collapsiblePane.isExpanded () != expanded )
        {
            collapsiblePane.setExpanded ( expanded, false );
        }
    }
}