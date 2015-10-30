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

package com.alee.utils.drag;

import com.alee.utils.DragUtils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Custom transferable that represents File flavors in a few different ways.
 * This is made to support files drop into system file managers like Windows Explorer, Mac OS X Finder and others.
 *
 * @author Mikle Garin
 */

public class FileTransferable implements Transferable
{
    /**
     * FileTransferable data flavors.
     * These are constant and will always be provided.
     */
    public static final DataFlavor[] flavors = new DataFlavor[]{ DataFlavor.javaFileListFlavor, DragUtils.getUriListDataFlavor () };

    /**
     * Transferred files.
     */
    protected final List<File> files;

    /**
     * Constructs new FileTransferable for a single file.
     *
     * @param file transferred file
     */
    public FileTransferable ( final File file )
    {
        this ( Arrays.asList ( file ) );
    }

    /**
     * Constructs new FileTransferable for a list of files.
     *
     * @param files transferred files list
     */
    public FileTransferable ( final List<File> files )
    {
        super ();
        this.files = files;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported ( final DataFlavor flavor )
    {
        for ( final DataFlavor dataFlavor : flavors )
        {
            if ( dataFlavor.equals ( flavor ) )
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException, IOException
    {
        if ( DataFlavor.javaFileListFlavor.equals ( flavor ) )
        {
            return files;
        }
        else if ( DragUtils.getUriListDataFlavor ().equals ( flavor ) )
        {
            return DragUtils.fileListToTextURIList ( files );
        }
        return null;
    }
}