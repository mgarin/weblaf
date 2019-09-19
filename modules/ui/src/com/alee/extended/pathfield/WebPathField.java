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

package com.alee.extended.pathfield;

import com.alee.api.annotations.NotNull;
import com.alee.extended.filechooser.PathFieldListener;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.list.ListCellParameters;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.drag.transfer.FilesTransferHandler;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.*;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.filefilter.DirectoriesFilter;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Custom component which allows file system path selection.
 *
 * @author Mikle Garin
 * @author Michka Popoff
 */
public class WebPathField extends WebPanel
{
    /**
     * todo 1. Full and proper UI & styling implementation
     * todo 2. Proper focus handling after finishing manual path editing
     * todo 3. Enhance path update method performance
     * todo 4. While menu is opened - open other menus on simple rollover
     */

    /**
     * Custom property used to store file icon into button.
     */
    protected static final String FILE_ICON = "fileIcon";

    /**
     * File system view.
     */
    protected static FileSystemView fsv = FileSystemView.getFileSystemView ();

    /**
     * Field listeners.
     */
    protected List<PathFieldListener> listeners = new ArrayList<PathFieldListener> ( 1 );

    /**
     * UI components.
     */
    protected WebPanel contentPanel;
    protected WebTextField pathField;
    protected FocusAdapter pathFocusListener;
    protected WebButton myComputer = null;

    /**
     * Whether hidden files are displayed or not.
     */
    protected boolean showHiddenFiles = false;

    /**
     * Autocomplete.
     */
    protected boolean autocompleteEnabled = true;
    protected JWindow autocompleteDialog = null;

    /**
     * Root item menu.
     */
    protected int rootsMenuItemsCount = 0;
    protected WebPopupMenu rootsMenu = null;
    protected WebToggleButton rootsArrowButton = null;

    /**
     * Field settings.
     */
    protected AbstractFileFilter fileFilter = new DirectoriesFilter ();
    protected int preferredWidth = -1;
    protected boolean filesDropEnabled = true;
    protected File selectedPath;

    public WebPathField ()
    {
        this ( StyleId.auto );
    }

    public WebPathField ( final String path )
    {
        this ( StyleId.auto, path );
    }

    public WebPathField ( final File path )
    {
        this ( StyleId.auto, path );
    }

    public WebPathField ( final StyleId id )
    {
        this ( id, FileUtils.getDiskRoots ()[ 0 ] );
    }

    public WebPathField ( final StyleId id, final String path )
    {
        this ( id, new File ( path ) );
    }

    public WebPathField ( final StyleId id, final File path )
    {
        super ( id );

        // Files TransferHandler
        setTransferHandler ( new FilesTransferHandler ( false, true )
        {
            @Override
            public boolean isDropEnabled ()
            {
                return filesDropEnabled;
            }

            @Override
            public boolean filesDropped ( final List<File> files )
            {
                // Setting dragged files
                final FileFilter filter = getFileFilter ();
                for ( final File file : files )
                {
                    final File actualFile = file.isDirectory () ? file : FileUtils.getParent ( file );
                    if ( filter == null || filter.accept ( actualFile ) )
                    {
                        folderSelected ( actualFile );
                        return true;
                    }
                }
                return false;
            }
        } );

        final StyleId contentStyle = StyleId.pathfieldContentPanel.at ( this );
        contentPanel = new WebPanel ( contentStyle, new HorizontalFlowLayout ( 0, true ) );
        add ( contentPanel, BorderLayout.CENTER );

        //        WebImage editImage = new WebImage ( WebPathField.class, Icons.edit );
        //        editImage.setCursor ( Cursor.getPredefinedCursor ( Cursor.TEXT_CURSOR ) );
        //        editImage.addMouseListener ( new MouseAdapter ()
        //        {
        //            public void mousePressed ( MouseEvent e )
        //            {
        //                if ( SwingUtilities.isLeftMouseButton ( e ) )
        //                {
        //                    startEditing ();
        //                }
        //            }
        //        } );
        //        add ( editImage,BorderLayout.EAST );

        pathField = new WebTextField ( StyleId.pathfieldPathField.at ( contentPanel ) );
        pathField.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( pathField.getText ().trim ().equals ( "" ) )
                {
                    folderSelected ( null );
                }
                else
                {
                    final File chosenPath = new File ( pathField.getText () );
                    if ( chosenPath.exists () && chosenPath.isDirectory () )
                    {
                        folderSelected ( chosenPath );
                    }
                    else
                    {
                        updatePath ();
                    }
                }
                WebPathField.this.transferFocus ();
            }
        } );
        pathField.addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( autocompleteDialog == null || !autocompleteDialog.isVisible () )
                {
                    if ( Hotkey.ESCAPE.isTriggered ( e ) )
                    {
                        if ( selectedPath == null && pathField.getText ().trim ().equals ( "" ) ||
                                selectedPath != null && getProperSelectedPath ().equals ( pathField.getText () ) )
                        {
                            updatePath ();
                            WebPathField.this.transferFocus ();
                        }
                        else
                        {
                            pathField.setText ( getProperSelectedPath () );
                        }
                    }
                }
            }
        } );

        pathFocusListener = new FocusAdapter ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                if ( selectedPath == null && pathField.getText ().trim ().equals ( "" ) ||
                        selectedPath != null && getProperSelectedPath ().equals ( pathField.getText () ) )
                {
                    updatePath ();
                }
            }
        };

        // Autocomplete dialog listener
        pathField.addCaretListener ( new CaretListener ()
        {
            private WebList list = null;
            private WebScrollPane listScroll;

            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                if ( !autocompleteEnabled || !pathField.isVisible () || !pathField.isShowing () )
                {
                    if ( autocompleteDialog != null && autocompleteDialog.isVisible () )
                    {
                        hideDialog ();
                    }
                    return;
                }

                if ( autocompleteDialog == null )
                {
                    autocompleteDialog = new JWindow ( CoreSwingUtils.getWindowAncestor ( WebPathField.this ) );
                    autocompleteDialog.getContentPane ().setLayout ( new BorderLayout () );
                    autocompleteDialog.setFocusable ( false );

                    CoreSwingUtils.getWindowAncestor ( WebPathField.this ).addComponentListener ( new ComponentAdapter ()
                    {
                        @Override
                        public void componentMoved ( final ComponentEvent e )
                        {
                            hideDialog ();
                        }

                        @Override
                        public void componentResized ( final ComponentEvent e )
                        {
                            hideDialog ();
                        }
                    } );

                    list = new WebList ();
                    list.setFocusable ( false );
                    list.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
                    list.setSelectOnHover ( true );
                    list.setCellRenderer ( new WebListCellRenderer<File, WebList, ListCellParameters<File, WebList>> ()
                    {
                        @Override
                        protected Icon iconForValue ( final ListCellParameters<File, WebList> parameters )
                        {
                            return FileUtils.getFileIcon ( parameters.value () );
                        }

                        @Override
                        protected String textForValue ( final ListCellParameters<File, WebList> parameters )
                        {
                            return FileUtils.getDisplayFileName ( parameters.value () );
                        }
                    } );
                    list.addMouseListener ( new MouseAdapter ()
                    {
                        @Override
                        public void mousePressed ( final MouseEvent e )
                        {
                            final int index = list.getUI ().locationToIndex ( list, e.getPoint () );
                            if ( SwingUtilities.isLeftMouseButton ( e ) && index != -1 )
                            {
                                setSelectedPath ( ( File ) list.getModel ().getElementAt ( index ) );
                            }
                        }
                    } );
                    listScroll = new WebScrollPane ( StyleId.pathfieldPopupScroll.at ( pathField ), list );
                    autocompleteDialog.getContentPane ().add ( listScroll, BorderLayout.CENTER );

                    pathField.addKeyListener ( new KeyAdapter ()
                    {
                        @Override
                        public void keyPressed ( final KeyEvent e )
                        {
                            if ( autocompleteDialog.isShowing () && list.getModel ().getSize () > 0 )
                            {
                                if ( list.getSelectedIndex () != -1 )
                                {
                                    if ( Hotkey.ESCAPE.isTriggered ( e ) || Hotkey.F2.isTriggered ( e ) ||
                                            Hotkey.CTRL_ENTER.isTriggered ( e ) )
                                    {
                                        hideDialog ();
                                    }
                                    else if ( Hotkey.RIGHT.isTriggered ( e ) )
                                    {
                                        if ( pathField.getCaretPosition () == pathField.getText ().length () )
                                        {
                                            setSelectedPath ( ( File ) list.getSelectedValue () );
                                        }
                                    }
                                    else if ( Hotkey.UP.isTriggered ( e ) )
                                    {
                                        if ( list.getSelectedIndex () == 0 )
                                        {
                                            list.setSelectedIndex ( list.getModel ().getSize () - 1 );
                                            scrollToSelected ();
                                        }
                                        else
                                        {
                                            list.setSelectedIndex ( list.getSelectedIndex () - 1 );
                                            scrollToSelected ();
                                        }
                                    }
                                    else if ( Hotkey.DOWN.isTriggered ( e ) )
                                    {
                                        if ( list.getSelectedIndex () == list.getModel ().getSize () - 1 )
                                        {
                                            list.setSelectedIndex ( 0 );
                                            scrollToSelected ();
                                        }
                                        else
                                        {
                                            list.setSelectedIndex ( list.getSelectedIndex () + 1 );
                                            scrollToSelected ();
                                        }
                                    }
                                }
                                else
                                {
                                    list.setSelectedIndex ( 0 );
                                    scrollToSelected ();
                                }
                            }
                        }

                        private void scrollToSelected ()
                        {
                            list.scrollRectToVisible (
                                    list.getUI ().getCellBounds ( list, list.getSelectedIndex (), list.getSelectedIndex () ) );
                        }
                    } );

                    pathField.addFocusListener ( new FocusAdapter ()
                    {
                        @Override
                        public void focusLost ( final FocusEvent e )
                        {
                            hideDialog ();
                        }
                    } );
                }

                //                new Thread ( new Runnable ()
                //                {
                //                    public void run ()
                //                    {
                // Taking only the part till the caret
                final String t = pathField.getText ().substring ( 0, pathField.getCaretPosition () );

                // Retrieving parent path
                int beginIndex = t.lastIndexOf ( File.separator );
                beginIndex = beginIndex != -1 ? beginIndex + 1 : 0;

                // Parent file
                final String parentPath = t.substring ( 0, beginIndex );
                final File parent = parentPath.trim ().equals ( "" ) ? null : new File ( parentPath );

                final List<File> similar = getSimilarFileChildren ( parent, t.substring ( beginIndex ) );
                if ( similar != null && similar.size () > 0 )
                {
                    final List<File> filteredList = new ArrayList<File> ();
                    for ( final File file : similar )
                    {
                        if ( showHiddenFiles || !FileUtils.isHidden ( file ) )
                        {
                            filteredList.add ( file );
                        }
                    }
                    updateList ( filteredList );
                }
                else
                {
                    hideDialog ();
                }
                //                    }
                //                } ).start ();
            }

            private void updateList ( final List<File> similar )
            {
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        list.setModel ( new AbstractListModel ()
                        {
                            @Override
                            public int getSize ()
                            {
                                return similar.size ();
                            }

                            @Override
                            public Object getElementAt ( final int i )
                            {
                                return similar.get ( i );
                            }
                        } );
                        list.setVisibleRowCount ( Math.min ( similar.size (), 6 ) );
                        list.updateUI ();
                        if ( similar.size () > 0 )
                        {
                            list.setSelectedIndex ( 0 );
                        }

                        // Fixing window bounds
                        final Point los = CoreSwingUtils.locationOnScreen ( pathField );
                        autocompleteDialog.setSize ( pathField.getWidth (), listScroll.getPreferredSize ().height );
                        autocompleteDialog.setLocation ( pathField.getComponentOrientation ().isLeftToRight () ? los.x :
                                los.x + pathField.getWidth () - autocompleteDialog.getWidth (), los.y + pathField.getHeight () );

                        // Showing dialog if needed
                        if ( !autocompleteDialog.isShowing () )
                        {
                            autocompleteDialog.setVisible ( true );
                            WebPathField.this.transferFocus ();
                        }
                    }
                } );
            }

            private void hideDialog ()
            {
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        autocompleteDialog.setVisible ( false );
                    }
                } );
            }

            private void setSelectedPath ( final File path )
            {
                String text = path.getAbsolutePath ();
                text = text.endsWith ( File.separator ) ? text : text + File.separator;
                pathField.setText ( text );
                pathField.setCaretPosition ( text.length () );
            }
        } );

        // Edit start listeners
        contentPanel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    startEditing ();
                }
            }
        } );
        HotkeyManager.registerHotkey ( WebPathField.this, WebPathField.this, Hotkey.F2, new HotkeyRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                startEditing ();
            }
        }, true );

        // Resize listener
        addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                if ( !pathField.isShowing () )
                {
                    updatePath ();
                }
            }
        } );

        // Updating initial path
        updatePath ( path );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.pathfield;
    }

    protected void startEditing ()
    {
        if ( !pathField.isFocusOwner () )
        {
            // Updating path field preferred size
            pathField.setPreferredSize ( new Dimension ( 1, contentPanel.getHeight () ) );

            // Clearing content
            contentPanel.removeAll ();

            // Updating path text
            if ( selectedPath != null )
            {
                pathField.setText ( getProperSelectedPath () );
            }
            else
            {
                pathField.setText ( "" );
            }
            pathField.selectAll ();

            // Adding field as main component
            contentPanel.add ( pathField );
            contentPanel.revalidate ();
            contentPanel.repaint ();

            // Focusing field
            WebPathField.this.transferFocus ();
            pathField.addFocusListener ( pathFocusListener );
        }
    }

    protected String getProperSelectedPath ()
    {
        String path = selectedPath.getAbsolutePath ();
        path = path.endsWith ( File.separator ) ? path : path + File.separator;
        return path;
    }

    public boolean isEditing ()
    {
        return pathField.isFocusOwner ();
    }

    @Override
    public void setForeground ( final Color foreground )
    {
        super.setForeground ( foreground );
        if ( isShowing () )
        {
            SwingUtils.setForegroundRecursively ( contentPanel, foreground );
            pathField.setForeground ( foreground );
        }
    }

    public boolean isAutocompleteEnabled ()
    {
        return autocompleteEnabled;
    }

    public void setAutocompleteEnabled ( final boolean autocompleteEnabled )
    {
        this.autocompleteEnabled = autocompleteEnabled;
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
    }

    public AbstractFileFilter getFileFilter ()
    {
        return fileFilter;
    }

    public void setFileFilter ( final AbstractFileFilter fileFilter )
    {
        setFileFilter ( fileFilter, true );
    }

    public void setFileFilter ( final AbstractFileFilter fileFilter, final boolean updatePath )
    {
        this.fileFilter = fileFilter;
        if ( updatePath )
        {
            updatePath ();
        }
    }

    public boolean isFilesDropEnabled ()
    {
        return filesDropEnabled;
    }

    public void setFilesDropEnabled ( final boolean filesDropEnabled )
    {
        this.filesDropEnabled = filesDropEnabled;
    }

    public File getSelectedPath ()
    {
        return selectedPath;
    }

    public void setSelectedPath ( final File selectedPath )
    {
        updatePath ( selectedPath );
    }

    public WebTextField getPathField ()
    {
        return pathField;
    }

    public void updatePath ()
    {
        updatePath ( selectedPath );
    }

    protected synchronized void updatePath ( final File path )
    {
        // todo check if path is proper (filter/hidden)

        // todo Save focused state properly
        // Saving focus state
        // boolean hadFocus = focusOwner;

        // Saving new path
        selectedPath = path;

        // Clearing old path components
        pathField.removeFocusListener ( pathFocusListener );
        contentPanel.removeAll ();

        // Determining orientation
        final boolean ltr = WebPathField.this.getComponentOrientation ().isLeftToRight ();

        // Determining root
        if ( SystemUtils.isWindows () )
        {
            final WebButton computerButton = getMyComputer ();
            contentPanel.add ( computerButton );
            contentPanel.add ( getRootsArrowButton ( ltr ) );
        }

        if ( selectedPath != null )
        {
            // Creating parents list
            File folder = new File ( selectedPath.getAbsolutePath () );
            final List<File> parents = new ArrayList<File> ();
            parents.add ( 0, folder );
            File parent = FileUtils.getParent ( folder );
            while ( parent != null )
            {
                folder = parent;
                parent = FileUtils.getParent ( folder );
                parents.add ( 0, folder );
            }

            // Adding path buttons
            boolean first = true;
            for ( final File file : parents )
            {
                final WebButton wb = new WebButton ( StyleId.pathfieldElementButton.at ( contentPanel ) );
                if ( !SystemUtils.isWindows () && first )
                {
                    wb.setIcon ( Icons.computer );
                    wb.putClientProperty ( FILE_ICON, Icons.computer );
                }
                else
                {
                    wb.setText ( fsv.getSystemDisplayName ( file ) );
                    wb.putClientProperty ( FILE_ICON, FileUtils.getFileIcon ( file, false ) );
                }
                wb.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        folderSelected ( file );
                    }
                } );
                contentPanel.add ( wb );

                int childrenCount = 0;
                final WebPopupMenu menu = new WebPopupMenu ();
                final File[] fileChildren = getFileChildren ( file );
                final List<File> filteredFileChildren = new ArrayList<File> ();
                if ( fileChildren != null )
                {
                    for ( final File fileChild : fileChildren )
                    {
                        if ( showHiddenFiles || !FileUtils.isHidden ( fileChild ) )
                        {
                            filteredFileChildren.add ( fileChild );
                        }
                    }
                }

                final File[] files = FileUtils.sortFiles ( filteredFileChildren.toArray ( new File[ filteredFileChildren.size () ] ) );
                if ( files != null )
                {
                    for ( final File root : files )
                    {
                        if ( root.isDirectory () )
                        {
                            final WebMenuItem menuItem = new WebMenuItem ( FileUtils.getDisplayFileName ( root ) );
                            menuItem.setIcon ( FileUtils.getFileIcon ( root, false ) );
                            menuItem.addActionListener ( new ActionListener ()
                            {
                                @Override
                                public void actionPerformed ( final ActionEvent e )
                                {
                                    folderSelected ( root );
                                }
                            } );
                            menu.add ( menuItem );
                            childrenCount++;
                        }
                    }
                }
                if ( !SystemUtils.isWindows () && first )
                {
                    setRootsMenu ( menu, childrenCount );
                }

                final WebToggleButton children = new WebToggleButton ( StyleId.pathfieldMenuToggleButton.at ( contentPanel ) );
                children.setIcon ( ltr ? Icons.rightSmall : Icons.leftSmall );
                children.setSelectedIcon ( Icons.downSmall );
                children.setEnabled ( childrenCount > 0 );
                children.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        // todo Apply orientation globally on change, not here
                        WebPathField.this.transferFocus ();
                        applyOrientation ( menu );
                        menu.showBelowMiddle ( children );
                    }
                } );
                contentPanel.add ( children );

                menu.addPopupMenuListener ( new PopupMenuListener ()
                {
                    @Override
                    public void popupMenuWillBecomeVisible ( final PopupMenuEvent e )
                    {
                        //
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible ( final PopupMenuEvent e )
                    {
                        children.setSelected ( false );
                    }

                    @Override
                    public void popupMenuCanceled ( final PopupMenuEvent e )
                    {
                        children.setSelected ( false );
                    }
                } );

                first = false;
            }
        }

        // Filling space
        contentPanel.add ( new JLabel () );

        // Shortening long elements
        if ( !SystemUtils.isWindows () )
        {
            while ( getRootsMenu ().getComponentCount () > getRootsMenuItemsCount () )
            {
                getRootsMenu ().remove ( 0 );
            }
        }
        if ( canShortenPath () )
        {
            getRootsMenu ().addSeparator ( 0 );
        }
        while ( canShortenPath () )
        {
            // Adding menu element
            final WebButton wb = ( WebButton ) contentPanel.getComponent ( 2 );
            final WebMenuItem menuItem = new WebMenuItem ();
            menuItem.setIcon ( ( Icon ) wb.getClientProperty ( FILE_ICON ) );
            menuItem.setText ( wb.getText () );
            menuItem.addActionListener ( wb.getActionListeners ()[ 0 ] );
            getRootsMenu ().add ( menuItem, 0 );

            // Removing hidden path and menu buttons from panel
            contentPanel.remove ( 2 );
            contentPanel.remove ( 2 );
        }

        // Updating pane
        revalidate ();
        repaint ();
    }

    protected List<File> getSimilarFileChildren ( final File file, final String namePart )
    {
        final String searchText = namePart.toLowerCase ( Locale.ROOT );
        final File[] children = getFileChildren ( file );
        final List<File> similar = new ArrayList<File> ();
        if ( children != null )
        {
            for ( final File child : children )
            {
                if ( child.getName ().toLowerCase ( Locale.ROOT ).contains ( searchText ) )
                {
                    similar.add ( child );
                }
            }
        }
        return similar;
    }

    protected File[] getFileChildren ( final File file )
    {
        return file != null ? file.listFiles ( fileFilter ) : FileUtils.getDiskRoots ();
    }

    protected boolean canShortenPath ()
    {
        return contentPanel.getPreferredSize ().width > contentPanel.getWidth () && contentPanel.getComponentCount () > 5;
    }

    protected WebButton getMyComputer ()
    {
        if ( myComputer == null )
        {
            myComputer = new WebButton ( StyleId.pathfieldRootButton.at ( contentPanel ), Icons.computer );
            myComputer.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    folderSelected ( null );
                }
            } );
        }
        return myComputer;
    }

    public WebPopupMenu getRootsMenu ()
    {
        return rootsMenu;
    }

    public int getRootsMenuItemsCount ()
    {
        return rootsMenuItemsCount;
    }

    public void setRootsMenu ( final WebPopupMenu rootsMenu, final int childrenCount )
    {
        this.rootsMenu = rootsMenu;
        this.rootsMenuItemsCount = childrenCount;
    }

    protected WebToggleButton getRootsArrowButton ( final boolean ltr )
    {
        if ( rootsArrowButton == null )
        {
            rootsMenu = new WebPopupMenu ();

            final File[] rootFiles = FileUtils.getDiskRoots ();
            for ( final File root : FileUtils.sortFiles ( rootFiles ) )
            {
                final WebMenuItem menuItem = new WebMenuItem ( FileUtils.getDisplayFileName ( root ) );
                menuItem.setIcon ( FileUtils.getFileIcon ( root, false ) );
                menuItem.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        folderSelected ( root );
                    }
                } );
                rootsMenu.add ( menuItem );
                rootsMenuItemsCount++;
            }

            rootsArrowButton = new WebToggleButton ( StyleId.pathfieldMenuToggleButton.at ( contentPanel ) );
            rootsArrowButton.setIcon ( ltr ? Icons.rightSmall : Icons.leftSmall );
            rootsArrowButton.setSelectedIcon ( Icons.downSmall );
            rootsArrowButton.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    WebPathField.this.transferFocus ();
                    applyOrientation ( rootsMenu );
                    rootsMenu.showBelowMiddle ( rootsArrowButton );
                }
            } );

            rootsMenu.addPopupMenuListener ( new PopupMenuListener ()
            {
                @Override
                public void popupMenuWillBecomeVisible ( final PopupMenuEvent e )
                {

                }

                @Override
                public void popupMenuWillBecomeInvisible ( final PopupMenuEvent e )
                {
                    rootsArrowButton.setSelected ( false );
                }

                @Override
                public void popupMenuCanceled ( final PopupMenuEvent e )
                {
                    rootsArrowButton.setSelected ( false );
                }
            } );
        }
        else
        {
            rootsArrowButton.setIcon ( ltr ? Icons.rightSmall : Icons.leftSmall );
        }
        while ( rootsMenu.getComponentCount () > rootsMenuItemsCount )
        {
            rootsMenu.remove ( 0 );
        }
        return rootsArrowButton;
    }

    protected void folderSelected ( File folder )
    {
        // Normalize file
        folder = FileUtils.normalize ( folder );

        // Update visual path
        updatePath ( folder );

        // Notify about selection change
        fireDirectoryChanged ( folder );

        // Requesting focus as it is internal change from internal event
        WebPathField.this.transferFocus ();
    }

    public void addPathFieldListener ( final PathFieldListener listener )
    {
        listeners.add ( listener );
    }

    public void removePathFieldListener ( final PathFieldListener listener )
    {
        listeners.remove ( listener );
    }

    protected void fireDirectoryChanged ( final File newDirectory )
    {
        for ( final PathFieldListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.directoryChanged ( newDirectory );
        }
    }

    @Override
    public void applyComponentOrientation ( final ComponentOrientation o )
    {
        super.applyComponentOrientation ( o );
        updatePath ();
    }

    /**
     * Applies component orientation to specified component.
     *
     * @param component component to modify
     */
    @Deprecated
    private static void applyOrientation ( final Component component )
    {
        applyOrientation ( component, false );
    }

    /**
     * Applies component orientation to specified component if needed or if forced.
     *
     * @param component component to modify
     * @param forced    force orientation change
     */
    @Deprecated
    private static void applyOrientation ( final Component component, final boolean forced )
    {
        final ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
        if ( forced || orientation.isLeftToRight () != component.getComponentOrientation ().isLeftToRight () )
        {
            component.applyComponentOrientation ( orientation );
        }
    }
}