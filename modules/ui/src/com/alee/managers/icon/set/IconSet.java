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

package com.alee.managers.icon.set;

import com.alee.api.Identifiable;
import com.alee.managers.icon.data.IconData;

import javax.swing.*;
import java.util.List;

/**
 * Icon set (collection) description.
 * Provides commong methods to add and retrieve icons from the set.
 *
 * @author Mikle Garin
 */

public interface IconSet extends Identifiable
{
    /**
     * Returns IDs of all icons in the set.
     *
     * @return IDs of all icons in the set
     */
    public List<String> getIds ();

    /**
     * Adds new icon into the set.
     *
     * @param icon new icon information
     */
    public void addIcon ( IconData icon );

    /**
     * Returns icon for the specified ID.
     *
     * @param id unique icon ID
     * @return icon for the specified ID
     */
    public Icon getIcon ( String id );
}