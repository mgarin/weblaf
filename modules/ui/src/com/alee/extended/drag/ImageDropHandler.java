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

package com.alee.extended.drag;

import com.alee.utils.DragUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ImageDropHandler extends TransferHandler
{
    @Override
    public boolean canImport ( final TransferHandler.TransferSupport info )
    {
        return isDropEnabled ();
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport info )
    {
        return info.isDrop () && importData ( info.getTransferable () );
    }

    public boolean importData ( final Transferable t )
    {
        if ( isDropEnabled () )
        {
            final List<ImageIcon> images = new ArrayList<ImageIcon> ();

            // Check imported files
            final List<File> files = DragUtils.getImportedFiles ( t );
            if ( files != null )
            {
                for ( final File file : files )
                {
                    images.add ( new ImageIcon ( file.getAbsolutePath () ) );
                }
            }

            // Check imported raw image
            final Image image = DragUtils.getImportedImage ( t );
            if ( image != null )
            {
                images.add ( new ImageIcon ( image ) );
            }

            return isDropEnabled () && imagesImported ( images );
        }
        else
        {
            return false;
        }
    }

    protected boolean isDropEnabled ()
    {
        return true;
    }

    protected boolean imagesImported ( final List<ImageIcon> images )
    {
        return true;
    }
}