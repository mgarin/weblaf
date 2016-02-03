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

import com.alee.utils.CompareUtils;
import com.alee.utils.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * File name and type comparator.
 * This comparator is used by some file components by default.
 *
 * @author Mikle Garin
 */

public class FileComparator implements Comparator<File>, Serializable
{
    /**
     * Compares two files by their names and type.
     *
     * @param f1 first file to be compared
     * @param f2 second file to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
     */
    @Override
    public int compare ( final File f1, final File f2 )
    {
        if ( FileUtils.isDirectory ( f1 ) && !FileUtils.isDirectory ( f2 ) )
        {
            return -1;
        }
        else if ( !FileUtils.isDirectory ( f1 ) && FileUtils.isDirectory ( f2 ) )
        {
            return 1;
        }
        else if ( FileUtils.isHidden ( f1 ) && !FileUtils.isHidden ( f2 ) )
        {
            return -1;
        }
        else if ( !FileUtils.isHidden ( f1 ) && FileUtils.isHidden ( f2 ) )
        {
            return 1;
        }
        else
        {
            return CompareUtils.compareNames ( f1.getName (), f2.getName () );
        }
    }
}