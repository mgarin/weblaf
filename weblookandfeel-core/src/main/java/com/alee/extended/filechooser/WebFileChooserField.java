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

import com.alee.extended.drag.FileDropHandler;
import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

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

    private List<FilesSelectionListener> listeners = new ArrayList<FilesSelectionListener> ();

    private List<File> selectedFiles = new ArrayList<File> ();

    private SelectionMode selectionMode = SelectionMode.MULTIPLE_SELECTION;
    private int preferredWidth = -1;
    private boolean showFileShortName = true;
    private boolean showFileIcon = true;
    private boolean showRemoveButton = true;
    private boolean showFileExtensions = false;
    private boolean showChooseButton = true;
    private boolean filesDropEnabled = true;

    private WebFileChooser webFileChooser = null;

    private WebPanel contentPanel;
    private WebScrollPane scroll;
    private WebButton chooseButton;

    public WebFileChooserField ()
    {
        this ( null );
    }

    public WebFileChooserField ( Window parent )
    {
        this ( parent, true );
    }

    public WebFileChooserField ( boolean showChooseButton )
    {
        this ( null, showChooseButton );
    }

    public WebFileChooserField ( Window owner, boolean showChooseButton )
    {
        super ( true );

        this.showChooseButton = showChooseButton;

        setOpaque ( false );
        setLayout ( new BorderLayout ( 0, 0 ) );
        setWebColored ( false );
        setDrawFocus ( true );
        setMargin ( -1 );
        setBackground ( Color.WHITE );

        contentPanel = new WebPanel ();
        contentPanel.setOpaque ( false );

        // Files TransferHandler
        setTransferHandler ( new FileDropHandler ()
        {
            protected boolean isDropEnabled ()
            {
                return filesDropEnabled;
            }

            protected boolean filesImported ( List<File> files )
            {
                // Setting dragged files
                setSelectedFiles ( files );
                return getSelectedFiles ().size () > 0;
            }
        } );

        // For wide content scrolling
        contentPanel.addMouseWheelListener ( new MouseWheelListener ()
        {
            public void mouseWheelMoved ( MouseWheelEvent e )
            {
                Rectangle vr = contentPanel.getVisibleRect ();
                contentPanel.scrollRectToVisible ( new Rectangle ( vr.x + e.getWheelRotation () * 25, vr.y, vr.width, vr.height ) );
            }
        } );

        scroll = new WebScrollPane ( contentPanel, false )
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
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
            webFileChooser = new WebFileChooser ( owner );
            webFileChooser.setSelectionMode ( selectionMode );
            webFileChooser.setOkListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    // Adding all selected files
                    setSelectedFiles ( webFileChooser.getSelectedFiles () );
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
                public void actionPerformed ( ActionEvent e )
                {
                    // Files selection
                    webFileChooser.setVisible ( true );

                    // Requesting focus back to this component after file chooser close
                    chooseButton.requestFocusInWindow ();
                }
            } );
            contentPanel.addMouseListener ( new MouseAdapter ()
            {
                public void mousePressed ( MouseEvent e )
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
        contentPanel.setLayout ( new HorizontalFlowLayout ( 0, isSingleSelection () ) );
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

    public SelectionMode getSelectionMode ()
    {
        return selectionMode;
    }

    public void setSelectionMode ( SelectionMode selectionMode )
    {
        this.selectionMode = selectionMode;
        if ( webFileChooser != null )
        {
            webFileChooser.setSelectionMode ( selectionMode );
        }
        updateContentLayout ();
    }

    public boolean isFilesDropEnabled ()
    {
        return filesDropEnabled;
    }

    public void setFilesDropEnabled ( boolean filesDropEnabled )
    {
        this.filesDropEnabled = filesDropEnabled;
    }

    public void setCurrentDirectory ( String dir )
    {
        webFileChooser.setCurrentDirectory ( dir );
    }

    public void setCurrentDirectory ( File dir )
    {
        webFileChooser.setCurrentDirectory ( dir );
    }

    public File getCurrentDirectory ()
    {
        return webFileChooser.getCurrentDirectory ();
    }

    public FilesToChoose getFilesToChoose ()
    {
        return webFileChooser.getFilesToChoose ();
    }

    public void setFilesToChoose ( FilesToChoose filesToChoose )
    {
        webFileChooser.setFilesToChoose ( filesToChoose );
    }

    public List<DefaultFileFilter> getAvailableFilters ()
    {
        if ( webFileChooser != null )
        {
            return webFileChooser.getAvailableFilters ();
        }
        else
        {
            return null;
        }
    }

    public void setAvailableFilter ( DefaultFileFilter availableFilter )
    {
        webFileChooser.setAvailableFilter ( availableFilter );
    }

    public void setAvailableFilters ( List<DefaultFileFilter> availableFilters )
    {
        webFileChooser.setAvailableFilters ( availableFilters );
    }

    public boolean isShowFileShortName ()
    {
        return showFileShortName;
    }

    public void setShowFileShortName ( boolean showFileShortName )
    {
        this.showFileShortName = showFileShortName;
        updateSelectedFiles ();
    }

    public boolean isShowFileIcon ()
    {
        return showFileIcon;
    }

    public void setShowFileIcon ( boolean showFileIcon )
    {
        this.showFileIcon = showFileIcon;
        updateSelectedFiles ();
    }

    public boolean isShowRemoveButton ()
    {
        return showRemoveButton;
    }

    public void setShowRemoveButton ( boolean showRemoveButton )
    {
        this.showRemoveButton = showRemoveButton;
        updateSelectedFiles ();
    }

    public boolean isShowFileExtensions ()
    {
        return showFileExtensions;
    }

    public void setShowFileExtensions ( boolean showFileExtensions )
    {
        this.showFileExtensions = showFileExtensions;
        updateSelectedFiles ();
    }

    public List<File> getSelectedFiles ()
    {
        return selectedFiles;
    }

    public void setSelectedFile ( File selectedFile )
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

    public void setSelectedFiles ( List<File> selectedFiles )
    {
        this.selectedFiles.clear ();
        if ( selectedFiles != null && selectedFiles.size () > 0 )
        {
            if ( isSingleSelection () )
            {
                for ( File file : selectedFiles )
                {
                    if ( FileUtils.isFileAccepted ( file, getAvailableFilters () ) )
                    {
                        this.selectedFiles.add ( file );
                        break;
                    }
                }
            }
            else
            {
                for ( File file : selectedFiles )
                {
                    if ( FileUtils.isFileAccepted ( file, getAvailableFilters () ) )
                    {
                        this.selectedFiles.add ( file );
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
        for ( File file : selectedFiles )
        {
            FilePlate filePlate = new FilePlate ( file );
            filePlate.applyComponentOrientation ( WebFileChooserField.this.getComponentOrientation () );
            contentPanel.add ( filePlate );
        }
        WebFileChooserField.this.revalidate ();
        WebFileChooserField.this.repaint ();
    }

    private boolean isSingleSelection ()
    {
        return getSelectionMode ().equals ( SelectionMode.SINGLE_SELECTION );
    }

    public class FilePlate extends WebPanel
    {
        public FilePlate ( final File file )
        {
            super ( true );

            setLayout ( new BorderLayout () );
            setDrawSides ( false, false, false, true );
            setShadeWidth ( 0 );
            setMargin ( 0, 3, 0, 1 );
            setFocusable ( true );

            // File name label
            String actualFileName = FileUtils.getDisplayFileName ( file );
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

                public void mousePressed ( MouseEvent e )
                {
                    FilePlate.this.requestFocusInWindow ();
                    if ( SwingUtilities.isRightMouseButton ( e ) )
                    {
                        if ( getSelectionMode ().equals ( SelectionMode.MULTIPLE_SELECTION ) )
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
                public void focusGained ( FocusEvent e )
                {
                    scrollToPlate ();
                }
            } );

            // Remove button
            if ( showRemoveButton )
            {
                WebButton remove = new WebButton ( CROSS_ICON );
                remove.setLeftRightSpacing ( 0 );
                remove.setInnerShadeWidth ( 2 );
                remove.setRound ( StyleConstants.smallRound );
                remove.setShadeWidth ( StyleConstants.shadeWidth );
                remove.setFocusable ( false );
                remove.setRolloverDecoratedOnly ( true );
                remove.addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
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
            Rectangle b = FilePlate.this.getBounds ();
            int w = scroll.getWidth () - 2;
            b.width = w < b.width ? w : b.width;
            contentPanel.scrollRectToVisible ( b );
        }
    }

    public void addSelectedFilesListener ( FilesSelectionListener listener )
    {
        listeners.add ( listener );
    }

    public void removeSelectedFilesListener ( FilesSelectionListener listener )
    {
        listeners.remove ( listener );
    }

    private void fireSelectionChanged ( List<File> selectedFiles )
    {
        for ( FilesSelectionListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ( selectedFiles );
        }
    }
}
