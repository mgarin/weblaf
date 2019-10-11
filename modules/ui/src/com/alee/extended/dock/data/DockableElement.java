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

package com.alee.extended.dock.data;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Mergeable;
import com.alee.extended.dock.WebDockablePane;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * Base interface for all structural data elements within {@link com.alee.extended.dock.WebDockablePane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */
public interface DockableElement extends Identifiable, Mergeable, Cloneable, Serializable
{
    @NotNull
    @Override
    public String getId ();

    /**
     * Called upon this element addition to {@link DockableContainer}.
     *
     * @param parent parent {@link DockableContainer} or {@code null} for root element
     */
    public void added ( @Nullable DockableContainer parent );

    /**
     * Returns parent {@link DockableContainer} or {@code null} for root element.
     *
     * @return parent {@link DockableContainer} or {@code null} for root element
     */
    @Nullable
    public DockableContainer getParent ();

    /**
     * Called upon this element removal from {@link DockableContainer}.
     *
     * @param parent parent {@link DockableContainer} or {@code null} for root element
     */
    public void removed ( @Nullable DockableContainer parent );

    /**
     * Returns whether this element is or contains {@link DockableContentElement}.
     *
     * @return true if this element is or contains {@link DockableContentElement}, false otherwise
     */
    public boolean isContent ();

    /**
     * Returns element size.
     *
     * @return element size
     */
    @NotNull
    public Dimension getSize ();

    /**
     * Sets element size.
     *
     * @param size element size
     */
    public void setSize ( @NotNull Dimension size );

    /**
     * Returns actual element bounds on dockable pane.
     *
     * @return actual element bounds on dockable pane
     */
    @Nullable
    public Rectangle getBounds ();

    /**
     * Sets actual element bounds on dockable pane.
     *
     * @param bounds actual element bounds on dockable pane
     */
    public void setBounds ( @NotNull Rectangle bounds );

    /**
     * Returns whether or not this element is currently visible on the dockable pane.
     *
     * @param dockablePane dockable pane
     * @return true if this element is currently visible on the dockable pane, false otherwise
     */
    public boolean isVisible ( @NotNull WebDockablePane dockablePane );

    /**
     * Layout this element and its children on the specified dockable pane.
     *
     * @param dockablePane    dockable pane to place element on
     * @param bounds          element bounds
     * @param resizeableAreas resizeable areas cache
     */
    public void layout ( @NotNull WebDockablePane dockablePane, @NotNull Rectangle bounds, @NotNull List<ResizeData> resizeableAreas );

    /**
     * Returns minimum size of this element.
     *
     * @param dockablePane dockable pane to place element on
     * @return minimum size of this element
     */
    @NotNull
    public Dimension getMinimumSize ( @NotNull WebDockablePane dockablePane );

    /**
     * Performs element size validation.
     * If element size is lesser than returned by {@link #getMinimumSize(com.alee.extended.dock.WebDockablePane)} it will be adjusted.
     *
     * @param dockablePane dockable pane
     */
    public void validateSize ( @NotNull WebDockablePane dockablePane );
}