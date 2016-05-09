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

package com.alee.extended.tab;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Custom Transferable for WebDocumentPane documents.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 * @see com.alee.extended.tab.DocumentData
 * @see com.alee.extended.tab.DocumentPaneTransferInfo
 */

public class DocumentTransferable implements Transferable
{
    /**
     * {@link com.alee.extended.tab.WebDocumentPane} drag operation information flavor.
     */
    public static final DataFlavor transferFlavor = new DataFlavor ( DocumentPaneTransferInfo.class, "DocumentPaneTransferInfo" );

    /**
     * {@link com.alee.extended.tab.DocumentData} flavor.
     */
    public static final DataFlavor dataFlavor = new DataFlavor ( DocumentData.class, "DocumentData" );

    /**
     * DocumentData data flavors array.
     */
    public static final DataFlavor[] flavors = new DataFlavor[]{ dataFlavor, transferFlavor };

    /**
     * Dragged DocumentData instance.
     */
    private final DocumentData data;

    /**
     * Document pane transfer info
     */
    private final DocumentPaneTransferInfo transferInfo;

    /**
     * Constructs new DocumentTransferable for the specified DocumentData.
     *
     * @param data         dragged {@link com.alee.extended.tab.DocumentData}
     * @param transferInfo {@link com.alee.extended.tab.DocumentPaneTransferInfo}
     */
    public DocumentTransferable ( final DocumentData data, final DocumentPaneTransferInfo transferInfo )
    {
        super ();
        this.data = data;
        this.transferInfo = transferInfo;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported ( final DataFlavor flavor )
    {
        return DocumentTransferable.dataFlavor.equals ( flavor );
    }

    @Override
    public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException, IOException
    {
        if ( flavor == transferFlavor )
        {
            return transferInfo;
        }
        else if ( flavor == dataFlavor )
        {
            return data;
        }
        else
        {
            return null;
        }
    }
}