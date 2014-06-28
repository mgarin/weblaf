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

import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.tree.FileTreeNode;
import com.alee.extended.tree.FileTreeRootType;
import com.alee.extended.tree.WebFileTree;
import com.alee.global.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.filefilter.AbstractFileFilter;

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
 * This custom component provides a directory chooser functionality.
 * It similar to file chooser component but has its own unique interface and doesn't need a lot of configuring to use.
 *
 * @author Mikle Garin
 */

public class WebDirectoryChooserPanel extends WebPanel
{
    /**
     * Used icons.
     */
    protected static final ImageIcon FOLDER_UP_ICON =
            new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/folder_up.png" ) );
    protected static final ImageIcon FOLDER_HOME_ICON =
            new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/folder_home.png" ) );
    protected static final ImageIcon FOLDER_NEW_ICON =
            new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/folder_new.png" ) );
    protected static final ImageIcon REFRESH_ICON = new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/refresh.png" ) );
    protected static final ImageIcon REMOVE_ICON = new ImageIcon ( WebDirectoryChooserPanel.class.getResource ( "icons/remove.png" ) );

    /**
     * Directory chooser listeners.
     */
    protected List<DirectoryChooserListener> listeners = new ArrayList<DirectoryChooserListener> ( 1 );

    /**
     * Currently selected directory.
     */
    protected File selectedDirectory = FileUtils.getDiskRoots ()[ 0 ];

    /**
     * File filter.
     */
    protected AbstractFileFilter filter = GlobalConstants.NON_HIDDEN_DIRECTORIES_FILTER;

    /**
     * Toolbar components.
     */
    protected WebButton folderUp;
    protected WebButton folderHome;
    protected WebButton folderNew;
    protected WebButton refresh;
    protected WebButton remove;

    /**
     * Path field.
     */
    protected WebPathField webPathField;

    /**
     * File tree.
     */
    protected WebFileTree fileTree;
    protected TreeSelectionListener fileTreeListener;

    /**
     * Constructs new directory chooser panel.
     */
    public WebDirectoryChooserPanel ()
    {
        super ();

        // Panel settings
        setOpaque ( true );

        // Controls pane
        final WebToolBar contolsToolbar = new WebToolBar ( WebToolBar.HORIZONTAL );
        contolsToolbar.setToolbarStyle ( ToolbarStyle.attached );
        contolsToolbar.setFloatable ( false );

        folderUp = new WebButton ( FOLDER_UP_ICON );
        folderUp.setLanguage ( "weblaf.ex.dirchooser.folderup" );
        folderUp.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.ALT_UP ).setHotkeyDisplayWay ( TooltipWay.down );
        folderUp.setRolloverDecoratedOnly ( true );
        folderUp.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( selectedDirectory != null )
                {
                    updateSelectedDirectory ( selectedDirectory.getParentFile (), true, true );
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                updateSelectedDirectory ( FileUtils.getUserHome (), true, true );
            }
        } );
        contolsToolbar.add ( folderHome );

        contolsToolbar.addSeparator ();

        for ( final File file : FileTreeRootType.drives.getRoots () )
        {
            final WebButton root = new WebButton ( FileUtils.getFileIcon ( file ) );
            TooltipManager.setTooltip ( root, FileUtils.getDisplayFileName ( file ) );
            root.setRolloverDecoratedOnly ( true );
            root.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    updateSelectedDirectory ( file, true, true );
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( selectedDirectory != null )
                {
                    fileTree.reloadChilds ( selectedDirectory );
                }
                else
                {
                    fileTree.reloadRootNode ();
                }
            }
        } );
        contolsToolbar.add ( refresh, ToolbarLayout.END );

        folderNew = new WebButton ( FOLDER_NEW_ICON );
        folderNew.setLanguage ( "weblaf.ex.dirchooser.newfolder" );
        folderNew.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.CTRL_N ).setHotkeyDisplayWay ( TooltipWay.down );
        folderNew.setRolloverDecoratedOnly ( true );
        folderNew.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( selectedDirectory != null )
                {
                    final String defaultName = LanguageManager.get ( "weblaf.ex.dirchooser.newfolder.name" );
                    final String freeName = FileUtils.getAvailableName ( selectedDirectory, defaultName );
                    final File file = new File ( selectedDirectory, freeName );
                    if ( file.mkdir () )
                    {
                        // Updating filestree
                        fileTree.addFile ( selectedDirectory, file );

                        // Editing added folder name
                        fileTree.startEditingFile ( file );
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final File file = fileTree.getSelectedFile ();
                if ( file == null )
                {
                    return;
                }

                // Displaying delete confirmation
                final String message = LanguageManager.get ( "weblaf.ex.dirchooser.delete.confirm.text" );
                final String title = LanguageManager.get ( "weblaf.ex.dirchooser.delete.confirm.title" );
                final int confirm = WebOptionPane
                        .showConfirmDialog ( WebDirectoryChooserPanel.this, message, title, WebOptionPane.YES_NO_OPTION,
                                WebOptionPane.QUESTION_MESSAGE );

                // Proceed if delete was confirmed
                if ( confirm == WebOptionPane.YES_OPTION )
                {
                    // Retrieving index of deleted file node in parent node
                    final FileTreeNode parentNode = fileTree.getSelectedNode ().getParent ();
                    final int index = parentNode.indexOfFileChild ( file );
                    final int count = parentNode.getChildCount ();

                    // Removing file
                    FileUtils.deleteFile ( file );
                    fileTree.removeFile ( file );

                    // Restoring selection
                    fileTree.setSelectedNode ( count == 1 ? parentNode :
                            ( index < count - 1 ? parentNode.getChildAt ( index ) : parentNode.getChildAt ( index - 1 ) ) );
                }
            }
        } );
        contolsToolbar.add ( remove, ToolbarLayout.END );

        // Path field
        webPathField = new WebPathField ( selectedDirectory );
        webPathField.setFileFilter ( filter );
        webPathField.addPathFieldListener ( new PathFieldListener ()
        {
            @Override
            public void directoryChanged ( final File newDirectory )
            {
                updateSelectedDirectory ( webPathField.getSelectedPath (), false, true );
            }
        } );

        // Files tree
        fileTree = new WebFileTree ( FileTreeRootType.drives );
        fileTree.setVisibleRowCount ( 15 );
        fileTree.setFileFilter ( filter );
        fileTree.setSelectedFile ( selectedDirectory, true );
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
        fileTree.setEditable ( true );

        // Selected directory update
        fileTreeListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                updateSelectedDirectory ( fileTree.getSelectedFile (), true, false );
            }
        };
        fileTree.addTreeSelectionListener ( fileTreeListener );

        // Toolbar update
        fileTree.addTreeSelectionListener ( new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                updateToolbarControlsState ();
            }
        } );

        // Tree scroll
        final WebScrollPane treeScroll = new WebScrollPane ( fileTree );
        treeScroll.setPreferredWidth ( 400 );

        // Panel content
        setLayout ( new BorderLayout ( 0, 3 ) );
        add ( contolsToolbar, BorderLayout.NORTH );
        final WebPanel panel = new WebPanel ( new BorderLayout ( 0, 1 ) );
        panel.setMargin ( 0, 3, 2, 3 );
        panel.add ( webPathField, BorderLayout.NORTH );
        panel.add ( treeScroll, BorderLayout.CENTER );
        add ( panel, BorderLayout.CENTER );

        updateSelectedDirectory ( null, true, true );
        updateToolbarControlsState ();
    }

    /**
     * Updates toolbar controls state.
     */
    protected void updateToolbarControlsState ()
    {
        final File selectedFile = fileTree.getSelectedFile ();

        folderUp.setEnabled ( selectedFile != null && selectedFile.getParentFile () != null );

        folderNew.setEnabled ( selectedFile != null && selectedFile.canWrite () );
        remove.setEnabled ( selectedFile != null && selectedFile.getParentFile () != null &&
                selectedFile.canWrite () );
    }

    /**
     * Updates currently selected directory.
     *
     * @param directory  directory to select
     * @param updatePath whether to update path field or not
     * @param updateTree whether to update file tree or not
     */
    protected void updateSelectedDirectory ( final File directory, final boolean updatePath, final boolean updateTree )
    {
        this.selectedDirectory = directory;

        if ( updatePath )
        {
            webPathField.setSelectedPath ( directory );
        }

        if ( updateTree )
        {
            if ( directory != null )
            {
                fileTree.expandToFile ( directory, false, true, new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        fileTree.removeTreeSelectionListener ( fileTreeListener );
                        fileTree.setSelectedNode ( fileTree.getNode ( directory ) );
                        fileTree.addTreeSelectionListener ( fileTreeListener );
                    }
                } );
            }
            else
            {
                fileTree.clearSelection ();
                fileTree.scrollToStart ();
            }
        }

        fireSelectionChanged ( this.selectedDirectory );
    }

    /**
     * Returns directory chooser file filter.
     *
     * @return directory chooser file filter
     */
    public AbstractFileFilter getFilter ()
    {
        return filter;
    }

    /**
     * Sets directory chooser file filter.
     *
     * @param filter directory chooser file filter
     */
    public void setFilter ( final AbstractFileFilter filter )
    {
        this.filter = filter;
        fileTree.setFileFilter ( filter );
    }

    /**
     * Returns currently selected directory.
     *
     * @return currently selected directory
     */
    public File getSelectedDirectory ()
    {
        return selectedDirectory;
    }

    /**
     * Sets currently selected directory.
     *
     * @param selectedDirectory currently selected directory
     */
    public void setSelectedDirectory ( final File selectedDirectory )
    {
        updateSelectedDirectory ( selectedDirectory, true, true );
    }

    /**
     * Returns whether hotkeys are allowed or not.
     *
     * @return true if hotkeys are allowed, false otherwise
     */
    public boolean allowHotkeys ()
    {
        // todo Remove this when hotkeys system will get updated
        return !fileTree.isEditing () && !webPathField.isEditing () /*&& !fileList.isEditing()*/;
    }

    /**
     * Adds directory chooser listener.
     *
     * @param listener directory chooser listener to add
     */
    public void addDirectoryChooserListener ( final DirectoryChooserListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes directory chooser listener.
     *
     * @param listener directory chooser listener to remove
     */
    public void removeDirectoryChooserListener ( final DirectoryChooserListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Fires when directory selection changed.
     *
     * @param file newly selected directory
     */
    protected void fireSelectionChanged ( final File file )
    {
        for ( final DirectoryChooserListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ( file );
        }
    }
}