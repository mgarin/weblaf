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

package com.alee.extended.filechooser;

import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.managers.language.LM;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * File table cell renderer.
 *
 * @author Mikle Garin
 */

public class WebFileTableCellRenderer extends WebTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column );

        final File file = ( File ) value;
        final String columnId = ( String ) table.getColumnModel ().getColumn ( column ).getIdentifier ();
        final boolean isFile = FileUtils.isFile ( file );
        if ( columnId.equals ( WebFileTableModel.NUMBER_COLUMN ) )
        {
            setIcon ( null );
            setText ( "" + ( row + 1 ) );
            setHorizontalAlignment ( TRAILING );
        }
        else if ( columnId.equals ( WebFileTableModel.NAME_COLUMN ) )
        {
            setIcon ( FileUtils.getFileIcon ( file ) );
            setText ( FileUtils.getDisplayFileName ( file ) );
            setHorizontalAlignment ( LEADING );
        }
        else if ( columnId.equals ( WebFileTableModel.SIZE_COLUMN ) )
        {
            setIcon ( null );
            setText ( isFile ? FileUtils.getDisplayFileSize ( file ) : "" );
            setHorizontalAlignment ( LEADING );
        }
        else if ( columnId.equals ( WebFileTableModel.EXTENSION_COLUMN ) )
        {
            setIcon ( null );
            setText ( isFile ? FileUtils.getFileExtPart ( file.getName (), true ) : LM.get ( "weblaf.file.type.folder" ) );
            setHorizontalAlignment ( LEADING );
        }
        else if ( columnId.equals ( WebFileTableModel.CREATION_DATE_COLUMN ) )
        {
            setIcon ( null );
            setText ( FileUtils.getDisplayFileCreationDate ( file ) );
            setHorizontalAlignment ( LEADING );
        }
        else if ( columnId.equals ( WebFileTableModel.MODIFICATION_DATE_COLUMN ) )
        {
            setIcon ( null );
            setText ( FileUtils.getDisplayFileModificationDate ( file ) );
            setHorizontalAlignment ( LEADING );
        }

        return this;
    }
}