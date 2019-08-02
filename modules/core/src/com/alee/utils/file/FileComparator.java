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

package com.alee.utils.file;

import com.alee.utils.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * {@link File} name and type comparator.
 * This comparator is used by some file-related components by default.
 *
 * @author Mikle Garin
 */
public class FileComparator implements Comparator<File>, Serializable
{
    /**
     * {@link File} name comparator.
     */
    protected final FileNameComparator fileNameComparator;

    /**
     * Constructs new {@link FileComparator}.
     */
    public FileComparator ()
    {
        this.fileNameComparator = new FileNameComparator ();
    }

    /**
     * Compares two files by their names and type.
     *
     * @param file1 first file to be compared
     * @param file2 second file to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
     */
    @Override
    public int compare ( final File file1, final File file2 )
    {
        if ( FileUtils.isDirectory ( file1 ) && !FileUtils.isDirectory ( file2 ) )
        {
            return -1;
        }
        else if ( !FileUtils.isDirectory ( file1 ) && FileUtils.isDirectory ( file2 ) )
        {
            return 1;
        }
        else if ( FileUtils.isHidden ( file1 ) && !FileUtils.isHidden ( file2 ) )
        {
            return -1;
        }
        else if ( !FileUtils.isHidden ( file1 ) && FileUtils.isHidden ( file2 ) )
        {
            return 1;
        }
        else
        {
            return fileNameComparator.compare ( file1.getName (), file2.getName () );
        }
    }
}