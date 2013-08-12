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

import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.laf.GlobalConstants;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.FileUtils;

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
import java.util.List;

/**
 * Custom UI for JFileChooser component.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileChooserUI extends FileChooserUI
{
    /**
     * File chooser which is decorated by this UI class.
     */
    private JFileChooser fileChooser;

    /**
     * Special FileView for file chooser.
     */
    private WebFileView fileView;

    /**
     * File chooser panel that contains al UI elements.
     */
    private WebFileChooserPanel fileChooserPanel;

    /**
     * FilChooser listeners.
     */
    private PropertyChangeListener propertyChangeListener;
    private FileChooserType chooserType;

    /**
     * Mark to ignore file selection property events.
     */
    private boolean ignoreFileSelectionChanges = false;

    /**
     * Returns an instance of the WebFileChooserUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebFileChooserUI
     */
    public static ComponentUI createUI ( JComponent c )
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

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    public void installUI ( JComponent c )
    {
        fileChooser = ( JFileChooser ) c;
        fileView = new WebFileView ();

        fileChooser.setLayout ( new BorderLayout () );
        fileChooser.setBorder ( BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );

        fileChooserPanel = new WebFileChooserPanel ( getFileChooserType (), fileChooser.getControlButtonsAreShown () );
        fileChooserPanel.setMultiSelectionEnabled ( fileChooser.isMultiSelectionEnabled () );
        fileChooserPanel.setApproveListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
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
            public void actionPerformed ( ActionEvent e )
            {
                fileChooser.cancelSelection ();
            }
        } );
        //        fileChooserPanel.addFileChooserListener ( new FileChooserListener ()
        //        {
        //            public void directoryChanged ( File newDirectory )
        //            {
        //                ignoreFileSelectionChanges = true;
        //                fileChooser.setCurrentDirectory ( newDirectory );
        //                ignoreFileSelectionChanges = false;
        //            }
        //
        //            public void selectionChanged ( List<File> selectedFiles )
        //            {
        //                ignoreFileSelectionChanges = true;
        //                fileChooser.setSelectedFiles ( selectedFiles.toArray ( new File[ selectedFiles.size () ] ) );
        //                ignoreFileSelectionChanges = false;
        //            }
        //        } );
        fileChooser.add ( fileChooserPanel, BorderLayout.CENTER );

        propertyChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
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
    public void uninstallUI ( JComponent c )
    {
        fileChooser.removePropertyChangeListener ( propertyChangeListener );
        fileChooserPanel = null;
        fileChooser = null;
        fileView = null;
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
    public List<DefaultFileFilter> getAvailableFilters ()
    {
        return fileChooserPanel.getAvailableFilters ();
    }

    /**
     * Returns currenly active file filter.
     *
     * @return currenly active file filter
     */
    public DefaultFileFilter getActiveFileFilter ()
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
    public void setGenerateThumbnails ( boolean generate )
    {
        fileChooserPanel.setGenerateThumbnails ( generate );
    }

    /**
     * Sets approve button text type.
     *
     * @param approveText approve button text type
     */
    public void setApproveButtonText ( FileApproveText approveText )
    {
        fileChooserPanel.setApproveButtonText ( approveText );
    }

    /**
     * Sets approve button language key.
     *
     * @param key approve button language key
     */
    public void setApproveButtonLanguage ( String key )
    {
        fileChooserPanel.setApproveButtonLanguage ( key );
    }

    /**
     * Fired when some of JFileChooser properties changes.
     *
     * @param event property change event
     */
    protected void propertyChanged ( PropertyChangeEvent event )
    {
        String prop = event.getPropertyName ();
        if ( prop.equals ( JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY ) )
        {
            // System.out.println ( "APPROVE_BUTTON_TEXT_CHANGED_PROPERTY " + fileChooser.getApproveButtonText () );
            fileChooserPanel.setApproveButtonText ( fileChooser.getApproveButtonText () );
        }
        else if ( prop.equals ( JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY ) )
        {
            // System.out.println ( "CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY " + fileChooser.getControlButtonsAreShown () );
            fileChooserPanel.setShowControlButtons ( fileChooser.getControlButtonsAreShown () );
        }
        else if ( prop.equals ( JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY ) )
        {
            // System.out.println ( "MULTI_SELECTION_ENABLED_CHANGED_PROPERTY " + fileChooser.isMultiSelectionEnabled () );
            fileChooserPanel.setMultiSelectionEnabled ( fileChooser.isMultiSelectionEnabled () );
        }
        else if ( prop.equals ( JFileChooser.FILE_FILTER_CHANGED_PROPERTY ) ||
                prop.equals ( JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY ) ||
                prop.equals ( JFileChooser.ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY ) )
        {
            final FileFilter[] filters = fileChooser.getChoosableFileFilters ();
            // System.out.println ( "FILE_FILTER_CHANGED_PROPERTY... " + filters.length );
            if ( filters.length == 0 )
            {
                fileChooserPanel.setFileFilter ( GlobalConstants.ALL_FILES_FILTER );
            }
            else
            {
                fileChooserPanel.setFileFilters ( filters );
            }
        }
        else if ( prop.equals ( JFileChooser.DIRECTORY_CHANGED_PROPERTY ) )
        {
            // System.out.println ( "DIRECTORY_CHANGED_PROPERTY " + fileChooser.getCurrentDirectory () +
            //         ( ignoreFileSelectionChanges ? "(ignored)" : "" ) );
            if ( !ignoreFileSelectionChanges )
            {
                fileChooserPanel.setCurrentFolder ( fileChooser.getCurrentDirectory () );
            }
        }
        else if ( prop.equals ( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY ) )
        {
            // System.out.println ( "SELECTED_FILE_CHANGED_PROPERTY " + fileChooser.getSelectedFiles ().length +
            //         ( ignoreFileSelectionChanges ? "(ignored)" : "" ) );
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
            // System.out.println ( "DIALOG_TYPE_CHANGED_PROPERTY " + getFileChooserType () );
            fileChooserPanel.setChooserType ( getFileChooserType () );
        }
        // todo FILE_SELECTION_MODE_CHANGED_PROPERTY (should work atop of filters)
    }

    /**
     * {@inheritDoc}
     */
    public FileFilter getAcceptAllFileFilter ( JFileChooser fc )
    {
        return GlobalConstants.ALL_FILES_FILTER;
    }

    /**
     * {@inheritDoc}
     */
    public FileView getFileView ( JFileChooser fc )
    {
        return fileView;
    }

    /**
     * @param fileView
     */
    public void setFileView ( WebFileView fileView )
    {
        this.fileView = fileView;
    }

    /**
     * {@inheritDoc}
     */
    public String getApproveButtonText ( JFileChooser fc )
    {
        return fileChooserPanel.getApproveButtonText ();
    }

    /**
     * {@inheritDoc}
     */
    public String getDialogTitle ( JFileChooser fc )
    {
        String dialogTitle = fc.getDialogTitle ();
        return dialogTitle != null ? dialogTitle : LanguageManager.get ( "weblaf.filechooser.title" );
    }

    /**
     * {@inheritDoc}
     */
    public void rescanCurrentDirectory ( JFileChooser fc )
    {
        fileChooserPanel.reloadCurrentFolder ();
    }

    /**
     * {@inheritDoc}
     */
    public void ensureFileIsVisible ( JFileChooser fc, File f )
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

    /**
     * Special FileView for file chooser.
     */
    protected class WebFileView extends FileView
    {
        /**
         * Constructs new WebFileView instance.
         */
        public WebFileView ()
        {
            super ();
        }

        /**
         * {@inheritDoc}
         */
        public String getName ( File f )
        {
            return FileUtils.getDisplayFileName ( f );
        }

        /**
         * {@inheritDoc}
         */
        public String getDescription ( File f )
        {
            return getTypeDescription ( f );
        }

        /**
         * {@inheritDoc}
         */
        public String getTypeDescription ( File f )
        {
            return FileUtils.getFileTypeDescription ( f );
        }

        /**
         * {@inheritDoc}
         */
        public Icon getIcon ( File f )
        {
            return FileUtils.getFileIcon ( f );
        }
    }
}