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

import com.alee.api.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

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
     * {@link FrameDragData} flavor.
     */
    public static final DataFlavor dataFlavor = new DataFlavor ( FrameDragData.class, "FrameDragData" );

    /**
     * Data flavors array.
     */
    public static final DataFlavor[] flavors = new DataFlavor[]{ dataFlavor };

    /**
     * {@link FrameDragData} instance.
     */
    @NotNull
    protected final FrameDragData data;

    /**
     * Constructs new {@link FrameTransferable} for the specified {@link FrameDragData}.
     *
     * @param data {@link FrameDragData}
     */
    public FrameTransferable ( @NotNull final FrameDragData data )
    {
        this.data = data;
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
        return dataFlavor.equals ( flavor );
    }

    @NotNull
    @Override
    public Object getTransferData ( @NotNull final DataFlavor flavor ) throws UnsupportedFlavorException
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