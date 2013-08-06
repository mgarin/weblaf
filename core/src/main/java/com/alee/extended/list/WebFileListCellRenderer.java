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

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.GlobalConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.FileUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.file.FileDescription;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom list cell renderer for WebFileList component.
 * This renderer is also able to generate image thumbnails for image file elements.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileListCellRenderer extends WebListCellRenderer
{
    /**
     * Image thumbnails size.
     */
    private static final int THUMBNAIL_SIZE = 50;

    /**
     * File list in which this list cell renderer is used.
     */
    private WebFileList fileList;

    /**
     * Thumbnail icon label.
     */
    private WebLabel iconLabel;

    /**
     * File description panel.
     */
    private WebPanel descriptionPanel;

    /**
     * File name label.
     */
    private WebLabel nameLabel;

    /**
     * File size label.
     */
    private WebLabel sizeLabel;

    /**
     * File description label.
     */
    private WebLabel descriptionLabel;

    /**
     * Thumbnails queue lock object.
     */
    private static final Object thumbnailsQueueLock = new Object ();

    /**
     * Thumbnails generation queue.
     */
    private static List<FileElement> thumbnailsQueue = new ArrayList<FileElement> ();

    /**
     * Thumbnails generation thread.
     */
    private static Thread thumbnailsThread = null;

    /**
     * Constructs cell renderer for the specified file list.
     *
     * @param fileList file list in which this cell renderer is used
     */
    public WebFileListCellRenderer ( WebFileList fileList )
    {
        super ();

        this.fileList = fileList;

        iconLabel = new WebLabel ();
        iconLabel.setHorizontalAlignment ( JLabel.CENTER );
        iconLabel.setPreferredSize ( new Dimension ( 54, 54 ) );

        descriptionPanel = new WebPanel ();
        descriptionPanel.setLayout ( new VerticalFlowLayout ( VerticalFlowLayout.MIDDLE, 0, 0, true, false ) );
        descriptionPanel.setOpaque ( false );

        nameLabel = new WebLabel ( WebLabel.LEADING );
        nameLabel.setFont ( nameLabel.getFont ().deriveFont ( Font.PLAIN ) );
        nameLabel.setForeground ( Color.BLACK );
        nameLabel.setVerticalAlignment ( JLabel.CENTER );

        descriptionLabel = new WebLabel ( WebLabel.LEADING );
        descriptionLabel.setFont ( descriptionLabel.getFont ().deriveFont ( Font.PLAIN ) );
        descriptionLabel.setForeground ( Color.GRAY );

        sizeLabel = new WebLabel ( WebLabel.LEADING );
        sizeLabel.setFont ( sizeLabel.getFont ().deriveFont ( Font.PLAIN ) );
        sizeLabel.setForeground ( new Color ( 49, 77, 179 ) );

        fileList.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ENABLED_PROPERTY, new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateEnabledState ();
            }
        } );

        updateFilesView ();
    }

    /**
     * Updates renderer components structure.
     */
    public void updateFilesView ()
    {
        if ( fileList.getFileListViewType ().equals ( FileListViewType.tiles ) )
        {
            removeAll ();
            setBorder ( BorderFactory.createEmptyBorder ( 6, 6, 5, 7 ) );
            TableLayout layout = new TableLayout ( new double[][]{ { 54, TableLayout.FILL }, { TableLayout.PREFERRED } } );
            layout.setHGap ( 4 );
            setLayout ( layout );
            add ( iconLabel, "0,0" );
            add ( descriptionPanel, "1,0" );
            setPreferredSize ( new Dimension ( 220, 65 ) );
            nameLabel.setHorizontalAlignment ( JLabel.LEADING );
            nameLabel.setMargin ( 0 );
        }
        else
        {
            removeAll ();
            setBorder ( BorderFactory.createEmptyBorder ( 5, 5, 5, 5 ) );
            TableLayout layout = new TableLayout ( new double[][]{ { TableLayout.FILL, 54, TableLayout.FILL }, { 54, TableLayout.FILL } } );
            layout.setHGap ( 0 );
            layout.setVGap ( 4 );
            setLayout ( layout );
            add ( iconLabel, "1,0" );
            add ( descriptionPanel, "0,1,2,1" );
            setPreferredSize ( new Dimension ( 90, 90 ) );
            nameLabel.setHorizontalAlignment ( JLabel.CENTER );
            nameLabel.setMargin ( 0, 2, 0, 2 );
        }
        updateFixedCellSize ();
        updateEnabledState ();
    }

    /**
     * Updates fixed renderer sizes.
     * Simple optimization that is required to disable full list rendering at its first appearance.
     */
    public void updateFixedCellSize ()
    {
        if ( fileList.getFileListViewType ().equals ( FileListViewType.tiles ) )
        {
            fileList.setFixedCellWidth ( 220 );
            fileList.setFixedCellHeight ( 65 );
        }
        else
        {
            fileList.setFixedCellWidth ( 90 );
            fileList.setFixedCellHeight ( 90 );
        }
    }

    /**
     * Updates renderer components enabled state.
     */
    private void updateEnabledState ()
    {
        SwingUtils.setEnabledRecursively ( this, fileList.isEnabled () );
    }

    /**
     * Returns thumbnail icon label.
     *
     * @return thumbnail icon label
     */
    public JLabel getIconLabel ()
    {
        return iconLabel;
    }

    /**
     * Returns file description panel.
     *
     * @return file description panel
     */
    public JPanel getDescriptionPanel ()
    {
        return descriptionPanel;
    }

    /**
     * Returns file name label.
     *
     * @return file name label
     */
    public JLabel getNameLabel ()
    {
        return nameLabel;
    }

    /**
     * Returns file size label.
     *
     * @return file size label
     */
    public JLabel getSizeLabel ()
    {
        return sizeLabel;
    }

    /**
     * Returns file description label.
     *
     * @return file description label
     */
    public JLabel getDescriptionLabel ()
    {
        return descriptionLabel;
    }

    /**
     * Returns list cell renderer component.
     *
     * @param list         tree
     * @param value        cell value
     * @param index        cell index
     * @param isSelected   whether cell is selected or not
     * @param cellHasFocus whether cell has focus or not
     * @return cell renderer component
     */
    public Component getListCellRendererComponent ( JList list, Object value, final int index, boolean isSelected, boolean cellHasFocus )
    {
        super.getListCellRendererComponent ( list, "", index, isSelected, cellHasFocus );

        final FileElement element = ( FileElement ) value;
        final File file = element.getFile ();
        final String absolutePath = file.getAbsolutePath ();

        // Renderer icon
        String imageSize = null;

        if ( element.isThumbnailQueued () )
        {
            if ( element.getEnabledThumbnail () != null )
            {
                // Image thumbnail
                iconLabel.setIcon ( element.getEnabledThumbnail () );
                iconLabel.setDisabledIcon ( element.getDisabledThumbnail () );
                imageSize = element.getEnabledThumbnail ().getDescription ();
            }
            else
            {
                iconLabel.setIcon ( null );
                iconLabel.setDisabledIcon ( null );
            }
        }
        else
        {
            queueThumbnailLoad ( element );
            iconLabel.setIcon ( null );
            iconLabel.setDisabledIcon ( null );
        }

        // Settings description
        FileDescription fileDescription = FileUtils.getFileDescription ( file, imageSize );
        descriptionPanel.removeAll ();

        if ( fileList.getEditedCell () != index )
        {
            nameLabel.setText ( fileDescription.getName () );
            descriptionPanel.add ( nameLabel );

            if ( fileList.getFileListViewType ().equals ( FileListViewType.tiles ) )
            {
                descriptionLabel.setText ( fileDescription.getDescription () );
                descriptionPanel.add ( descriptionLabel );

                if ( fileDescription.getSize () != null )
                {
                    sizeLabel.setText ( fileDescription.getSize () );
                    descriptionPanel.add ( sizeLabel );
                }
            }
        }

        return this;
    }

    /**
     * Adds specified element into thumbnails queue.
     *
     * @param element element to add
     */
    private void queueThumbnailLoad ( FileElement element )
    {
        synchronized ( thumbnailsQueueLock )
        {
            element.setThumbnailQueued ( true );
            thumbnailsQueue.add ( element );
        }
        pushQueue ();
    }

    /**
     * Forces thumbnails generation thread to start in case it is not running yet.
     */
    private void pushQueue ()
    {
        if ( thumbnailsThread == null || !thumbnailsThread.isAlive () )
        {
            thumbnailsThread = new Thread ( new Runnable ()
            {
                public void run ()
                {
                    // Check whether we have more elements in queue or not
                    boolean available;
                    synchronized ( thumbnailsQueueLock )
                    {
                        available = thumbnailsQueue.size () > 0;
                    }

                    // Start processing the queue
                    FileElement element;
                    while ( available )
                    {
                        // Removing element from queue
                        synchronized ( thumbnailsQueueLock )
                        {
                            element = thumbnailsQueue.remove ( 0 );
                        }

                        // Retrieving image
                        final String absolutePath = element.getFile ().getAbsolutePath ();
                        final String ext = FileUtils.getFileExtPart ( element.getFile ().getName (), false ).toLowerCase ();
                        if ( fileList.isGenerateThumbnails () && GlobalConstants.IMAGE_FORMATS.contains ( ext ) )
                        {
                            final ImageIcon thumb = ImageUtils.createThumbnailIcon ( absolutePath, THUMBNAIL_SIZE );
                            if ( thumb != null )
                            {
                                element.setEnabledThumbnail ( thumb );
                                element.setDisabledThumbnail ( ImageUtils.createDisabledCopy ( thumb ) );
                            }
                            else
                            {
                                element.setEnabledThumbnail ( FileUtils.getStandartFileIcon ( element.getFile (), true, true ) );
                                element.setDisabledThumbnail ( FileUtils.getStandartFileIcon ( element.getFile (), true, false ) );
                            }
                        }
                        else
                        {
                            element.setEnabledThumbnail ( FileUtils.getStandartFileIcon ( element.getFile (), true, true ) );
                            element.setDisabledThumbnail ( FileUtils.getStandartFileIcon ( element.getFile (), true, false ) );
                        }

                        // todo Sometimes seems to call repaint when list is not visible (for now fixed inside method)
                        // Repainting cell with image
                        fileList.repaint ( element );

                        // Check whether we have more elements in queue or not
                        synchronized ( thumbnailsQueueLock )
                        {
                            available = thumbnailsQueue.size () > 0;
                        }
                    }
                }
            } );
            thumbnailsThread.start ();
        }
    }
}