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

import com.alee.api.jdk.Objects;
import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.managers.language.LM;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * Default {@link javax.swing.table.TableCellRenderer} implementation for {@link WebFileTable}.
 *
 * @param <V> {@link File} type
 * @param <C> {@link WebFileTable} type
 * @param <P> {@link FileTableCellParameters} type
 * @author Mikle Garin
 * @see FileTableCellParameters
 */
public class WebFileTableCellRenderer<V extends File, C extends WebFileTable, P extends FileTableCellParameters<V, C>>
        extends WebTableCellRenderer<V, C, P>
{
    @Override
    protected int horizontalAlignmentForValue ( final P parameters )
    {
        final int alignment;
        if ( Objects.equals ( parameters.columnId (), WebFileTableModel.NUMBER_COLUMN, WebFileTableModel.SIZE_COLUMN ) )
        {
            alignment = SwingConstants.TRAILING;
        }
        else
        {
            alignment = SwingConstants.LEADING;
        }
        return alignment;
    }

    @Override
    protected Icon iconForValue ( final P parameters )
    {
        final Icon icon;
        if ( Objects.equals ( parameters.columnId (), WebFileTableModel.NAME_COLUMN ) )
        {
            icon = FileUtils.getFileIcon ( parameters.value () );
        }
        else
        {
            icon = null;
        }
        return icon;
    }

    @Override
    protected String textForValue ( final P parameters )
    {
        final String text;
        final boolean isFile = FileUtils.isFile ( parameters.value () );
        if ( Objects.equals ( parameters.columnId (), WebFileTableModel.NUMBER_COLUMN ) )
        {
            text = Integer.toString ( parameters.row () + 1 );
        }
        else if ( Objects.equals ( parameters.columnId (), WebFileTableModel.NAME_COLUMN ) )
        {
            text = FileUtils.getDisplayFileName ( parameters.value () );
        }
        else if ( Objects.equals ( parameters.columnId (), WebFileTableModel.SIZE_COLUMN ) )
        {
            text = isFile ? FileUtils.getDisplayFileSize ( parameters.value () ) : "";
        }
        else if ( Objects.equals ( parameters.columnId (), WebFileTableModel.EXTENSION_COLUMN ) )
        {
            text = isFile ? FileUtils.getFileExtPart ( parameters.value ().getName (), true ) : LM.get ( "weblaf.file.type.folder" );
        }
        else if ( Objects.equals ( parameters.columnId (), WebFileTableModel.CREATION_DATE_COLUMN ) )
        {
            text = FileUtils.getDisplayFileCreationDate ( parameters.value () );
        }
        else if ( Objects.equals ( parameters.columnId (), WebFileTableModel.MODIFICATION_DATE_COLUMN ) )
        {
            text = FileUtils.getDisplayFileModificationDate ( parameters.value () );
        }
        else
        {
            throw new IllegalArgumentException ( "Unknown column identifier: " + parameters.columnId () );
        }
        return text;
    }

    @Override
    protected P getRenderingParameters ( final WebFileTable table, final File value, final boolean isSelected,
                                         final boolean hasFocus, final int row, final int column )
    {
        return ( P ) new FileTableCellParameters ( table, value, row, column, isSelected, hasFocus );
    }

    /**
     * A subclass of {@link WebFileTableCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <V> {@link File} type
     * @param <C> {@link WebFileTable} type
     * @param <P> {@link FileTableCellParameters} type
     */
    public static final class UIResource<V extends File, C extends WebFileTable, P extends FileTableCellParameters<V, C>>
            extends WebFileTableCellRenderer<V, C, P> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebFileTableCellRenderer}.
         */
    }
}