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

package com.alee.extended.list;

import com.alee.laf.list.WebListModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom list model for WebFileList component.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class FileListModel extends WebListModel<FileElement>
{
    /**
     * Constructs empty file list model.
     */
    public FileListModel ()
    {
        super ();
    }

    /**
     * Constructs file list model with files from directory under the specified path.
     *
     * @param directoryPath directory path
     */
    public FileListModel ( String directoryPath )
    {
        super ();
        setData ( directoryPath );
    }

    /**
     * Constructs file list model with files from the specified directory.
     *
     * @param directory directory
     */
    public FileListModel ( File directory )
    {
        super ();
        setData ( directory );
    }

    /**
     * Constructs file list model with the specified files.
     *
     * @param data files array
     */
    public FileListModel ( File[] data )
    {
        super ();
        setData ( data );
    }

    /**
     * Constructs file list model with the specified files.
     *
     * @param data files list
     */
    public FileListModel ( List<File> data )
    {
        super ();
        setData ( data );
    }

    /**
     * Clears list data and fills it with files from directory under the specified path.
     *
     * @param directoryPath directory path to process
     */
    public void setData ( String directoryPath )
    {
        setElements ( toElementsList ( getData ( new File ( directoryPath ) ) ) );
    }

    /**
     * Clears list data and fills it with files from the specified directory.
     *
     * @param directory directory to process
     */
    public void setData ( File directory )
    {
        setElements ( toElementsList ( getData ( directory ) ) );
    }

    /**
     * Clears list data and fills it with specified files.
     *
     * @param data files array
     */
    public void setData ( File[] data )
    {
        setElements ( toElementsList ( data ) );
    }

    /**
     * Clears list data and fills it with specified files.
     *
     * @param data files list
     */
    public void setData ( List<File> data )
    {
        setElements ( toElementsList ( data ) );
    }

    /**
     * Returns files under the specified directory.
     *
     * @param directory directory to process
     * @return files array
     */
    public static File[] getData ( File directory )
    {
        if ( directory != null )
        {
            return directory.listFiles ();
        }
        else
        {
            return new File[ 0 ];
        }
    }

    /**
     * Converts File array into FileElement list.
     *
     * @param data File array to process
     * @return FileElement list
     */
    public static List<FileElement> toElementsList ( File[] data )
    {
        List<FileElement> elements = new ArrayList<FileElement> ( data != null ? data.length : 0 );
        if ( data != null )
        {
            for ( File file : data )
            {
                elements.add ( new FileElement ( file ) );
            }
        }
        return elements;
    }

    /**
     * Converts File list into FileElement list.
     *
     * @param data File list to process
     * @return FileElement list
     */
    public static List<FileElement> toElementsList ( List<File> data )
    {
        List<FileElement> elements = new ArrayList<FileElement> ( data != null ? data.size () : 0 );
        if ( data != null )
        {
            for ( File file : data )
            {
                elements.add ( new FileElement ( file ) );
            }
        }
        return elements;
    }
}