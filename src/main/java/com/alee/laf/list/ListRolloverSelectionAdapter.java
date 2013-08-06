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

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * This adapter allows you to install (and uninstall if needed) select-on-rollover behavior into any JList without any additional coding.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class ListRolloverSelectionAdapter implements MouseMotionListener
{
    /**
     * List into which this adapter is installed.
     */
    private JList list;

    /**
     * Constructs rollover selection adapter for the specified list.
     *
     * @param list list into which this adapter is installed
     */
    public ListRolloverSelectionAdapter ( JList list )
    {
        super ();
        this.list = list;
    }

    /**
     * Performs selection change on rollover.
     *
     * @param e mouse event
     */
    public void mouseMoved ( MouseEvent e )
    {
        // Disabled lists aren't affected
        if ( list.isEnabled () )
        {
            // Compute cell under point
            int index = list.locationToIndex ( e.getPoint () );
            if ( index != list.getSelectedIndex () )
            {
                // Change selection
                list.setSelectedIndex ( index );
            }
        }
    }

    /**
     * Ignored mouse event.
     *
     * @param e mouse event
     */
    public void mouseDragged ( MouseEvent e )
    {
        //
    }

    /**
     * Installs rollover selection adapter into list and ensures that it is the only one installed.
     *
     * @param list list to modify
     * @return installed rollover selection adapter
     */
    public static ListRolloverSelectionAdapter install ( JList list )
    {
        // Uninstall old adapters first
        uninstall ( list );

        // Add new adapter
        ListRolloverSelectionAdapter adapter = new ListRolloverSelectionAdapter ( list );
        list.addMouseMotionListener ( adapter );
        return adapter;
    }

    /**
     * Uninstalls all rollover selection adapters from the specified list.
     *
     * @param list list to modify
     */
    public static void uninstall ( JList list )
    {
        for ( MouseMotionListener listener : list.getMouseMotionListeners () )
        {
            if ( listener instanceof ListRolloverSelectionAdapter )
            {
                list.removeMouseMotionListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified list has any rollover selection adapters installed or not.
     *
     * @param list list to process
     * @return true if the specified list has any rollover selection adapters installed, false otherwise
     */
    public static boolean isInstalled ( JList list )
    {
        for ( MouseMotionListener listener : list.getMouseMotionListeners () )
        {
            if ( listener instanceof ListRolloverSelectionAdapter )
            {
                return true;
            }
        }
        return false;
    }
}