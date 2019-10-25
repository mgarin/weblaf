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

package com.alee.managers.drag.transfer;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom transferable that represents {@link Image}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public class ImageTransferable implements Transferable
{
    /**
     * {@link ImageTransferable} data flavors.
     */
    public static final DataFlavor[] flavors = new DataFlavor[]{ DataFlavor.imageFlavor };

    /**
     * Transferred image.
     */
    @NotNull
    protected final Image image;

    /**
     * Constructs new {@link ImageTransferable} for image.
     *
     * @param image transferred image
     */
    public ImageTransferable ( @NotNull final Image image )
    {
        this.image = image;
    }

    @NotNull
    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported ( @NotNull final DataFlavor flavor )
    {
        return flavor.equals ( DataFlavor.imageFlavor );
    }

    @NotNull
    @Override
    public Object getTransferData ( @NotNull final DataFlavor flavor ) throws UnsupportedFlavorException
    {
        final Object transferData;
        if ( flavor.equals ( DataFlavor.imageFlavor ) )
        {
            transferData = image;
        }
        else
        {
            throw new UnsupportedFlavorException ( flavor );
        }
        return transferData;
    }

    /**
     * Returns whether or not transferable contains images.
     *
     * @param transferable transferable
     * @return true if transferable contains images, false otherwise
     */
    public static boolean hasImagesList ( @NotNull final Transferable transferable )
    {
        final DataFlavor[] df = transferable.getTransferDataFlavors ();
        return hasImageFlavor ( df ) || FilesTransferable.hasURIListFlavor ( df ) || FilesTransferable.hasFileListFlavor ( df );
    }

    /**
     * Returns list of imported images retrieved from the specified transferable.
     *
     * @param transferable transferable
     * @return list of imported images
     */
    @NotNull
    public static List<ImageIcon> getImagesList ( @NotNull final Transferable transferable )
    {
        final List<ImageIcon> images = new ArrayList<ImageIcon> ();

        // Check imported files
        final List<File> files = FilesTransferable.getFilesList ( transferable );
        if ( files != null )
        {
            for ( final File file : files )
            {
                images.add ( new ImageIcon ( file.getAbsolutePath () ) );
            }
        }

        // Check imported raw image
        final Image image = getImage ( transferable );
        if ( image != null )
        {
            images.add ( new ImageIcon ( image ) );
        }

        return images;
    }

    /**
     * Returns whether flavors array has image flavor or not.
     *
     * @param flavors flavors array
     * @return true if flavors array has image flavor, false otherwise
     */
    public static boolean hasImageFlavor ( @NotNull final DataFlavor[] flavors )
    {
        boolean hasImageFlavor = false;
        for ( final DataFlavor flavor : flavors )
        {
            if ( DataFlavor.imageFlavor.equals ( flavor ) )
            {
                hasImageFlavor = true;
                break;
            }
        }
        return hasImageFlavor;
    }

    /**
     * Returns imported image retrieved from the specified transferable.
     *
     * @param transferable transferable
     * @return imported image
     */
    @Nullable
    public static Image getImage ( @NotNull final Transferable transferable )
    {
        Image image = null;
        if ( transferable.isDataFlavorSupported ( DataFlavor.imageFlavor ) )
        {
            try
            {
                final Object data = transferable.getTransferData ( DataFlavor.imageFlavor );
                if ( data instanceof Image )
                {
                    image = ( Image ) data;
                }
            }
            catch ( final UnsupportedFlavorException ignored )
            {
                //
            }
            catch ( final IOException ignored )
            {
                //
            }
        }
        return image;
    }
}