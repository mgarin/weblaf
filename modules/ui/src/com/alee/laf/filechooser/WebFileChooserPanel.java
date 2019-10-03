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
import com.alee.api.jdk.Function;
import com.alee.api.jdk.Supplier;
import com.alee.api.ui.RenderingParameters;
import com.alee.extended.filechooser.*;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.list.FileElement;
import com.alee.extended.list.WebFileList;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.pathfield.WebPathField;
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.ListCellParameters;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.list.editor.ListEditAdapter;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.ToolbarLayout;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.drag.transfer.FilesTransferHandler;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.icon.Icons;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.*;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.compare.Filter;
import com.alee.utils.file.FileComparator;
import com.alee.utils.filefilter.*;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.CellEditorAdapter;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * File chooser panel component.
 * Basically used to provide {@link WebFileChooserUI} with all required UI elements.
 *
 * @author Mikle Garin
 */
public class WebFileChooserPanel extends WebPanel
{
    /**
     * todo 1. Replace cancel and accept listeners with FileChooserListener methods
     * todo 2. When setting "show hidden files" to false - move out from hidden directories
     * todo 3. Proper hotkeys usage within window
     * todo 4. Context menus for file selection components
     */

    /**
     * File name provider.
     */
    public static final Function<File, String> QUOTED_FILE_NAME = new Function<File, String> ()
    {
        @Override
        public String apply ( final File object )
        {
            return "\"" + object.getName () + "\"";
        }
    };

    /**
     * Custom selected files filter.
     * Dialog type check is not used here since it might be (and usually will be) specified only upon opening the dialog.
     * Therefore we do not know how exactly files should be filtered until then and cannot do it properly.
     */
    protected final Filter<File> selectedFilesFilter = new Filter<File> ()
    {
        @Override
        public boolean accept ( final File file )
        {
            return getFileSelectionMode ().accept ( file );
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
     * File selection mode.
     */
    protected FileSelectionMode fileSelectionMode = FileSelectionMode.filesAndDirectories;

    /**
     * Whether hidden files are displayed or not.
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
     * Whether multiple files selection allowed or not.
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
     * Custom accept button listener.
     * By default panel does nothing on accept button press.
     */
    protected ActionListener acceptListener;

    /**
     * Custom cancel button listener.
     * By default panel does nothing on cancel button press.
     */
    protected ActionListener cancelListener;

    /**
     * Preferred width of the tree on the left.
     */
    protected int dividerLocation = 250;

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
    protected WebButton folderDesktop;
    protected WebButton folderNew;
    protected WebButton refresh;
    protected WebButton remove;
    protected WebButton view;

    /**
     * Center panel components.
     */
    protected WebPanel centralContainer;
    protected WebSplitPane centralSplit;
    protected WebFileTree fileTree;
    protected TreeSelectionListener fileTreeListener;
    protected WebScrollPane treeScroll;
    protected WebFileList fileList;
    protected WebScrollPane fileListScroll;
    protected WebFileTable fileTable;
    protected WebScrollPane fileTableScroll;

    /**
     * South panel components.
     */
    protected WebFileChooserField selectedFilesViewField;
    protected WebTextField selectedFilesTextField;
    protected WebComboBox fileFilters;
    protected WebButton acceptButton;
    protected WebButton cancelButton;
    protected WebPanel controlsPanel;

    /**
     * Accessory component
     */
    protected JComponent accessory;

    /**
     * Editing state provider.
     * todo This is a temporary workaround for HotkeysManager actions
     */
    protected Supplier<Boolean> hotkeysAllowed = new Supplier<Boolean> ()
    {
        @Override
        public Boolean get ()
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
     * Constructs new file chooser panel without control buttons.
     */
    public WebFileChooserPanel ()
    {
        this ( FileChooserType.open, false );
    }

    /**
     * Constructs new file chooser panel with or without control buttons.
     *
     * @param chooserType file chooser type
     */
    public WebFileChooserPanel ( final FileChooserType chooserType )
    {
        this ( chooserType, false );
    }

    /**
     * Constructs new file chooser panel with or without control buttons.
     *
     * @param showControlButtons whether to add control buttons or not
     */
    public WebFileChooserPanel ( final boolean showControlButtons )
    {
        this ( FileChooserType.open, showControlButtons );
    }

    /**
     * Constructs new file chooser panel with or without control buttons.
     *
     * @param chooserType        file chooser type
     * @param showControlButtons whether to add control buttons or not
     */
    public WebFileChooserPanel ( final FileChooserType chooserType, final boolean showControlButtons )
    {
        super ( StyleId.filechooserPanel );

        // Default settings
        this.showControlButtons = showControlButtons;
        this.chooserType = chooserType;

        // Panel settings
        setOpaque ( false );
        setLayout ( new BorderLayout ( 0, 0 ) );

        // Panel content
        add ( createTools (), BorderLayout.NORTH );
        add ( createContent (), BorderLayout.CENTER );
        add ( createControls (), BorderLayout.SOUTH );

        // Updating view data
        updateSelectionMode ();
        updateDirectoryComponentFilters ();
        setFileFilter ( new AllFilesFilter () );
        restoreButtonText ();
    }

    /**
     * Returns north panel content.
     *
     * @return north panel content
     */
    protected Component createTools ()
    {
        final StyleId decoratedId = StyleId.filechooserToolbar.at ( this );
        final StyleId undecoratedId = StyleId.filechooserUndecoratedToolbar.at ( this );
        final WebToolBar toolBar = new WebToolBar ( decoratedId );
        toolBar.setLayout ( new ToolbarLayout ( 2, 4 ) );
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

            /**
             * Updates style according to window decoration.
             * todo This might not work well if panel is used outside of the common dialog
             */
            private void updateToolbarStyle ()
            {
                toolBar.setStyleId ( LafUtils.isInDecoratedWindow ( WebFileChooserPanel.this ) ? undecoratedId : decoratedId );
            }
        } );

        final StyleId toolbarButtonId = StyleId.filechooserToolbarButton.at ( toolBar );

        backward = new WebButton ( toolbarButtonId, Icons.backward );
        backward.setLanguage ( "weblaf.filechooser.back" );
        backward.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_LEFT ).setHotkeyDisplayWay ( TooltipWay.down );
        backward.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () )
                {
                    updateHistoryState ( currentHistoryIndex - 1 );
                }
            }
        } );

        forward = new WebButton ( toolbarButtonId, Icons.forward );
        forward.setLanguage ( "weblaf.filechooser.forward" );
        forward.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_RIGHT ).setHotkeyDisplayWay ( TooltipWay.trailing );
        forward.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () )
                {
                    updateHistoryState ( currentHistoryIndex + 1 );
                }
            }
        } );

        history = new WebButton ( toolbarButtonId, Icons.history );
        history.setLanguage ( "weblaf.filechooser.history" );
        history.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu historyPopup = new WebPopupMenu ();

                final WebList historyList = new WebList ( navigationHistory );
                historyList.setOpaque ( false );
                historyList.setVisibleRowCount ( Math.min ( 10, navigationHistory.size () ) );
                historyList.setSelectOnHover ( true );
                historyList.setCellRenderer ( new WebListCellRenderer<File, WebList, ListCellParameters<File, WebList>> ()
                {
                    @Override
                    protected Font fontForValue ( final ListCellParameters<File, WebList> parameters )
                    {
                        final Font font = super.fontForValue ( parameters );
                        return parameters.index () == currentHistoryIndex ? font.deriveFont ( Font.BOLD ) : font;
                    }

                    @Override
                    protected Icon iconForValue ( final ListCellParameters<File, WebList> parameters )
                    {
                        return parameters.value () != null ? FileUtils.getFileIcon ( parameters.value () ) : Icons.computer;
                    }

                    @Override
                    protected String textForValue ( final ListCellParameters<File, WebList> parameters )
                    {
                        return parameters.value () != null ?
                                TextUtils.shortenText ( FileUtils.getDisplayFileName ( parameters.value () ), 40, true ) :
                                LM.get ( "weblaf.filechooser.root" );
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

                final StyleId historyScrollId = StyleId.filechooserHistoryScrollPane.at ( toolBar );
                historyPopup.add ( new WebScrollPane ( historyScrollId, historyList ) );

                historyPopup.showBelowMiddle ( history );

                historyList.setSelectedIndex ( currentHistoryIndex );
                historyList.scrollToCell ( currentHistoryIndex );
            }
        } );

        pathField = new WebPathField ( StyleId.filechooserPathField.at ( toolBar ) );
        pathField.setPreferredWidth ( 10 );
        pathField.setShowHiddenFiles ( showHiddenFiles );
        pathFieldListener = new PathFieldListener ()
        {
            @Override
            public void directoryChanged ( final File newDirectory )
            {
                updateCurrentFolder ( newDirectory, UpdateSource.path );
            }
        };
        pathField.addPathFieldListener ( pathFieldListener );

        folderUp = new WebButton ( toolbarButtonId, Icons.folderUp );
        folderUp.setLanguage ( "weblaf.filechooser.folderup" );
        folderUp.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_UP ).setHotkeyDisplayWay ( TooltipWay.down );
        folderUp.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () && currentFolder != null )
                {
                    updateCurrentFolder ( FileUtils.getParent ( currentFolder ), UpdateSource.toolbar );
                }
            }
        } );

        folderHome = new WebButton ( toolbarButtonId, Icons.folderHome );
        folderHome.setLanguage ( "weblaf.filechooser.home" );
        folderHome.addHotkey ( WebFileChooserPanel.this, Hotkey.ALT_HOME ).setHotkeyDisplayWay ( TooltipWay.down );
        folderHome.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () )
                {
                    updateCurrentFolder ( FileUtils.getUserHome (), UpdateSource.toolbar );
                }
            }
        } );

        final File desktop = FileUtils.getDesktop ();
        if ( desktop != null )
        {
            folderDesktop = new WebButton ( toolbarButtonId, Icons.folderDesktop );
            folderDesktop.setLanguage ( "weblaf.filechooser.desktop" );
            folderDesktop.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( hotkeysAllowed.get () )
                    {
                        updateCurrentFolder ( desktop, UpdateSource.toolbar );
                    }
                }
            } );
        }

        refresh = new WebButton ( toolbarButtonId, Icons.refresh );
        refresh.setLanguage ( "weblaf.filechooser.refresh" );
        refresh.addHotkey ( WebFileChooserPanel.this, Hotkey.F5 ).setHotkeyDisplayWay ( TooltipWay.down );
        refresh.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () )
                {
                    reloadCurrentFolder ();
                }
            }
        } );

        folderNew = new WebButton ( toolbarButtonId, Icons.folderNew );
        folderNew.setLanguage ( "weblaf.filechooser.newfolder" );
        folderNew.addHotkey ( WebFileChooserPanel.this, Hotkey.CTRL_N ).setHotkeyDisplayWay ( TooltipWay.down );
        folderNew.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () && currentFolder != null )
                {
                    final String defaultName = LM.get ( "weblaf.filechooser.newfolder.name" );
                    final String freeName = FileUtils.getAvailableName ( currentFolder, defaultName );
                    final File file = new File ( currentFolder, freeName );
                    if ( file.mkdir () )
                    {
                        // Update view
                        // This action can be optimized, but that will make a lot of additional actions and most likely cause some troubles
                        reloadCurrentFolder ();

                        // Changing folder name
                        setSelectedFile ( file );
                        editSelectedFileName ();
                    }
                    else
                    {
                        final String message = LM.get ( "weblaf.filechooser.newfolder.error.text" );
                        final String title = LM.get ( "weblaf.filechooser.newfolder.error.title" );
                        WebOptionPane.showMessageDialog ( WebFileChooserPanel.this, message, title, WebOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        } );

        remove = new WebButton ( toolbarButtonId, Icons.remove );
        remove.setLanguage ( "weblaf.filechooser.delete" );
        remove.addHotkey ( WebFileChooserPanel.this, Hotkey.DELETE ).setHotkeyDisplayWay ( TooltipWay.down );
        remove.setEnabled ( false );
        remove.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( hotkeysAllowed.get () )
                {
                    deleteSelectedFiles ();
                }
            }
        } );

        view = new WebButton ( toolbarButtonId, Icons.view );
        view.setLanguage ( "weblaf.filechooser.view" );

        final Action viewIcons = new AbstractAction ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                setViewType ( FileChooserViewType.icons );
            }
        };

        final Action viewTiles = new AbstractAction ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                setViewType ( FileChooserViewType.tiles );
            }
        };

        final Action viewTable = new AbstractAction ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                setViewType ( FileChooserViewType.table );
            }
        };

        final int menu = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask ();
        final InputMap inputMap = getInputMap ( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
        final ActionMap actionMap = getActionMap ();
        final KeyStroke ksViewIcons = KeyStroke.getKeyStroke ( KeyEvent.VK_1, menu );
        final KeyStroke ksViewTiles = KeyStroke.getKeyStroke ( KeyEvent.VK_2, menu );
        final KeyStroke ksViewTable = KeyStroke.getKeyStroke ( KeyEvent.VK_3, menu );
        inputMap.put ( ksViewIcons, "viewIcons" );
        actionMap.put ( "viewIcons", viewIcons );
        inputMap.put ( ksViewTiles, "viewTiles" );
        actionMap.put ( "viewTiles", viewTiles );
        inputMap.put ( ksViewTable, "viewTable" );
        actionMap.put ( "viewTable", viewTable );

        view.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu viewChoose = new WebPopupMenu ();

                final WebRadioButtonMenuItem icons = new WebRadioButtonMenuItem ( Icons.icons );
                icons.setLanguage ( "weblaf.filechooser.view.icons" );
                icons.setAccelerator ( new HotkeyData ( ksViewIcons ) );
                icons.setSelected ( getViewType ().equals ( FileChooserViewType.icons ) );
                icons.addActionListener ( viewIcons );
                viewChoose.add ( icons );

                final WebRadioButtonMenuItem tiles = new WebRadioButtonMenuItem ( Icons.tiles );
                tiles.setLanguage ( "weblaf.filechooser.view.tiles" );
                tiles.setAccelerator ( new HotkeyData ( ksViewTiles ) );
                tiles.setSelected ( getViewType ().equals ( FileChooserViewType.tiles ) );
                tiles.addActionListener ( viewTiles );
                viewChoose.add ( tiles );

                final WebRadioButtonMenuItem table = new WebRadioButtonMenuItem ( Icons.table );
                table.setLanguage ( "weblaf.filechooser.view.table" );
                table.setAccelerator ( new HotkeyData ( ksViewTable ) );
                table.setSelected ( getViewType ().equals ( FileChooserViewType.table ) );
                table.addActionListener ( viewTable );
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
        if ( desktop != null )
        {
            toolBar.addToEnd ( folderDesktop );
        }
        toolBar.addToEnd ( refresh );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( folderNew );
        toolBar.addToEnd ( remove );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( view );
        add ( toolBar, BorderLayout.NORTH );

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
    protected Component createContent ()
    {
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

        centralContainer = new WebPanel ( StyleId.filechooserCenterPanel.at ( this ), new BorderLayout ( 4, 0 ) );

        centralSplit = new WebSplitPane ( StyleId.filechooserCenterSplit.at ( centralContainer ), WebSplitPane.HORIZONTAL_SPLIT );
        centralSplit.setOneTouchExpandable ( true );

        createFileTree ();
        createFileList ();
        createFileTable ();

        centralSplit.setLeftComponent ( treeScroll );
        centralSplit.setRightComponent ( fileListScroll );
        centralSplit.setDividerLocation ( dividerLocation );

        centralContainer.add ( centralSplit );

        return centralContainer;
    }

    /**
     * Creates file tree and all related components.
     */
    protected void createFileTree ()
    {
        treeScroll = new WebScrollPane ( StyleId.filechooserNavScroll.at ( centralSplit ) );
        treeScroll.setPreferredSize ( new Dimension ( dividerLocation, 1 ) );

        fileTree = new WebFileTree ( StyleId.filechooserFileTree.at ( treeScroll ) );
        fileTree.setExpandSelected ( true );
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
        treeScroll.setViewportView ( fileTree );

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
    }

    /**
     * Creates file list and all related components.
     */
    protected void createFileList ()
    {
        fileListScroll = new WebScrollPane ( StyleId.filechooserViewScroll.at ( centralSplit ) );
        fileListScroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        fileListScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

        fileList = new WebFileList ( StyleId.filechooserFileListTiles.at ( fileListScroll ) );
        fileList.setDropMode ( DropMode.ON );
        fileList.setEditable ( true );
        fileList.setTransferHandler ( new FilesLocateDropHandler ( UpdateSource.list ) );
        fileListScroll.setViewportView ( fileList );

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
                if ( SwingUtils.isDoubleClick ( e ) && fileList.getSelectedIndex () != -1 )
                {
                    final File file = fileList.getSelectedFile ();
                    if ( file.isDirectory () )
                    {
                        updateCurrentFolder ( file, UpdateSource.list );
                    }
                    else
                    {
                        fireAcceptAction ( new ActionEvent ( fileList, e.getID (), "Files selected", e.getWhen (), e.getModifiers () ) );
                    }
                }
            }
        } );

        fileList.addListSelectionListener ( new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting () )
                {
                    updateSelectedFilesField ();
                }
            }
        } );

        fileList.addListEditListener ( new ListEditAdapter ()
        {
            @Override
            public void editFinished ( final int index, final Object oldValue, final Object newValue )
            {
                // Saving for future selection
                final File file = ( ( FileElement ) newValue ).getFile ();

                // Updating current view
                // This action can be optimized, but that will make a lot of additional actions and most likely cause some troubles
                reloadCurrentFolder ();

                // Updating list selection after edit
                fileList.setSelectedFile ( file );
            }
        } );
    }

    /**
     * Creates file table and all related components.
     */
    protected void createFileTable ()
    {
        fileTableScroll = new WebScrollPane ( StyleId.filechooserViewScroll.at ( centralSplit ) );
        fileTableScroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        fileTableScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

        fileTable = new WebFileTable ( StyleId.filechooserFileTable.at ( fileTableScroll ) );
        fileTable.setOpaque ( false );
        fileTable.setEditable ( true );
        fileTable.setRowSorter ( createFileTableRowSorter () );
        fileTable.setTransferHandler ( new FilesLocateDropHandler ( UpdateSource.table ) );
        fileTableScroll.setViewportView ( fileTable );

        // Custom action map
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

        // File table listeners
        fileTable.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( SwingUtils.isDoubleClick ( e ) && fileTable.getSelectedRow () != -1 )
                {
                    final File file = fileTable.getSelectedFile ();
                    if ( file.isDirectory () )
                    {
                        updateCurrentFolder ( file, UpdateSource.table );
                    }
                    else
                    {
                        fireAcceptAction ( new ActionEvent ( fileTable, e.getID (), "Files selected", e.getWhen (), e.getModifiers () ) );
                    }
                }
            }
        } );
        fileTable.getSelectionModel ().addListSelectionListener ( new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting () )
                {
                    updateSelectedFilesField ();
                }
            }
        } );
        fileTable.getDefaultEditor ( File.class ).addCellEditorListener ( new CellEditorAdapter ()
        {
            @Override
            public void editingStopped ( final ChangeEvent e )
            {
                // Saving for future selection
                final File file = fileTable.getSelectedFile ();

                // Updating current view
                // This action can be optimized, but that will make a lot of additional actions and most likely cause some troubles
                reloadCurrentFolder ();

                // Updating list selection after edit
                fileTable.setSelectedFile ( file );
            }
        } );
    }

    /**
     * Returns file table row sorter.
     *
     * @return file table row sorter
     */
    protected TableRowSorter<WebFileTableModel> createFileTableRowSorter ()
    {
        final WebFileTableModel tableModel = fileTable.getFileTableModel ();
        final TableRowSorter<WebFileTableModel> rowSorter = new TableRowSorter<WebFileTableModel> ( tableModel );
        for ( int i = 0; i < tableModel.getColumnCount (); i++ )
        {
            final String columnId = tableModel.getColumnId ( i );
            final Comparator<File> comp;
            if ( columnId.equals ( FileTableColumns.NAME_COLUMN ) )
            {
                comp = new FileComparator ();
            }
            else if ( columnId.equals ( FileTableColumns.SIZE_COLUMN ) )
            {
                comp = new Comparator<File> ()
                {
                    @Override
                    public int compare ( final File a, final File b )
                    {
                        final boolean ad = FileUtils.isDirectory ( a );
                        final boolean bd = FileUtils.isDirectory ( b );
                        if ( ad && bd )
                        {
                            return 0;
                        }
                        if ( ad )
                        {
                            return -1;
                        }
                        if ( bd )
                        {
                            return +1;
                        }
                        final long al = a.length ();
                        final long bl = b.length ();
                        return al < bl ? -1 : al > bl ? +1 : 0;
                    }
                };
            }
            else if ( columnId.equals ( FileTableColumns.EXTENSION_COLUMN ) )
            {
                comp = new Comparator<File> ()
                {
                    @Override
                    public int compare ( final File a, final File b )
                    {
                        final boolean af = FileUtils.isFile ( a );
                        final boolean bf = FileUtils.isFile ( b );
                        final String as = af ? FileUtils.getFileExtPart ( a.getName (), true ) : "";
                        final String bs = bf ? FileUtils.getFileExtPart ( b.getName (), true ) : "";
                        return as.compareToIgnoreCase ( bs );
                    }
                };
            }
            else if ( columnId.equals ( FileTableColumns.MODIFICATION_DATE_COLUMN ) )
            {
                comp = new Comparator<File> ()
                {
                    @Override
                    public int compare ( final File a, final File b )
                    {
                        final long al = a.lastModified ();
                        final long bl = b.lastModified ();
                        return al < bl ? -1 : al > bl ? +1 : 0;
                    }
                };
            }
            else
            {
                comp = null;
            }

            if ( comp == null )
            {
                rowSorter.setSortable ( i, false );
            }
            else
            {
                rowSorter.setComparator ( i, comp );
            }
        }
        return rowSorter;
    }

    /**
     * Returns south panel content.
     *
     * @return south panel content
     */
    protected Component createControls ()
    {
        final TableLayout layout = new TableLayout ( new double[][]{
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED },
                { TableLayout.PREFERRED }
        }, 4, 4 );
        controlsPanel = new WebPanel ( StyleId.filechooserSouthPanel.at ( this ), layout );
        add ( controlsPanel, BorderLayout.SOUTH );

        final StyleId captionStyleId = StyleId.filechooserSelectedLabel.at ( controlsPanel );
        final WebLabel captionLabel = new WebLabel ( captionStyleId, "weblaf.filechooser.files.selected" );
        controlsPanel.add ( captionLabel, "0,0" );

        selectedFilesViewField = new WebFileChooserField ( false );
        selectedFilesViewField.setPreferredWidth ( 10 );
        selectedFilesViewField.setShowRemoveButton ( false );
        selectedFilesViewField.setShowFileShortName ( true );
        selectedFilesViewField.setFilesDropEnabled ( false );

        selectedFilesTextField = new WebTextField ( 1 );
        selectedFilesTextField.addCaretListener ( new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                updateAcceptButtonState ();
            }
        } );
        selectedFilesTextField.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                acceptButton.doClick ( 0 );
            }
        } );

        controlsPanel.add ( chooserType == FileChooserType.save ? selectedFilesTextField : selectedFilesViewField, "1,0" );

        fileFilters = new WebComboBox ();
        fileFilters.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final javax.swing.filechooser.FileFilter oldFilter = fileFilter;
                final AbstractFileFilter newFilter = ( AbstractFileFilter ) fileFilters.getSelectedItem ();
                setActiveFileFilter ( newFilter, false );
                fireFileFilterChanged ( oldFilter, newFilter );
            }
        } );
        controlsPanel.add ( fileFilters, "2,0" );

        acceptButton = new WebButton ( StyleId.filechooserAcceptButton.at ( controlsPanel ), Icons.accept );
        acceptButton.addHotkey ( WebFileChooserPanel.this, Hotkey.CTRL_ENTER, TooltipWay.up );
        acceptButton.putClientProperty ( GroupPanel.FILL_CELL, true );
        acceptButton.setEnabled ( false );
        acceptButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fireAcceptAction ( e );
            }
        } );

        cancelButton = new WebButton ( StyleId.filechooserCancelButton.at ( controlsPanel ), "weblaf.filechooser.cancel", Icons.cancel );
        cancelButton.addHotkey ( WebFileChooserPanel.this, Hotkey.ESCAPE, TooltipWay.up );
        cancelButton.putClientProperty ( GroupPanel.FILL_CELL, true );
        cancelButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                fireCancelAction ( e );
            }
        } );

        final List<String> properties = new ImmutableList<String> ( AbstractButton.TEXT_CHANGED_PROPERTY );
        SwingUtils.equalizeComponentsWidth ( properties, acceptButton, cancelButton );

        if ( showControlButtons )
        {
            controlsPanel.add ( acceptButton, "3,0" );
            controlsPanel.add ( cancelButton, "4,0" );
        }

        return controlsPanel;
    }

    /**
     * Sets accessory component.
     *
     * @param accessory new accessory component
     */
    public void setAccessory ( final JComponent accessory )
    {
        if ( this.accessory != accessory )
        {
            // Removing old accessory
            if ( this.accessory != null )
            {
                centralContainer.remove ( this.accessory );
            }

            // Updating accessory reference
            this.accessory = accessory;

            // Adding accessory if it isn't null
            if ( accessory != null )
            {
                centralContainer.add ( accessory, BorderLayout.LINE_END );
            }

            // Updating layout
            centralContainer.revalidate ();
        }
    }

    /**
     * Returns field used to display selected files in {@link FileChooserType#save} mode.
     *
     * @return field used to display selected files in {@link FileChooserType#save} mode
     */
    public WebTextField getSelectedFilesTextField ()
    {
        return selectedFilesTextField;
    }

    /**
     * Returns field used to display selected files in {@link FileChooserType#open} mode.
     *
     * @return field used to display selected files in {@link FileChooserType#open} mode
     */
    public WebFileChooserField getSelectedFilesViewField ()
    {
        return selectedFilesViewField;
    }

    /**
     * Returns file tree.
     *
     * @return file tree
     */
    public WebFileTree getFileTree ()
    {
        return fileTree;
    }

    /**
     * Returns file list.
     *
     * @return file list
     */
    public WebFileList getFileList ()
    {
        return fileList;
    }

    /**
     * Returns accessory component.
     *
     * @return accessory component
     */
    public JComponent getAccessory ()
    {
        return accessory;
    }

    /**
     * Returns file table.
     *
     * @return file table
     */
    public WebFileTable getFileTable ()
    {
        return fileTable;
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
        final int dividerLocation = centralSplit.getDividerLocation ();
        switch ( viewType )
        {
            case icons:
            {
                fileList.setStyleId ( StyleId.filechooserFileListIcons.at ( fileListScroll ) );
                centralSplit.setRightComponent ( fileListScroll );
                if ( viewChanged )
                {
                    fileList.setSelectedFiles ( fileTable.getSelectedFiles () );
                }
                fileList.requestFocusInWindow ();
                break;
            }
            case tiles:
            {
                fileList.setStyleId ( StyleId.filechooserFileListTiles.at ( fileListScroll ) );
                centralSplit.setRightComponent ( fileListScroll );
                if ( viewChanged )
                {
                    fileList.setSelectedFiles ( fileTable.getSelectedFiles () );
                }
                fileList.requestFocusInWindow ();
                break;
            }
            case table:
            {
                centralSplit.setRightComponent ( fileTableScroll );
                if ( viewChanged )
                {
                    fileTable.setSelectedFiles ( fileList.getSelectedFiles () );
                }
                fileTable.requestFocusInWindow ();
                break;
            }
        }
        centralSplit.setDividerLocation ( dividerLocation );
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
            file = FileUtils.getParent ( file );
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

            // Updating control
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

        // Updating controls
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
        folderUp.setEnabled ( SystemUtils.isWindows () ? currentFolder != null :
                currentFolder != null && FileUtils.getParent ( currentFolder ) != null );
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
            final String fileName = selectedFilesTextField.getText ().trim ();
            final File file = new File ( currentFolder, fileName );
            return CollectionUtils.asList ( file );
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
    protected List<File> getFilteredSelectedFiles ( final Collection<File> allFiles )
    {
        final List<File> files = FileUtils.filterFiles ( allFiles, fileFilter );
        return FileUtils.filterFiles ( files, selectedFilesFilter );
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
        if ( file != null && !file.exists () )
        {
            updateSelectedFilesFieldImpl ( CollectionUtils.asList ( file ) );
        }
        else
        {
            updateSelectedFilesFieldImpl ( new ArrayList<File> ( 0 ) );
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
        updateSelectedFilesFieldImpl ( files );
    }

    /**
     * Updates currently selected files field.
     */
    protected void updateSelectedFilesField ()
    {
        updateSelectedFilesFieldImpl ( getAllSelectedFiles () );
    }

    /**
     * Updates currently selected files field.
     *
     * @param selected selected files
     */
    protected void updateSelectedFilesFieldImpl ( final Collection<File> selected )
    {
        // Filtered selected files
        final List<File> files = getFilteredSelectedFiles ( selected );

        // Updating controls
        folderNew.setEnabled ( currentFolder != null );
        remove.setEnabled ( currentFolder != null && selected.size () > 0 );

        // Updating selected files view component
        if ( chooserType == FileChooserType.save )
        {
            if ( files.size () > 0 )
            {
                final File file = files.get ( 0 );
                selectedFilesViewField.setSelectedFile ( file );
                selectedFilesTextField.setText ( getSingleFileView ( file ) );
            }
        }
        else
        {
            selectedFilesViewField.setSelectedFiles ( files );
            selectedFilesTextField.setText ( getFilesView ( files ) );
        }

        // When choose action allowed
        updateAcceptButtonState ( files );

        // Firing selection change
        fireFileSelectionChanged ( files );
    }

    /**
     * Returns text representation for specified files list.
     *
     * @param files files list
     * @return text representation for specified files list
     */
    private String getFilesView ( final List<File> files )
    {
        return files.size () > 1 ? getMultiFilesView ( files ) : files.size () == 1 ? getSingleFileView ( files.get ( 0 ) ) : "";
    }

    /**
     * Returns text representation for single file.
     *
     * @param file file
     * @return text representation for single file
     */
    protected String getSingleFileView ( final File file )
    {
        return file.getName ();
    }

    /**
     * Returns text representation for multiple files.
     *
     * @param files files list
     * @return text representation for multiple files
     */
    protected String getMultiFilesView ( final List<File> files )
    {
        return TextUtils.listToString ( files, ", ", QUOTED_FILE_NAME );
    }

    /**
     * Updates accept button state.
     */
    protected void updateAcceptButtonState ()
    {
        updateAcceptButtonState ( getAllSelectedFiles () );
    }

    /**
     * Updates accept button state.
     *
     * @param files filtered selected files
     */
    protected void updateAcceptButtonState ( final List<File> files )
    {
        if ( chooserType == FileChooserType.save )
        {
            // Accept enabled due to entered file name
            acceptButton.setEnabled ( TextUtils.notBlank ( selectedFilesTextField.getText () ) );
        }
        else
        {
            // Accept enabled due to selected files
            acceptButton.setEnabled ( files.size () > 0 );
        }
    }

    /**
     * Updates selected files field panel content.
     */
    protected void updateSelectedFilesFieldPanel ()
    {
        controlsPanel.remove ( selectedFilesTextField, selectedFilesViewField );
        controlsPanel.add ( chooserType == FileChooserType.save ? selectedFilesTextField : selectedFilesViewField, "1,0" );
        controlsPanel.revalidate ();
        controlsPanel.repaint ();
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
        fileList.setFileFilter ( applyHiddenFilesFilter ( applyDirectoriesFilter ( fileFilter ) ) );
        fileTable.setFileFilter ( applyHiddenFilesFilter ( applyDirectoriesFilter ( fileFilter ) ) );
    }

    /**
     * Adds "isDirectory" as one of filter OR conditions.
     *
     * @param fileFilter filter to process
     * @return new file filter with additional condition
     */
    protected GroupedFileFilter applyDirectoriesFilter ( final AbstractFileFilter fileFilter )
    {
        return new GroupedFileFilter (
                getFileSelectionMode () == FileSelectionMode.directoriesOnly ? FilterGroupType.AND : FilterGroupType.OR,
                fileFilter, new DirectoriesFilter ()
        );
    }

    /**
     * Updates directory selection components filters.
     */
    protected void updateDirectoryComponentFilters ()
    {
        pathField.setFileFilter ( applyHiddenFilesFilter ( new DirectoriesFilter () ) );
        fileTree.setFileFilter ( applyHiddenFilesFilter ( new DirectoriesFilter () ) );
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
        fileTree.reloadChildren ( currentFolder );
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
        all.add ( new WebLabel ( "weblaf.filechooser.delete.confirm.text" ), BorderLayout.NORTH );

        final VerticalFlowLayout removalListLayout = new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 5, true, false );
        final WebPanel deleteFilesPanel = new WebPanel ( StyleId.filechooserRemovalListPanel.at ( this ), removalListLayout );
        for ( final File file : files )
        {
            deleteFilesPanel.add ( new WebLabel ( file.getName (), FileUtils.getFileIcon ( file ), WebLabel.LEFT ) );
        }
        final WebScrollPane scroll = new WebScrollPane ( deleteFilesPanel )
        {
            @NotNull
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

        final int confirm = WebOptionPane.showConfirmDialog ( WebFileChooserPanel.this,
                all, LM.get ( "weblaf.filechooser.delete.confirm.title" ),
                WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE );

        if ( confirm == WebOptionPane.YES_OPTION )
        {
            FileUtils.deleteFiles ( files );
            reloadCurrentFolder ();
        }
    }

    /**
     * Returns accept button listener.
     *
     * @return accept button listener
     */
    public ActionListener getAcceptListener ()
    {
        return acceptListener;
    }

    /**
     * Sets accept button listener.
     *
     * @param acceptListener accept button listener
     */
    public void setAcceptListener ( final ActionListener acceptListener )
    {
        this.acceptListener = acceptListener;
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
     * Fires accept action.
     *
     * @param e action event
     */
    protected void fireAcceptAction ( final ActionEvent e )
    {
        if ( acceptListener != null )
        {
            acceptListener.actionPerformed ( e );
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
     * Returns currently active file filter.
     *
     * @return currently active file filter
     */
    public AbstractFileFilter getActiveFileFilter ()
    {
        return fileFilter;
    }

    /**
     * Sets the specified file filter as the only one available.
     *
     * @param fileFilter file filter to set
     */
    public void setFileFilter ( final FileFilter fileFilter )
    {
        setFileFilter ( FileUtils.transformFileFilter ( fileFilter ) );
    }

    /**
     * Sets the specified file filter as the only one available.
     *
     * @param fileFilter file filter to set
     */
    public void setFileFilter ( final javax.swing.filechooser.FileFilter fileFilter )
    {
        setFileFilter ( FileUtils.transformFileFilter ( fileFilter ) );
    }

    /**
     * Sets the specified file filter as the only one available.
     *
     * @param fileFilter file filter to set
     */
    public void setFileFilter ( final AbstractFileFilter fileFilter )
    {
        this.availableFilters = CollectionUtils.asList ( fileFilter );
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
        for ( final javax.swing.filechooser.FileFilter fileFilter : fileFilters )
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
        this.availableFilters = CollectionUtils.asList ( fileFilters );
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
        controlsPanel.remove ( acceptButton, cancelButton );
        if ( showControlButtons )
        {
            controlsPanel.add ( acceptButton, "3,0" );
            controlsPanel.add ( cancelButton, "4,0" );
        }
        controlsPanel.revalidate ();
        controlsPanel.repaint ();
    }

    /**
     * Returns accept button text.
     *
     * @return accept button text
     */
    public String getAcceptButtonText ()
    {
        return acceptButton.getText ();
    }

    /**
     * Restores default accept button text for the specified chooser type.
     */
    public void restoreButtonText ()
    {
        setAcceptButtonText ( null );
    }

    /**
     * Sets accept button text.
     * You can specify null to reset text to default value.
     *
     * @param text accept button text
     */
    public void setAcceptButtonText ( final String text )
    {
        if ( text == null )
        {
            setAcceptButtonText ( chooserType.getApproveButtonText () );
        }
        else
        {
            acceptButton.removeLanguage ();
            acceptButton.setLanguage ( text );
        }
    }

    /**
     * Sets accept button language key.
     *
     * @param key accept button language key
     */
    public void setAcceptButtonLanguage ( final String key )
    {
        acceptButton.setLanguage ( key );
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
     * Returns file selection mode.
     *
     * @return file selection mode
     */
    public FileSelectionMode getFileSelectionMode ()
    {
        return fileSelectionMode;
    }

    /**
     * Sets file selection mode.
     *
     * @param fileSelectionMode file selection mode
     */
    public void setFileSelectionMode ( final FileSelectionMode fileSelectionMode )
    {
        this.fileSelectionMode = fileSelectionMode;
        updateFileComponentFilters ();
        updateSelectedFilesField ();
    }

    /**
     * Returns whether hidden files are displayed or not.
     *
     * @return true if should display hidden files, false otherwise
     */
    public boolean isShowHiddenFiles ()
    {
        return showHiddenFiles;
    }

    /**
     * Sets whether hidden files should be displayed or not.
     *
     * @param showHiddenFiles whether should display hidden files or not
     */
    public void setShowHiddenFiles ( final boolean showHiddenFiles )
    {
        this.showHiddenFiles = showHiddenFiles;
        updateDirectoryComponentFilters ();
        updateFileComponentFilters ();
        pathField.setShowHiddenFiles ( showHiddenFiles );
    }

    /**
     * Adds file chooser listener.
     *
     * @param listener new file chooser listener
     */
    public void addFileChooserListener ( final FileChooserListener listener )
    {
        listenerList.add ( FileChooserListener.class, listener );
    }

    /**
     * Removes file chooser listener.
     *
     * @param listener file chooser listener to remove
     */
    public void removeFileChooserListener ( final FileChooserListener listener )
    {
        listenerList.remove ( FileChooserListener.class, listener );
    }

    /**
     * Fired when displayed in file chooser directory changes.
     *
     * @param newDirectory newly displayed directory
     */
    protected void fireDirectoryChanged ( final File newDirectory )
    {
        for ( final FileChooserListener listener : listenerList.getListeners ( FileChooserListener.class ) )
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
        for ( final FileChooserListener listener : listenerList.getListeners ( FileChooserListener.class ) )
        {
            listener.selectionChanged ( selectedFiles );
        }
    }

    /**
     * Fired when selected file filter change.
     *
     * @param oldFilter previously used filter
     * @param newFilter currently used filter
     */
    protected void fireFileFilterChanged ( final javax.swing.filechooser.FileFilter oldFilter,
                                           final javax.swing.filechooser.FileFilter newFilter )
    {
        for ( final FileChooserListener listener : listenerList.getListeners ( FileChooserListener.class ) )
        {
            listener.fileFilterChanged ( oldFilter, newFilter );
        }
    }

    /**
     * Returns whether multiple files selection is allowed or not.
     *
     * @return true if multiple files selection is allowed, false otherwise
     */
    public boolean isMultiSelectionEnabled ()
    {
        return multiSelectionEnabled;
    }

    /**
     * Sets whether multiple files selection is allowed or not.
     *
     * @param multiSelectionEnabled whether multiple files selection is allowed or not
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
         * Mostly refers to updates made from outside using open API.
         */
        other
    }

    /**
     * FileDragAndDropHandler extension to provide drop-to-find-file functionality.
     */
    protected class FilesLocateDropHandler extends FilesTransferHandler
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
            super ( false, true );
            this.updateSource = updateSource;
        }

        @Override
        public boolean filesDropped ( final List<File> files )
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
                    updateCurrentFolder ( FileUtils.getParent ( file ), updateSource );
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
        @Nullable
        @Override
        public Icon getIcon ( @NotNull final RenderingParameters parameters )
        {
            return null;
        }

        @Override
        public String getDescription ()
        {
            return null;
        }

        @Override
        public boolean accept ( final File file )
        {
            return showHiddenFiles || !FileUtils.isHidden ( file );
        }
    }
}