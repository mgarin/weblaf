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

import com.alee.api.annotations.NotNull;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.BoundsType;
import com.alee.managers.style.StyleId;
import com.alee.utils.FileUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.file.FileDescription;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Custom {@link ListCellRenderer} implementation for {@link WebFileList}.
 * This renderer is able to display extended file information and generate thumbnails for file image files.
 *
 * @author Mikle Garin
 */
public class WebFileListCellRenderer extends WebPanel implements ListCellRenderer
{
    /**
     * todo 1. Replace all parts with single component and IContent acting as parts
     * todo 2. Move thumbnail generation into `WebImage` component as a feature?
     */

    /**
     * Gap between renderer elements.
     */
    protected int gap = 4;

    /**
     * File list in which this list cell renderer is used.
     */
    @NotNull
    protected final WebFileList fileList;

    /**
     * Thumbnail icon label.
     */
    @NotNull
    protected final WebLabel iconLabel;

    /**
     * File name label.
     */
    @NotNull
    protected final WebLabel nameLabel;

    /**
     * File size label.
     */
    @NotNull
    protected final WebLabel sizeLabel;

    /**
     * File description label.
     */
    @NotNull
    protected final WebLabel descriptionLabel;

    /**
     * Constructs cell renderer for the specified file list.
     *
     * @param fileList file list in which this cell renderer is used
     */
    public WebFileListCellRenderer ( @NotNull final WebFileList fileList )
    {
        super ( StyleId.filelistCellRenderer.at ( fileList ) );

        this.fileList = fileList;

        iconLabel = new WebLabel ( StyleId.filelistCellRendererIcon.at ( this ) );

        nameLabel = new WebLabel ( StyleId.filelistCellRendererName.at ( this ) );
        nameLabel.setFont ( nameLabel.getFont ().deriveFont ( Font.PLAIN ) );

        descriptionLabel = new WebLabel ( StyleId.filelistCellRendererDescription.at ( this ) );
        descriptionLabel.setFont ( descriptionLabel.getFont ().deriveFont ( Font.PLAIN ) );

        sizeLabel = new WebLabel ( StyleId.filelistCellRendererSize.at ( this ) );
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
    @NotNull
    public Rectangle getDescriptionBounds ()
    {
        return ( ( FileCellLayout ) getLayout () ).getDescriptionBounds ();
    }

    /**
     * Updates renderer elements view.
     */
    public void updateFilesView ()
    {
        nameLabel.setHorizontalAlignment ( isTilesView () ? JLabel.LEADING : JLabel.CENTER );
    }

    /**
     * Returns thumbnail icon label.
     *
     * @return thumbnail icon label
     */
    @NotNull
    public JLabel getIconLabel ()
    {
        return iconLabel;
    }

    /**
     * Returns file name label.
     *
     * @return file name label
     */
    @NotNull
    public JLabel getNameLabel ()
    {
        return nameLabel;
    }

    /**
     * Returns file size label.
     *
     * @return file size label
     */
    @NotNull
    public JLabel getSizeLabel ()
    {
        return sizeLabel;
    }

    /**
     * Returns file description label.
     *
     * @return file description label
     */
    @NotNull
    public JLabel getDescriptionLabel ()
    {
        return descriptionLabel;
    }

    @NotNull
    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                    final boolean cellHasFocus )
    {
        final FileElement element = ( FileElement ) value;
        final File file = element.getFile ();

        // Updating style identifier
        setStyleId ( getStyleId ( list, value, index, isSelected, cellHasFocus ) );

        // Renderer icon
        String imageSize = null;
        if ( iconLabel.isEnabled () )
        {
            // Thumbnail loading
            ThumbnailGenerator.queueThumbnailLoad ( fileList, element, BoundsType.padding.bounds ( iconLabel ).getSize (), false );

            // Image thumbnail
            final Icon thumbnail = element.getEnabledThumbnail ();
            iconLabel.setIcon ( thumbnail );

            // Optional image description
            if ( thumbnail != null && thumbnail instanceof ImageIcon )
            {
                imageSize = ( ( ImageIcon ) thumbnail ).getDescription ();
            }
            else if ( thumbnail != null )
            {
                imageSize = thumbnail.getIconWidth () + "x" + thumbnail.getIconHeight ();
            }
        }
        else
        {
            // Disabled thumbnail loading
            ThumbnailGenerator.queueThumbnailLoad ( fileList, element, BoundsType.padding.bounds ( iconLabel ).getSize (), true );

            // Image disabled thumbnail
            iconLabel.setDisabledIcon ( element.getDisabledThumbnail () );
        }

        // Updating file description elements
        if ( fileList.getEditedCell () != index )
        {
            // Settings description
            final FileDescription fileDescription = FileUtils.getFileDescription ( file, imageSize );
            final String name = fileDescription.getName ();
            nameLabel.setText ( TextUtils.notEmpty ( name ) ? name : " " );

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

    /**
     * Returns {@link StyleId} for specific list cell.
     *
     * @param list         tree
     * @param value        cell value
     * @param index        cell index
     * @param isSelected   whether cell is selected or not
     * @param cellHasFocus whether cell has focus or not
     * @return {@link StyleId} for specific list cell
     */
    @NotNull
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
        public void layoutContainer ( @NotNull final Container container )
        {
            // Constants for further layout calculations
            final Dimension cellSize = getSize ();
            final Dimension iconSize = iconLabel.getPreferredSize ();
            final boolean ltr = fileList.getComponentOrientation ().isLeftToRight ();
            final Insets i = getInsets ();
            final boolean tilesView = isTilesView ();
            final boolean hasDescription = tilesView && TextUtils.notEmpty ( descriptionLabel.getText () );
            final boolean hasFileSize = tilesView && TextUtils.notEmpty ( sizeLabel.getText () );

            // Updating elements visibility
            descriptionLabel.setVisible ( hasDescription );
            sizeLabel.setVisible ( hasFileSize );

            // Updating elements bounds
            if ( tilesView )
            {
                // Left-middle icon
                iconLabel.setBounds ( ltr ? i.left : cellSize.width - i.right - iconSize.width, i.top, iconSize.width, iconSize.height );

                // Constants for further description positioning calculations
                final Dimension nps = nameLabel.getPreferredSize ();
                final Dimension dps = hasDescription ? descriptionLabel.getPreferredSize () : new Dimension ( 0, 0 );
                final Dimension sps = hasFileSize ? sizeLabel.getPreferredSize () : new Dimension ( 0, 0 );
                final int dh = nps.height + ( hasDescription ? gap + dps.height : 0 ) + ( hasFileSize ? gap + sps.height : 0 );
                final int dx = ltr ? i.left + iconSize.width + gap : i.left;
                final int dw = cellSize.width - i.left - i.right - iconSize.width - gap;

                // Name element
                int dy = i.top + ( cellSize.height - i.top - i.bottom ) / 2 - dh / 2;
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
            else
            {
                // Top-middle icon
                final int cw = cellSize.width - i.left - i.right;
                iconLabel.setBounds ( i.left + cw / 2 - 27, i.top, iconSize.width, iconSize.height );

                // Name element
                final int ny = i.top + iconSize.height + gap;
                nameLabel.setBounds ( i.left, ny, cw, cellSize.height - ny - i.bottom );
            }
        }

        @NotNull
        @Override
        public Dimension preferredLayoutSize ( @NotNull final Container container )
        {
            final Dimension ps;
            final Insets i = container.getInsets ();
            final Dimension is = iconLabel.getPreferredSize ();
            final Dimension ns = nameLabel.getPreferredSize ();
            if ( isTilesView () )
            {
                final Dimension bs = new Dimension ( ns.width, ns.height );
                if ( descriptionLabel.isVisible () )
                {
                    final Dimension ds = descriptionLabel.getPreferredSize ();
                    bs.width = Math.max ( bs.width, ds.width );
                    bs.height += gap + ds.height;
                }
                if ( sizeLabel.isVisible () )
                {
                    final Dimension ss = sizeLabel.getPreferredSize ();
                    bs.width = Math.max ( bs.width, ss.width );
                    bs.height += gap + ss.height;
                }
                ps = new Dimension (
                        i.left + is.width + gap + bs.width + i.right,
                        i.top + Math.max ( is.height, bs.height ) + i.bottom
                );
            }
            else
            {
                ps = new Dimension (
                        i.left + Math.max ( is.width, ns.width ) + i.right,
                        i.top + is.height + gap + ns.height + i.bottom
                );
            }
            return ps;
        }

        /**
         * Returns description bounds for list cell.
         *
         * @return description bounds for list cell
         */
        @NotNull
        public Rectangle getDescriptionBounds ()
        {
            final Rectangle bounds;
            final Dimension cellSize = getSize ();
            final Dimension iconSize = iconLabel.getPreferredSize ();
            final boolean ltr = fileList.getComponentOrientation ().isLeftToRight ();
            final Insets i = getInsets ();
            final boolean tilesView = isTilesView ();
            if ( tilesView )
            {
                if ( ltr )
                {
                    // Icon at the left side, description at the right side
                    final int x = i.left + iconSize.width + gap;
                    bounds = new Rectangle (
                            x,
                            i.top,
                            cellSize.width - x - i.right,
                            cellSize.height - i.top - i.bottom
                    );
                }
                else
                {
                    // Icon at the right side, description at the left side
                    bounds = new Rectangle (
                            i.left,
                            i.top,
                            cellSize.width - gap - iconSize.width - i.right,
                            cellSize.height - i.top - i.bottom
                    );
                }
            }
            else
            {
                final int ny = i.top + iconSize.height + gap;
                bounds = new Rectangle (
                        i.left,
                        ny,
                        cellSize.width - i.left - i.right,
                        cellSize.height - ny - i.bottom
                );
            }
            return bounds;
        }
    }
}