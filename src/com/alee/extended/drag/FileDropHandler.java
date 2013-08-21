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
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

/**
 * User: mgarin Date: 07.10.11 Time: 14:37
 */

public class FileDropHandler extends TransferHandler
{
    @Override
    public boolean canImport ( TransferHandler.TransferSupport info )
    {
        return isDropEnabled ();
    }

    @Override
    public boolean importData ( TransferHandler.TransferSupport info )
    {
        return info.isDrop () && importData ( info.getTransferable () );
    }

    public boolean importData ( Transferable t )
    {
        return isDropEnabled () && filesImported ( DragUtils.getImportedFiles ( t ) );
    }

    protected boolean isDropEnabled ()
    {
        return true;
    }

    protected boolean filesImported ( List<File> files )
    {
        return true;
    }
}
