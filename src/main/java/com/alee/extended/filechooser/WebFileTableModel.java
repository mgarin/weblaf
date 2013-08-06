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

import com.alee.managers.language.LanguageManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Data model for WebFileTable.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileTableModel extends AbstractTableModel implements WebFileTableColumns
{
    /**
     * List of displayed column ids.
     */
    private List<String> columns;

    /**
     * List of displayed files.
     */
    private List<File> files;

    /**
     * Constructs empty model with default displayed columns.
     */
    public WebFileTableModel ()
    {
        this ( new ArrayList<File> (), CollectionUtils.copy ( DEFAULT_COLUMNS ) );
    }

    /**
     * Constructs empty model with specified displayed columns.
     *
     * @param columns columns to display
     */
    public WebFileTableModel ( String... columns )
    {
        this ( new ArrayList<File> (), Arrays.asList ( columns ) );
    }

    /**
     * Constructs empty model with specified displayed columns.
     *
     * @param columns columns to display
     */
    public WebFileTableModel ( List<String> columns )
    {
        this ( new ArrayList<File> (), columns );
    }

    /**
     * Constructs model with specified displayed columns and files.
     *
     * @param files   files to display
     * @param columns columns to display
     */
    public WebFileTableModel ( Collection<File> files, String... columns )
    {
        this ( files, Arrays.asList ( columns ) );
    }

    /**
     * Constructs model with specified displayed columns and files.
     *
     * @param files   files to display
     * @param columns columns to display
     */
    public WebFileTableModel ( Collection<File> files, List<String> columns )
    {
        super ();
        this.columns = columns;
        this.files = new ArrayList<File> ( files );
    }

    /**
     * Sets displayed files.
     *
     * @param files files to display
     */
    public void setFiles ( Collection<File> files )
    {
        this.files.clear ();
        this.files.addAll ( files );
        fireTableDataChanged ();
    }

    /**
     * Adds displayed files.
     *
     * @param files files to display
     */
    public void addFiles ( Collection<File> files )
    {
        if ( files.size () > 0 )
        {
            int lastIndex = this.files.size ();
            this.files.addAll ( files );
            fireTableRowsInserted ( lastIndex, lastIndex + files.size () - 1 );
        }
    }

    /**
     * Returns index of row with the specified file.
     *
     * @param file file to find
     * @return index of row with the specified file
     */
    public int getFileRow ( File file )
    {
        return files.indexOf ( file );
    }

    /**
     * Returns file for the specified row.
     *
     * @param row row to process
     * @return file for the specified row
     */
    public File getFileAtRow ( int row )
    {
        return files.get ( row );
    }

    /**
     * Sets displayed column ids list.
     *
     * @param columns displayed column ids list
     */
    public void setColumns ( List<String> columns )
    {
        this.columns = columns;
        fireTableStructureChanged ();
    }

    /**
     * Returns column id at the specified index.
     *
     * @param column column index
     * @return column id at the specified index
     */
    public String getColumnId ( int column )
    {
        return columns.get ( column );
    }

    /**
     * Returns whether the specified column is displayed or not.
     *
     * @param column column to process
     * @return true if the specified column is displayed, false otherwise
     */
    public boolean isColumnDisplayed ( String column )
    {
        return columns.contains ( column );
    }

    /**
     * Returns index of the specified column.
     *
     * @param column column to process
     * @return index of the specified column
     */
    public int getColumnIndex ( String column )
    {
        return columns.indexOf ( column );
    }

    /**
     * {@inheritDoc}
     */
    public String getColumnName ( int column )
    {
        return LanguageManager.get ( columns.get ( column ) );
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount ()
    {
        return files.size ();
    }

    /**
     * {@inheritDoc}
     */
    public int getColumnCount ()
    {
        return columns.size ();
    }

    /**
     * {@inheritDoc}
     */
    public Object getValueAt ( int rowIndex, int columnIndex )
    {
        return files.get ( rowIndex );
    }

    /**
     * {@inheritDoc}
     */
    public void setValueAt ( Object aValue, int rowIndex, int columnIndex )
    {
        files.set ( rowIndex, ( File ) aValue );
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getColumnClass ( int columnIndex )
    {
        return File.class;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCellEditable ( int rowIndex, int columnIndex )
    {
        return getColumnId ( columnIndex ).equals ( NAME_COLUMN ) && FileUtils.isNameEditable ( getFileAtRow ( rowIndex ) );
    }
}