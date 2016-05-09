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

package com.alee.laf.list;

import com.alee.utils.swing.AbstractObjectHoverBehavior;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract behavior that provides hover events for {@link javax.swing.JList} items.
 * To install this behavior you will need to add it as {@link java.awt.event.MouseListener}, {@link java.awt.event.MouseMotionListener} and
 * {@link java.awt.event.ComponentListener} into the list, otherwise it will not function properly.
 * <p>
 * It uses mouse enter/exit/move events and component resized/moved/shown/hidden events to track hover index.
 * It might seem excessive, but simple move listener does not cover whole variety of possible cases when hover index can be changed.
 *
 * @author Mikle Garin
 */

public abstract class ListItemHoverBehavior extends AbstractObjectHoverBehavior<JList, Object>
{
    /**
     * Constructs behavior for the specified list.
     *
     * @param list list into which this behavior is installed
     */
    public ListItemHoverBehavior ( final JList list )
    {
        super ( list );
    }

    /**
     * Constructs behavior for the specified list.
     *
     * @param list        list into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover events when list is enabled
     */
    public ListItemHoverBehavior ( final JList list, final boolean enabledOnly )
    {
        super ( list, enabledOnly );
    }

    @Override
    protected Object getObjectAt ( final Point point )
    {
        final int index = component.locationToIndex ( point );
        if ( index == -1 )
        {
            return null;
        }
        else
        {
            final Rectangle rectangle = component.getCellBounds ( index, index );
            return rectangle.contains ( point ) ?  component.getModel ().getElementAt ( index ) : null;
        }
    }
}