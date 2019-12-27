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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.utils.FileUtils;
import com.alee.utils.filefilter.AllFilesFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom UI for {@link JFileChooser} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebFileChooserUI extends WFileChooserUI
{
    /**
     * Runtime variables.
     */
    protected transient JFileChooser fileChooser;
    protected transient WebFileView fileView;
    protected transient WebFileChooserPanel fileChooserPanel;
    protected transient boolean ignoreFileSelectionChanges = false;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the {@link WebFileChooserUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebFileChooserUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebFileChooserUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
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
            public void actionPerformed ( @NotNull final ActionEvent e )
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
            public void actionPerformed ( @NotNull final ActionEvent e )
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
            public void propertyChange ( @NotNull final PropertyChangeEvent evt )
            {
                propertyChanged ( evt );
            }
        };
        fileChooser.addPropertyChangeListener ( propertyChangeListener );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
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
     * Fired when some of {@link JFileChooser} properties changes.
     *
     * @param event property change event
     */
    protected void propertyChanged ( @NotNull final PropertyChangeEvent event )
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
                    fileChooserPanel.setFileFilter ( new AllFilesFilter () );
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
            /**
             * We are not listening to {@link JFileChooser#SELECTED_FILES_CHANGED_PROPERTY} as it will only generate unnecessary event.
             * Property {@link JFileChooser#SELECTED_FILE_CHANGED_PROPERTY} event is always triggered so it is sufficient.
             */
            if ( !ignoreFileSelectionChanges )
            {
                if ( event.getNewValue () instanceof File )
                {
                    // Simply pass the file, it will be selected when directory is opened
                    fileChooserPanel.setSelectedFile ( fileChooser.getSelectedFile () );
                }
                else
                {
                    // Update displayed directory and select all files
                    fileChooserPanel.setSelectedFiles ( fileChooser.getSelectedFiles () );
                }
            }
        }
        else if ( prop.equals ( JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY ) )
        {
            fileChooserPanel.setChooserType ( FileChooserType.forType ( fileChooser.getDialogType () ) );
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

    /**
     * Returns main file chooser panel.
     *
     * @param fileChooser {@link JFileChooser}
     * @return main file chooser panel
     */
    @NotNull
    protected WebFileChooserPanel createPanel ( @NotNull final JFileChooser fileChooser )
    {
        return new WebFileChooserPanel (
                FileChooserType.forType ( fileChooser.getDialogType () ),
                fileChooser.getControlButtonsAreShown ()
        );
    }

    @NotNull
    @Override
    public WebFileChooserPanel getFileChooserPanel ()
    {
        return fileChooserPanel;
    }

    @Override
    public FileFilter getAcceptAllFileFilter ( @NotNull final JFileChooser fc )
    {
        return new AllFilesFilter ();
    }

    @Override
    public FileView getFileView ( @NotNull final JFileChooser fc )
    {
        return fileView;
    }

    @Override
    public String getApproveButtonText ( @NotNull final JFileChooser fc )
    {
        return fileChooserPanel.getAcceptButtonText ();
    }

    @Override
    public String getDialogTitle ( @NotNull final JFileChooser fc )
    {
        final String dialogTitle = fc.getDialogTitle ();
        return dialogTitle != null ? dialogTitle : LM.get ( "weblaf.filechooser.title" );
    }

    @Override
    public void rescanCurrentDirectory ( @NotNull final JFileChooser fc )
    {
        fileChooserPanel.reloadCurrentFolder ();
    }

    @Override
    public void ensureFileIsVisible ( @NotNull final JFileChooser fc, @NotNull final File f )
    {
        //        // This is pretty annoying and pointless method, will ignore it for now
        //        ignoreFileSelectionChanges = true;
        //        fileChooserPanel.setSelectedFile ( f );
        //        ignoreFileSelectionChanges = true;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return null;
    }

    /**
     * Special FileView for file chooser.
     *
     * todo 1. Make caching optional?
     * todo 2. Make multiple default implementations?
     */
    protected class WebFileView extends FileView
    {
        @NotNull
        @Override
        public String getName ( @NotNull final File f )
        {
            return FileUtils.getDisplayFileName ( f );
        }

        @NotNull
        @Override
        public String getDescription ( @NotNull final File f )
        {
            return getTypeDescription ( f );
        }

        @NotNull
        @Override
        public String getTypeDescription ( @NotNull final File f )
        {
            return FileUtils.getFileTypeDescription ( f );
        }

        @Nullable
        @Override
        public Icon getIcon ( @NotNull final File f )
        {
            return FileUtils.getFileIcon ( f );
        }
    }
}