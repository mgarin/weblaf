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

package com.alee.laf.filechooser;

import com.alee.global.GlobalConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.FileUtils;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom UI for JFileChooser component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebFileChooserUI extends FileChooserUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( FileChooserPainter.class )
    protected IFileChooserPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected JFileChooser fileChooser;
    protected WebFileView fileView;
    protected WebFileChooserPanel fileChooserPanel;
    protected boolean ignoreFileSelectionChanges = false;

    /**
     * Listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the WebFileChooserUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebFileChooserUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebFileChooserUI ();
    }

    /**
     * Constructs new WebFileChooserUI.
     */
    public WebFileChooserUI ()
    {
        super ();
    }

    protected WebFileChooserPanel createPanel ( final JFileChooser fileChooser )
    {
        return new WebFileChooserPanel ( getFileChooserType (), fileChooser.getControlButtonsAreShown () );
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Saving file chooser reference
        fileChooser = ( JFileChooser ) c;

        // Applying skin
        StyleManager.installSkin ( fileChooser );

        fileView = new WebFileView ();
        fileChooser.setLayout ( new BorderLayout () );

        fileChooserPanel = createPanel ( fileChooser );
        fileChooserPanel.setFileSelectionMode ( FileSelectionMode.get ( fileChooser.getFileSelectionMode () ) );
        fileChooserPanel.setMultiSelectionEnabled ( fileChooser.isMultiSelectionEnabled () );
        fileChooserPanel.setShowHiddenFiles ( !fileChooser.isFileHidingEnabled () );
        fileChooserPanel.setAcceptListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                ignoreFileSelectionChanges = true;
                final List<File> selectedFiles = fileChooserPanel.getSelectedFiles ();
                fileChooser.setSelectedFiles ( selectedFiles.toArray ( new File[ selectedFiles.size () ] ) );
                ignoreFileSelectionChanges = false;
                fileChooser.approveSelection ();
            }
        } );
        fileChooserPanel.setCancelListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fileChooser.cancelSelection ();
            }
        } );
        fileChooserPanel.addFileChooserListener ( new FileChooserListener ()
        {
            @Override
            public void directoryChanged ( final File newDirectory )
            {
                ignoreFileSelectionChanges = true;
                fileChooser.setCurrentDirectory ( newDirectory );
                ignoreFileSelectionChanges = false;
            }

            @Override
            public void selectionChanged ( final List<File> selectedFiles )
            {
                ignoreFileSelectionChanges = true;
                fileChooser.setSelectedFiles ( selectedFiles.toArray ( new File[ selectedFiles.size () ] ) );
                ignoreFileSelectionChanges = false;
            }

            @Override
            public void fileFilterChanged ( final FileFilter oldFilter, final FileFilter newFilter )
            {
                ignoreFileSelectionChanges = true;
                fileChooser.setFileFilter ( FileUtils.getSwingFileFilter ( fileChooserPanel.getActiveFileFilter () ) );
                ignoreFileSelectionChanges = false;
            }
        } );
        fileChooser.add ( fileChooserPanel, BorderLayout.CENTER );

        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                propertyChanged ( evt );
            }
        };
        fileChooser.addPropertyChangeListener ( propertyChangeListener );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( fileChooser );

        // Removing content
        fileChooser.removePropertyChangeListener ( propertyChangeListener );
        fileChooserPanel = null;
        fileView = null;

        // Removing file chooser reference
        fileChooser = null;
    }

    /**
     * Fired when some of JFileChooser properties changes.
     *
     * @param event property change event
     */
    protected void propertyChanged ( final PropertyChangeEvent event )
    {
        final String prop = event.getPropertyName ();
        if ( prop.equals ( JFileChooser.ACCESSORY_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setAccessory ( ( JComponent ) event.getNewValue () );
        }
        else if ( prop.equals ( JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setAcceptButtonText ( fileChooser.getApproveButtonText () );
        }
        else if ( prop.equals ( JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setShowControlButtons ( fileChooser.getControlButtonsAreShown () );
        }
        else if ( prop.equals ( JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setMultiSelectionEnabled ( fileChooser.isMultiSelectionEnabled () );
        }
        else if ( prop.equals ( JFileChooser.FILE_FILTER_CHANGED_PROPERTY ) ||
                prop.equals ( JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY ) ||
                prop.equals ( JFileChooser.ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY ) )
        {
            if ( !ignoreFileSelectionChanges )
            {
                final FileFilter filter = fileChooser.getFileFilter ();
                final FileFilter[] filters = fileChooser.getChoosableFileFilters ();

                // Collecting all filters
                final int initialCapacity = ( filters != null ? filters.length : 0 ) + ( filter != null ? 1 : 0 );
                final List<FileFilter> collected = new ArrayList<FileFilter> ( initialCapacity );
                if ( filter != null )
                {
                    collected.add ( filter );
                }
                if ( filters != null && filters.length > 0 )
                {
                    for ( final FileFilter fileFilter : filters )
                    {
                        if ( !collected.contains ( fileFilter ) )
                        {
                            collected.add ( fileFilter );
                        }
                    }
                }

                // Applying filters
                if ( collected.size () > 0 )
                {
                    fileChooserPanel.setFileFilters ( collected.toArray ( new FileFilter[ collected.size () ] ) );
                }
                else
                {
                    fileChooserPanel.setFileFilter ( GlobalConstants.ALL_FILES_FILTER );
                }
            }
        }
        else if ( prop.equals ( JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY ) )
        {
            final int mode = fileChooser.getFileSelectionMode ();
            fileChooserPanel.setFileSelectionMode ( FileSelectionMode.get ( mode ) );
        }
        else if ( prop.equals ( JFileChooser.DIRECTORY_CHANGED_PROPERTY ) )
        {
            if ( !ignoreFileSelectionChanges )
            {
                fileChooserPanel.setCurrentFolder ( fileChooser.getCurrentDirectory () );
            }
        }
        else if ( prop.equals ( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY ) )
        {
            // We are not listening to SELECTED_FILES_CHANGED_PROPERTY as it will only generate additional pointless event
            // Property SELECTED_FILE_CHANGED_PROPERTY event is always triggered so it is sufficient
            if ( !ignoreFileSelectionChanges )
            {
                final File[] selectedFiles = fileChooser.getSelectedFiles ();
                if ( selectedFiles.length > 0 )
                {
                    // Update displayed directory and select all files
                    // fileChooserPanel.setCurrentFolder ( selectedFiles[ 0 ].getParentFile () );
                    // fileChooserPanel.setSelectedFiles ( selectedFiles );
                    fileChooserPanel.setSelectedFiles ( selectedFiles );

                }
                else
                {
                    // Simply pass the file, it will be selected when directory is opened
                    // fileChooserPanel.setCurrentFolder ( fileChooser.getSelectedFile () );
                    fileChooserPanel.setSelectedFile ( fileChooser.getSelectedFile () );
                }
            }
        }
        else if ( prop.equals ( JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setChooserType ( getFileChooserType () );
        }
        else if ( prop.equals ( JFileChooser.FILE_HIDING_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setShowHiddenFiles ( !fileChooser.isFileHidingEnabled () );
        }
        else if ( prop.equals ( JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setMultiSelectionEnabled ( fileChooser.isMultiSelectionEnabled () );
        }
        else if ( prop.equals ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
        {
            fileChooserPanel.applyComponentOrientation ( fileChooser.getComponentOrientation () );
        }
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( fileChooser );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( fileChooser, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( fileChooser, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns file chooser painter.
     *
     * @return file chooser painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets file chooser painter.
     * Pass null to remove file chooser painter.
     *
     * @param painter new file chooser painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( fileChooser, new DataRunnable<IFileChooserPainter> ()
        {
            @Override
            public void run ( final IFileChooserPainter newPainter )
            {
                WebFileChooserUI.this.painter = newPainter;
            }
        }, this.painter, painter, IFileChooserPainter.class, AdaptiveFileChooserPainter.class );
    }

    /**
     * Returns file chooser panel.
     *
     * @return file chooser panel
     */
    public WebFileChooserPanel getFileChooserPanel ()
    {
        return fileChooserPanel;
    }

    /**
     * Returns list of available file filters.
     *
     * @return list of available file filters
     */
    public List<AbstractFileFilter> getAvailableFilters ()
    {
        return fileChooserPanel.getAvailableFilters ();
    }

    /**
     * Returns currently active file filter.
     *
     * @return currently active file filter
     */
    public AbstractFileFilter getActiveFileFilter ()
    {
        return fileChooserPanel.getActiveFileFilter ();
    }

    /**
     * Returns whether file thumbnails are generated or not.
     *
     * @return true if file thumbnails are generated, false otherwise
     */
    public boolean isGenerateThumbnails ()
    {
        return fileChooserPanel.isGenerateThumbnails ();
    }

    /**
     * Sets whether file thumbnails should be generated or not.
     *
     * @param generate whether file thumbnails should be generated or not
     */
    public void setGenerateThumbnails ( final boolean generate )
    {
        fileChooserPanel.setGenerateThumbnails ( generate );
    }

    /**
     * Sets approve button text type.
     *
     * @param approveText approve button text type
     */
    public void setApproveButtonText ( final FileAcceptText approveText )
    {
        fileChooserPanel.setAcceptButtonText ( approveText );
    }

    /**
     * Sets approve button language key.
     *
     * @param key approve button language key
     */
    public void setApproveButtonLanguage ( final String key )
    {
        fileChooserPanel.setAcceptButtonLanguage ( key );
    }

    @Override
    public FileFilter getAcceptAllFileFilter ( final JFileChooser fc )
    {
        return GlobalConstants.ALL_FILES_FILTER;
    }

    @Override
    public FileView getFileView ( final JFileChooser fc )
    {
        return fileView;
    }

    /**
     * Sets file view.
     *
     * @param fileView new file view
     */
    public void setFileView ( final WebFileView fileView )
    {
        this.fileView = fileView;
    }

    @Override
    public String getApproveButtonText ( final JFileChooser fc )
    {
        return fileChooserPanel.getAcceptButtonText ();
    }

    @Override
    public String getDialogTitle ( final JFileChooser fc )
    {
        final String dialogTitle = fc.getDialogTitle ();
        return dialogTitle != null ? dialogTitle : LanguageManager.get ( "weblaf.filechooser.title" );
    }

    @Override
    public void rescanCurrentDirectory ( final JFileChooser fc )
    {
        fileChooserPanel.reloadCurrentFolder ();
    }

    @Override
    public void ensureFileIsVisible ( final JFileChooser fc, final File f )
    {
        //        // This is pretty annoying and pointless method, will ignore it for now
        //        ignoreFileSelectionChanges = true;
        //        fileChooserPanel.setSelectedFile ( f );
        //        ignoreFileSelectionChanges = true;
    }

    /**
     * Returns JFileChooser type converted into FileChooserType form.
     *
     * @return JFileChooser type converted into FileChooserType form
     */
    public FileChooserType getFileChooserType ()
    {
        if ( fileChooser.getDialogType () == JFileChooser.SAVE_DIALOG )
        {
            return FileChooserType.save;
        }
        else if ( fileChooser.getDialogType () == JFileChooser.OPEN_DIALOG )
        {
            return FileChooserType.open;
        }
        else
        {
            return FileChooserType.custom;
        }
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    /**
     * Special FileView for file chooser.
     */
    protected class WebFileView extends FileView
    {
        @Override
        public String getName ( final File f )
        {
            return FileUtils.getDisplayFileName ( f );
        }

        @Override
        public String getDescription ( final File f )
        {
            return getTypeDescription ( f );
        }

        @Override
        public String getTypeDescription ( final File f )
        {
            return FileUtils.getFileTypeDescription ( f );
        }

        @Override
        public Icon getIcon ( final File f )
        {
            return FileUtils.getFileIcon ( f );
        }
    }
}