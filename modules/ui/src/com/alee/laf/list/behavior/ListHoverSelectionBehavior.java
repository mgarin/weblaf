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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.Behavior;
import com.alee.laf.list.WebList;
import com.alee.utils.swing.HoverListener;

/**
 * List behavior that automatically selects any hovered element.
 * It is using optimized {@link HoverListener} based on custom UI functionality.
 *
 * @author Mikle Garin
 */
public class ListHoverSelectionBehavior implements HoverListener<Integer>, Behavior
{
    /**
     * List using this behavior.
     */
    @NotNull
    protected final WebList list;

    /**
     * Constructs new list hover selection behavior.
     *
     * @param list list using this behavior
     */
    public ListHoverSelectionBehavior ( @NotNull final WebList list )
    {
        this.list = list;
    }

    @Override
    public void hoverChanged ( @Nullable final Integer previous, @Nullable final Integer current )
    {
        if ( current != null && current != -1 )
        {
            list.setSelectedIndex ( current );
        }
        else
        {
            list.clearSelection ();
        }
    }

    /**
     * Installs behavior into list and ensures that it is the only one installed.
     *
     * @param list list to modify
     * @return installed behavior
     */
    public static ListHoverSelectionBehavior install ( @NotNull final WebList list )
    {
        // Uninstalling old behavior first
        uninstall ( list );

        // Installing new behavior
        final ListHoverSelectionBehavior behavior = new ListHoverSelectionBehavior ( list );
        list.addHoverListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls all behaviors from the specified list.
     *
     * @param list list to modify
     */
    public static void uninstall ( @NotNull final WebList list )
    {
        for ( final HoverListener listener : list.getHoverListeners () )
        {
            if ( listener instanceof ListHoverSelectionBehavior )
            {
                list.removeHoverListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified list has any behaviors installed or not.
     *
     * @param list list to process
     * @return true if the specified list has any behaviors installed, false otherwise
     */
    public static boolean isInstalled ( @NotNull final WebList list )
    {
        boolean installed = false;
        for ( final HoverListener listener : list.getHoverListeners () )
        {
            if ( listener instanceof ListHoverSelectionBehavior )
            {
                installed = true;
                break;
            }
        }
        return installed;
    }
}