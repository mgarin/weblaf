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
 * @author Mikle Garin
 */

public class DocumentTransferable implements Transferable
{
    public static final DataFlavor flavor = new DataFlavor ( DocumentData.class, "DocumentData" );
    public static final DataFlavor[] flavors = new DataFlavor[]{ flavor };

    private final DocumentData document;

    public DocumentTransferable ( final DocumentData document )
    {
        super ();
        this.document = document;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported ( final DataFlavor flavor )
    {
        return DocumentTransferable.flavor.equals ( flavor );
    }

    @Override
    public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException, IOException
    {
        return document;
    }
}