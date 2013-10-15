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

import com.alee.extended.filefilter.AbstractFileFilter;
import com.alee.laf.table.WebTable;
import com.alee.utils.FileUtils;

import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * File table component.
 * It can either display specified folder content or custom list of files.
 *
 * @author Mikle Garin
 */

public class WebFileTable extends WebTable implements WebFileTableColumns
{
    /**
     * File filter.
     */
    private AbstractFileFilter fileFilter = WebFileTableStyle.fileFilter;

    /**
     * Displayed directory.
     */
    private File displayedDirectory;

    /**
     * Constructs empty WebFileTable.
     */
    public WebFileTable ()
    {
        super ();

        setVisibleRowCount ( 18 );
        setRowHeight ( 22 );
        setShowHorizontalLines ( true );
        setShowVerticalLines ( false );
        setGridColor ( new Color ( 237, 237, 237 ) );

        setModel ( new WebFileTableModel () );
        updateColumnSizes ();

        setDefaultRenderer ( File.class, new WebFileTableCellRenderer () );

        setEditable ( true );
        setDefaultEditor ( File.class, new WebFileTableCellEditor () );
    }

    /**
     * Updates column preferred sizes.
     */
    private void updateColumnSizes ()
    {
        for ( int i = 0; i < getColumnCount (); i++ )
        {
            final WebFileTableModel model = getFileTableModel ();
            final TableColumn column = getColumnModel ().getColumn ( i );
            column.setIdentifier ( model.getColumnId ( i ) );
            if ( model.getColumnId ( i ).equals ( NUMBER_COLUMN ) )
            {
                column.setPreferredWidth ( 30 );
            }
            else if ( model.getColumnId ( i ).equals ( NAME_COLUMN ) )
            {
                column.setPreferredWidth ( 340 );
            }
            else if ( model.getColumnId ( i ).equals ( SIZE_COLUMN ) || model.getColumnId ( i ).equals ( EXTENSION_COLUMN ) )
            {
                column.setPreferredWidth ( 90 );
            }
            else if ( model.getColumnId ( i ).equals ( CREATION_DATE_COLUMN ) ||
                    model.getColumnId ( i ).equals ( MODIFICATION_DATE_COLUMN ) )
            {
                column.setPreferredWidth ( 130 );
            }
        }
    }

    /**
     * Returns specific for WebFileTable model.
     *
     * @return specific for WebFileTable model
     */
    public WebFileTableModel getFileTableModel ()
    {
        return ( WebFileTableModel ) getModel ();
    }

    /**
     * Returns file filter.
     *
     * @return file filter
     */
    public AbstractFileFilter getFileFilter ()
    {
        return fileFilter;
    }

    /**
     * Sets file filter.
     *
     * @param fileFilter new file filter
     */
    public void setFileFilter ( AbstractFileFilter fileFilter )
    {
        this.fileFilter = fileFilter;
        reloadFiles ();
    }

    /**
     * Reloads files from displayed directory.
     */
    public void reloadFiles ()
    {
        setDisplayedDirectory ( getDisplayedDirectory () );
    }

    /**
     * Returns displayed directory.
     * Returned File might be null in case custom files list was set or no data is loaded into list yet.
     *
     * @return displayed directory
     */
    public File getDisplayedDirectory ()
    {
        return displayedDirectory;
    }

    /**
     * Sets displayed directory.
     * This will force list to load and display files from the specified directory.
     *
     * @param file new displayed directory
     */
    public void setDisplayedDirectory ( File file )
    {
        // Stop cell editing
        stopCellEditing ();

        // Saving selection to restore later
        final List<File> oldSelection = getSelectedFiles ();

        // Update files data
        final File[] files = file != null ? FileUtils.sortFiles ( FileUtils.listFiles ( file, fileFilter ) ) : FileUtils.getDiskRoots ();
        getFileTableModel ().setFiles ( Arrays.asList ( files ) );

        // Restoring selection if its same folder
        if ( FileUtils.equals ( displayedDirectory, file ) )
        {
            setSelectedFiles ( oldSelection );
        }

        // Saving new displayed directory
        displayedDirectory = file;
    }

    /**
     * Sets displayed files.
     *
     * @param files files to display
     */
    public void setFiles ( Collection<File> files )
    {
        displayedDirectory = null;
        getFileTableModel ().setFiles ( files );
    }

    /**
     * Adds displayed files.
     *
     * @param files files to display
     */
    public void addFiles ( Collection<File> files )
    {
        getFileTableModel ().addFiles ( files );
    }

    /**
     * Sets displayed column ids.
     *
     * @param columns displayed column ids
     */
    public void setColumns ( String... columns )
    {
        setColumns ( Arrays.asList ( columns ) );
    }

    /**
     * Sets displayed column ids list.
     *
     * @param columns displayed column ids list
     */
    public void setColumns ( List<String> columns )
    {
        getFileTableModel ().setColumns ( columns );
        updateColumnSizes ();
    }

    /**
     * Returns selected file.
     *
     * @return selected file
     */
    public File getSelectedFile ()
    {
        final int row = getSelectedRow ();
        return row != -1 ? getFileAtRow ( row ) : null;
    }

    /**
     * Returns list of selected files.
     *
     * @return list of selected files
     */
    public List<File> getSelectedFiles ()
    {
        final int[] selectedRows = getSelectedRows ();
        final List<File> selectedFiles = new ArrayList<File> ( selectedRows.length );
        for ( int row : selectedRows )
        {
            selectedFiles.add ( getFileAtRow ( row ) );
        }
        return selectedFiles;
    }

    /**
     * Returns file for the specified row.
     *
     * @param row row to process
     * @return file for the specified row
     */
    private File getFileAtRow ( int row )
    {
        return getFileTableModel ().getFileAtRow ( row );
    }

    /**
     * Sets selected file.
     *
     * @param file file to select
     */
    public void setSelectedFile ( File file )
    {
        setSelectedFile ( file, true );
    }

    /**
     * Sets selected file.
     *
     * @param file file to select
     */
    public void setSelectedFile ( File file, boolean shouldScroll )
    {
        setSelectedRow ( getFileTableModel ().getFileRow ( file ), shouldScroll );
    }

    /**
     * Sets selected files
     *
     * @param files files to select
     */
    public void setSelectedFiles ( File[] files )
    {
        clearSelection ();
        for ( File file : files )
        {
            addSelectedRow ( getFileTableModel ().getFileRow ( file ) );
        }
    }

    /**
     * Returns whether the specified column is displayed or not.
     *
     * @param column column to process
     * @return true if the specified column is displayed, false otherwise
     */
    public boolean isColumnDisplayed ( String column )
    {
        return getFileTableModel ().isColumnDisplayed ( column );
    }

    /**
     * Sets selected files
     *
     * @param files files to select
     */
    public void setSelectedFiles ( Collection<File> files )
    {
        clearSelection ();
        for ( File file : files )
        {
            addSelectedRow ( getFileTableModel ().getFileRow ( file ) );
        }
    }

    /**
     * Starts editing selected file name.
     *
     * @return true if cell editing started, false otherwise
     */
    public boolean editSelectedFileName ()
    {
        return editFileNameAt ( getSelectedRow () );
    }

    /**
     * Starts editing specified file name.
     *
     * @param file file to edit
     * @return true if cell editing started, false otherwise
     */
    public boolean editFileName ( File file )
    {
        return editFileNameAt ( getFileTableModel ().getFileRow ( file ) );
    }

    /**
     * Starts editing file name at the specified row.
     *
     * @param row file row
     * @return true if cell editing started, false otherwise
     */
    public boolean editFileNameAt ( int row )
    {
        return isColumnDisplayed ( NAME_COLUMN ) && editCellAt ( row, getColumnModel ().getColumnIndex ( NAME_COLUMN ) );
    }
}