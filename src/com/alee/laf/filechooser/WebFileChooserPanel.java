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

import com.alee.extended.drag.FileDropHandler;
import com.alee.extended.filechooser.PathFieldListener;
import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.extended.filechooser.WebFileTable;
import com.alee.extended.filechooser.WebPathField;
import com.alee.extended.filefilter.AbstractFileFilter;
import com.alee.extended.filefilter.FilterGroupType;
import com.alee.extended.filefilter.GroupedFileFilter;
import com.alee.extended.filefilter.NonHiddenFilter;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.list.FileElement;
import com.alee.extended.list.FileListViewType;
import com.alee.extended.list.WebFileList;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.GlobalConstants;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.laf.combobox.WebComboBoxStyle;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.list.editor.ListEditAdapter;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.*;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.DataProvider;
import com.alee.utils.swing.DefaultFileFilterListCellRenderer;
import com.alee.utils.text.FileNameProvider;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * File chooser panel component.
 * Basically used to provide WebFileChooserUI with all required UI elements.
 *
 * @author Mikle Garin
 */

public class WebFileChooserPanel extends WebPanel
{
    /**
     * todo 1. When setting "show hidden files" to false - move out from hidden directories
     * todo 2. Proper hotkeys usage within window
     * todo 3. Context menu for file selection components
     */

    /**
     * Used icons.
     */
    public static final ImageIcon BACKWARD_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/backward.png" ) );
    public static final ImageIcon FORWARD_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/forward.png" ) );
    public static final ImageIcon HISTORY_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/history.png" ) );
    public static final ImageIcon FOLDER_UP_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/folder_up.png" ) );
    public static final ImageIcon FOLDER_HOME_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/folder_home.png" ) );
    public static final ImageIcon FOLDER_NEW_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/folder_new.png" ) );
    public static final ImageIcon REFRESH_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/refresh.png" ) );
    public static final ImageIcon REMOVE_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/remove.png" ) );
    public static final ImageIcon VIEW_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/view.png" ) );
    public static final ImageIcon VIEW_ICONS_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/icons.png" ) );
    public static final ImageIcon VIEW_TILES_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/tiles.png" ) );
    public static final ImageIcon VIEW_TABLE_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/table.png" ) );
    public static final ImageIcon SETTINGS_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/settings.png" ) );
    public static final ImageIcon APPROVE_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/approve.png" ) );
    public static final ImageIcon CANCEL_ICON = new ImageIcon ( WebFileChooserPanel.class.getResource ( "icons/cancel.png" ) );

    /**
     * File name provider.
     */
    public static final FileNameProvider quotedFileNameProvider = new FileNameProvider ()
    {
        @Override
        public String provide ( final File object )
        {
            return "\"" + super.provide ( object ) + "\"";
        }
    };

    /**
     * Whether to show control buttons or not.
     */
    protected boolean showControlButtons;

    /**
     * File chooser type.
     */
    protected FileChooserType chooserType;

    /**
     * Whether should display hidden files or not.
     */
    protected boolean showHiddenFiles = false;

    /**
     * Default file filter for this file chooser panel.
     */
    protected AbstractFileFilter fileFilter;

    /**
     * All available file filters for this file chooser panel.
     */
    protected List<AbstractFileFilter> availableFilters;

    /**
     * Directory files view type.
     */
    protected FileChooserViewType viewType = FileChooserViewType.tiles;

    /**
     * Whether multiply files selection allowed or not.
     */
    protected boolean multiSelectionEnabled = false;

    /**
     * Currently viewed folder.
     */
    protected File currentFolder = null;

    /**
     * Current view history index.
     */
    protected int currentHistoryIndex = -1;

    /**
     * Current view history.
     */
    protected List<File> navigationHistory = new ArrayList<File> ();

    /**
     * Custom approve button listener.
     * By default panel does nothing on approve button press.
     */
    protected ActionListener approveListener;

    /**
     * Custom cancel button listener.
     * By default panel does nothing on cancel button press.
     */
    protected ActionListener cancelListener;

    /**
     * File chooser listeners.
     */
    protected List<FileChooserListener> chooserListeners = new ArrayList<FileChooserListener> ( 1 );

    /**
     * North panel components.
     */
    protected WebButton backward;
    protected WebButton forward;
    protected WebButton history;
    protected WebPathField pathField;
    protected PathFieldListener pathFieldListener;
    protected WebButton folderUp;
    protected WebButton folderHome;
    protected WebButton folderNew;
    protected WebButton refresh;
    protected WebButton remove;
    protected WebButton view;

    /**
     * Center panel components.
     */
    protected WebFileTree fileTree;
    protected TreeSelectionListener fileTreeListener;
    protected WebScrollPane treeScroll;
    protected WebFileList fileList;
    protected WebScrollPane fileListScroll;
    protected WebFileTable fileTable;
    protected WebScrollPane fileTableScroll;
    protected WebSplitPane centralSplit;

    /**
     * South panel components.
     */
    protected WebFileChooserField selectedFilesViewField;
    protected WebTextField selectedFilesTextField;
    protected WebPanel selectedFilesPanel;
    protected WebPanel controlsPanel;
    protected WebComboBox fileFilters;
    protected WebButton approveButton;
    protected WebButton cancelButton;

    /**
     * Editing state provider.
     * todo This is a temporary workaround for HotkeysManager actions
     */
    protected DataProvider<Boolean> hotkeysAllowed = new DataProvider<Boolean> ()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Boolean provide ()
        {
            return !fileTree.isEditing () && !fileList.isEditing () && !fileTable.isEditing () && !pathField.isEditing () &&
                    !selectedFilesTextField.isFocusOwner ();
        }
    };

    /**
     * Hidden files filter attached to this panel.
     */
    protected HiddenFilesFilter hiddenFilesFilter = new HiddenFilesFilter ();

    /**
     * Constructs new file chooser panel without contol buttons.
     */
    public WebFileChooserPanel ()
    {
        this ( FileChooserType.open, false );
    }

    /**
     * Constructs new file chooser panel with or without contol buttons.
     *
     * @param chooserType file chooser type
     */
    public WebFileChooserPanel ( final FileChooserType chooserType )
    {
        this ( chooserType, false );
    }

    /**
     * Constructs new file chooser panel with or without contol buttons.
     *
     * @param showControlButtons whether to add control buttons or not
     */
    public WebFileChooserPanel ( final boolean showControlButtons )
    {
        this ( FileChooserType.open, showControlButtons );
    }

    /**
     * Constructs new file chooser panel with or without contol buttons.
     *
     * @param chooserType        file chooser type
     * @param showControlButtons whether to add control buttons or not
     */
    public WebFileChooserPanel ( final FileChooserType chooserType, final boolean showControlButtons )
    {
        super ();

        // Default settings
        this.showControlButtons = showControlButtons;
        this.chooserType = chooserType;

        // Panel settings
        setOpaque ( false );
        setLayout ( new BorderLayout ( 0, 0 ) );

        // Panel content
        add ( createNorthContent (), BorderLayout.NORTH );
        add ( createCenterContent (), BorderLayout.CENTER );
        add ( createSouthContent (), BorderLayout.SOUTH );

        // Updating view data
        updateSelectionMode ();
        updateDirectoryComponentFilters ();
        setFileFilter ( GlobalConstants.ALL_FILES_FILTER );
        restoreButtonText ();
    }

    /**
     * Returns north panel content.
     *
     * @return north panel content
     */
    protected Component createNorthContent ()
    {
        final WebToolBar toolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar.setToolbarStyle ( ToolbarStyle.attached );
        toolBar.setSpacing ( 0 );
        toolBar.setFloatable ( false );
        toolBar.addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateToolbarStyle ();
            }

            @Override
            public void ancestorMoved ( final AncestorEvent event )
            {
                updateToolbarStyle ();
            }

            private void updateToolbarStyle ()
            {
                toolBar.setUndecorated ( SwingUtils.isLafDecorated ( WebFileChooserPanel.this ) );
            }
        } );
        add ( toolBar, BorderLayout.NORTH );

        backward = new WebButton ( BACKWARD_ICON );
        backward.setLanguage ( "weblaf.filechooser.back" );
        backward.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_LEFT ).setHotkeyDisplayWay ( TooltipWay.down );
        backward.setRolloverDecoratedOnly ( true );
        backward.setFocusable ( false );
        backward.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () )
                {
                    updateHistoryState ( currentHistoryIndex - 1 );
                }
            }
        } );

        forward = new WebButton ( FORWARD_ICON );
        forward.setLanguage ( "weblaf.filechooser.forward" );
        forward.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_RIGHT ).setHotkeyDisplayWay ( TooltipWay.trailing );
        forward.setRolloverDecoratedOnly ( true );
        forward.setFocusable ( false );
        forward.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () )
                {
                    updateHistoryState ( currentHistoryIndex + 1 );
                }
            }
        } );

        history = new WebButton ( HISTORY_ICON );
        history.setLanguage ( "weblaf.filechooser.history" );
        history.setRolloverDecoratedOnly ( true );
        history.setFocusable ( false );
        history.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu historyPopup = new WebPopupMenu ();

                final WebList historyList = new WebList ( navigationHistory );
                historyList.setOpaque ( false );
                historyList.setVisibleRowCount ( Math.min ( 10, navigationHistory.size () ) );
                historyList.setRolloverSelectionEnabled ( true );
                historyList.setCellRenderer ( new WebComboBoxCellRenderer ( historyList )
                {
                    @Override
                    public Component getListCellRendererComponent ( final JList list, final Object value, final int index,
                                                                    final boolean isSelected, final boolean cellHasFocus )
                    {
                        super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );

                        final File file = ( File ) value;
                        if ( file == null )
                        {
                            renderer.setIcon ( FileUtils.getMyComputerIcon () );
                            renderer.setText ( LanguageManager.get ( "weblaf.filechooser.root" ) );
                        }
                        else
                        {
                            renderer.setIcon ( FileUtils.getFileIcon ( file ) );
                            renderer.setText ( TextUtils.shortenText ( FileUtils.getDisplayFileName ( file ), 40, true ) );
                        }
                        renderer.setBoldFont ( index == currentHistoryIndex );

                        return renderer;
                    }
                } );
                historyList.addMouseListener ( new MouseAdapter ()
                {
                    @Override
                    public void mouseReleased ( final MouseEvent e )
                    {
                        updateHistoryState ( historyList.getSelectedIndex () );
                        historyPopup.setVisible ( false );
                    }
                } );

                final WebScrollPane scrollPane = new WebScrollPane ( historyList, false, false );
                scrollPane.setOpaque ( false );
                scrollPane.getViewport ().setOpaque ( false );
                scrollPane.setShadeWidth ( 0 );

                final WebScrollBar vsb = scrollPane.getWebVerticalScrollBar ();
                vsb.setThumbRound ( WebComboBoxStyle.scrollBarThumbRound );
                vsb.setMargin ( WebComboBoxStyle.scrollBarMargin );
                vsb.setButtonsVisible ( WebComboBoxStyle.scrollBarButtonsVisible );
                vsb.setDrawTrack ( WebComboBoxStyle.scrollBarTrackVisible );

                historyPopup.add ( scrollPane );

                historyPopup.showBelowMiddle ( history );

                historyList.setSelectedIndex ( currentHistoryIndex );
                historyList.scrollToCell ( currentHistoryIndex );
            }
        } );

        pathField = new WebPathField ();
        pathFieldListener = new PathFieldListener ()
        {
            @Override
            public void directoryChanged ( final File newDirectory )
            {
                updateCurrentFolder ( newDirectory, UpdateSource.path );
            }
        };
        pathField.addPathFieldListener ( pathFieldListener );

        folderUp = new WebButton ( FOLDER_UP_ICON );
        folderUp.setLanguage ( "weblaf.filechooser.folderup" );
        folderUp.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_UP ).setHotkeyDisplayWay ( TooltipWay.down );
        folderUp.setRolloverDecoratedOnly ( true );
        folderUp.setFocusable ( false );
        folderUp.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () && currentFolder != null )
                {
                    updateCurrentFolder ( currentFolder.getParentFile (), UpdateSource.toolbar );
                }
            }
        } );

        folderHome = new WebButton ( FOLDER_HOME_ICON );
        folderHome.setLanguage ( "weblaf.filechooser.home" );
        folderHome.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_HOME ).setHotkeyDisplayWay ( TooltipWay.down );
        folderHome.setRolloverDecoratedOnly ( true );
        folderHome.setFocusable ( false );
        folderHome.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () )
                {
                    updateCurrentFolder ( FileUtils.getUserHome (), UpdateSource.toolbar );
                }
            }
        } );

        refresh = new WebButton ( REFRESH_ICON );
        refresh.setLanguage ( "weblaf.filechooser.refresh" );
        refresh.addHotkey ( WebFileChooserPanel.this, Hotkey.F5 ).setHotkeyDisplayWay ( TooltipWay.down );
        refresh.setRolloverDecoratedOnly ( true );
        refresh.setFocusable ( false );
        refresh.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () )
                {
                    reloadCurrentFolder ();
                }
            }
        } );

        folderNew = new WebButton ( FOLDER_NEW_ICON );
        folderNew.setLanguage ( "weblaf.filechooser.newfolder" );
        folderNew.addHotkey ( WebFileChooserPanel.this, Hotkey.CTRL_N ).setHotkeyDisplayWay ( TooltipWay.down );
        folderNew.setRolloverDecoratedOnly ( true );
        folderNew.setFocusable ( false );
        folderNew.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () && currentFolder != null )
                {
                    final String defaultName = LanguageManager.get ( "weblaf.filechooser.newfolder.name" );
                    final String freeName = FileUtils.getAvailableName ( currentFolder, defaultName );
                    final File file = new File ( currentFolder, freeName );
                    if ( file.mkdir () )
                    {
                        // Update view
                        // This action can be optimized, but that will make a lot of additional actions and most likely cause some troubles
                        reloadCurrentFolder ();

                        // Chaging folder name
                        setSelectedFile ( file );
                        editSelectedFileName ();
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
        remove.setFocusable ( false );
        remove.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.provide () )
                {
                    deleteSelectedFiles ();
                }
            }
        } );

        view = new WebButton ( VIEW_ICON );
        view.setLanguage ( "weblaf.filechooser.view" );
        view.setRolloverDecoratedOnly ( true );
        view.setFocusable ( false );
        view.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu viewChoose = new WebPopupMenu ();

                final WebRadioButtonMenuItem icons = new WebRadioButtonMenuItem ( VIEW_ICONS_ICON );
                icons.setLanguage ( "weblaf.filechooser.view.icons" );
                icons.setSelected ( getViewType ().equals ( FileChooserViewType.icons ) );
                icons.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        setViewType ( FileChooserViewType.icons );
                    }
                } );
                viewChoose.add ( icons );

                final WebRadioButtonMenuItem tiles = new WebRadioButtonMenuItem ( VIEW_TILES_ICON );
                tiles.setLanguage ( "weblaf.filechooser.view.tiles" );
                tiles.setSelected ( getViewType ().equals ( FileChooserViewType.tiles ) );
                tiles.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        setViewType ( FileChooserViewType.tiles );
                    }
                } );
                viewChoose.add ( tiles );

                final WebRadioButtonMenuItem table = new WebRadioButtonMenuItem ( VIEW_TABLE_ICON );
                table.setLanguage ( "weblaf.filechooser.view.table" );
                table.setSelected ( getViewType ().equals ( FileChooserViewType.table ) );
                table.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        setViewType ( FileChooserViewType.table );
                    }
                } );
                viewChoose.add ( table );

                final ButtonGroup viewGroup = new ButtonGroup ();
                viewGroup.add ( icons );
                viewGroup.add ( tiles );
                viewGroup.add ( table );

                viewChoose.showBelowMiddle ( view );
            }
        } );

        toolBar.add ( backward );
        toolBar.add ( forward );
        toolBar.add ( history );
        toolBar.addFill ( pathField );
        toolBar.addToEnd ( folderUp );
        toolBar.addToEnd ( folderHome );
        toolBar.addToEnd ( refresh );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( folderNew );
        toolBar.addToEnd ( remove );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( view );
        return toolBar;
    }

    /**
     * Updates current history state.
     *
     * @param historyIndex new history index
     */
    protected void updateHistoryState ( final int historyIndex )
    {
        if ( historyIndex >= 0 && historyIndex < navigationHistory.size () )
        {
            currentHistoryIndex = historyIndex;
            updateCurrentFolder ( navigationHistory.get ( historyIndex ), UpdateSource.history );
        }
    }

    /**
     * Returns center panel content.
     *
     * @return center panel content
     */
    protected Component createCenterContent ()
    {
        createFileTree ();
        createFileList ();
        createFileTable ();

        // todo Context menu for view components
        //----------------
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
        //----------------

        centralSplit = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT );
        centralSplit.setOneTouchExpandable ( true );
        centralSplit.setLeftComponent ( treeScroll );
        centralSplit.setRightComponent ( fileListScroll );
        centralSplit.setDividerLocation ( 160 );
        centralSplit.setMargin ( 4, 4, 4, 4 );
        return centralSplit;
    }

    /**
     * Creates file tree and all related components.
     */
    protected void createFileTree ()
    {
        fileTree = new WebFileTree ();
        fileTree.setAutoExpandSelectedNode ( true );
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );

        fileTreeListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                if ( fileTree.getSelectionCount () > 0 )
                {
                    updateCurrentFolder ( fileTree.getSelectedFile (), UpdateSource.tree );
                }
            }
        };
        fileTree.addTreeSelectionListener ( fileTreeListener );

        treeScroll = new WebScrollPane ( fileTree, true );
        treeScroll.setPreferredSize ( new Dimension ( 160, 1 ) );
    }

    /**
     * Creates file list and all related components.
     */
    protected void createFileList ()
    {
        fileList = new WebFileList ();
        fileList.setGenerateThumbnails ( true );
        fileList.setDropMode ( DropMode.ON );
        fileList.setEditable ( true );
        fileList.setPreferredColumnCount ( 3 );
        fileList.setPreferredRowCount ( 5 );
        fileList.setTransferHandler ( new FilesLocateDropHandler ( UpdateSource.list ) );

        fileList.getInputMap ().put ( KeyStroke.getKeyStroke ( KeyEvent.VK_ENTER, 0 ), "openFolder" );
        fileList.getActionMap ().put ( "openFolder", new AbstractAction ()
        {
            @Override
            public boolean isEnabled ()
            {
                return fileList.getSelectedIndex () != -1;
            }

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final File file = fileList.getSelectedFile ();
                if ( file.isDirectory () )
                {
                    updateCurrentFolder ( file, UpdateSource.list );
                }
            }
        } );

        fileList.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) && e.getClickCount () % 2 == 0 && fileList.getSelectedIndex () != -1 )
                {
                    final File file = fileList.getSelectedFile ();
                    if ( file.isDirectory () )
                    {
                        updateCurrentFolder ( file, UpdateSource.list );
                    }
                    else
                    {
                        fireApproveAction ( new ActionEvent ( fileList, e.getID (), "Files selected", e.getWhen (), e.getModifiers () ) );
                    }
                }
            }
        } );

        fileList.addListSelectionListener ( new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                updateSelectedFilesField ();
            }
        } );

        fileList.addListEditListener ( new ListEditAdapter ()
        {
            @Override
            public void editFinished ( final int index, final Object oldValue, final Object newValue )
            {
                // Saving for futher selection
                final File file = ( ( FileElement ) newValue ).getFile ();

                // Updating current view
                // This action can be optimized, but that will make a lot of additional actions and most likely cause some troubles
                reloadCurrentFolder ();

                // Updating list selection after edit
                fileList.setSelectedFile ( file );
            }
        } );

        fileListScroll = fileList.getScrollView ();
        fileListScroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        fileListScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
    }

    /**
     * Creates file table and all related components.
     */
    protected void createFileTable ()
    {
        fileTable = new WebFileTable ();
        fileTable.setOpaque ( false );
        fileTable.setTransferHandler ( new FilesLocateDropHandler ( UpdateSource.table ) );

        fileTable.getInputMap ().put ( KeyStroke.getKeyStroke ( KeyEvent.VK_ENTER, 0 ), "openFolder" );
        fileTable.getActionMap ().put ( "openFolder", new AbstractAction ()
        {
            @Override
            public boolean isEnabled ()
            {
                return fileTable.getSelectedRow () != -1;
            }

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final File file = fileTable.getSelectedFile ();
                if ( file.isDirectory () )
                {
                    updateCurrentFolder ( file, UpdateSource.table );
                }
            }
        } );

        fileTable.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) && e.getClickCount () % 2 == 0 && fileTable.getSelectedRow () != -1 )
                {
                    final File file = fileTable.getSelectedFile ();
                    if ( file.isDirectory () )
                    {
                        updateCurrentFolder ( file, UpdateSource.table );
                    }
                    else
                    {
                        fireApproveAction ( new ActionEvent ( fileTable, e.getID (), "Files selected", e.getWhen (), e.getModifiers () ) );
                    }
                }
            }
        } );

        fileTable.getSelectionModel ().addListSelectionListener ( new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                updateSelectedFilesField ();
            }
        } );

        fileTable.getDefaultEditor ( File.class ).addCellEditorListener ( new CellEditorListener ()
        {
            @Override
            public void editingStopped ( final ChangeEvent e )
            {
                // Saving for futher selection
                final File file = fileTable.getSelectedFile ();

                // Updating current view
                // This action can be optimized, but that will make a lot of additional actions and most likely cause some troubles
                reloadCurrentFolder ();

                // Updating list selection after edit
                fileTable.setSelectedFile ( file );
            }

            @Override
            public void editingCanceled ( final ChangeEvent e )
            {
                // Do nothing
            }
        } );

        fileTableScroll = new WebScrollPane ( fileTable, true );
        fileTableScroll.getViewport ().setOpaque ( true );
        fileTableScroll.getViewport ().setBackground ( Color.WHITE );
        fileTableScroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        fileTableScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
    }

    /**
     * Returns south panel content.
     *
     * @return south panel content
     */
    protected Component createSouthContent ()
    {
        final WebPanel southPanel = new WebPanel ();
        southPanel.setLayout ( new ToolbarLayout ( 4 ) );
        southPanel.setOpaque ( false );
        southPanel.setMargin ( 0, 4, 4, 4 );
        add ( southPanel, BorderLayout.SOUTH );

        final WebLabel selectedFilesLabel = new WebLabel ();
        selectedFilesLabel.setLanguage ( "weblaf.filechooser.files.selected" );
        selectedFilesLabel.setDrawShade ( true );
        selectedFilesLabel.setMargin ( 0, 4, 0, 0 );
        southPanel.add ( selectedFilesLabel );

        selectedFilesViewField = new WebFileChooserField ( false );
        selectedFilesViewField.setShowRemoveButton ( false );
        selectedFilesViewField.setShowFileShortName ( true );
        selectedFilesViewField.setFilesDropEnabled ( false );

        selectedFilesTextField = new WebTextField ( 0, true );
        selectedFilesTextField.addCaretListener ( new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                // No need to specify files, they will be calculated when needed
                updateApproveButtonState ( null );
            }
        } );
        selectedFilesTextField.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Try to approve selection
                approveButton.doClick ( 0 );
            }
        } );

        selectedFilesPanel = new WebPanel ( chooserType == FileChooserType.save ? selectedFilesTextField : selectedFilesViewField );
        southPanel.add ( selectedFilesPanel, ToolbarLayout.FILL );

        fileFilters = new WebComboBox ();
        fileFilters.setRenderer ( new DefaultFileFilterListCellRenderer ( fileFilters ) );
        fileFilters.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                setActiveFileFilter ( ( AbstractFileFilter ) fileFilters.getSelectedItem (), false );
            }
        } );

        approveButton = new WebButton ( "", APPROVE_ICON );
        //        approveButton.addHotkey ( WebFileChooserPanel.this, Hotkey.CTRL_ENTER ).setHotkeyDisplayWay ( TooltipWay.up );
        approveButton.setRolloverShine ( StyleConstants.highlightControlButtons );
        approveButton.setShineColor ( StyleConstants.greenHighlight );
        approveButton.putClientProperty ( GroupPanel.FILL_CELL, true );
        approveButton.setEnabled ( false );
        approveButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fireApproveAction ( e );
            }
        } );

        cancelButton = new WebButton ( "", CANCEL_ICON );
        cancelButton.setLanguage ( "weblaf.filechooser.cancel" );
        //        cancelButton.addHotkey ( WebFileChooserPanel.this, Hotkey.ESCAPE ).setHotkeyDisplayWay ( TooltipWay.up );
        cancelButton.setRolloverShine ( StyleConstants.highlightControlButtons );
        cancelButton.setShineColor ( StyleConstants.redHighlight );
        cancelButton.putClientProperty ( GroupPanel.FILL_CELL, true );
        cancelButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fireCancelAction ( e );
            }
        } );

        controlsPanel = new WebPanel ();
        updateControls ();
        southPanel.add ( controlsPanel, ToolbarLayout.END );

        // For proper equal sizing of control buttons
        SwingUtils.equalizeComponentsSize ( approveButton, cancelButton );
        final PropertyChangeListener pcl = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                approveButton.setPreferredSize ( null );
                cancelButton.setPreferredSize ( null );
                SwingUtils.equalizeComponentsSize ( approveButton, cancelButton );
                southPanel.revalidate ();
            }
        };
        approveButton.addPropertyChangeListener ( AbstractButton.TEXT_CHANGED_PROPERTY, pcl );
        cancelButton.addPropertyChangeListener ( AbstractButton.TEXT_CHANGED_PROPERTY, pcl );

        return southPanel;
    }

    /**
     * Returns directory files view type.
     *
     * @return directory files view type
     */
    public FileChooserViewType getViewType ()
    {
        return viewType;
    }

    /**
     * Sets directory files view type
     *
     * @param viewType directory files view type
     */
    public void setViewType ( final FileChooserViewType viewType )
    {
        // Flag used to transfer selection between different view components
        final boolean viewChanged = viewType.getComponentIndex () != this.viewType.getComponentIndex ();

        // Saving view type
        this.viewType = viewType;

        // Updating view component and selection
        switch ( viewType )
        {
            case icons:
            {
                fileList.setPreferredColumnCount ( 7 );
                fileList.setPreferredRowCount ( 4 );
                fileList.setFileListViewType ( FileListViewType.icons );
                centralSplit.setRightComponent ( fileListScroll );
                if ( viewChanged )
                {
                    fileList.setSelectedFiles ( fileTable.getSelectedFiles () );
                    fileList.requestFocusInWindow ();
                }
                break;
            }
            case tiles:
            {
                fileList.setPreferredColumnCount ( 3 );
                fileList.setPreferredRowCount ( 6 );
                fileList.setFileListViewType ( FileListViewType.tiles );
                centralSplit.setRightComponent ( fileListScroll );
                if ( viewChanged )
                {
                    fileList.setSelectedFiles ( fileTable.getSelectedFiles () );
                    fileList.requestFocusInWindow ();
                }
                break;
            }
            case table:
            {
                centralSplit.setRightComponent ( fileTableScroll );
                if ( viewChanged )
                {
                    fileTable.setSelectedFiles ( fileList.getSelectedFiles () );
                    fileTable.requestFocusInWindow ();
                }
                break;
            }
        }
        centralSplit.revalidate ();
    }

    /**
     * Sets currently opened folder.
     *
     * @param folder folder to be opened
     */
    public void setCurrentFolder ( final File folder )
    {
        updateCurrentFolder ( folder, UpdateSource.other );
    }

    /**
     * Updates currently opened folder.
     *
     * @param file         folder to be opened or file to be displayed
     * @param updateSource update call source
     */
    protected void updateCurrentFolder ( File file, final UpdateSource updateSource )
    {
        // Open parent directory instead of file
        File toSelect = null;
        if ( file != null && !FileUtils.isDirectory ( file ) )
        {
            toSelect = file;
            file = file.getParentFile ();
        }
        // Replacing root file for non-windows OS
        if ( file == null && !SystemUtils.isWindows () )
        {
            file = FileUtils.getDiskRoots ()[ 0 ];
        }

        // Ignore if folder didn't change
        if ( FileUtils.equals ( currentFolder, file ) )
        {
            // Selecting passed file
            if ( toSelect != null )
            {
                setSelectedFile ( toSelect );
            }

            // Updating contols
            updateControlsState ();

            return;
        }

        // Filling history if needed
        if ( updateSource != UpdateSource.history )
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
        }

        // Updating view
        if ( updateSource != UpdateSource.path )
        {
            updatePath ( file );
        }
        if ( updateSource != UpdateSource.tree )
        {
            updateTree ( file );
        }
        updateList ( file );
        updateTable ( file );
        currentFolder = file;

        // Updating contols
        updateControlsState ();

        // Selecting passed file
        if ( toSelect != null )
        {
            setSelectedFile ( toSelect );
        }

        // Firing events
        fireDirectoryChanged ( currentFolder );
    }

    /**
     * Updates toolbar controls state.
     */
    protected void updateControlsState ()
    {
        backward.setEnabled ( currentHistoryIndex > 0 );
        forward.setEnabled ( currentHistoryIndex + 1 < navigationHistory.size () );
        folderNew.setEnabled ( currentFolder != null );
        folderUp.setEnabled (
                SystemUtils.isWindows () ? currentFolder != null : currentFolder != null && currentFolder.getParentFile () != null );
    }

    /**
     * Returns list of selected files which are accepted by active filter.
     *
     * @return list of selected files which are accepted by active filter
     */
    public List<File> getSelectedFiles ()
    {
        if ( chooserType == FileChooserType.save )
        {
            // Returning custom file
            return Arrays.asList ( new File ( currentFolder, selectedFilesTextField.getText () ) );
        }
        else
        {
            // Retrieving files, selected in current view
            final List<File> files = getAllSelectedFiles ();

            // Filtering them using unmodified file filter
            return getFilteredSelectedFiles ( files );
        }
    }

    /**
     * Returns list of filtered selected files.
     *
     * @param allFiles files to filter
     * @return list of filtered selected files
     */
    protected List<File> getFilteredSelectedFiles ( final List<File> allFiles )
    {
        return FileUtils.filterFiles ( allFiles, fileFilter );
    }

    /**
     * Returns list of selected files.
     *
     * @return list of selected files
     */
    protected List<File> getAllSelectedFiles ()
    {
        final List<File> files;
        if ( viewType.getComponentIndex () == 0 )
        {
            files = fileList.getSelectedFiles ();
        }
        else if ( viewType.getComponentIndex () == 1 )
        {
            files = fileTable.getSelectedFiles ();
        }
        else
        {
            files = new ArrayList<File> ( 0 );
        }
        return files;
    }

    /**
     * Sets file selected in currently displayed directory.
     *
     * @param file file to select
     */
    public void setSelectedFile ( final File file )
    {
        if ( viewType.getComponentIndex () == 0 )
        {
            fileList.setSelectedFile ( file );
        }
        if ( viewType.getComponentIndex () == 1 )
        {
            fileTable.setSelectedFile ( file );
        }
    }

    /**
     * Sets files selected in currently displayed directory.
     *
     * @param files files to select
     */
    public void setSelectedFiles ( final File[] files )
    {
        setSelectedFiles ( CollectionUtils.toList ( files ) );
    }

    /**
     * Sets files selected in currently displayed directory.
     *
     * @param files files to select
     */
    public void setSelectedFiles ( final Collection<File> files )
    {
        if ( viewType.getComponentIndex () == 0 )
        {
            fileList.setSelectedFiles ( files );
        }
        if ( viewType.getComponentIndex () == 1 )
        {
            fileTable.setSelectedFiles ( files );
        }
    }

    /**
     * Updates currently selected files field.
     */
    protected void updateSelectedFilesField ()
    {
        // All selected files
        final List<File> allFiles = getAllSelectedFiles ();

        // Filtered selected files
        final List<File> files = getFilteredSelectedFiles ( allFiles );

        // Updating controls
        folderNew.setEnabled ( currentFolder != null );
        remove.setEnabled ( currentFolder != null && allFiles.size () > 0 );

        // Updating selected files view component
        if ( chooserType == FileChooserType.save )
        {
            if ( files.size () > 0 )
            {
                // Accept only file as selection, otherwise leave old file selected
                final File file = files.get ( 0 );
                if ( FileUtils.isFile ( file ) )
                {
                    selectedFilesViewField.setSelectedFile ( file );
                    selectedFilesTextField.setText ( file.getName () );
                }
            }
        }
        else
        {
            selectedFilesViewField.setSelectedFiles ( files );
            selectedFilesTextField.setText ( TextUtils.listToString ( files, ", ", quotedFileNameProvider ) );
        }

        // When choose action allowed
        updateApproveButtonState ( files );

        // Firing selection change
        fireFileSelectionChanged ( files );
    }

    /**
     * Updates approve button state.
     *
     * @param files filtered selected files
     */
    protected void updateApproveButtonState ( List<File> files )
    {
        if ( chooserType == FileChooserType.save )
        {
            // Approve enabled due to entered file name
            approveButton.setEnabled ( !selectedFilesTextField.getText ().trim ().equals ( "" ) );
        }
        else
        {
            // Approve enabled due to selected files
            if ( files == null )
            {
                files = getFilteredSelectedFiles ( getAllSelectedFiles () );
            }
            approveButton.setEnabled ( files.size () > 0 );
        }
    }

    /**
     * Updates selected files field panel content.
     */
    protected void updateSelectedFilesFieldPanel ()
    {
        selectedFilesPanel.removeAll ();
        selectedFilesPanel.add ( chooserType == FileChooserType.save ? selectedFilesTextField : selectedFilesViewField );
        selectedFilesPanel.revalidate ();
    }

    /**
     * Updates path field view.
     *
     * @param file new current folder
     */
    protected void updatePath ( final File file )
    {
        pathField.removePathFieldListener ( pathFieldListener );
        pathField.setSelectedPath ( file );
        pathField.addPathFieldListener ( pathFieldListener );
    }

    /**
     * Updates files tree view.
     *
     * @param file new current folder
     */
    protected void updateTree ( final File file )
    {
        if ( file != null )
        {
            fileTree.expandToFile ( file, false, false, new Runnable ()
            {
                @Override
                public void run ()
                {
                    fileTree.removeTreeSelectionListener ( fileTreeListener );
                    fileTree.setSelectedNode ( fileTree.getNode ( file ) );
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

    /**
     * Updates files list view.
     *
     * @param file new current folder
     */
    protected void updateList ( final File file )
    {
        fileList.setDisplayedDirectory ( file );
    }

    /**
     * Updates files table view.
     *
     * @param file new current folder
     */
    protected void updateTable ( final File file )
    {
        fileTable.setDisplayedDirectory ( file );
    }

    /**
     * Updates file filters combobox view.
     */
    protected void updateFiltersComboBox ()
    {
        fileFilters.setModel ( new DefaultComboBoxModel ( availableFilters.toArray () ) );
    }

    /**
     * Sets currently active file filter.
     * Notice that this filter must be in the filters list to be activated.
     *
     * @param fileFilter file filter to make active
     */
    public void setActiveFileFilter ( final AbstractFileFilter fileFilter )
    {
        setActiveFileFilter ( fileFilter, true );
    }

    /**
     * Sets currently active file filter.
     * Notice that this filter must be in the filters list to be activated.
     *
     * @param fileFilter file filter to make active
     * @param select     whether to select active file filter in combobox or not
     */
    protected void setActiveFileFilter ( AbstractFileFilter fileFilter, final boolean select )
    {
        // Simply take the first available filter if the specified one is not available
        if ( !availableFilters.contains ( fileFilter ) )
        {
            fileFilter = availableFilters.get ( 0 );
        }

        // Save active filter
        this.fileFilter = fileFilter;

        // Select in combobox (this won't cause action event to fire, so its fine)
        if ( select )
        {
            fileFilters.setSelectedItem ( fileFilter );
        }

        // Updating filters
        updateFileComponentFilters ();
    }

    /**
     * Updates files selection components filters.
     */
    protected void updateFileComponentFilters ()
    {
        fileList.setFileFilter ( applyHiddenFilesFilter ( applyOrDirectoriesFilter ( fileFilter ) ) );
        fileTable.setFileFilter ( applyHiddenFilesFilter ( applyOrDirectoriesFilter ( fileFilter ) ) );
    }

    /**
     * Updates directory selection components filters.
     */
    protected void updateDirectoryComponentFilters ()
    {
        pathField.setFileFilter ( applyHiddenFilesFilter ( GlobalConstants.DIRECTORIES_FILTER ) );
        fileTree.setFileFilter ( applyHiddenFilesFilter ( GlobalConstants.DIRECTORIES_FILTER ) );
    }

    /**
     * Adds "isDirectory" as one of filter OR conditions.
     *
     * @param fileFilter filter to process
     * @return new file filter with additional condition
     */
    protected GroupedFileFilter applyOrDirectoriesFilter ( final AbstractFileFilter fileFilter )
    {
        return new GroupedFileFilter ( FilterGroupType.OR, fileFilter, GlobalConstants.DIRECTORIES_FILTER );
    }

    /**
     * Adds hidden files filter condition to the specified files filter.
     *
     * @param fileFilter filter to process
     * @return new file filter with additional condition
     */
    protected GroupedFileFilter applyHiddenFilesFilter ( final AbstractFileFilter fileFilter )
    {
        return new GroupedFileFilter ( FilterGroupType.AND, fileFilter, hiddenFilesFilter );
    }

    /**
     * Reloads files from currently opened folder into all available view components.
     */
    public void reloadCurrentFolder ()
    {
        // Clearing all caches for folder files
        if ( currentFolder != null )
        {
            FileUtils.clearFilesCaches ( currentFolder.listFiles () );
        }

        // Updating view in a specific way
        pathField.updatePath ();
        fileTree.reloadChilds ( currentFolder );
        fileList.reloadFiles ();
        fileTable.reloadFiles ();
    }

    /**
     * Starts editing name of selected file in currently visible view.
     */
    public void editSelectedFileName ()
    {
        if ( viewType.getComponentIndex () == 0 )
        {
            fileList.editSelectedCell ();
        }
        if ( viewType.getComponentIndex () == 1 )
        {
            fileTable.editSelectedFileName ();
        }
    }

    /**
     * Delete all selected in view files.
     */
    public void deleteSelectedFiles ()
    {
        final List<File> files = getAllSelectedFiles ();
        if ( files.isEmpty () )
        {
            return;
        }

        final WebPanel all = new WebPanel ( new BorderLayout ( 0, 5 ) );
        all.add ( new WebLabel ( LanguageManager.get ( "weblaf.filechooser.delete.confirm.text" ) ), BorderLayout.NORTH );

        final WebPanel deleteFilesPanel = new WebPanel ( new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 5, true, false ) );
        deleteFilesPanel.setMargin ( 3 );
        deleteFilesPanel.setBackground ( Color.WHITE );
        for ( final File file : files )
        {
            deleteFilesPanel.add ( new WebLabel ( file.getName (), FileUtils.getFileIcon ( file ), WebLabel.LEFT ) );
        }
        final WebScrollPane scroll = new WebScrollPane ( deleteFilesPanel )
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();

                final JScrollBar vsb = getVerticalScrollBar ();
                if ( vsb != null && vsb.isShowing () )
                {
                    ps.width = ps.width + vsb.getPreferredSize ().width;
                }

                ps.height = Math.min ( ps.height, 100 );

                return ps;
            }
        };
        all.add ( scroll, BorderLayout.CENTER );

        final String title = LanguageManager.get ( "weblaf.filechooser.delete.confirm.title" );
        final int confirm = WebOptionPane
                .showConfirmDialog ( WebFileChooserPanel.this, all, title, WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE );

        if ( confirm == WebOptionPane.YES_OPTION )
        {
            FileUtils.deleteFiles ( files );
            reloadCurrentFolder ();
        }
    }

    /**
     * Returns approve button listener.
     *
     * @return approve button listener
     */
    public ActionListener getApproveListener ()
    {
        return approveListener;
    }

    /**
     * Sets approve button listener.
     *
     * @param approveListener approve button listener
     */
    public void setApproveListener ( final ActionListener approveListener )
    {
        this.approveListener = approveListener;
    }

    /**
     * Returns cancel button listener.
     *
     * @return cancel button listener
     */
    public ActionListener getCancelListener ()
    {
        return cancelListener;
    }

    /**
     * Sets cancel button listener.
     *
     * @param cancelListener cancel button listener
     */
    public void setCancelListener ( final ActionListener cancelListener )
    {
        this.cancelListener = cancelListener;
    }

    /**
     * Fires approve action.
     *
     * @param e action event
     */
    protected void fireApproveAction ( final ActionEvent e )
    {
        if ( approveListener != null )
        {
            approveListener.actionPerformed ( e );
        }
    }

    /**
     * Fires cancel action.
     *
     * @param e action event
     */
    protected void fireCancelAction ( final ActionEvent e )
    {
        if ( cancelListener != null )
        {
            cancelListener.actionPerformed ( e );
        }
    }

    /**
     * Returns list of available file filters.
     *
     * @return list of available file filters
     */
    public List<AbstractFileFilter> getAvailableFilters ()
    {
        return availableFilters;
    }

    /**
     * Returns currenly active file filter.
     *
     * @return currenly active file filter
     */
    public AbstractFileFilter getActiveFileFilter ()
    {
        return fileFilter;
    }

    /**
     * Sets the specified file filter as the only one avaiable.
     *
     * @param fileFilter file filter to set
     */
    public void setFileFilter ( final FileFilter fileFilter )
    {
        setFileFilter ( FileUtils.transformFileFilter ( fileFilter ) );
    }

    /**
     * Sets the specified file filter as the only one avaiable.
     *
     * @param fileFilter file filter to set
     */
    public void setFileFilter ( final javax.swing.filechooser.FileFilter fileFilter )
    {
        setFileFilter ( FileUtils.transformFileFilter ( fileFilter ) );
    }

    /**
     * Sets the specified file filter as the only one avaiable.
     *
     * @param fileFilter file filter to set
     */
    public void setFileFilter ( final AbstractFileFilter fileFilter )
    {
        this.availableFilters = Arrays.asList ( fileFilter );
        updateFiltersComboBox ();
        setActiveFileFilter ( fileFilter );
    }

    /**
     * Sets available file filters.
     * The first one specified will be the default selected file filter.
     *
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final FileFilter[] fileFilters )
    {
        setFileFilters ( 0, fileFilters );
    }

    /**
     * Sets available file filters.
     *
     * @param index       default filter index
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final int index, final FileFilter[] fileFilters )
    {
        availableFilters = new ArrayList<AbstractFileFilter> ( fileFilters.length );
        for ( final FileFilter fileFilter : fileFilters )
        {
            availableFilters.add ( FileUtils.transformFileFilter ( fileFilter ) );
        }
        updateFiltersComboBox ();
        setActiveFileFilter ( availableFilters.get ( index ) );
    }

    /**
     * Sets available file filters.
     * The first one specified will be the default selected file filter.
     *
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final javax.swing.filechooser.FileFilter[] fileFilters )
    {
        setFileFilters ( 0, fileFilters );
    }

    /**
     * Sets available file filters.
     *
     * @param index       default filter index
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final int index, final javax.swing.filechooser.FileFilter[] fileFilters )
    {
        availableFilters = new ArrayList<AbstractFileFilter> ( fileFilters.length );
        for ( final javax.swing.filechooser.FileFilter filtfileFilter : fileFilters )
        {
            availableFilters.add ( FileUtils.transformFileFilter ( filtfileFilter ) );
        }
        updateFiltersComboBox ();
        setActiveFileFilter ( availableFilters.get ( index ) );
    }

    /**
     * Sets available file filters.
     * The first one specified will be the default selected file filter.
     *
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final AbstractFileFilter[] fileFilters )
    {
        setFileFilters ( 0, fileFilters );
    }

    /**
     * Sets available file filters.
     *
     * @param index       default filter index
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final int index, final AbstractFileFilter[] fileFilters )
    {
        this.availableFilters = Arrays.asList ( fileFilters );
        updateFiltersComboBox ();
        setActiveFileFilter ( availableFilters.get ( index ) );
    }

    /**
     * Sets available file filters.
     * The first one specified will be the default selected file filter.
     *
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final List<AbstractFileFilter> fileFilters )
    {
        setFileFilters ( 0, fileFilters );
    }

    /**
     * Sets available file filters.
     *
     * @param index       default filter index
     * @param fileFilters available file filters
     */
    public void setFileFilters ( final int index, final List<AbstractFileFilter> fileFilters )
    {
        this.availableFilters = CollectionUtils.copy ( fileFilters );
        updateFiltersComboBox ();
        setActiveFileFilter ( availableFilters.get ( index ) );
    }

    /**
     * Returns whether control buttons are displayed or not.
     *
     * @return true if control buttons are displayed, false otherwise
     */
    public boolean isShowControlButtons ()
    {
        return showControlButtons;
    }

    /**
     * Sets whether to display control buttons or not.
     *
     * @param showControlButtons whether to display control buttons or not
     */
    public void setShowControlButtons ( final boolean showControlButtons )
    {
        this.showControlButtons = showControlButtons;
        updateControls ();
    }

    /**
     * Updates controls display.
     */
    protected void updateControls ()
    {
        controlsPanel.removeAll ();
        controlsPanel.add ( showControlButtons ? new GroupPanel ( 4, fileFilters, approveButton, cancelButton ) : fileFilters );
    }

    /**
     * Returns approve button text.
     *
     * @return approve button text
     */
    public String getApproveButtonText ()
    {
        return approveButton.getText ();
    }

    /**
     * Restores default approve button text for the specified chooser type.
     */
    public void restoreButtonText ()
    {
        setApproveButtonText ( ( String ) null );
    }

    /**
     * Sets approve button text.
     * You can specify null to reset text to default value.
     *
     * @param text approve button text
     */
    public void setApproveButtonText ( final String text )
    {
        if ( text == null )
        {
            setApproveButtonText ( chooserType == FileChooserType.save ? FileApproveText.save :
                    ( chooserType == FileChooserType.open ? FileApproveText.open : FileApproveText.choose ) );
        }
        else
        {
            approveButton.removeLanguage ();
            approveButton.setText ( text );
        }
    }

    /**
     * Sets approve button text type.
     *
     * @param approveText approve button text type
     */
    public void setApproveButtonText ( final FileApproveText approveText )
    {
        setApproveButtonLanguage ( approveText.getLanguageKey () );
    }

    /**
     * Sets approve button language key.
     *
     * @param key approve button language key
     */
    public void setApproveButtonLanguage ( final String key )
    {
        approveButton.setLanguage ( key );
    }

    /**
     * Returns chooser type.
     *
     * @return chooser type
     */
    public FileChooserType getChooserType ()
    {
        return chooserType;
    }

    /**
     * Sets chooser type.
     *
     * @param chooserType new chooser type
     */
    public void setChooserType ( final FileChooserType chooserType )
    {
        this.chooserType = chooserType;
        updateSelectionMode ();
        updateSelectedFilesFieldPanel ();
        updateSelectedFilesField ();
        restoreButtonText ();
    }

    /**
     * Sets whether should display hidden files or not.
     *
     * @return true if should display hidden files, false otherwise
     */
    public boolean isShowHiddenFiles ()
    {
        return showHiddenFiles;
    }

    /**
     * Sets whether should display hidden files or not.
     *
     * @param showHiddenFiles whether should display hidden files or not
     */
    public void setShowHiddenFiles ( final boolean showHiddenFiles )
    {
        this.showHiddenFiles = showHiddenFiles;
        updateDirectoryComponentFilters ();
        updateFileComponentFilters ();
    }

    /**
     * Adds file chooser listener.
     *
     * @param listener new file chooser listener
     */
    public void addFileChooserListener ( final FileChooserListener listener )
    {
        chooserListeners.add ( listener );
    }

    /**
     * Removes file chooser listener.
     *
     * @param listener file chooser listener to remove
     */
    public void removeFileChooserListener ( final FileChooserListener listener )
    {
        chooserListeners.remove ( listener );
    }

    /**
     * Fired when displayed in file chooser directory changes.
     *
     * @param newDirectory newly displayed directory
     */
    protected void fireDirectoryChanged ( final File newDirectory )
    {
        for ( final FileChooserListener listener : CollectionUtils.copy ( chooserListeners ) )
        {
            listener.directoryChanged ( newDirectory );
        }
    }

    /**
     * Fired when selected in file chooser files change.
     *
     * @param selectedFiles newly selected files
     */
    protected void fireFileSelectionChanged ( final List<File> selectedFiles )
    {
        for ( final FileChooserListener listener : CollectionUtils.copy ( chooserListeners ) )
        {
            listener.selectionChanged ( selectedFiles );
        }
    }

    /**
     * Returns whether multiply files selection is allowed or not.
     *
     * @return true if multiply files selection is allowed, false otherwise
     */
    public boolean isMultiSelectionEnabled ()
    {
        return multiSelectionEnabled;
    }

    /**
     * Sets whether multiply files selection is allowed or not.
     *
     * @param multiSelectionEnabled whether multiply files selection is allowed or not
     */
    public void setMultiSelectionEnabled ( final boolean multiSelectionEnabled )
    {
        this.multiSelectionEnabled = multiSelectionEnabled;
        updateSelectionMode ();
    }

    /**
     * Updates view components selection modes.
     */
    protected void updateSelectionMode ()
    {
        final boolean ms = multiSelectionEnabled && chooserType != FileChooserType.save;

        final int mode = ms ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION;
        fileList.setSelectionMode ( mode );
        fileTable.setSelectionMode ( mode );

        selectedFilesViewField.setMultiSelectionEnabled ( ms );
    }

    /**
     * Returns whether file thumbnails are generated or not.
     *
     * @return true if file thumbnails are generated, false otherwise
     */
    public boolean isGenerateThumbnails ()
    {
        return fileList.isGenerateThumbnails ();
    }

    /**
     * Sets whether file thumbnails should be generated or not.
     *
     * @param generate whether file thumbnails should be generated or not
     */
    public void setGenerateThumbnails ( final boolean generate )
    {
        this.fileList.setGenerateThumbnails ( generate );
    }

    /**
     * This enumeration represents the type of source that caused view update.
     */
    protected enum UpdateSource
    {
        /**
         * Path field source.
         */
        path,

        /**
         * Files tree source.
         */
        tree,

        /**
         * Files list source.
         */
        list,

        /**
         * Files table source.
         */
        table,

        /**
         * Toolbar button source.
         */
        toolbar,

        /**
         * History action source.
         */
        history,

        /**
         * Other source.
         */
        other
    }

    /**
     * FileDropHandler extension to provide drop-to-find-file functionality.
     */
    protected class FilesLocateDropHandler extends FileDropHandler
    {
        /**
         * Source of updates.
         */
        protected UpdateSource updateSource;

        /**
         * Constructs new FilesLocateDropHandler.
         *
         * @param updateSource source of updates
         */
        public FilesLocateDropHandler ( final UpdateSource updateSource )
        {
            super ();
            this.updateSource = updateSource;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean filesImported ( final List<File> files )
        {
            if ( files.size () > 0 )
            {
                final File file = files.get ( 0 );
                if ( files.size () == 1 && FileUtils.isDirectory ( file ) )
                {
                    updateCurrentFolder ( file, updateSource );
                }
                else
                {
                    updateCurrentFolder ( file.getParentFile (), updateSource );
                    setSelectedFiles ( files );
                }
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Custom hidden/non-hidden files filter.
     */
    protected class HiddenFilesFilter extends NonHiddenFilter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public ImageIcon getIcon ()
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription ()
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean accept ( final File file )
        {
            return showHiddenFiles || !file.isHidden ();
        }
    }
}