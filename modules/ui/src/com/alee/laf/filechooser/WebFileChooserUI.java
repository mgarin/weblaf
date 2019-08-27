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
import com.alee.api.jdk.Consumer;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LM;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
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
public class WebFileChooserUI extends WFileChooserUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( FileChooserPainter.class )
    protected IFileChooserPainter painter;

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
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebFileChooserUI ();
    }

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
     * Fired when some of {@link JFileChooser} properties changes.
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

    /**
     * Returns main file chooser panel.
     *
     * @param fileChooser {@link JFileChooser}
     * @return main file chooser panel
     */
    protected WebFileChooserPanel createPanel ( final JFileChooser fileChooser )
    {
        return new WebFileChooserPanel ( getFileChooserType (), fileChooser.getControlButtonsAreShown () );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( fileChooser, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( fileChooser, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( fileChooser, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( fileChooser );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( fileChooser, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( fileChooser );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( fileChooser, padding );
    }

    /**
     * Returns file chooser painter.
     *
     * @return file chooser painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets file chooser painter.
     * Pass null to remove file chooser painter.
     *
     * @param painter new file chooser painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( fileChooser, this, new Consumer<IFileChooserPainter> ()
        {
            @Override
            public void accept ( final IFileChooserPainter newPainter )
            {
                WebFileChooserUI.this.painter = newPainter;
            }
        }, this.painter, painter, IFileChooserPainter.class, AdaptiveFileChooserPainter.class );
    }

    @Override
    public WebFileChooserPanel getFileChooserPanel ()
    {
        return fileChooserPanel;
    }

    @Override
    public FileFilter getAcceptAllFileFilter ( final JFileChooser fc )
    {
        return new AllFilesFilter ();
    }

    @Override
    public FileView getFileView ( final JFileChooser fc )
    {
        return fileView;
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
        return dialogTitle != null ? dialogTitle : LM.get ( "weblaf.filechooser.title" );
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
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    /**
     * Special FileView for file chooser.
     *
     * todo 1. Make caching optional?
     * todo 2. Make multiple default implementations?
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