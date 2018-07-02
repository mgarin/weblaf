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

package com.alee.extended.dock.drag;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Custom {@link Transferable} for {@link com.alee.extended.dock.WebDockableFrame}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockableFrame
 * @see com.alee.extended.dock.WebDockablePane
 * @see com.alee.managers.drag.DragManager
 */
public class FrameTransferable implements Transferable
{
    /**
     * {@link com.alee.extended.dock.drag.FrameDragData} flavor.
     */
    public static final DataFlavor dataFlavor = new DataFlavor ( FrameDragData.class, "FrameDragData" );

    /**
     * Data flavors array.
     */
    public static final DataFlavor[] flavors = new DataFlavor[]{ dataFlavor };

    /**
     * {@link FrameDragData} instance.
     */
    private final FrameDragData data;

    /**
     * Constructs new {@link FrameTransferable} for the specified {@link FrameDragData}.
     *
     * @param data {@link FrameDragData}
     */
    public FrameTransferable ( final FrameDragData data )
    {
        this.data = data;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported ( final DataFlavor flavor )
    {
        return dataFlavor.equals ( flavor );
    }

    @Override
    public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException, IOException
    {
        if ( flavor == dataFlavor )
        {
            return data;
        }
        else
        {
            throw new UnsupportedFlavorException ( flavor );
        }
    }
}