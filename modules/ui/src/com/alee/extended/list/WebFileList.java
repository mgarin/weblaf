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
import com.alee.laf.scroll.WebScrollPane;
import com.alee.utils.FileUtils;
import com.alee.utils.file.FileThumbnailProvider;

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
 */

public class WebFileList extends WebList
{
    /**
     * Whether to generate image file thumbnails or not.
     * Thumbnails generation might slow down list rendering in some cases.
     */
    protected boolean generateThumbnails = WebFileListStyle.generateThumbnails;

    /**
     * Preferred visible column count.
     */
    protected int preferredColumnCount = WebFileListStyle.preferredColumnCount;

    /**
     * Preferred visible row count.
     */
    protected int preferredRowCount = WebFileListStyle.preferredRowCount;

    /**
     * File view mode.
     */
    protected FileListViewType fileListViewType = WebFileListStyle.fileListViewType;

    /**
     * File filter.
     */
    protected FileFilter fileFilter = WebFileListStyle.fileFilter;

    /**
     * Custom thumbnail provider.
     */
    protected FileThumbnailProvider thumbnailProvider;

    /**
     * Displayed directory.
     */
    protected File displayedDirectory = null;

    /**
     * Scroll pane with fixed preferred size that fits file list settings.
     */
    protected WebScrollPane scrollView = null;

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
    public WebFileList ( final String directoryPath )
    {
        super ( new FileListModel ( directoryPath ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from the specified directory.
     *
     * @param directory directory
     */
    public WebFileList ( final File directory )
    {
        super ( new FileListModel ( directory ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param data files array
     */
    public WebFileList ( final File[] data )
    {
        super ( new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param data files list
     */
    public WebFileList ( final List<File> data )
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
    @Override
    protected ListCellEditor createDefaultCellEditor ()
    {
        return new WebFileListCellEditor ();
    }

    /**
     * Sets preferred visible column count.
     *
     * @param preferredColumnCount new preferred visible column count
     */
    public void setPreferredColumnCount ( final int preferredColumnCount )
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
    public void setPreferredRowCount ( final int preferredRowCount )
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
    public void setGenerateThumbnails ( final boolean generateThumbnails )
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
    public void setFileListViewType ( final FileListViewType fileListViewType )
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
    public void setFileFilter ( final FileFilter fileFilter )
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
     * Returns custom thumbnail provider.
     *
     * @return custom thumbnail provider
     */
    public FileThumbnailProvider getThumbnailProvider ()
    {
        return thumbnailProvider;
    }

    /**
     * Sets custom thumbnail provider.
     *
     * @param provider custom thumbnail provider
     */
    public void setThumbnailProvider ( final FileThumbnailProvider provider )
    {
        final FileThumbnailProvider oldProvider = this.thumbnailProvider;
        this.thumbnailProvider = provider;

        // Aborting thumbnail loading for previously and newly accepted elements
        abortThumbnailsGeneration ( oldProvider );
        abortThumbnailsGeneration ( provider );

        // Forcing list repaint to queue new generators
        repaint ();
    }

    /**
     * Aborts thumbnail generators for all elements accepted by the specified provider.
     *
     * @param provider provider to abort generators accepted by
     */
    protected void abortThumbnailsGeneration ( final FileThumbnailProvider provider )
    {
        if ( provider != null )
        {
            for ( final FileElement element : getFileListModel ().getElements () )
            {
                if ( provider.accept ( element.getFile () ) )
                {
                    ThumbnailGenerator.abortThumbnailLoad ( element );
                }
            }
        }
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
        for ( final Object value : selectedValues )
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
    public void setSelectedFile ( final File file )
    {
        setSelectedFile ( file, true );
    }

    /**
     * Selects specified file if it presents in the list.
     *
     * @param file         file to select
     * @param shouldScroll whether to scroll to selected file or not
     */
    public void setSelectedFile ( final File file, final boolean shouldScroll )
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
    public void setSelectedFiles ( final Collection<File> files )
    {
        final List<FileElement> elements = new ArrayList<FileElement> ( files.size () );
        for ( final File file : files )
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
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                final int fcw = getFixedCellWidth ();
                final int fch = getFixedCellHeight ();
                final Dimension oneCell;
                if ( fcw != -1 && fch != -1 )
                {
                    oneCell = new Dimension ( fcw, fch );
                }
                else
                {
                    if ( getModel ().getSize () > 0 )
                    {
                        oneCell = getCellBounds ( 0, 0 ).getSize ();
                    }
                    else
                    {
                        final WebFileListCellRenderer fileListCellRenderer = getWebFileListCellRenderer ();
                        if ( fileListCellRenderer != null )
                        {
                            oneCell = fileListCellRenderer.getPreferredSize ();
                        }
                        else
                        {
                            oneCell = new Dimension ( 90, 90 );
                        }
                    }
                    if ( fcw != -1 )
                    {
                        oneCell.width = fcw;
                    }
                    else if ( fch != -1 )
                    {
                        oneCell.width = fcw;
                    }
                }
                final Insets bi = getInsets ();
                final JScrollBar vsb = getVerticalScrollBar ();
                final int sbw = vsb != null && vsb.isShowing () ? vsb.getPreferredSize ().width : 0;
                ps.width = oneCell.width * preferredColumnCount + bi.left + bi.right + sbw + 1;
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
    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        if ( getModel ().getSize () > 0 )
        {
            final Dimension oneCell = getCellBounds ( 0, 0 ).getSize ();
            ps.width = oneCell.width * preferredColumnCount;
        }
        return ps;
    }
}