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

import com.alee.utils.file.FileComparator;

import java.util.Comparator;

/**
 * {@link FileTreeNode}s comparator based on {@link FileComparator}.
 *
 * @author Mikle Garin
 * @see FileComparator
 */

public class FileTreeNodeComparator implements Comparator<FileTreeNode>
{
    /**
     * Actual comparator.
     */
    protected FileComparator fileComparator = new FileComparator ();

    @Override
    public int compare ( final FileTreeNode o1, final FileTreeNode o2 )
    {
        return fileComparator.compare ( o1.getFile (), o2.getFile () );
    }
}