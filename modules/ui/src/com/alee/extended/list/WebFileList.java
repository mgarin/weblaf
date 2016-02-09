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

import com.alee.global.GlobalConstants;
import com.alee.laf.list.WebList;
import com.alee.laf.list.editor.ListCellEditor;
import com.alee.managers.style.StyleId;
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
     * todo 1. Move preferred col/row count into WebList
     */

    /**
     * Whether to generate image file thumbnails or not.
     * Thumbnails generation might slow down list rendering in some cases.
     */
    protected boolean generateThumbnails;

    /**
     * Preferred visible column count.
     */
    protected int preferredColumnCount;

    /**
     * Preferred visible row count.
     */
    protected int preferredRowCount;

    /**
     * File view mode.
     */
    protected FileListViewType fileListViewType;

    /**
     * File filter.
     */
    protected FileFilter fileFilter = GlobalConstants.NON_HIDDEN_ONLY_FILTER;

    /**
     * Custom thumbnail provider.
     */
    protected FileThumbnailProvider thumbnailProvider;

    /**
     * Displayed directory.
     */
    protected File displayedDirectory = null;

    /**
     * Constructs empty file list.
     */
    public WebFileList ()
    {
        super ( StyleId.filelist, new FileListModel () );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from directory under the specified path.
     *
     * @param directoryPath directory path
     */
    public WebFileList ( final String directoryPath )
    {
        super ( StyleId.filelist, new FileListModel ( directoryPath ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from the specified directory.
     *
     * @param directory directory
     */
    public WebFileList ( final File directory )
    {
        super ( StyleId.filelist, new FileListModel ( directory ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param data files array
     */
    public WebFileList ( final File[] data )
    {
        super ( StyleId.filelist, new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param data files list
     */
    public WebFileList ( final List<File> data )
    {
        super ( StyleId.filelist, new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs empty file list.
     *
     * @param id style ID
     */
    public WebFileList ( final StyleId id )
    {
        super ( id, new FileListModel () );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from directory under the specified path.
     *
     * @param id            style ID
     * @param directoryPath directory path
     */
    public WebFileList ( final StyleId id, final String directoryPath )
    {
        super ( id, new FileListModel ( directoryPath ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with files from the specified directory.
     *
     * @param id        style ID
     * @param directory directory
     */
    public WebFileList ( final StyleId id, final File directory )
    {
        super ( id, new FileListModel ( directory ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param id   style ID
     * @param data files array
     */
    public WebFileList ( final StyleId id, final File[] data )
    {
        super ( id, new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Constructs file list with the specified files.
     *
     * @param id   style ID
     * @param data files list
     */
    public WebFileList ( final StyleId id, final List<File> data )
    {
        super ( id, new FileListModel ( data ) );
        initializeDefaultSettings ();
    }

    /**
     * Initializes default file list settings.
     */
    protected void initializeDefaultSettings ()
    {
        // This is necessary for proper elements wrapping
        // If it is set to specific number list will fix the columns amount
        setVisibleRowCount ( -1 );

        // Files list renderer
        setCellRenderer ( new WebFileListCellRenderer ( WebFileList.this ) );
    }

    /**
     * Returns specific for WebFileList renderer.
     * Be aware that this method might throw ClassCastException if renderer is altered by user.
     *
     * @return specific for WebFileList renderer
     * @throws java.lang.ClassCastException if wrong file list cell renderer type is set
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
     * @throws java.lang.ClassCastException if wrong file list model type is set
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
     * Returns preferred visible column count.
     *
     * @return preferred visible column count
     */
    public int getPreferredColumnCount ()
    {
        return preferredColumnCount;
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

        // Updating renderer view
        final WebFileListCellRenderer wr = getWebFileListCellRenderer ();
        if ( wr != null )
        {
            wr.updateFilesView ();
        }
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

    @Override
    public Dimension getPreferredScrollableViewportSize ()
    {
        final Insets bi = getInsets ();
        final Dimension oneCell = getPreferredCellSize ();
        final int width = oneCell.width * getPreferredColumnCount () + bi.left + bi.right;
        final int height = oneCell.height * getPreferredRowCount () + bi.top + bi.bottom;
        return new Dimension ( width, height );
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
        final Insets bi = getInsets ();
        ps.width = getPreferredCellSize ().width * getPreferredColumnCount () + bi.left + bi.right;
        return ps;
    }

    /**
     * Returns single cell preferred size.
     *
     * @return single cell preferred size
     */
    protected Dimension getPreferredCellSize ()
    {
        final Dimension ps = new Dimension ( getFixedCellWidth (), getFixedCellHeight () );
        if ( ps.width == -1 || ps.height == -1 )
        {
            Dimension cps = getPrototypeCellSize ();
            if ( cps == null )
            {
                if ( getModel ().getSize () > 0 )
                {
                    final Object v = getModel ().getElementAt ( 0 );
                    final ListCellRenderer renderer = getCellRenderer ();
                    final Component r = renderer.getListCellRendererComponent ( WebFileList.this, v, 0, false, false );
                    cps = r.getPreferredSize ();
                }
                else
                {
                    cps = new Dimension ( 0, 0 );
                }
            }
            if ( ps.width == -1 )
            {
                ps.width = cps.width;
            }
            if ( ps.height == -1 )
            {
                ps.height = cps.height;
            }
        }
        return ps;
    }

    /**
     * Returns cell size that will be used as prototype for all other cell sizes.
     *
     * @return cell size that will be used as prototype for all other cell sizes
     */
    protected Dimension getPrototypeCellSize ()
    {
        final FileListViewType vt = getFileListViewType ();
        return vt == FileListViewType.icons ? WebFileListCellRenderer.iconCellSize : WebFileListCellRenderer.tileCellSize;
    }
}