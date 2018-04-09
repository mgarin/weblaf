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
public interface DockableElement extends Mergeable, Identifiable, Serializable
{
    /**
     * Called upon this element addition to {@link com.alee.extended.dock.data.DockableContainer}.
     *
     * @param parent parent {@link com.alee.extended.dock.data.DockableContainer}
     */
    public void added ( DockableContainer parent );

    /**
     * Called upon this element removal from {@link com.alee.extended.dock.data.DockableContainer}.
     *
     * @param parent parent {@link com.alee.extended.dock.data.DockableContainer}
     */
    public void removed ( DockableContainer parent );

    /**
     * Returns parent {@link com.alee.extended.dock.data.DockableContainer}.
     *
     * @return parent {@link com.alee.extended.dock.data.DockableContainer}
     */
    public DockableContainer getParent ();

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
    public Dimension getSize ();

    /**
     * Sets element size.
     *
     * @param size element size
     */
    public void setSize ( Dimension size );

    /**
     * Returns actual element bounds on dockable pane.
     *
     * @return actual element bounds on dockable pane
     */
    public Rectangle getBounds ();

    /**
     * Sets actual element bounds on dockable pane.
     *
     * @param bounds actual element bounds on dockable pane
     */
    public void setBounds ( Rectangle bounds );

    /**
     * Returns whether or not this element is currently visible on the dockable pane.
     *
     * @param dockablePane dockable pane
     * @return true if this element is currently visible on the dockable pane, false otherwise
     */
    public boolean isVisible ( WebDockablePane dockablePane );

    /**
     * Layout this element and its children on the specified dockable pane.
     *
     * @param dockablePane    dockable pane to place element on
     * @param bounds          element bounds
     * @param resizeableAreas resizeable areas cache
     */
    public void layout ( WebDockablePane dockablePane, Rectangle bounds, List<ResizeData> resizeableAreas );

    /**
     * Returns minimum size of this element.
     *
     * @param dockablePane dockable pane to place element on
     * @return minimum size of this element
     */
    public Dimension getMinimumSize ( WebDockablePane dockablePane );

    /**
     * Performs element size validation.
     * If element size is lesser than returned by {@link #getMinimumSize(com.alee.extended.dock.WebDockablePane)} it will be adjusted.
     *
     * @param dockablePane dockable pane
     */
    public void validateSize ( WebDockablePane dockablePane );
}