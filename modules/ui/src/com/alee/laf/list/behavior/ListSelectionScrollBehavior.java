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

import com.alee.extended.behavior.Behavior;
import com.alee.laf.list.WebList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * List behavior that automatically scrolls to any selected element.
 *
 * @author Mikle Garin
 */
public class ListSelectionScrollBehavior implements ListSelectionListener, Behavior
{
    /**
     * List using this behavior.
     */
    protected final WebList list;

    /**
     * Constructs new list selection scroll behavior.
     *
     * @param list list using this behavior
     */
    public ListSelectionScrollBehavior ( final WebList list )
    {
        super();
        this.list = list;
    }

    @Override
    public void valueChanged ( final ListSelectionEvent e )
    {
        if (  list.getSelectedIndex () != -1 )
        {
            final int index = list.getLeadSelectionIndex ();
            final Rectangle selection = list.getCellBounds ( index, index );
            if ( selection != null && !selection.intersects ( list.getVisibleRect () ) )
            {
                list.scrollRectToVisible ( selection );
            }
        }
    }

    /**
     * Installs behavior into list and ensures that it is the only one installed.
     *
     * @param list list to modify
     * @return installed behavior
     */
    public static ListSelectionScrollBehavior install ( final WebList list )
    {
        // Uninstalling old behavior first
        uninstall ( list );

        // Installing new behavior
        final ListSelectionScrollBehavior behavior = new ListSelectionScrollBehavior ( list );
        list.addListSelectionListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls all behaviors from the specified list.
     *
     * @param list list to modify
     */
    public static void uninstall ( final WebList list )
    {
        for ( final ListSelectionListener listener : list.getListSelectionListeners () )
        {
            if ( listener instanceof ListSelectionScrollBehavior )
            {
                list.removeListSelectionListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified list has any behaviors installed or not.
     *
     * @param list list to process
     * @return true if the specified list has any behaviors installed, false otherwise
     */
    public static boolean isInstalled ( final WebList list )
    {
        for ( final ListSelectionListener listener : list.getListSelectionListeners () )
        {
            if ( listener instanceof ListSelectionScrollBehavior )
            {
                return true;
            }
        }
        return false;
    }
}