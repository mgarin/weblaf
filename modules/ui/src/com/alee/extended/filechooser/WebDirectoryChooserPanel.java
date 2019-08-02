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

import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.pathfield.WebPathField;
import com.alee.extended.tree.FileTreeNode;
import com.alee.extended.tree.FileTreeRootType;
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.ToolbarLayout;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyCondition;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.icon.Icons;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.filefilter.*;

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
    protected AbstractFileFilter filter = new GroupedFileFilter ( FilterGroupType.AND, new DirectoriesFilter (), new NonHiddenFilter () );

    /**
     * Toolbar components.
     */
    protected WebButton folderUp;
    protected WebButton folderHome;
    protected WebButton folderDesktop;
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
     * Control components.
     */
    protected WebButton acceptButton;
    protected WebButton cancelButton;

    /**
     * Constructs new directory chooser panel.
     */
    public WebDirectoryChooserPanel ()
    {
        this ( StyleId.directorychooser );
    }

    /**
     * Constructs new directory chooser panel.
     *
     * @param id style ID
     */
    public WebDirectoryChooserPanel ( final StyleId id )
    {
        super ( id, new TableLayout ( new double[][]{
                { TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED }
        } ) );

        // Panel content
        add ( createToolBar (), "0,0" );
        add ( createPathField (), "0,1" );
        add ( createFileTree (), "0,2" );
        add ( createControlsPanel (), "0,3" );

        // Updating selected directory
        updateSelectedDirectory ( null, true, true );
        updateToolbarControlsState ();

        // Hotkeys preview action and hotkeys global condition
        HotkeyManager.installShowAllHotkeysAction ( this, Hotkey.F1 );
        HotkeyManager.addContainerHotkeyCondition ( this, new HotkeyCondition ()
        {
            @Override
            public boolean checkCondition ( final Component component )
            {
                return allowHotkeys ();
            }
        } );
    }

    /**
     * Returns file tree component.
     *
     * @return file tree component
     */
    protected WebScrollPane createFileTree ()
    {
        final WebScrollPane treeScroll = new WebScrollPane ( StyleId.directorychooserScroll.at ( this ) );
        treeScroll.setPreferredWidth ( 400 );

        fileTree = new WebFileTree ( StyleId.directorychooserFileSystem.at ( treeScroll ), FileTreeRootType.drives );
        fileTree.setVisibleRowCount ( 15 );
        fileTree.setFileFilter ( filter );
        fileTree.setSelectedFile ( selectedDirectory, true );
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
        fileTree.setEditable ( true );
        treeScroll.setViewportView ( fileTree );

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

        return treeScroll;
    }

    /**
     * Returns path field component.
     *
     * @return path field component
     */
    protected WebPathField createPathField ()
    {
        // Path field
        webPathField = new WebPathField ( StyleId.directorychooserPathField.at ( this ), selectedDirectory );
        webPathField.setFileFilter ( filter );
        webPathField.addPathFieldListener ( new PathFieldListener ()
        {
            @Override
            public void directoryChanged ( final File newDirectory )
            {
                updateSelectedDirectory ( webPathField.getSelectedPath (), false, true );
            }
        } );
        return webPathField;
    }

    /**
     * Returns toolbar component.
     *
     * @return toolbar component
     */
    protected WebToolBar createToolBar ()
    {
        // Controls pane
        final WebToolBar toolBar = new WebToolBar ( StyleId.directorychooserToolbar.at ( this ) );

        final StyleId folderUpId = StyleId.directorychooserFolderUpButton.at ( toolBar );
        folderUp = new WebButton ( folderUpId, "weblaf.ex.dirchooser.folderup", Icons.folderUp );
        folderUp.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.ALT_UP ).setHotkeyDisplayWay ( TooltipWay.down );
        folderUp.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( selectedDirectory != null )
                {
                    updateSelectedDirectory ( FileUtils.getParent ( selectedDirectory ), true, true );
                }
            }
        } );
        toolBar.add ( folderUp );

        final StyleId folderHomeId = StyleId.directorychooserHomeButton.at ( toolBar );
        folderHome = new WebButton ( folderHomeId, "weblaf.ex.dirchooser.home", Icons.folderHome );
        folderHome.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.ALT_H ).setHotkeyDisplayWay ( TooltipWay.trailing );
        folderHome.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                updateSelectedDirectory ( FileUtils.getUserHome (), true, true );
            }
        } );
        toolBar.add ( folderHome );

        final File desktop = FileUtils.getDesktop ();
        if ( desktop != null )
        {
            final StyleId desktopHomeId = StyleId.directorychooserDesktopButton.at ( toolBar );
            folderDesktop = new WebButton ( desktopHomeId, "weblaf.ex.dirchooser.desktop", Icons.folderDesktop );
            folderDesktop.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    updateSelectedDirectory ( desktop, true, true );
                }
            } );
            toolBar.add ( folderDesktop );
        }

        toolBar.addSeparator ();

        for ( final File file : FileTreeRootType.drives.getRoots () )
        {
            final StyleId driveId = StyleId.directorychooserDriveButton.at ( toolBar );
            final WebButton drive = new WebButton ( driveId, Icons.drive );
            drive.setToolTip ( FileUtils.getDisplayFileName ( file ) );
            drive.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    updateSelectedDirectory ( file, true, true );
                }
            } );
            toolBar.add ( drive );
        }

        final StyleId refreshId = StyleId.directorychooserRefreshButton.at ( toolBar );
        refresh = new WebButton ( refreshId, "weblaf.ex.dirchooser.refresh", Icons.refresh );
        refresh.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.F5 ).setHotkeyDisplayWay ( TooltipWay.leading );
        refresh.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( selectedDirectory != null )
                {
                    fileTree.reloadChildren ( selectedDirectory );
                }
                else
                {
                    fileTree.reloadRootNode ();
                }
            }
        } );
        toolBar.add ( refresh, ToolbarLayout.END );

        final StyleId folderNewId = StyleId.directorychooserNewFolderButton.at ( toolBar );
        folderNew = new WebButton ( folderNewId, "weblaf.ex.dirchooser.newfolder", Icons.folderNew );
        folderNew.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.CTRL_N ).setHotkeyDisplayWay ( TooltipWay.down );
        folderNew.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( selectedDirectory != null )
                {
                    final String defaultName = LM.get ( "weblaf.ex.dirchooser.newfolder.name" );
                    final String freeName = FileUtils.getAvailableName ( selectedDirectory, defaultName );
                    final File file = new File ( selectedDirectory, freeName );
                    if ( file.mkdir () )
                    {
                        // Updating files tree
                        fileTree.addFile ( selectedDirectory, file );

                        // Editing added folder name
                        fileTree.startEditingFile ( file );
                    }
                    else
                    {
                        final String message = LM.get ( "weblaf.ex.dirchooser.newfolder.error.text" );
                        final String title = LM.get ( "weblaf.ex.dirchooser.newfolder.error.title" );
                        WebOptionPane.showMessageDialog ( WebDirectoryChooserPanel.this, message, title, WebOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        } );
        toolBar.add ( folderNew, ToolbarLayout.END );

        final StyleId removeId = StyleId.directorychooserDeleteButton.at ( toolBar );
        remove = new WebButton ( removeId, "weblaf.ex.dirchooser.delete", Icons.remove );
        remove.addHotkey ( WebDirectoryChooserPanel.this, Hotkey.DELETE ).setHotkeyDisplayWay ( TooltipWay.down );
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
                final String message = LM.get ( "weblaf.ex.dirchooser.delete.confirm.text" );
                final String title = LM.get ( "weblaf.ex.dirchooser.delete.confirm.title" );
                final int confirm = WebOptionPane.showConfirmDialog ( WebDirectoryChooserPanel.this,
                        message, title, WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE );

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
                            index < count - 1 ? parentNode.getChildAt ( index ) : parentNode.getChildAt ( index - 1 ) );
                }
            }
        } );
        toolBar.add ( remove, ToolbarLayout.END );
        return toolBar;
    }

    /**
     * Returns controls panel.
     *
     * @return controls panel
     */
    protected WebPanel createControlsPanel ()
    {
        final StyleId controlsPanelId = StyleId.directorychooserControlsPanel.at ( this );
        final WebPanel controlsPanel = new WebPanel ( controlsPanelId, new BorderLayout ( 0, 0 ) );

        final StyleId acceptButtonId = StyleId.directorychooserAcceptButton.at ( controlsPanel );
        acceptButton = new WebButton ( acceptButtonId, "weblaf.ex.dirchooser.choose", Icons.accept );
        acceptButton.addHotkey ( Hotkey.CTRL_ENTER );
        acceptButton.setEnabled ( false );
        acceptButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fireAccepted ( getSelectedDirectory () );
            }
        } );

        final StyleId cancelButtonId = StyleId.directorychooserCancelButton.at ( controlsPanel );
        cancelButton = new WebButton ( cancelButtonId, "weblaf.ex.dirchooser.cancel", Icons.cancel );
        cancelButton.addHotkey ( Hotkey.ESCAPE );
        cancelButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fireCancelled ();
            }
        } );

        controlsPanel.add ( new GroupPanel ( 4, acceptButton, cancelButton ), BorderLayout.LINE_END );

        final List<String> properties = new ImmutableList<String> ( AbstractButton.TEXT_CHANGED_PROPERTY );
        SwingUtils.equalizeComponentsWidth ( properties, acceptButton, cancelButton );

        // Buttons updater
        updateButtonsState ( getSelectedDirectory () );
        addDirectoryChooserListener ( new DirectoryChooserAdapter ()
        {
            @Override
            public void selectionChanged ( final File file )
            {
                updateButtonsState ( file );
            }
        } );

        return controlsPanel;
    }

    /**
     * Forces buttons update according to selected file.
     *
     * @param file newly selected file
     */
    protected void updateButtonsState ( final File file )
    {
        acceptButton.setEnabled ( file != null );
    }

    /**
     * Updates toolbar controls state.
     */
    protected void updateToolbarControlsState ()
    {
        final File selectedFile = fileTree.getSelectedFile ();
        folderUp.setEnabled ( selectedFile != null && FileUtils.getParent ( selectedFile ) != null );
        folderNew.setEnabled ( selectedFile != null && selectedFile.canWrite () );
        remove.setEnabled ( selectedFile != null && FileUtils.getParent ( selectedFile ) != null && selectedFile.canWrite () );
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

    /**
     * Fires when directory selection accepted.
     *
     * @param file newly selected directory
     */
    protected void fireAccepted ( final File file )
    {
        for ( final DirectoryChooserListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.accepted ( file );
        }
    }

    /**
     * Fires when directory selection cancelled.
     */
    protected void fireCancelled ()
    {
        for ( final DirectoryChooserListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.cancelled ();
        }
    }
}