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

import com.alee.extended.drag.FileDragAndDropHandler;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.filefilter.AbstractFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 07.08.11 Time: 15:44
 */

public class WebFileChooserField extends WebPanel
{
    public static final ImageIcon CROSS_ICON = new ImageIcon ( WebFileChooserField.class.getResource ( "icons/cross.png" ) );

    private final List<FilesSelectionListener> listeners = new ArrayList<FilesSelectionListener> ( 1 );

    private final List<File> selectedFiles = new ArrayList<File> ();

    /**
     * Whether multiply files selection allowed or not.
     */
    protected boolean multiSelectionEnabled = false;

    private boolean showFileShortName = true;
    private boolean showFileIcon = true;
    private boolean showRemoveButton = true;
    private boolean showFileExtensions = false;
    private boolean showChooseButton = true;
    private boolean filesDropEnabled = true;

    private WebFileChooser webFileChooser = null;

    private final WebPanel contentPanel;
    private final WebScrollPane scroll;
    private WebButton chooseButton;

    public WebFileChooserField ()
    {
        this ( null );
    }

    public WebFileChooserField ( final Window parent )
    {
        this ( parent, true );
    }

    public WebFileChooserField ( final boolean showChooseButton )
    {
        this ( null, showChooseButton );
    }

    public WebFileChooserField ( final Window owner, final boolean showChooseButton )
    {
        super ( true );

        this.showChooseButton = showChooseButton;

        setOpaque ( false );
        setLayout ( new BorderLayout ( 0, 0 ) );
        setWebColoredBackground ( false );
        setPaintFocus ( true );
        setMargin ( -1 );
        setBackground ( Color.WHITE );

        contentPanel = new WebPanel ();
        contentPanel.setOpaque ( false );

        // Files TransferHandler
        setTransferHandler ( new FileDragAndDropHandler ()
        {
            @Override
            public boolean isDropEnabled ()
            {
                return filesDropEnabled;
            }

            @Override
            public boolean filesDropped ( final List<File> files )
            {
                // Setting dragged files
                setSelectedFiles ( files );
                return getSelectedFiles ().size () > 0;
            }
        } );

        // For wide content scrolling
        contentPanel.addMouseWheelListener ( new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                final Rectangle vr = contentPanel.getVisibleRect ();
                contentPanel.scrollRectToVisible ( new Rectangle ( vr.x + e.getWheelRotation () * 25, vr.y, vr.width, vr.height ) );
            }
        } );

        scroll = new WebScrollPane ( contentPanel, false )
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                ps.height = contentPanel.getPreferredSize ().height;
                return ps;
            }
        };
        scroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_NEVER );
        scroll.setMargin ( 1 );
        scroll.setOpaque ( false );
        scroll.getViewport ().setOpaque ( false );
        add ( scroll, BorderLayout.CENTER );

        if ( this.showChooseButton )
        {
            webFileChooser = new WebFileChooser ();
            webFileChooser.setMultiSelectionEnabled ( multiSelectionEnabled );
            webFileChooser.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( e.getActionCommand ().equals ( WebFileChooser.APPROVE_SELECTION ) )
                    {
                        // Adding all selected files
                        setSelectedFiles ( CollectionUtils.toList ( webFileChooser.getSelectedFiles () ) );
                    }
                }
            } );

            chooseButton = new WebButton ( "..." );
            chooseButton.setRound ( WebFileChooserField.this.getRound () );
            chooseButton.setLeftRightSpacing ( StyleConstants.smallLeftRightSpacing );
            chooseButton.setDrawFocus ( false );
            chooseButton.setDrawLeft ( false );
            chooseButton.setDrawLeftLine ( true );
            chooseButton.setRolloverDarkBorderOnly ( false );
            chooseButton.setShadeWidth ( 0 );
            chooseButton.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    // Files selection
                    webFileChooser.showOpenDialog ( owner );

                    // Requesting focus back to this component after file chooser close
                    chooseButton.requestFocusInWindow ();
                }
            } );
            contentPanel.addMouseListener ( new MouseAdapter ()
            {
                @Override
                public void mousePressed ( final MouseEvent e )
                {
                    // For easier files choose
                    if ( SwingUtilities.isLeftMouseButton ( e ) )
                    {
                        chooseButton.doClick ( 0 );
                    }
                }
            } );
            add ( chooseButton, BorderLayout.LINE_END );
        }

        // Updating layout
        updateContentLayout ();
    }

    private void updateContentLayout ()
    {
        contentPanel.setLayout ( new HorizontalFlowLayout ( 0, !multiSelectionEnabled ) );
    }

    public WebFileChooser getWebFileChooser ()
    {
        return webFileChooser;
    }

    public WebButton getChooseButton ()
    {
        return chooseButton;
    }

    public boolean isShowChooseButton ()
    {
        return showChooseButton;
    }

    // todo Implement this feature
    //    public void setChooseButton ( WebButton chooseButton )
    //    {
    //        this.chooseButton = chooseButton;
    //        // ...
    //    }

    public boolean isMultiSelectionEnabled ()
    {
        return multiSelectionEnabled;
    }

    public void setMultiSelectionEnabled ( final boolean multiSelectionEnabled )
    {
        this.multiSelectionEnabled = multiSelectionEnabled;
        if ( webFileChooser != null )
        {
            webFileChooser.setMultiSelectionEnabled ( multiSelectionEnabled );
        }
        updateContentLayout ();
    }

    public boolean isFilesDropEnabled ()
    {
        return filesDropEnabled;
    }

    public void setFilesDropEnabled ( final boolean filesDropEnabled )
    {
        this.filesDropEnabled = filesDropEnabled;
    }

    public boolean isShowFileShortName ()
    {
        return showFileShortName;
    }

    public void setShowFileShortName ( final boolean showFileShortName )
    {
        this.showFileShortName = showFileShortName;
        updateSelectedFiles ();
    }

    public boolean isShowFileIcon ()
    {
        return showFileIcon;
    }

    public void setShowFileIcon ( final boolean showFileIcon )
    {
        this.showFileIcon = showFileIcon;
        updateSelectedFiles ();
    }

    public boolean isShowRemoveButton ()
    {
        return showRemoveButton;
    }

    public void setShowRemoveButton ( final boolean showRemoveButton )
    {
        this.showRemoveButton = showRemoveButton;
        updateSelectedFiles ();
    }

    public boolean isShowFileExtensions ()
    {
        return showFileExtensions;
    }

    public void setShowFileExtensions ( final boolean showFileExtensions )
    {
        this.showFileExtensions = showFileExtensions;
        updateSelectedFiles ();
    }

    public List<File> getSelectedFiles ()
    {
        return selectedFiles;
    }

    public void setSelectedFile ( final File selectedFile )
    {
        this.selectedFiles.clear ();
        if ( selectedFile != null && FileUtils.isFileAccepted ( selectedFile, getAvailableFilters () ) )
        {
            this.selectedFiles.add ( selectedFile );
        }
        updateSelectedFiles ();

        // Inform that selection changed
        fireSelectionChanged ( this.selectedFiles );
    }

    public void setSelectedFiles ( final List<File> selectedFiles )
    {
        this.selectedFiles.clear ();
        if ( selectedFiles != null && selectedFiles.size () > 0 )
        {
            if ( multiSelectionEnabled )
            {
                for ( final File file : selectedFiles )
                {
                    if ( FileUtils.isFileAccepted ( file, getAvailableFilters () ) )
                    {
                        this.selectedFiles.add ( file );
                    }
                }
            }
            else
            {
                for ( final File file : selectedFiles )
                {
                    if ( FileUtils.isFileAccepted ( file, getAvailableFilters () ) )
                    {
                        this.selectedFiles.add ( file );
                        break;
                    }
                }
            }
        }
        updateSelectedFiles ();

        // Inform that selection changed
        fireSelectionChanged ( this.selectedFiles );
    }

    public void clearSelectedFiles ()
    {
        setSelectedFiles ( null );
    }

    private void updateSelectedFiles ()
    {
        contentPanel.removeAll ();
        for ( final File file : selectedFiles )
        {
            final FilePlate filePlate = new FilePlate ( file );
            filePlate.applyComponentOrientation ( WebFileChooserField.this.getComponentOrientation () );
            contentPanel.add ( filePlate );
        }
        WebFileChooserField.this.revalidate ();
        WebFileChooserField.this.repaint ();
    }

    private List<AbstractFileFilter> getAvailableFilters ()
    {
        return webFileChooser == null ? null : webFileChooser.getAvailableFilters ();
    }

    public class FilePlate extends WebPanel
    {
        public FilePlate ( final File file )
        {
            super ( true );

            setLayout ( new BorderLayout () );
            setPaintSides ( false, false, false, true );
            setShadeWidth ( 0 );
            setMargin ( 0, 3, 0, 1 );
            setFocusable ( true );

            // File name label
            final String actualFileName = FileUtils.getDisplayFileName ( file );
            final String displayFileName =
                    showFileExtensions || file.isDirectory () ? actualFileName : FileUtils.getFileNamePart ( actualFileName );
            final String absolutePath = file.getAbsolutePath ();
            final WebLabel fileName = new WebLabel ( showFileShortName ? displayFileName : absolutePath );
            fileName.setIcon ( showFileIcon ? FileUtils.getFileIcon ( file, false ) : null );
            fileName.setMargin ( 0, 0, 0, 2 );
            add ( fileName, BorderLayout.CENTER );

            addMouseListener ( new MouseAdapter ()
            {
                private boolean showShortName = showFileShortName;

                @Override
                public void mousePressed ( final MouseEvent e )
                {
                    FilePlate.this.requestFocusInWindow ();
                    if ( SwingUtilities.isRightMouseButton ( e ) )
                    {
                        if ( multiSelectionEnabled )
                        {
                            showShortName = !showShortName;
                            fileName.setText ( showShortName ? displayFileName : absolutePath );
                        }
                        else
                        {
                            setShowFileShortName ( !isShowFileShortName () );
                        }
                    }
                    scrollToPlate ();
                }
            } );

            addFocusListener ( new FocusAdapter ()
            {
                @Override
                public void focusGained ( final FocusEvent e )
                {
                    scrollToPlate ();
                }
            } );

            // Remove button
            if ( showRemoveButton )
            {
                final WebButton remove = new WebButton ( CROSS_ICON );
                remove.setLeftRightSpacing ( 0 );
                remove.setInnerShadeWidth ( 2 );
                remove.setRound ( StyleConstants.smallRound );
                remove.setShadeWidth ( StyleConstants.shadeWidth );
                remove.setFocusable ( false );
                remove.setRolloverDecoratedOnly ( true );
                remove.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        // Remove file
                        selectedFiles.remove ( file );
                        contentPanel.remove ( FilePlate.this );
                        WebFileChooserField.this.revalidate ();
                        WebFileChooserField.this.repaint ();

                        // Inform that selected files changed
                        fireSelectionChanged ( selectedFiles );
                    }
                } );
                add ( new CenterPanel ( remove ), BorderLayout.LINE_END );
            }
        }

        private void scrollToPlate ()
        {
            final Rectangle b = FilePlate.this.getBounds ();
            final int w = scroll.getWidth () - 2;
            b.width = w < b.width ? w : b.width;
            contentPanel.scrollRectToVisible ( b );
        }
    }

    public void addSelectedFilesListener ( final FilesSelectionListener listener )
    {
        listeners.add ( listener );
    }

    public void removeSelectedFilesListener ( final FilesSelectionListener listener )
    {
        listeners.remove ( listener );
    }

    private void fireSelectionChanged ( final List<File> selectedFiles )
    {
        for ( final FilesSelectionListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ( selectedFiles );
        }
    }
}
