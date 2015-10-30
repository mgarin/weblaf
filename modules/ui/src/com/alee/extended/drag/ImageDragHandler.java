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

import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Mikle Garin
 */

public class ImageDragHandler extends TransferHandler
{
    private static final DataFlavor[] imageFlavor = new DataFlavor[]{ DataFlavor.imageFlavor };

    private BufferedImage image;
    private boolean defaultBehavior = true;

    public ImageDragHandler ( final JComponent component, final ImageIcon icon )
    {
        this ( component, ImageUtils.getBufferedImage ( icon ) );
    }

    public ImageDragHandler ( final JComponent component, final Image image )
    {
        this ( component, ImageUtils.getBufferedImage ( image ) );
    }

    public ImageDragHandler ( final JComponent component, final BufferedImage image )
    {
        super ();
        setImage ( image );

        component.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( isDefaultBehavior () && SwingUtilities.isLeftMouseButton ( e ) &&
                        component.isEnabled () )
                {
                    exportAsDrag ( component, e, getSourceActions ( component ) );
                }
            }
        } );
    }

    public BufferedImage getImage ()
    {
        return image;
    }

    public void setImage ( final BufferedImage image )
    {
        this.image = image;
    }

    public boolean isDefaultBehavior ()
    {
        return defaultBehavior;
    }

    public void setDefaultBehavior ( final boolean defaultBehavior )
    {
        this.defaultBehavior = defaultBehavior;
    }

    @Override
    public int getSourceActions ( final JComponent c )
    {
        return COPY;
    }

    @Override
    protected Transferable createTransferable ( final JComponent c )
    {
        return new Transferable ()
        {
            @Override
            public DataFlavor[] getTransferDataFlavors ()
            {
                return imageFlavor;
            }

            @Override
            public boolean isDataFlavorSupported ( final DataFlavor flavor )
            {
                return flavor.equals ( DataFlavor.imageFlavor );
            }

            @Override
            public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException, IOException
            {
                if ( isDataFlavorSupported ( flavor ) )
                {
                    return image;
                }
                else
                {
                    return null;
                }
            }
        };
    }
}