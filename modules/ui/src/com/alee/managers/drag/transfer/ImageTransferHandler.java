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

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.util.List;

/**
 * Custom {@link TransferHandler} that provides easy and convenient way to support images drag and drop in any Swing component.
 * It is based on {@link FilesTransferHandler} and only adds an additional supported flavor.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public class ImageTransferHandler extends FilesTransferHandler
{
    /**
     * Constructs new {@link com.alee.managers.drag.transfer.ImageTransferHandler}.
     *
     * @param dragEnabled whether drag operations are allowed
     * @param dropEnabled whether drop operations are allowed
     */
    public ImageTransferHandler ( final boolean dragEnabled, final boolean dropEnabled )
    {
        super ( dragEnabled, dropEnabled );
    }

    @Override
    protected Transferable createTransferable ( final JComponent component )
    {
        if ( isDragEnabled () )
        {
            final Image image = getDraggedImage ();
            return image != null ? new ImageTransferable ( image ) : null;
        }
        else
        {
            return null;
        }
    }

    /**
     * Informs that drag action has started and dragged image should be provided.
     * You can override this method to provide custom dragged image.
     *
     * @return dragged image
     */
    public Image getDraggedImage ()
    {
        return null;
    }

    @Override
    public boolean canImport ( final TransferHandler.TransferSupport support )
    {
        return isDropEnabled () && ImageTransferable.hasImagesList ( support.getTransferable () );
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport info )
    {
        return isDropEnabled () && info.isDrop () && imagesImported ( ImageTransferable.getImagesList ( info.getTransferable () ) );
    }

    /**
     * Informs that specified images were dropped.
     * You can override this method to perform desired actions.
     *
     * @param images list of dropped images
     * @return true if drop was successfully completed, false otherwise
     */
    protected boolean imagesImported ( final List<ImageIcon> images )
    {
        return true;
    }
}