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

package com.alee.laf.scroll.layout;

import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Settings for single {@link javax.swing.JScrollBar} within {@link javax.swing.JScrollPane}.
 * It is only used within {@link ScrollPaneLayout} to customize scroll bars and it is not applicable to default layout.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "ScrollBarSettings" )
public class ScrollBarSettings implements Mergeable, Cloneable, Serializable
{
    /**
     * Whether or not scroll bar should take over the leading corner in scroll pane.
     * Exact corner taken by the scroll bar depends on the scroll bar orientation.
     * This setting is set to {@code false} by default to replicate common Swing scroll panes behavior.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean leading;

    /**
     * Whether or not scroll bar should take over the trailing corner in scroll pane.
     * Exact corner taken by the scroll bar depends on the scroll bar orientation.
     * This setting is set to {@code false} by default to replicate common Swing scroll panes behavior.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean trailing;

    /**
     * Whether or not scroll bar should hover above the scroll pane content instead of taking extra space.
     * This setting is set to {@code false} by default to replicate common Swing scroll panes behavior.
     * *
     * It is important to know that hovering non-opaque scroll bar also forces underlying components to be non-opaque,
     * otherwise you will encounter many repainting issues with the content and scroll bar itself.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean hovering;

    /**
     * Whether or not scroll bar should be counted in scroll pane preferred size when it is {@link #hovering}.
     * It could be useful to receive a better scroll pane preferred size to avoid scroll bar obstructing any content.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean extending;

    /**
     * Constructs new {@link ScrollBarSettings} with default settings.
     */
    public ScrollBarSettings ()
    {
        this ( null, null, null, null );
    }

    /**
     * Constructs new {@link ScrollBarSettings} with the specified settings.
     *
     * @param leading   whether or not scroll bar should take over the leading corner in scroll pane
     * @param trailing  whether or not scroll bar should take over the trailing corner in scroll pane
     * @param hovering  whether scroll bar should hover above the scroll pane content instead of taking extra space
     * @param extending whether or not scroll bar should be counted in scroll pane preferred size
     */
    public ScrollBarSettings ( @Nullable final Boolean leading, @Nullable final Boolean trailing,
                               @Nullable final Boolean hovering, @Nullable final Boolean extending )
    {
        this.leading = leading;
        this.trailing = trailing;
        this.hovering = hovering;
        this.extending = extending;
    }

    /**
     * Returns whether or not scroll bar should take over the leading corner in scroll pane.
     *
     * @return {@code true} if scroll bar should take over the leading corner in scroll pane, {@code false} otherwise
     */
    public boolean isLeading ()
    {
        return leading != null && leading;
    }

    /**
     * Returns whether or not scroll bar should take over the trailing corner in scroll pane.
     *
     * @return {@code true} if scroll bar should take over the trailing corner in scroll pane, {@code false} otherwise
     */
    public boolean isTrailing ()
    {
        return trailing != null && trailing;
    }

    /**
     * Returns whether or not scroll bar should hover above the scroll pane content instead of taking extra space.
     *
     * @return {@code true} if scroll bar should hover above the scroll pane content instead of taking extra space, {@code false} otherwise
     */
    public boolean isHovering ()
    {
        return hovering != null && hovering;
    }

    /**
     * Returns whether or not scroll bar should be counted in scroll pane preferred size.
     *
     * @return {@code true} if scroll bar should be counted in scroll pane preferred size, {@code false} otherwise
     */
    public boolean isExtending ()
    {
        return extending != null && extending;
    }
}