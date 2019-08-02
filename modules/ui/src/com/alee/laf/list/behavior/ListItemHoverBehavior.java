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

package com.alee.laf.list.behavior;

import com.alee.extended.behavior.AbstractObjectHoverBehavior;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract behavior that provides hover events for {@link JList} items.
 * For a simple installation and uninstallation you can call {@link #install()} and {@link #uninstall()} methods.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class ListItemHoverBehavior<C extends JList> extends AbstractObjectHoverBehavior<C, Integer>
{
    /**
     * Constructs behavior for the specified {@link JList}.
     *
     * @param list list into which this behavior is installed
     */
    public ListItemHoverBehavior ( final C list )
    {
        this ( list, true );
    }

    /**
     * Constructs behavior for the specified {@link JList}.
     *
     * @param list        {@link JList} into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover events when list is enabled
     */
    public ListItemHoverBehavior ( final C list, final boolean enabledOnly )
    {
        super ( list, enabledOnly );
    }

    @Override
    protected Integer getObjectAt ( final Point location )
    {
        // Retrieving index for location
        int index = component.locationToIndex ( location );
        if ( index != getEmptyObject () )
        {
            // Ensure that cell under the index is within visible bounds
            final Rectangle rectangle = component.getCellBounds ( index, index );
            index = rectangle.contains ( location ) ? index : getEmptyObject ();
        }
        return index;
    }

    @Override
    protected Integer getEmptyObject ()
    {
        return -1;
    }
}