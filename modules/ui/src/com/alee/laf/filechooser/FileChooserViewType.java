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

package com.alee.laf.filechooser;

/**
 * This enumerations represents different view types of {@link javax.swing.JFileChooser}.
 *
 * @author Mikle Garin
 */
public enum FileChooserViewType
{
    /**
     * Display list with file icon/thumbnail and its name.
     */
    icons,

    /**
     * Display list with file icon/thumbnail, its name, description and some additional information.
     */
    tiles,

    /**
     * Display table with detailed file information.
     */
    table;

    /**
     * Returns component index for the specified view type.
     *
     * @return component index for the specified view type
     */
    public int getComponentIndex ()
    {
        switch ( this )
        {
            case icons:
            case tiles:
                return 0;
            case table:
                return 1;
        }
        return -1;
    }
}