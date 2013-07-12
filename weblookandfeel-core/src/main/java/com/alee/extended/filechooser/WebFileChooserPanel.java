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
import com.alee.extended.filefilter.GroupType;
import com.alee.extended.filefilter.GroupedFileFilter;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.list.FileElement;
import com.alee.extended.list.FileViewType;
import com.alee.extended.list.WebFileList;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.tree.FileTreeNode;
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.GlobalConstants;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.editor.ListEditAdapter;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollBarUI;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 28.06.11 Time: 0:31
 */

public class WebFileChooserPanel extends WebPanel
{
    private static final ImageIcon BACKWARD_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/backward.png" ) );
    private static final ImageIcon FORWARD_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/forward.png" ) );

    private static final ImageIcon FOLDER_UP_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/folder_up.png" ) );
    private static final ImageIcon FOLDER_HOME_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/folder_home.png" ) );
    private static final ImageIcon FOLDER_NEW_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/folder_new.png" ) );
    private static final ImageIcon REFRESH_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/refresh.png" ) );
    private static final ImageIcon REMOVE_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/remove.png" ) );
    private static final ImageIcon VIEW_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/view.png" ) );

    //    private static final ImageIcon VIEW_TABLE_ICON =
    //            new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/table.png" ) );
    private static final ImageIcon VIEW_ICONS_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/icons.png" ) );
    private static final ImageIcon VIEW_TILES_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/tiles.png" ) );

    private static final ImageIcon SETTINGS_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/settings.png" ) );
    private static final ImageIcon OK_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/ok.png" ) );
    private static final ImageIcon CANCEL_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/cancel.png" ) );

    private static final File nonexisting = new File ( "nonexisting" );

    private List<FileChooserListener> chooserListeners = new ArrayList<FileChooserListener> ();

    private List<DefaultFileFilter> availableFilters;
    private DefaultFileFilter previewFilter;
    private DefaultFileFilter chooseFilter;

    private FilesToChoose filesToChoose;

    private File currentFolder = nonexisting;
    private List<File> navigationHistory = new ArrayList<File> ();
    private int currentHistoryIndex = -1;

    private SelectionMode selectionMode = SelectionMode.SINGLE_SELECTION;

    private FileViewType fileViewType = FileViewType.tiles;

    private WebPathField pathField;
    private WebFileTree fileTree;
    private TreeSelectionListener fileTreeListener;
    private WebFileList fileList;

    private WebButton backward;
    private WebButton forward;

    private WebButton folderUp;
    private WebButton folderHome;
    private WebButton folderNew;
    private WebButton refresh;
    private WebButton remove;
    private WebButton view;

    //    private WebTextField selectedFiles;
    private WebFileChooserField selectedFiles;
    private WebComboBox fileFilters;

    private WebButton ok;
    private ActionListener okListener;
    private WebButton cancel;
    private ActionListener cancelListener;

    public WebFileChooserPanel ( boolean showControlButtons )
    {
        super ();

        // Panel settings
        setOpaque ( false );
        setLayout ( new BorderLayout ( 0, 0 ) );

        // Default filters
        filesToChoose = FilesToChoose.filesOnly;
        availableFilters = GlobalConstants.DEFAULT_FILTERS;
        previewFilter = availableFilters.get ( 0 );
        chooseFilter = availableFilters.get ( 0 );


        // North panel
        WebToolBar toolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar.setToolbarStyle ( ToolbarStyle.attached );
        toolBar.setSpacing ( 0 );
        toolBar.setFloatable ( false );
        add ( toolBar, BorderLayout.NORTH );


        // Path component
        pathField = new WebPathField ();
        pathField.setFileFilter ( previewFilter, false );
        pathField.addWebPathFieldListener ( new PathFieldListener ()
        {
            public void directoryChanged ( File newDirectory )
            {
                updateShownFolder ( newDirectory, false, true, true );
            }
        } );

        // Controls

        backward = new WebButton ( BACKWARD_ICON );
        backward.setLanguage ( "weblaf.filechooser.back" );
        backward.addHotkey ( WebFileChooserPanel.this, Hotkey.BACKSPACE ).setHotkeyDisplayWay ( TooltipWay.down );
        backward.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_LEFT ).setHotkeyDisplayWay ( TooltipWay.down );
        backward.setRolloverDecoratedOnly ( true );
        backward.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                currentHistoryIndex -= 1;
                updateShownFolder ( navigationHistory.get ( currentHistoryIndex ), true, true, true, false );
            }
        } );

        forward = new WebButton ( FORWARD_ICON );
        forward.setLanguage ( "weblaf.filechooser.forward" );
        forward.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_RIGHT ).setHotkeyDisplayWay ( TooltipWay.trailing );
        forward.setRolloverDecoratedOnly ( true );
        forward.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                currentHistoryIndex += 1;
                updateShownFolder ( navigationHistory.get ( currentHistoryIndex ), true, true, true, false );
            }
        } );

        folderUp = new WebButton ( FOLDER_UP_ICON );
        folderUp.setLanguage ( "weblaf.filechooser.folderup" );
        folderUp.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_UP ).setHotkeyDisplayWay ( TooltipWay.down );
        folderUp.setRolloverDecoratedOnly ( true );
        folderUp.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( currentFolder != null )
                {
                    updateShownFolder ( currentFolder.getParentFile (), true, true, true );
                }
            }
        } );

        folderHome = new WebButton ( FOLDER_HOME_ICON );
        folderHome.setLanguage ( "weblaf.filechooser.home" );
        folderHome.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_HOME ).setHotkeyDisplayWay ( TooltipWay.down );
        folderHome.setRolloverDecoratedOnly ( true );
        folderHome.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                updateShownFolder ( FileUtils.getUserHome (), true, true, true );
            }
        } );

        refresh = new WebButton ( REFRESH_ICON );
        refresh.setLanguage ( "weblaf.filechooser.refresh" );
        refresh.addHotkey ( WebFileChooserPanel.this, Hotkey.F5 ).setHotkeyDisplayWay ( TooltipWay.down );
        refresh.setRolloverDecoratedOnly ( true );
        refresh.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                updateCurrentFolderView ();
            }
        } );

        folderNew = new WebButton ( FOLDER_NEW_ICON );
        folderNew.setLanguage ( "weblaf.filechooser.newfolder" );
        folderNew.addHotkey ( WebFileChooserPanel.this, Hotkey.CTRL_N ).setHotkeyDisplayWay ( TooltipWay.down );
        folderNew.setRolloverDecoratedOnly ( true );
        folderNew.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( currentFolder != null )
                {
                    final String defaultName = LanguageManager.get ( "weblaf.filechooser.newfolder.name" );
                    final String freeName = FileUtils.getAvailableName ( currentFolder, defaultName );
                    final File file = new File ( currentFolder, freeName );
                    if ( file.mkdir () )
                    {
                        // Updating list
                        fileList.getFileListModel ().addElement ( new FileElement ( file ) );

                        // Updating filestree
                        fileTree.addFile ( currentFolder, file );

                        // Chaging folder name
                        fileList.setSelectedValue ( file, true );
                        fileList.editSelectedCell ();
                    }
                    else
                    {
                        final String message = LanguageManager.get ( "weblaf.filechooser.newfolder.error.text" );
                        final String title = LanguageManager.get ( "weblaf.filechooser.newfolder.error.title" );
                        WebOptionPane.showMessageDialog ( WebFileChooserPanel.this, message, title, WebOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        } );

        remove = new WebButton ( REMOVE_ICON );
        remove.setLanguage ( "weblaf.filechooser.delete" );
        remove.addHotkey ( WebFileChooserPanel.this, Hotkey.DELETE ).setHotkeyDisplayWay ( TooltipWay.down );
        remove.setRolloverDecoratedOnly ( true );
        remove.setEnabled ( false );
        remove.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                Object[] files = fileList.getSelectedValues ();

                WebPanel all = new WebPanel ( new BorderLayout ( 0, 0 ) );
                all.add ( new WebLabel ( LanguageManager.get ( "weblaf.filechooser.delete.confirm.text" ) ), BorderLayout.NORTH );
                WebPanel deleteFilesPanel = new WebPanel ( new VerticalFlowLayout ( VerticalFlowLayout.TOP, true, false ) );
                for ( Object f : files )
                {
                    File file = ( File ) f;
                    deleteFilesPanel.add ( new WebLabel ( ( ( File ) f ).getName (), FileUtils.getFileIcon ( file ), WebLabel.LEFT ) );
                }
                WebScrollPane scroll = new WebScrollPane ( deleteFilesPanel )
                {
                    public Dimension getPreferredSize ()
                    {
                        Dimension ps = super.getPreferredSize ();
                        ps.width = ps.width + WebScrollBarUI.LENGTH;
                        ps.height = Math.min ( ps.height, 100 );
                        return ps;
                    }
                };
                all.add ( scroll, BorderLayout.CENTER );

                final String title = LanguageManager.get ( "weblaf.filechooser.delete.confirm.title" );
                int confirm = WebOptionPane.showConfirmDialog ( WebFileChooserPanel.this, all, title, WebOptionPane.YES_NO_OPTION,
                        WebOptionPane.QUESTION_MESSAGE );

                if ( confirm == WebOptionPane.YES_OPTION )
                {
                    FileUtils.deleteFiles ( files );

                    fileTree.removeFiles ( FileUtils.toFilesList ( files ) );

                    updateCurrentFolderView ();
                }
            }
        } );

        view = new WebButton ( VIEW_ICON );
        view.setLanguage ( "weblaf.filechooser.view" );
        view.setRolloverDecoratedOnly ( true );
        view.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                WebPopupMenu viewChoose = new WebPopupMenu ();

                // todo Table view
                //                WebRadioButtonMenuItem table = new WebRadioButtonMenuItem ( VIEW_TABLE_ICON );
                //                table.setLanguage ( "weblaf.filechooser.view.table" );
                //                table.setSelected ( getFileViewType ().equals ( FileViewType.table ) );
                //                table.setEnabled ( false );
                //                table.addActionListener ( new ActionListener ()
                //                {
                //                    public void actionPerformed ( ActionEvent e )
                //                    {
                //                        updateFilesView ( FileViewType.table );
                //                    }
                //                } );
                //                viewChoose.add ( table );

                WebRadioButtonMenuItem icons = new WebRadioButtonMenuItem ( VIEW_ICONS_ICON );
                icons.setLanguage ( "weblaf.filechooser.view.icons" );
                icons.setSelected ( getFileViewType ().equals ( FileViewType.icons ) );
                icons.addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
                    {
                        setFileViewType ( FileViewType.icons );
                    }
                } );
                viewChoose.add ( icons );

                WebRadioButtonMenuItem tiles = new WebRadioButtonMenuItem ( VIEW_TILES_ICON );
                tiles.setLanguage ( "weblaf.filechooser.view.tiles" );
                tiles.setSelected ( getFileViewType ().equals ( FileViewType.tiles ) );
                tiles.addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
                    {
                        setFileViewType ( FileViewType.tiles );
                    }
                } );
                viewChoose.add ( tiles );

                ButtonGroup viewGroup = new ButtonGroup ();
                //                viewGroup.add ( table );
                viewGroup.add ( icons );
                viewGroup.add ( tiles );

                if ( view.getComponentOrientation ().isLeftToRight () )
                {
                    viewChoose.show ( view, view.getWidth () - viewChoose.getPreferredSize ().width, view.getHeight () );
                }
                else
                {
                    viewChoose.show ( view, 0, view.getHeight () );
                }
            }
        } );


        toolBar.add ( backward );
        toolBar.add ( forward );
        toolBar.addFill ( pathField );
        toolBar.addToEnd ( folderUp );
        toolBar.addToEnd ( folderHome );
        toolBar.addToEnd ( refresh );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( folderNew );
        toolBar.addToEnd ( remove );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( view );


        // Files filestree

        fileTree = new WebFileTree ();
        fileTree.setAutoExpandSelectedNode ( false );
        fileTree.setFileFilter ( new GroupedFileFilter ( GroupType.AND, GlobalConstants.DIRECTORIES_FILTER, previewFilter ) );

        final WebScrollPane treeScroll = new WebScrollPane ( fileTree, false );
        treeScroll.setBorder ( BorderFactory.createMatteBorder ( 0, 0, 0, 1, StyleConstants.darkBorderColor ) );
        treeScroll.setPreferredSize ( new Dimension ( 160, 1 ) );


        fileTreeListener = new TreeSelectionListener ()
        {
            public void valueChanged ( TreeSelectionEvent e )
            {
                if ( fileTree.getSelectionCount () > 0 )
                {
                    // Scrolling to opened path
                    TreePath selectionPath = fileTree.getSelectionPath ();
                    Rectangle bounds = fileTree.getPathBounds ( selectionPath );
                    if ( bounds != null )
                    {
                        fileTree.scrollRectToVisible ( bounds );
                    }

                    // Updating shown folder
                    updateShownFolder ( ( ( FileTreeNode ) selectionPath.getLastPathComponent () ).getFile (), true, true, false );
                }
            }
        };
        fileTree.addTreeSelectionListener ( fileTreeListener );


        // Main view

        fileList = new WebFileList ();
        fileList.setFileFilter ( previewFilter );
        fileList.setFileViewType ( fileViewType );
        fileList.setGenerateThumbnails ( true );
        fileList.setDropMode ( DropMode.ON );
        fileList.setEditable ( true );
        fileList.setTransferHandler ( new FileDropHandler ()
        {
            protected boolean filesImported ( List<File> files )
            {
                if ( files.size () > 0 )
                {
                    File file = files.get ( 0 );
                    if ( file.isDirectory () )
                    {
                        updateShownFolder ( file, true, true, true );
                    }
                    else
                    {
                        updateShownFolder ( file.getParentFile (), true, true, true );
                        fileList.setSelectedValue ( file, true );
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } );
        fileList.getInputMap ().put ( KeyStroke.getKeyStroke ( KeyEvent.VK_ENTER, 0 ), "openFolder" );
        fileList.getActionMap ().put ( "openFolder", new AbstractAction ()
        {
            public boolean isEnabled ()
            {
                return fileList.getSelectedIndex () != -1;
            }

            public void actionPerformed ( ActionEvent e )
            {
                updateShownFolder ( ( ( FileElement ) fileList.getSelectedValue () ).getFile (), true, true, true );
            }
        } );
        fileList.addMouseListener ( new MouseAdapter ()
        {
            public void mouseClicked ( MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) && e.getClickCount () % 2 == 0 && fileList.getSelectedIndex () != -1 )
                {
                    File file = ( ( FileElement ) fileList.getSelectedValue () ).getFile ();
                    if ( file.isDirectory () )
                    {
                        updateShownFolder ( file, true, true, true );
                    }
                    else if ( getSelectedFiles ().size () > 0 )
                    {
                        selectFiles ( new ActionEvent ( e, e.getID (), "select files" ) );
                    }
                }
            }
        } );
        fileList.addListSelectionListener ( new ListSelectionListener ()
        {
            public void valueChanged ( ListSelectionEvent e )
            {
                remove.setEnabled ( currentFolder != null && fileList.getSelectedIndex () != -1 &&
                        !currentFolder.equals ( nonexisting ) );
            }
        } );
        fileList.addListEditListener ( new ListEditAdapter ()
        {
            public void editFinished ( int index, Object oldValue, Object newValue )
            {
                // todo Proper rename in tree
                // Refreshing files filestree
                fileTree.removeFile ( ( File ) oldValue );
                fileTree.addFile ( ( ( File ) newValue ).getParentFile (), ( File ) newValue );

                // Refreshing other parts
                updateChosenFiles ();

                // todo update path field menus
            }
        } );

        // todo Context menu
        //- Select file(s) -if filter allows
        //- folder up -if not top
        //----------------
        //- delete
        //- new folder
        //- rename
        //- refresh
        //----------------
        //- view(if viewable image)/run(if runnable)/open(if folder)
        //- edit
        //----------------
        //- view

        // Updating and adding initial history record
        updateShownFolder ( null, true, true, true, true );

        // Tiles scroll
        final WebScrollPane filesListScroll = new WebScrollPane ( fileList, false );
        filesListScroll.setBorder ( BorderFactory.createMatteBorder ( 0, 1, 0, 0, StyleConstants.darkBorderColor ) );
        filesListScroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        filesListScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

        // Setting proper scroll preferred size
        Dimension oneCell = fileList.getCellBounds ( 0, 0 ).getSize ();
        Insets bi = filesListScroll.getInsets ();
        filesListScroll
                .setPreferredSize ( new Dimension ( oneCell.width * ( fileViewType.equals ( FileViewType.tiles ) ? 3 : 8 ) + bi.left +
                        bi.right + WebScrollBarUI.LENGTH + 1, oneCell.height * 6 + bi.top + bi.bottom + 1 ) );

        WebSplitPane split = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT );
        split.setOneTouchExpandable ( true );
        split.setLeftComponent ( treeScroll );
        split.setRightComponent ( filesListScroll );
        split.setDividerLocation ( 160 );
        split.setBorder ( BorderFactory.createMatteBorder ( 0, 0, 1, 0, StyleConstants.darkBorderColor ) );
        add ( split, BorderLayout.CENTER );


        // South panel

        WebPanel southPanel = new WebPanel ();
        southPanel.setLayout ( new ToolbarLayout ( 4 ) );
        southPanel.setOpaque ( false );
        southPanel.setMargin ( 4, 4, 4, 4 );
        add ( southPanel, BorderLayout.SOUTH );

        //        WebButton settingsButton = WebButton
        //                .createIconWebButton ( SETTINGS_ICON, 2, 2, StyleConstants.innerShadeWidth, true );
        //        settingsButton.addActionListener ( new ActionListener ()
        //        {
        //            public void actionPerformed ( ActionEvent e )
        //            {
        //                WebModalPopup settingsDialog = new WebModalPopup ();
        //                settingsDialog.setLayout ( new BorderLayout () );
        //
        //                // todo Settings button
        //
        //                WebComboBox combo = new WebComboBox ( new String[]{ "12345", "67890", "13579" } );
        //                settingsDialog.add ( combo );
        //
        //                PopupManager
        //                        .showModalPopup ( WebFileChooserPanel.this, settingsDialog, false, false );
        //            }
        //        } );

        WebLabel selectedFilesLabel = new WebLabel ();
        selectedFilesLabel.setLanguage ( "weblaf.filechooser.files.selected" );
        selectedFilesLabel.setDrawShade ( true );
        selectedFilesLabel.setMargin ( 0, 4, 0, 0 );
        southPanel.add ( selectedFilesLabel );

        selectedFiles = new WebFileChooserField ( false );
        selectedFiles.setShowRemoveButton ( false );
        selectedFiles.setShowFileShortName ( true );
        selectedFiles.setFilesDropEnabled ( false );
        southPanel.add ( selectedFiles, ToolbarLayout.FILL );

        fileFilters = new WebComboBox ( availableFilters.toArray () );
        fileFilters.setSelectedIndex ( 0 );
        fileFilters.setRenderer ( new WebComboBoxCellRenderer ( fileFilters )
        {
            public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
            {
                DefaultFileFilter defaultFileFilter = ( DefaultFileFilter ) value;
                WebLabel renderer = ( WebLabel ) super.getListCellRendererComponent ( list, "", index, isSelected, cellHasFocus );
                renderer.setIcon ( defaultFileFilter.getIcon () );
                renderer.setText ( defaultFileFilter.getDescription () );
                return renderer;
            }
        } );
        fileFilters.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                updateChooserFilters ();
                fileFilters.revalidate ();
                ( ( JComponent ) fileFilters.getParent () ).revalidate ();
            }
        } );

        ok = new WebButton ( "", OK_ICON );
        ok.setLanguage ( "weblaf.filechooser.choose" );
        ok.addHotkey ( WebFileChooserPanel.this, Hotkey.CTRL_ENTER ).setHotkeyDisplayWay ( TooltipWay.up );
        if ( StyleConstants.highlightControlButtons )
        {
            ok.setShineColor ( StyleConstants.greenHighlight );
        }
        ok.putClientProperty ( GroupPanel.FILL_CELL, true );
        ok.setEnabled ( false );
        ok.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                selectFiles ( e );
            }
        } );

        cancel = new WebButton ( "", CANCEL_ICON );
        cancel.setLanguage ( "weblaf.filechooser.cancel" );
        cancel.addHotkey ( WebFileChooserPanel.this, Hotkey.ESCAPE ).setHotkeyDisplayWay ( TooltipWay.up );
        if ( StyleConstants.highlightControlButtons )
        {
            cancel.setShineColor ( StyleConstants.redHighlight );
        }
        cancel.putClientProperty ( GroupPanel.FILL_CELL, true );
        cancel.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( cancelListener != null )
                {
                    cancelListener.actionPerformed ( e );
                }
            }
        } );

        southPanel.add ( showControlButtons ? new GroupPanel ( 4, fileFilters, ok, cancel ) : fileFilters, ToolbarLayout.END );

        SwingUtils.equalizeComponentsSize ( ok, cancel );

        // Updating selection mode
        setSelectionMode ( selectionMode );

        // Updating chosen files field
        updateChosenFiles ();

        // Adding choose listener
        fileList.addListSelectionListener ( new ListSelectionListener ()
        {
            public void valueChanged ( ListSelectionEvent e )
            {
                updateChosenFiles ();
            }
        } );
    }

    public boolean allowHotkeys ()
    {
        return !fileTree.isEditing () && !pathField.isEditing () /*&& !fileList.isEditing()*/;
    }

    private void selectFiles ( ActionEvent e )
    {
        if ( okListener != null )
        {
            okListener.actionPerformed ( e );
        }
    }

    public void setCurrentDirectory ( String dir )
    {
        setCurrentDirectory ( dir != null ? new File ( dir ) : null );
    }

    public void setCurrentDirectory ( File dir )
    {
        updateShownFolder ( dir, true, true, true, true );
    }

    public File getCurrentDirectory ()
    {
        return currentFolder;
    }

    public SelectionMode getSelectionMode ()
    {
        return selectionMode;
    }

    public void setSelectionMode ( SelectionMode selectionMode )
    {
        this.selectionMode = selectionMode;
        selectedFiles.setSelectionMode ( selectionMode );
        fileList.setSelectionMode ( selectionMode.equals ( SelectionMode.SINGLE_SELECTION ) ? ListSelectionModel.SINGLE_SELECTION :
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
    }

    private void updateChooserFilters ()
    {
        DefaultFileFilter filter = ( DefaultFileFilter ) fileFilters.getSelectedItem ();
        setPreviewFilter ( filter );
        setChooseFilter ( getActualChooseFilter ( filter ) );
    }

    private DefaultFileFilter getActualChooseFilter ( DefaultFileFilter filter )
    {
        if ( filesToChoose.equals ( FilesToChoose.filesOnly ) )
        {
            return new GroupedFileFilter ( GroupType.AND, GlobalConstants.FILES_FILTER, filter );
        }
        else if ( filesToChoose.equals ( FilesToChoose.foldersOnly ) )
        {
            return new GroupedFileFilter ( GroupType.AND, GlobalConstants.DIRECTORIES_FILTER, filter );
        }
        else
        {
            return filter;
        }
    }

    private void updateChosenFiles ()
    {
        if ( fileList.getSelectedIndex () != -1 )
        {
            List<File> accepted = getSelectedFiles ();

            // Updating controls state
            selectedFiles.setSelectedFiles ( accepted );
            ok.setEnabled ( accepted.size () > 0 );

            // Firing events
            fireFileSelectionChanged ( accepted );
        }
        else
        {
            // Updating controls state
            selectedFiles.setSelectedFile ( null );
            ok.setEnabled ( false );

            // Firing events
            fireFileSelectionChanged ( new ArrayList<File> () );
        }
    }

    public List<File> getSelectedFiles ()
    {
        List<File> accepted = new ArrayList<File> ();
        for ( Object value : fileList.getSelectedValues () )
        {
            File file = ( ( FileElement ) value ).getFile ();
            if ( chooseFilter.accept ( file ) )
            {
                accepted.add ( file );
            }
        }
        return accepted;
    }

    public List<DefaultFileFilter> getAvailableFilters ()
    {
        return availableFilters;
    }

    public void setAvailableFilters ( List<DefaultFileFilter> availableFilters )
    {
        if ( availableFilters == null || availableFilters.size () == 0 )
        {
            return;
        }

        // Settings new filter
        this.availableFilters = availableFilters;

        // Remembering selected filter
        DefaultFileFilter old = ( DefaultFileFilter ) fileFilters.getSelectedItem ();

        // Updating filters list
        fileFilters.setModel ( new DefaultComboBoxModel ( availableFilters.toArray () ) );
        if ( fileFilters.isShowing () )
        {
            fileFilters.updateUI ();
        }

        // Replacing old filter if it doesn't exist anymore
        if ( old == null || !this.availableFilters.contains ( old ) )
        {
            fileFilters.setSelectedIndex ( 0 );
        }
        else
        {
            fileFilters.setSelectedItem ( old );
        }
    }

    public FilesToChoose getFilesToChoose ()
    {
        return filesToChoose;
    }

    public void setFilesToChoose ( FilesToChoose filesToChoose )
    {
        this.filesToChoose = filesToChoose;
        updateChooserFilters ();
    }

    public ActionListener getOkListener ()
    {
        return okListener;
    }

    public void setOkListener ( ActionListener okListener )
    {
        this.okListener = okListener;
    }

    public ActionListener getCancelListener ()
    {
        return cancelListener;
    }

    public void setCancelListener ( ActionListener cancelListener )
    {
        this.cancelListener = cancelListener;
    }

    public DefaultFileFilter getPreviewFilter ()
    {
        return previewFilter;
    }

    public void setPreviewFilter ( DefaultFileFilter previewFilter )
    {
        this.previewFilter = previewFilter;
        updateCurrentFolderView ();
    }

    public DefaultFileFilter getChooseFilter ()
    {
        return chooseFilter;
    }

    public void setChooseFilter ( DefaultFileFilter chooseFilter )
    {
        this.chooseFilter = chooseFilter;
        updateChosenFiles ();
    }

    private void updateCurrentFolderView ()
    {
        updateShownFolder ( currentFolder, true, true, false );
    }

    private void updateShownFolder ( File file, boolean updatePath, boolean updateList, boolean updateTree )
    {
        updateShownFolder ( file, updatePath, updateList, updateTree, true );
    }

    private void updateShownFolder ( File file, boolean updatePath, boolean updateList, boolean updateTree, boolean updateHistory )
    {
        if ( file == null || file.isDirectory () )
        {
            // Replacing root file for non-windows OS
            if ( file == null && !SystemUtils.isWindows () )
            {
                file = FileUtils.getDiskRoots ()[ 0 ];
            }

            // Filling history if needed
            boolean directoryChanged = false;
            if ( updateHistory && !isSameFolder ( file ) )
            {
                // Deleting old records if needed
                if ( currentHistoryIndex > -1 )
                {
                    while ( currentHistoryIndex + 1 < navigationHistory.size () )
                    {
                        navigationHistory.remove ( currentHistoryIndex + 1 );
                    }
                }

                // Saving new history record
                navigationHistory.add ( file );
                currentHistoryIndex = navigationHistory.size () - 1;

                directoryChanged = true;
            }

            // Updating controls
            backward.setEnabled ( currentHistoryIndex > 0 );
            forward.setEnabled ( currentHistoryIndex + 1 < navigationHistory.size () );

            // Updating view
            if ( updatePath )
            {
                updatePath ( file );
            }
            if ( updateTree )
            {
                updateTree ( file );
            }
            if ( updateList )
            {
                updateList ( file );
            }
            currentFolder = file;

            // Updating contols
            folderNew.setEnabled ( currentFolder != null );
            folderUp.setEnabled (
                    SystemUtils.isWindows () ? currentFolder != null : currentFolder != null && currentFolder.getParentFile () != null );

            // Firing events
            if ( directoryChanged )
            {
                fireDirectoryChanged ( currentFolder );
            }
        }
    }

    private void updatePath ( File file )
    {
        pathField.setSelectedPath ( file );
    }

    private void updateTree ( File file )
    {
        fileTree.removeTreeSelectionListener ( fileTreeListener );
        fileTree.setSelectedFile ( file );
        fileTree.addTreeSelectionListener ( fileTreeListener );
    }

    private void updateList ( File file )
    {
        fileList.setDisplayedDirectory ( file );
    }

    private boolean isSameFolder ( File file )
    {
        return FileUtils.equals ( currentFolder, file );
    }

    public FileViewType getFileViewType ()
    {
        return fileViewType;
    }

    public void setFileViewType ( FileViewType fileViewType )
    {
        this.fileViewType = fileViewType;
        this.fileList.setFileViewType ( fileViewType );
        updateList ( currentFolder );
    }

    public boolean isGenerateImagePreviews ()
    {
        return fileList.isGenerateThumbnails ();
    }

    public void setGenerateImagePreviews ( boolean generate )
    {
        this.fileList.setGenerateThumbnails ( generate );
    }

    public WebFileTree getFileTree ()
    {
        return fileTree;
    }

    public WebFileList getFileList ()
    {
        return fileList;
    }

    public WebPathField getPathField ()
    {
        return pathField;
    }

    public void addFileChooserListener ( FileChooserListener listener )
    {
        chooserListeners.add ( listener );
    }

    public void removeFileChooserListener ( FileChooserListener listener )
    {
        chooserListeners.remove ( listener );
    }

    private void fireDirectoryChanged ( File newDirectory )
    {
        for ( FileChooserListener listener : CollectionUtils.copy ( chooserListeners ) )
        {
            listener.directoryChanged ( newDirectory );
        }
    }

    private void fireFileSelectionChanged ( List<File> selectedFiles )
    {
        for ( FileChooserListener listener : CollectionUtils.copy ( chooserListeners ) )
        {
            listener.selectionChanged ( selectedFiles );
        }
    }
}
