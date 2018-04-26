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

import com.alee.api.data.Orientation;

/**
 * Base interface for all container elements within {@link com.alee.extended.dock.WebDockablePane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */
public interface DockableContainer extends DockableElement
{
    /**
     * Returns container orientation.
     *
     * @return container orientation
     */
    public Orientation getOrientation ();

    /**
     * Sets container orientation.
     *
     * @param orientation container orientation
     */
    public void setOrientation ( Orientation orientation );

    /**
     * Returns child elements count.
     *
     * @return child elements count
     */
    public int getElementCount ();

    /**
     * Returns child element with the specified ID or {@code null}.
     *
     * @param id element ID
     * @return child element with the specified ID or {@code null}
     */
    public <E extends DockableElement> E get ( String id );

    /**
     * Returns whether or not structure contains child element with the specified ID.
     *
     * @param id element ID
     * @return true if structure contains child element with the specified ID, false otherwise
     */
    public boolean contains ( String id );

    /**
     * Returns child element index in this container.
     *
     * @param element element to retrieve index for
     * @return child element index in this container
     */
    public int indexOf ( DockableElement element );

    /**
     * Returns child element at the specified index.
     *
     * @param index child element index
     * @return child element at the specified index
     */
    public DockableElement get ( int index );

    /**
     * Adds new child element at the specified index.
     *
     * @param index   index to add at
     * @param element new child element
     */
    public void add ( int index, DockableElement element );

    /**
     * Removes child element from the list.
     *
     * @param element child element to remove
     */
    public void remove ( DockableElement element );
}