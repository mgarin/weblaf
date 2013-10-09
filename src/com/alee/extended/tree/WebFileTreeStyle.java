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

package com.alee.extended.tree;

import com.alee.utils.compare.Filter;

/**
 * WebFileTree style class.
 *
 * @author Mikle Garin
 */

public final class WebFileTreeStyle
{
    /**
     * Whether allow files location search by dropping a file onto the tree or not.
     */
    public static boolean filesDropSearchEnabled = true;

    /**
     * Default tree file filter.
     * Set to null if you want to display all available files.
     */
    public static Filter<FileTreeNode> filter = new FileTreeNodeFilter ();
}