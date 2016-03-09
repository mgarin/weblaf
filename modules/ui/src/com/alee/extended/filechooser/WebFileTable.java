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

import com.alee.global.GlobalConstants;
import com.alee.laf.table.WebTable;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.filefilter.AbstractFileFilter;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * File table component.
 * It can either display specified folder content or custom list of files.
 * <p>
 * Note that row indices are always specified in terms of the table model
 * and not in terms of the table view (which may change due to sorting).
 *
 * @author Mikle Garin
 */

public class WebFileTable extends WebTable implements FileTableColumns
{
    /**
     * todo 1. Provide additional constructors to provide initial data or model settings
     */

    /**
     * File filter.
     */
    private AbstractFileFilter fileFilter = GlobalConstants.NON_HIDDEN_ONLY_FILTER;

    /**
     * Displayed directory.
     */
    private File displayedDirectory;

    /**
     * Constructs empty WebFileTable.
     */
    public WebFileTable ()
    {
        this ( StyleId.filetable );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param files displayed files
     */
    public WebFileTable ( final List<File> files )
    {
        this ( StyleId.filetable, files );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param files   displayed files
     * @param columns displayed columns
     */
    public WebFileTable ( final List<File> files, final String... columns )
    {
        this ( StyleId.filetable, files, CollectionUtils.asList ( columns ) );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param files   displayed files
     * @param columns displayed columns
     */
    public WebFileTable ( final List<File> files, final List<String> columns )
    {
        this ( StyleId.filetable, files, columns );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param id style ID
     */
    public WebFileTable ( final StyleId id )
    {
        this ( id, new ArrayList<File> () );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param id    style ID
     * @param files displayed files
     */
    public WebFileTable ( final StyleId id, final List<File> files )
    {
        this ( id, files, CollectionUtils.copy ( FileTableColumns.DEFAULT_COLUMNS ) );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param id      style ID
     * @param files   displayed files
     * @param columns displayed columns
     */
    public WebFileTable ( final StyleId id, final List<File> files, final String... columns )
    {
        this ( id, files, CollectionUtils.asList ( columns ) );
    }

    /**
     * Constructs empty WebFileTable.
     *
     * @param id      style ID
     * @param files   displayed files
     * @param columns displayed columns
     */
    public WebFileTable ( final StyleId id, final List<File> files, final List<String> columns )
    {
        super ( id );

        // todo Move this into
        getColumnModel ().setColumnMargin ( 0 );

        // Installing default model
        setModel ( new WebFileTableModel ( files, columns ) );

        // File table renderer and editor
        setDefaultRenderer ( File.class, new WebFileTableCellRenderer () );
        setDefaultEditor ( File.class, new WebFileTableCellEditor () );
    }

    @Override
    public void setModel ( final TableModel model )
    {
        // Installing model
        super.setModel ( model );

        // Updating column sizes
        updateColumnSizes ();
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
    public void setFileFilter ( final AbstractFileFilter fileFilter )
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
    public void setDisplayedDirectory ( final File file )
    {
        // Stop cell editing
        stopCellEditing ();

        // Saving selection to restore later
        final List<File> oldSelection = getSelectedFiles ();

        // Update files data
        final File[] listedFiles = file != null ? FileUtils.listFiles ( file, ( FileFilter ) fileFilter ) : null;
        final File[] files = file != null ? FileUtils.sortFiles ( listedFiles ) : FileUtils.getDiskRoots ();
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
    public void setFiles ( final Collection<File> files )
    {
        displayedDirectory = null;
        getFileTableModel ().setFiles ( files );
    }

    /**
     * Adds displayed files.
     *
     * @param files files to display
     */
    public void addFiles ( final Collection<File> files )
    {
        getFileTableModel ().addFiles ( files );
    }

    /**
     * Sets displayed column ids.
     *
     * @param columns displayed column ids
     */
    public void setColumns ( final String... columns )
    {
        setColumns ( Arrays.asList ( columns ) );
    }

    /**
     * Sets displayed column ids list.
     *
     * @param columns displayed column ids list
     */
    public void setColumns ( final List<String> columns )
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
        return row != -1 ? getFileAtRow ( convertRowIndexToModel ( row ) ) : null;
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
        for ( final int row : selectedRows )
        {
            selectedFiles.add ( getFileAtRow ( convertRowIndexToModel ( row ) ) );
        }
        return selectedFiles;
    }

    /**
     * Returns file for the specified row.
     *
     * @param row row to process
     * @return file for the specified row
     */
    private File getFileAtRow ( final int row )
    {
        return getFileTableModel ().getFileAtRow ( row );
    }

    /**
     * Sets selected file.
     *
     * @param file file to select
     */
    public void setSelectedFile ( final File file )
    {
        setSelectedFile ( file, true );
    }

    /**
     * Sets selected file.
     *
     * @param file   file to select
     * @param scroll whether or not should scroll view to selected file
     */
    public void setSelectedFile ( final File file, final boolean scroll )
    {
        final int row = getFileTableModel ().getFileRow ( file );
        setSelectedRow ( row == -1 ? -1 : convertRowIndexToView ( row ), scroll );
    }

    /**
     * Sets selected files
     *
     * @param files files to select
     */
    public void setSelectedFiles ( final File[] files )
    {
        clearSelection ();
        for ( final File file : files )
        {
            final int row = getFileTableModel ().getFileRow ( file );
            if ( row != -1 )
            {
                addSelectedRow ( convertRowIndexToView ( row ) );
            }
        }
    }

    /**
     * Returns whether the specified column is displayed or not.
     *
     * @param column column to process
     * @return true if the specified column is displayed, false otherwise
     */
    public boolean isColumnDisplayed ( final String column )
    {
        return getFileTableModel ().isColumnDisplayed ( column );
    }

    /**
     * Sets selected files
     *
     * @param files files to select
     */
    public void setSelectedFiles ( final Collection<File> files )
    {
        clearSelection ();
        for ( final File file : files )
        {
            final int row = getFileTableModel ().getFileRow ( file );
            if ( row != -1 )
            {
                addSelectedRow ( convertRowIndexToView ( row ) );
            }
        }
    }

    /**
     * Starts editing selected file name.
     *
     * @return true if cell editing started, false otherwise
     */
    public boolean editSelectedFileName ()
    {
        final int row = getSelectedRow ();
        return row != -1 && editFileNameAt ( convertRowIndexToModel ( row ) );
    }

    /**
     * Starts editing specified file name.
     *
     * @param file file to edit
     * @return true if cell editing started, false otherwise
     */
    public boolean editFileName ( final File file )
    {
        return editFileNameAt ( getFileTableModel ().getFileRow ( file ) );
    }

    /**
     * Starts editing file name at the specified row.
     *
     * @param row file row
     * @return true if cell editing started, false otherwise
     */
    public boolean editFileNameAt ( final int row )
    {
        return row != 1 && isColumnDisplayed ( NAME_COLUMN ) &&
                editCellAt ( convertRowIndexToView ( row ), getColumnModel ().getColumnIndex ( NAME_COLUMN ) );
    }
}