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

import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

/**
 * This enumeration represents possible file tree root types.
 * Also this enumeration provides root files for each of the listed root types.
 *
 * @author Mikle Garin
 */
public enum FileTreeRootType
{
    /**
     * Root offered by underlying operating system.
     */
    system
            {
                @Override
                public List<File> getRoots ()
                {
                    return CollectionUtils.asList ( FileSystemView.getFileSystemView ().getRoots () );
                }
            },

    /**
     * System hard drives as roots.
     */
    drives
            {
                @Override
                public List<File> getRoots ()
                {
                    return CollectionUtils.asList ( FileUtils.getDiskRoots () );
                }
            },

    /**
     * User home folder as root.
     */
    userHome
            {
                @Override
                public List<File> getRoots ()
                {
                    return CollectionUtils.asList ( FileUtils.getUserHome () );
                }
            };

    /**
     * Returns root files for current tree root type.
     *
     * @return root files for current tree root type
     */
    public abstract List<File> getRoots ();
}