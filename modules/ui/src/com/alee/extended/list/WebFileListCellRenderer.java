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

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.utils.FileUtils;
import com.alee.utils.file.FileDescription;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Custom list cell renderer for WebFileList component.
 * This renderer is also able to generate image thumbnails for image file elements.
 *
 * @author Mikle Garin
 */

public class WebFileListCellRenderer extends WebListCellRenderer
{
    /**
     * Constant cell sizes.
     */
    public static final Dimension tileCellSize = new Dimension ( 220, 65 );
    public static final Dimension iconCellSize = new Dimension ( 90, 90 );

    /**
     * Image thumbnails size.
     */
    public static final int thumbSize = 50;

    /**
     * Image side length.
     */
    public static final int imageSide = 54;

    /**
     * Gap between renderer elements.
     */
    public static final int gap = 4;

    /**
     * File list in which this list cell renderer is used.
     */
    protected WebFileList fileList;

    /**
     * Thumbnail icon label.
     */
    protected WebLabel iconLabel;

    /**
     * File name label.
     */
    protected WebLabel nameLabel;

    /**
     * File size label.
     */
    protected WebLabel sizeLabel;

    /**
     * File description label.
     */
    protected WebLabel descriptionLabel;

    /**
     * Constructs cell renderer for the specified file list.
     *
     * @param fileList file list in which this cell renderer is used
     */
    public WebFileListCellRenderer ( final WebFileList fileList )
    {
        super ();

        this.fileList = fileList;

        iconLabel = new WebLabel ();
        iconLabel.setHorizontalAlignment ( JLabel.CENTER );
        iconLabel.setPreferredSize ( new Dimension ( imageSide, imageSide ) );

        final StyleId nameStyle = ChildStyleId.of ( "name" ).at ( fileList );
        nameLabel = new WebLabel ( nameStyle );
        nameLabel.setFont ( nameLabel.getFont ().deriveFont ( Font.PLAIN ) );
        nameLabel.setVerticalAlignment ( JLabel.CENTER );

        final StyleId descriptionStyle = ChildStyleId.of ( "description" ).at ( fileList );
        descriptionLabel = new WebLabel ( descriptionStyle, WebLabel.LEADING );
        descriptionLabel.setFont ( descriptionLabel.getFont ().deriveFont ( Font.PLAIN ) );

        final StyleId sizeStyle = ChildStyleId.of ( "size" ).at ( fileList );
        sizeLabel = new WebLabel ( sizeStyle, WebLabel.LEADING );
        sizeLabel.setFont ( sizeLabel.getFont ().deriveFont ( Font.PLAIN ) );

        setLayout ( new FileCellLayout () );
        add ( iconLabel );
        add ( nameLabel );
        add ( descriptionLabel );
        add ( sizeLabel );

        fileList.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final boolean enabled = fileList.isEnabled ();
                iconLabel.setEnabled ( enabled );
                nameLabel.setEnabled ( enabled );
                descriptionLabel.setEnabled ( enabled );
                sizeLabel.setEnabled ( enabled );
            }
        } );

        fileList.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final ComponentOrientation orientation = fileList.getComponentOrientation ();
                nameLabel.setComponentOrientation ( orientation );
                descriptionLabel.setComponentOrientation ( orientation );
                sizeLabel.setComponentOrientation ( orientation );
            }
        } );

        updateFilesView ();
    }

    /**
     * Returns description bounds for list cell.
     *
     * @return description bounds for list cell
     */
    public Rectangle getDescriptionBounds ()
    {
        return ( ( FileCellLayout ) getLayout () ).getDescriptionBounds ();
    }

    /**
     * Updates renderer elements view.
     */
    public void updateFilesView ()
    {
        if ( isTilesView () )
        {
            nameLabel.setHorizontalAlignment ( JLabel.LEADING );
            fileList.setFixedCellWidth ( tileCellSize.width );
            fileList.setFixedCellHeight ( tileCellSize.height );
        }
        else
        {
            nameLabel.setHorizontalAlignment ( JLabel.CENTER );
            fileList.setFixedCellWidth ( iconCellSize.width );
            fileList.setFixedCellHeight ( iconCellSize.height );
        }
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

    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                    final boolean cellHasFocus )
    {
        super.getListCellRendererComponent ( list, "", index, isSelected, cellHasFocus );

        final FileElement element = ( FileElement ) value;
        final File file = element.getFile ();

        // Renderer icon
        String imageSize = null;
        if ( iconLabel.isEnabled () )
        {
            // Thumbnail loading
            ThumbnailGenerator.queueThumbnailLoad ( fileList, element, false );

            // Image thumbnail
            final ImageIcon thumbnail = element.getEnabledThumbnail ();
            iconLabel.setIcon ( thumbnail );

            // Image description
            if ( thumbnail != null )
            {
                imageSize = thumbnail.getDescription ();
            }
        }
        else
        {
            // Disabled thumbnail loading
            ThumbnailGenerator.queueThumbnailLoad ( fileList, element, true );

            // Image disabled thumbnail
            iconLabel.setDisabledIcon ( element.getDisabledThumbnail () );
        }

        // Updating file description elements
        if ( fileList.getEditedCell () != index )
        {
            // Settings description
            final FileDescription fileDescription = FileUtils.getFileDescription ( file, imageSize );
            nameLabel.setText ( fileDescription.getName () );

            // Updating tile view additional description
            if ( isTilesView () )
            {
                descriptionLabel.setText ( fileDescription.getDescription () );

                // Updating size label
                if ( fileDescription.getSize () != null )
                {
                    sizeLabel.setText ( fileDescription.getSize () );
                }
                else
                {
                    sizeLabel.setText ( null );
                }
            }
            else
            {
                descriptionLabel.setText ( null );
                sizeLabel.setText ( null );
            }
        }
        else
        {
            nameLabel.setText ( null );
            descriptionLabel.setText ( null );
            sizeLabel.setText ( null );
        }

        return this;
    }

    @Override
    protected StyleId getStyleId ( final JList list, final Object value, final int index, final boolean isSelected,
                                   final boolean cellHasFocus )
    {
        return isTilesView () ? StyleId.filelistTileCellRenderer.at ( list ) : StyleId.filelistIconCellRenderer.at ( list );
    }

    /**
     * Returns whether list is currently displaying tiles or not.
     *
     * @return true if list is currently displaying tiles, false otherwise
     */
    protected boolean isTilesView ()
    {
        return fileList.getFileListViewType ().equals ( FileListViewType.tiles );
    }

    /**
     * Custom layout for file list cell element.
     */
    protected class FileCellLayout extends AbstractLayoutManager
    {
        @Override
        public Dimension preferredLayoutSize ( final Container parent )
        {
            return isTilesView () ? tileCellSize : iconCellSize;
        }

        @Override
        public void layoutContainer ( final Container parent )
        {
            // Constants for further layout calculations
            final boolean ltr = fileList.getComponentOrientation ().isLeftToRight ();
            final Insets i = getInsets ();
            final boolean tilesView = isTilesView ();
            final boolean hasName = nameLabel.getText () != null;
            final boolean hasDescription = tilesView && descriptionLabel.getText () != null;
            final boolean hasFileSize = tilesView && sizeLabel.getText () != null;

            // Updating elements visibility
            nameLabel.setVisible ( hasName );
            descriptionLabel.setVisible ( hasDescription );
            sizeLabel.setVisible ( hasFileSize );

            // Updating elements bounds
            if ( tilesView )
            {
                // Left-middle icon
                iconLabel.setBounds ( ltr ? i.left : tileCellSize.width - i.right - imageSide, i.top, imageSide, imageSide );

                // Description elements
                if ( hasName )
                {
                    // Constants for further description positioning calculations
                    final Dimension nps = nameLabel.getPreferredSize ();
                    final Dimension dps = hasDescription ? descriptionLabel.getPreferredSize () : new Dimension ( 0, 0 );
                    final Dimension sps = hasFileSize ? sizeLabel.getPreferredSize () : new Dimension ( 0, 0 );
                    final int dh = nps.height + ( hasDescription ? gap + dps.height : 0 ) + ( hasFileSize ? gap + sps.height : 0 );
                    final int dx = ltr ? i.left + imageSide + gap : i.left;
                    final int dw = tileCellSize.width - i.left - i.right - imageSide - gap;

                    // Name element
                    int dy = i.top + ( tileCellSize.height - i.top - i.bottom ) / 2 - dh / 2;
                    nameLabel.setBounds ( dx, dy, dw, nps.height );
                    dy += nps.height + gap;

                    // Description element
                    if ( hasDescription )
                    {
                        descriptionLabel.setBounds ( dx, dy, dw, dps.height );
                        dy += dps.height + gap;
                    }

                    // File size element
                    if ( hasFileSize )
                    {
                        sizeLabel.setBounds ( dx, dy, dw, sps.height );
                    }
                }
            }
            else
            {
                // Top-middle icon
                final int cw = iconCellSize.width - i.left - i.right;
                iconLabel.setBounds ( i.left + cw / 2 - 27, i.top, imageSide, imageSide );

                // Name element
                if ( hasName )
                {
                    final int ny = i.top + imageSide + gap;
                    nameLabel.setBounds ( i.left, ny, cw, iconCellSize.height - ny - i.bottom );
                }
            }
        }

        /**
         * Returns description bounds for list cell.
         *
         * @return description bounds for list cell
         */
        public Rectangle getDescriptionBounds ()
        {
            // Constants for further size calculations
            final boolean ltr = fileList.getComponentOrientation ().isLeftToRight ();
            final Insets i = getInsets ();
            final boolean tilesView = isTilesView ();

            // Determining
            if ( tilesView )
            {
                // Tile view
                if ( ltr )
                {
                    // Icon at the left side, description at the right side
                    final int x = i.left + imageSide + gap;
                    return new Rectangle ( x, i.top, tileCellSize.width - x - i.right, tileCellSize.height - i.top - i.bottom );
                }
                else
                {
                    // Icon at the right side, description at the left side
                    return new Rectangle ( i.left, i.top, tileCellSize.width - gap - imageSide - i.right,
                            tileCellSize.height - i.top - i.bottom );
                }
            }
            else
            {
                // Icon view
                final int ny = i.top + imageSide + gap;
                return new Rectangle ( i.left, ny, iconCellSize.width - i.left - i.right, iconCellSize.height - ny - i.bottom );
            }
        }
    }
}