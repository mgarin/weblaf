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

import com.alee.extended.dock.WebDockableFrame;

import javax.swing.*;
import java.awt.datatransfer.Transferable;

/**
 * Custom {@link TransferHandler} for {@link WebDockableFrame}.
 *
 * @author Mikle Garin
 */
public class DockableFrameTransferHandler extends TransferHandler
{
    @Override
    public int getSourceActions ( final JComponent c )
    {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable ( final JComponent c )
    {
        final WebDockableFrame frame = ( WebDockableFrame ) c;
        final FrameDragData data = new FrameDragData ( frame.getId () );
        return new FrameTransferable ( data );
    }
}