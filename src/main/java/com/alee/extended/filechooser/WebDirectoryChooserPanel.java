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

import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 13.10.11 Time: 4:08
 */

public class WebDirectoryChooserPanel extends WebPanel
{
    private static final ImageIcon FOLDER_UP_ICON = new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/folder_up.png" ) );
    private static final ImageIcon FOLDER_HOME_ICON =
            new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/folder_home.png" ) );
    private static final ImageIcon FOLDER_NEW_ICON =
            new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/folder_new.png" ) );
    private static final ImageIcon REFRESH_ICON = new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/refresh.png" ) );
    private static final ImageIcon REMOVE_ICON = new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/remove.png" ) );

    public static DefaultFileFilter defaultFileFilter = GlobalConstants.DIRECTORIES_FILTER;

    private List<DirectoryChooserListener> listeners = new ArrayList<DirectoryChooserListener> ();

    private File selectedFolder = FileUtils.getDiskRoots ()[ 0 ];

    private WebPathField webPathField;

    private WebFileTree fileTree;
    private TreeSelectionListener fileTreeListener;

    private WebButton folderUp;
    private WebButton folderHome;
    private WebButton folderNew;
    private WebButton refresh;
    private WebButton remove;

    public WebDirectoryChooserPanel ()
    {
        super ();

        // Panel settings
        setOpaque ( true );

        // Controls pane
        WebToolBar contolsToolbar = new WebToolBar ( WebToolBar.HORIZONTAL );
        contolsToolbar.setToolbarStyle ( ToolbarStyle.attached );
        contolsToolbar.setFloatable ( false );

        folderUp = new WebButton ( FOLDER_UP_ICON );
        folderUp.setLanguage ( "weblaf.ex.dirchooser.folderup" );
        folderUp.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.ALT_UP ).setHotkeyDisplayWay ( TooltipWay.down );
        folderUp.setRolloverDecoratedOnly ( true );
        folderUp.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( selectedFolder != null )
                {
                    updateShownFolder ( selectedFolder.getParentFile (), true, true );
                }
            }
        } );
        contolsToolbar.add ( folderUp );

        folderHome = new WebButton ( FOLDER_HOME_ICON );
        folderHome.setLanguage ( "weblaf.ex.dirchooser.home" );
        folderHome.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.ALT_H ).setHotkeyDisplayWay ( TooltipWay.trailing );
        folderHome.setRolloverDecoratedOnly ( true );
        folderHome.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                updateShownFolder ( FileUtils.getUserHome (), true, true );
            }
        } );
        contolsToolbar.add ( folderHome );

        contolsToolbar.addSeparator ();

        for ( final File file : FileUtils.getDiskRoots () )
        {
            WebButton root = new WebButton ( FileUtils.getFileIcon ( file ) );
            TooltipManager.setTooltip ( root, FileUtils.getDisplayFileName ( file ) );
            root.setRolloverDecoratedOnly ( true );
            root.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    updateShownFolder ( file, true, true );
                }
            } );
            contolsToolbar.add ( root );
        }


        refresh = new WebButton ( REFRESH_ICON );
        refresh.setLanguage ( "weblaf.ex.dirchooser.refresh" );
        refresh.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.F5 ).setHotkeyDisplayWay ( TooltipWay.leading );
        refresh.setRolloverDecoratedOnly ( true );
        refresh.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                fileTree.removeFile ( selectedFolder );
                updateShownFolder ( selectedFolder, true, true );
            }
        } );
        contolsToolbar.add ( refresh, ToolbarLayout.END );

        folderNew = new WebButton ( FOLDER_NEW_ICON );
        folderNew.setLanguage ( "weblaf.ex.dirchooser.newfolder" );
        folderNew.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.CTRL_N ).setHotkeyDisplayWay ( TooltipWay.down );
        folderNew.setRolloverDecoratedOnly ( true );
        folderNew.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( selectedFolder != null )
                {
                    final String defaultName = LanguageManager.get ( "weblaf.ex.dirchooser.newfolder.name" );
                    final String freeName = FileUtils.getAvailableName ( selectedFolder, defaultName );
                    final File file = new File ( selectedFolder, freeName );
                    if ( file.mkdir () )
                    {
                        // Updating filestree
                        fileTree.addFile ( selectedFolder, file );

                        // Selecting added folder
                        fileTree.setSelectedFile ( file );

                        // Starting folder name editing
                        fileTree.startEditingAtPath ( fileTree.getSelectionPath () );
                    }
                    else
                    {
                        final String message = LanguageManager.get ( "weblaf.ex.dirchooser.newfolder.error.text" );
                        final String title = LanguageManager.get ( "weblaf.ex.dirchooser.newfolder.error.title" );
                        WebOptionPane.showMessageDialog ( WebDirectoryChooserPanel.this, message, title, WebOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        } );
        contolsToolbar.add ( folderNew, ToolbarLayout.END );

        remove = new WebButton ( REMOVE_ICON );
        remove.setLanguage ( "weblaf.ex.dirchooser.delete" );
        remove.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.DELETE ).setHotkeyDisplayWay ( TooltipWay.down );
        remove.setRolloverDecoratedOnly ( true );
        remove.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                File file = fileTree.getSelectedFile ();
                if ( file == null )
                {
                    return;
                }

                final String message = LanguageManager.get ( "weblaf.ex.dirchooser.delete.confirm.text" );
                final String title = LanguageManager.get ( "weblaf.ex.dirchooser.delete.confirm.title" );
                int confirm = WebOptionPane.showConfirmDialog ( WebDirectoryChooserPanel.this, message, title, WebOptionPane.YES_NO_OPTION,
                        WebOptionPane.QUESTION_MESSAGE );

                if ( confirm == WebOptionPane.YES_OPTION )
                {
                    FileUtils.deleteFile ( file );

                    fileTree.removeFile ( file );

                    updateShownFolder ( selectedFolder, true, true );
                }
            }
        } );
        contolsToolbar.add ( remove, ToolbarLayout.END );

        // Path field
        webPathField = new WebPathField ( selectedFolder );
        webPathField.setFileFilter ( defaultFileFilter );
        webPathField.addWebPathFieldListener ( new PathFieldListener ()
        {
            public void directoryChanged ( File newDirectory )
            {
                updateShownFolder ( webPathField.getSelectedPath (), false, true );
            }
        } );

        // Files tree
        fileTree = new WebFileTree ();
        fileTree.setVisibleRowCount ( 15 );
        fileTree.setFileFilter ( defaultFileFilter );
        fileTree.setSelectedFile ( selectedFolder, true );
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );

        // Shown folder update
        fileTreeListener = new TreeSelectionListener ()
        {
            public void valueChanged ( TreeSelectionEvent e )
            {
                updateShownFolder ( fileTree.getSelectedFile (), true, false );
            }
        };
        fileTree.addTreeSelectionListener ( fileTreeListener );

        // Toolbar update
        fileTree.addTreeSelectionListener ( new TreeSelectionListener ()
        {
            public void valueChanged ( TreeSelectionEvent e )
            {
                updateToolbar ();
            }
        } );

        // Tree scroll
        WebScrollPane treeScroll = new WebScrollPane ( fileTree );
        treeScroll.setPreferredWidth ( 400 );

        // Initial toolbar update
        updateToolbar ();

        // Panel content
        setLayout ( new BorderLayout ( 0, 3 ) );
        add ( contolsToolbar, BorderLayout.NORTH );
        WebPanel panel = new WebPanel ( new BorderLayout ( 0, 1 ) );
        panel.setMargin ( 0, 3, 2, 3 );
        panel.add ( webPathField, BorderLayout.NORTH );
        panel.add ( treeScroll, BorderLayout.CENTER );
        add ( panel, BorderLayout.CENTER );
    }

    private void updateToolbar ()
    {
        File selectedFile = fileTree.getSelectedFile ();

        folderUp.setEnabled ( selectedFile != null && selectedFile.getParentFile () != null );

        folderNew.setEnabled ( selectedFile != null && selectedFile.canWrite () );
        remove.setEnabled ( selectedFile != null && selectedFile.getParentFile () != null &&
                selectedFile.canWrite () );
    }

    public boolean allowHotkeys ()
    {
        return !fileTree.isEditing () && !webPathField.isEditing () /*&& !fileList.isEditing()*/;
    }

    private void updateShownFolder ( File folder, boolean updatePath, boolean updateTree )
    {
        this.selectedFolder = folder;

        if ( updatePath )
        {
            webPathField.setSelectedPath ( folder );
        }

        if ( updateTree )
        {
            fileTree.removeTreeSelectionListener ( fileTreeListener );
            fileTree.setSelectedFile ( folder, true );
            fileTree.addTreeSelectionListener ( fileTreeListener );
        }

        fireSelectionChanged ( this.selectedFolder );
    }

    public File getSelectedFolder ()
    {
        return selectedFolder;
    }

    public void setSelectedFolder ( File selectedFolder )
    {
        updateShownFolder ( selectedFolder, true, true );
    }

    public void addWebDirectoryChooserListener ( DirectoryChooserListener listener )
    {
        listeners.add ( listener );
    }

    public void removeWebDirectoryChooserListener ( DirectoryChooserListener listener )
    {
        listeners.remove ( listener );
    }

    private void fireSelectionChanged ( File file )
    {
        for ( DirectoryChooserListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ( file );
        }
    }
}
