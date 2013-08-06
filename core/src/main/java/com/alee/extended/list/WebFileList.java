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

package com.alee.extended.list;

import com.alee.laf.list.WebList;
import com.alee.laf.list.editor.ListCellEditor;
import com.alee.laf.scroll.WebScrollBarUI;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Special list component that simplifies files rendering.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileList extends WebList
{
    /**
     * Whether to generate image file thumbnails or not.
     * Thumbnails generation might slow down list rendering in some cases.
     */
    private boolean generateThumbnails = WebFileListStyle.generateThumbnails;

    /**
     * Preferred visible column count.
     */
    private int preferredColumnCount = WebFileListStyle.preferredColumnCount;

    /**
     * Preferred visible row count.
     */
    private int preferredRowCount = WebFileListStyle.preferredRowCount;

    /**
     * File view mode.
     */
    private FileListViewType fileListViewType = WebFileListStyle.fileListViewType;

    /**
     * File filter.
     */
    private FileFilter fileFilter = WebFileListStyle.fileFilter;

    /**
     * Displayed directory.
     */
    private File displayedDirectory = null;

    /**
     * Scroll pane with fixed preferred size that fits file list settings.
     */
    private WebScrollPane scrollView = null;

    /**
     * Constructs empty file list.
     */
    public WebFileList ()
    {
        super ( new FileListModel () );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from directory under the specified path.
     *
     * @param directoryPath directory path
     */
    public WebFileList ( String directoryPath )
    {
        super ( new FileListModel ( directoryPath ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from the specified directory.
     *
     * @param directory directory
     */
    public WebFileList ( File directory )
    {
        super ( new FileListModel ( directory ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param data files array
     */
    public WebFileList ( File[] data )
    {
        super ( new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param data files list
     */
    public WebFileList ( List<File> data )
    {
        super ( new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Initializes default file list settings.
     */
    protected void initializeDefaultSettings ()
    {
        // Standard settings
        setLayoutOrientation ( JList.HORIZONTAL_WRAP );
        setVisibleRowCount ( 0 );

        // Files list renderer
        setCellRenderer ( new WebFileListCellRenderer ( WebFileList.this ) );
    }

    /**
     * Returns specific for WebFileList renderer.
     * Be aware that this method might throw ClassCastException if renderer is altered by user.
     *
     * @return specific for WebFileList renderer
     * @throws ClassCastException
     */
    public WebFileListCellRenderer getWebFileListCellRenderer () throws ClassCastException
    {
        final ListCellRenderer cellRenderer = getCellRenderer ();
        return cellRenderer instanceof WebFileListCellRenderer ? ( WebFileListCellRenderer ) cellRenderer : null;
    }

    /**
     * Returns specific for WebFileList model.
     * Be aware that this method might throw ClassCastException if model is altered by user.
     *
     * @return specific for WebFileList model
     * @throws ClassCastException
     */
    public FileListModel getFileListModel () throws ClassCastException
    {
        return ( FileListModel ) getModel ();
    }

    /**
     * Returns default list cell editor for this list.
     *
     * @return default list cell editor for this list
     */
    protected ListCellEditor createDefaultCellEditor ()
    {
        return new WebFileListCellEditor ();
    }

    /**
     * Sets preferred visible column count.
     *
     * @param preferredColumnCount new preferred visible column count
     */
    public void setPreferredColumnCount ( int preferredColumnCount )
    {
        this.preferredColumnCount = preferredColumnCount;
    }

    /**
     * Returns preferred visible column count.
     *
     * @return preferred visible column count
     */
    public int getPreferredColumnCount ()
    {
        return preferredColumnCount;
    }

    /**
     * Returns preferred visible row count.
     *
     * @return preferred visible row count
     */
    public int getPreferredRowCount ()
    {
        return preferredRowCount;
    }

    /**
     * Sets preferred visible row count.
     *
     * @param preferredRowCount new preferred visible row count
     */
    public void setPreferredRowCount ( int preferredRowCount )
    {
        this.preferredRowCount = preferredRowCount;
    }

    /**
     * Returns whether to generate image file thumbnails or not.
     *
     * @return true if file list renderer should generate image file thumbnails, false otherwise
     */
    public boolean isGenerateThumbnails ()
    {
        return generateThumbnails;
    }

    /**
     * Sets whether to generate image file thumbnails or not.
     *
     * @param generateThumbnails whether to generate image file thumbnails or not
     */
    public void setGenerateThumbnails ( boolean generateThumbnails )
    {
        this.generateThumbnails = generateThumbnails;
    }

    /**
     * Returns file view mode.
     *
     * @return file view mode
     */
    public FileListViewType getFileListViewType ()
    {
        return fileListViewType;
    }

    /**
     * Sets file view mode.
     *
     * @param fileListViewType new file view mode
     */
    public void setFileListViewType ( FileListViewType fileListViewType )
    {
        this.fileListViewType = fileListViewType;
        getWebFileListCellRenderer ().updateFilesView ();
    }

    /**
     * Returns file filter.
     *
     * @return file filter
     */
    public FileFilter getFileFilter ()
    {
        return fileFilter;
    }

    /**
     * Sets file filter.
     *
     * @param fileFilter new file filter
     */
    public void setFileFilter ( FileFilter fileFilter )
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

        // Getting files and updating list model
        final File[] files = file != null ? FileUtils.sortFiles ( file.listFiles ( fileFilter ) ) : FileUtils.getDiskRoots ();
        getFileListModel ().setData ( files );

        // Restoring selection if its same folder
        if ( FileUtils.equals ( displayedDirectory, file ) )
        {
            setSelectedFiles ( oldSelection );
        }

        // Saving new displayed directory
        this.displayedDirectory = file;
    }

    /**
     * Returns list of selected files.
     *
     * @return list of selected files
     */
    public List<File> getSelectedFiles ()
    {
        final Object[] selectedValues = getSelectedValues ();
        final List<File> selectedFiles = new ArrayList<File> ( selectedValues.length );
        for ( Object value : selectedValues )
        {
            selectedFiles.add ( ( ( FileElement ) value ).getFile () );
        }
        return selectedFiles;
    }

    /**
     * Returns selected file.
     *
     * @return selected file
     */
    public File getSelectedFile ()
    {
        final Object selectedValue = getSelectedValue ();
        return selectedValue != null ? ( ( FileElement ) selectedValue ).getFile () : null;
    }

    /**
     * Selects specified file if it presents in the list.
     *
     * @param file file to select
     */
    public void setSelectedFile ( File file )
    {
        setSelectedFile ( file, true );
    }

    /**
     * Selects specified file if it presents in the list.
     *
     * @param file         file to select
     * @param shouldScroll whether to scroll to selected file or not
     */
    public void setSelectedFile ( File file, boolean shouldScroll )
    {
        final FileElement element = getFileListModel ().getElement ( file );
        if ( element != null )
        {
            setSelectedValue ( element, shouldScroll );
        }
        else
        {
            clearSelection ();
        }
    }

    /**
     * Selects specified files if they present in the list.
     *
     * @param files files to select
     */
    public void setSelectedFiles ( Collection<File> files )
    {
        final List<FileElement> elements = new ArrayList<FileElement> ( files.size () );
        for ( File file : files )
        {
            final FileElement element = getFileListModel ().getElement ( file );
            if ( element != null )
            {
                elements.add ( element );
            }
        }
        setSelectedValues ( elements );
    }

    /**
     * Returns scroll pane with fixed preferred size that fits file list settings.
     *
     * @return scroll pane with fixed preferred size that fits file list settings
     */
    public WebScrollPane getScrollView ()
    {
        if ( scrollView == null )
        {
            scrollView = createScrollView ();
        }
        return scrollView;
    }

    /**
     * Returns new scroll pane with fixed preferred size that fits file list settings.
     *
     * @return new scroll pane with fixed preferred size that fits file list settings
     */
    public WebScrollPane createScrollView ()
    {
        return new WebScrollPane ( WebFileList.this )
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
                Dimension oneCell;
                if ( getModel ().getSize () > 0 )
                {
                    oneCell = getCellBounds ( 0, 0 ).getSize ();
                }
                else
                {
                    WebFileListCellRenderer fileListCellRenderer = getWebFileListCellRenderer ();
                    if ( fileListCellRenderer != null )
                    {
                        oneCell = fileListCellRenderer.getPreferredSize ();
                    }
                    else
                    {
                        oneCell = new Dimension ( 400, 300 );
                    }
                }
                Insets bi = getInsets ();
                ps.width = oneCell.width * preferredColumnCount + bi.left + bi.right + WebScrollBarUI.LENGTH + 1;
                ps.height = oneCell.height * preferredRowCount + bi.top + bi.bottom + 1;
                return ps;
            }
        };
    }

    /**
     * Returns preferred size for this list.
     *
     * @return preferred size for this list
     */
    public Dimension getPreferredSize ()
    {
        Dimension ps = super.getPreferredSize ();
        if ( getModel ().getSize () > 0 )
        {
            Dimension oneCell = getCellBounds ( 0, 0 ).getSize ();
            ps.width = oneCell.width * preferredColumnCount;
        }
        return ps;
    }

    /**
     * Repaints cell with specified element.
     *
     * @param element element to process
     */
    public void repaint ( FileElement element )
    {
        repaint ( getFileListModel ().indexOf ( element ) );
    }
}