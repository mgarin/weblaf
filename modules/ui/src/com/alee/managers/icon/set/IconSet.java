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
import com.alee.managers.icon.data.AbstractIconData;

import javax.swing.*;
import java.util.List;

/**
 * This interface is a base for any icon set (collection) implementation.
 * Its API has only a few methods to add and retrieve {@link Icon}s from the set.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see com.alee.managers.icon.IconManager
 */
public interface IconSet extends Identifiable
{
    /**
     * Returns identifiers of all {@link Icon}s in the set.
     *
     * @return identifiers of all {@link Icon}s in the set
     */
    public List<String> getIds ();

    /**
     * Adds new {@link Icon} referenced by specified {@link AbstractIconData} into the set.
     *
     * @param icon {@link AbstractIconData} of the {@link Icon} to add
     */
    public void addIcon ( AbstractIconData icon );

    /**
     * Returns {@link Icon} for the specified identifier.
     *
     * @param id unique {@link Icon} identifier
     * @return {@link Icon} for the specified identifier
     */
    public Icon getIcon ( String id );
}