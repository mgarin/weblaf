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
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.GlobalConstants;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 05.07.11 Time: 18:20
 */

public class WebPathField extends WebPanel
{
    /**
     * Used icons.
     */
    protected static final ImageIcon down = new ImageIcon ( WebPathField.class.getResource ( "icons/down.png" ) );
    protected static final ImageIcon left = new ImageIcon ( WebPathField.class.getResource ( "icons/left.png" ) );
    protected static final ImageIcon right = new ImageIcon ( WebPathField.class.getResource ( "icons/right.png" ) );

    protected static final String FILE_ICON = "fileIcon";

    protected List<PathFieldListener> listeners = new ArrayList<PathFieldListener> ();

    protected boolean focusOwner = false;

    protected static FileSystemView fsv = FileSystemView.getFileSystemView ();

    protected DefaultFileFilter fileFilter = GlobalConstants.DIRECTORIES_FILTER;

    protected int preferredWidth = -1;
    protected boolean filesDropEnabled = true;

    protected File selectedPath;

    protected boolean autocompleteEnabled = true;
    protected JWindow autocompleteDialog = null;

    protected WebPanel contentPanel;

    protected WebTextField pathField;
    protected FocusAdapter pathFocusListener;

    protected WebButton myComputer = null;

    protected int rootsMenuItemsCount = 0;
    protected WebPopupMenu rootsMenu = null;
    protected WebToggleButton rootsArrowButton = null;

    public WebPathField ()
    {
        this ( FileUtils.getDiskRoots ()[ 0 ] );
    }

    public WebPathField ( String path )
    {
        this ( new File ( path ) );
    }

    public WebPathField ( File path )
    {
        super ( true );

        // Default settings
        setMargin ( -1 );
        setOpaque ( false );
        setWebColored ( false );
        setDrawFocus ( true );
        setBackground ( Color.WHITE );

        // Files TransferHandler
        setTransferHandler ( new FileDropHandler ()
        {
            protected boolean isDropEnabled ()
            {
                return filesDropEnabled;
            }

            protected boolean filesImported ( List<File> files )
            {
                // Setting dragged files
                FileFilter filter = getFileFilter ();
                for ( File file : files )
                {
                    File actualFile = file.isDirectory () ? file : file.getParentFile ();
                    if ( filter == null || filter.accept ( actualFile ) )
                    {
                        folderSelected ( actualFile );
                        return true;
                    }
                }
                return false;
            }
        } );

        contentPanel = new WebPanel ();
        contentPanel.setOpaque ( false );
        contentPanel.setLayout ( new HorizontalFlowLayout ( 0, true ) );
        add ( contentPanel, BorderLayout.CENTER );

        //        WebImage editImage = new WebImage ( WebPathField.class, "icons/edit.png" );
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

        pathField = WebTextField.createWebTextField ( false );
        pathField.setMargin ( 2 );
        pathField.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( autocompleteDialog == null || !autocompleteDialog.isVisible () )
                {
                    if ( pathField.getText ().trim ().equals ( "" ) )
                    {
                        folderSelected ( null );
                    }
                    else
                    {
                        File choosenPath = new File ( pathField.getText () );
                        if ( choosenPath.exists () && choosenPath.isDirectory () )
                        {
                            folderSelected ( choosenPath );
                        }
                        else
                        {
                            updatePath ();
                        }
                    }
                    WebPathField.this.transferFocus ();
                }
            }
        } );
        pathField.addKeyListener ( new KeyAdapter ()
        {
            public void keyPressed ( KeyEvent e )
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
            public void focusLost ( FocusEvent e )
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

            public void caretUpdate ( CaretEvent e )
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
                    autocompleteDialog = new JWindow ( SwingUtils.getWindowAncestor ( WebPathField.this ) );
                    autocompleteDialog.getContentPane ().setLayout ( new BorderLayout () );
                    autocompleteDialog.setFocusable ( false );

                    SwingUtils.getWindowAncestor ( WebPathField.this ).addComponentListener ( new ComponentAdapter ()
                    {
                        public void componentMoved ( ComponentEvent e )
                        {
                            hideDialog ();
                        }

                        public void componentResized ( ComponentEvent e )
                        {
                            hideDialog ();
                        }
                    } );

                    list = new WebList ();
                    list.setFocusable ( false );
                    list.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
                    list.setRolloverSelectionEnabled ( true );
                    list.setCellRenderer ( new WebListCellRenderer ()
                    {
                        public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected,
                                                                        boolean cellHasFocus )
                        {
                            JLabel renderer =
                                    ( JLabel ) super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );
                            renderer.setIcon ( FileUtils.getFileIcon ( ( File ) value ) );
                            renderer.setText ( FileUtils.getDisplayFileName ( ( File ) value ) );
                            return renderer;
                        }
                    } );
                    list.addMouseListener ( new MouseAdapter ()
                    {
                        public void mousePressed ( MouseEvent e )
                        {
                            final int index = list.getUI ().locationToIndex ( list, e.getPoint () );
                            if ( SwingUtilities.isLeftMouseButton ( e ) && index != -1 )
                            {
                                setSelectedPath ( ( File ) list.getModel ().getElementAt ( index ) );
                            }
                        }
                    } );
                    list.addKeyListener ( new KeyAdapter ()
                    {
                        public void keyPressed ( KeyEvent e )
                        {
                            if ( Hotkey.ENTER.isTriggered ( e ) )
                            {
                                setSelectedPath ( ( File ) list.getSelectedValue () );
                            }
                        }
                    } );
                    listScroll = new WebScrollPane ( list );
                    listScroll.setShadeWidth ( 0 );
                    //                    listScroll.setHorizontalScrollBarPolicy (
                    //                            WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
                    autocompleteDialog.getContentPane ().add ( listScroll, BorderLayout.CENTER );

                    pathField.addKeyListener ( new KeyAdapter ()
                    {
                        public void keyPressed ( KeyEvent e )
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
                                    else if ( Hotkey.ENTER.isTriggered ( e ) )
                                    {
                                        setSelectedPath ( ( File ) list.getSelectedValue () );
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
                        public void focusLost ( FocusEvent e )
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
                String t = pathField.getText ().substring ( 0, pathField.getCaretPosition () );

                // Retrieving parent path
                int beginIndex = t.lastIndexOf ( File.separator );
                beginIndex = beginIndex != -1 ? beginIndex + 1 : 0;

                // Parent file
                String parentPath = t.substring ( 0, beginIndex );
                File parent = parentPath.trim ().equals ( "" ) ? null : new File ( parentPath );

                try
                {
                    List<File> similar = getSimilarFileChilds ( parent, t.substring ( beginIndex ) );
                    if ( similar != null && similar.size () > 0 )
                    {
                        updateList ( similar, pathField.getUI ().modelToView ( pathField, beginIndex ).x );
                    }
                    else
                    {
                        hideDialog ();
                    }
                }
                catch ( BadLocationException e1 )
                {
                    e1.printStackTrace ();
                }
                //                    }
                //                } ).start ();
            }

            private void updateList ( final List<File> similar, final int x )
            {
                SwingUtils.invokeLater ( new Runnable ()
                {
                    public void run ()
                    {
                        list.setModel ( new AbstractListModel ()
                        {
                            public int getSize ()
                            {
                                return similar.size ();
                            }

                            public Object getElementAt ( int i )
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
                        Point los = pathField.getLocationOnScreen ();
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
                SwingUtils.invokeLater ( new Runnable ()
                {
                    public void run ()
                    {
                        autocompleteDialog.setVisible ( false );
                    }
                } );
            }

            private void setSelectedPath ( File path )
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
            public void mousePressed ( MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    startEditing ();
                }
            }
        } );
        HotkeyManager.registerHotkey ( WebPathField.this, WebPathField.this, Hotkey.F2, new HotkeyRunnable ()
        {
            public void run ( KeyEvent e )
            {
                startEditing ();
            }
        }, true );

        // Resize listener
        addComponentListener ( new ComponentAdapter ()
        {
            public void componentResized ( ComponentEvent e )
            {
                if ( !pathField.isShowing () )
                {
                    updatePath ();
                }
            }
        } );

        // Focus listener
        FocusManager.registerFocusTracker ( new DefaultFocusTracker ( WebPathField.this )
        {
            public void focusChanged ( boolean focused )
            {
                focusOwner = focused;
                WebPathField.this.repaint ();
            }
        } );

        // Updatin initial path
        updatePath ( path );
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

    public boolean isAutocompleteEnabled ()
    {
        return autocompleteEnabled;
    }

    public void setAutocompleteEnabled ( boolean autocompleteEnabled )
    {
        this.autocompleteEnabled = autocompleteEnabled;
    }

    public DefaultFileFilter getFileFilter ()
    {
        return fileFilter;
    }

    public void setFileFilter ( DefaultFileFilter fileFilter )
    {
        setFileFilter ( fileFilter, true );
    }

    public void setFileFilter ( DefaultFileFilter fileFilter, boolean updatePath )
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

    public void setFilesDropEnabled ( boolean filesDropEnabled )
    {
        this.filesDropEnabled = filesDropEnabled;
    }

    public File getSelectedPath ()
    {
        return selectedPath;
    }

    public void setSelectedPath ( File selectedPath )
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

    protected synchronized void updatePath ( File path )
    {
        // todo check if path is proper (filter/hidden)

        // Saving focus state
        boolean hadFocus = focusOwner;

        // Saving new path
        selectedPath = path;

        // Clearing old path components
        pathField.removeFocusListener ( pathFocusListener );
        contentPanel.removeAll ();

        // Determining oriention
        boolean ltr = WebPathField.this.getComponentOrientation ().isLeftToRight ();

        // Determining root
        if ( SystemUtils.isWindows () )
        {
            WebButton computerButton = getMyComputer ();
            contentPanel.add ( computerButton );
            contentPanel.add ( getRootsArrowButton ( ltr ) );
        }

        if ( selectedPath != null )
        {
            // Creating parents list
            File folder = new File ( selectedPath.getAbsolutePath () );
            List<File> parents = new ArrayList<File> ();
            parents.add ( 0, folder );
            while ( folder.getParent () != null )
            {
                folder = folder.getParentFile ();
                parents.add ( 0, folder );
            }

            // Adding path buttons
            boolean first = true;
            for ( File file : parents )
            {
                final File ff = file;

                WebButton wb = new WebButton ();
                wb.setRound ( !SystemUtils.isWindows () && first ? StyleConstants.smallRound : 0 );
                wb.setShadeWidth ( 0 );
                wb.setLeftRightSpacing ( 0 );
                wb.setRolloverDecoratedOnly ( true );
                wb.setRolloverDarkBorderOnly ( false );
                wb.setFocusable ( false );
                if ( !SystemUtils.isWindows () && first )
                {
                    wb.setIcon ( FileUtils.getMyComputerIcon () );
                    wb.putClientProperty ( FILE_ICON, FileUtils.getMyComputerIcon () );
                }
                else
                {
                    wb.setText ( fsv.getSystemDisplayName ( ff ) );
                    wb.putClientProperty ( FILE_ICON, FileUtils.getFileIcon ( ff, false ) );
                }
                wb.addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
                    {
                        folderSelected ( ff );
                    }
                } );
                contentPanel.add ( wb );

                int childsCount = 0;
                final WebPopupMenu menu = new WebPopupMenu ();
                File[] files = FileUtils.sortFiles ( getFileChilds ( ff ) );
                if ( files != null )
                {
                    for ( final File root : files )
                    {
                        if ( root.isDirectory () )
                        {
                            WebMenuItem menuItem = new WebMenuItem ( FileUtils.getDisplayFileName ( root ) );
                            menuItem.setIcon ( FileUtils.getFileIcon ( root, false ) );
                            menuItem.addActionListener ( new ActionListener ()
                            {
                                public void actionPerformed ( ActionEvent e )
                                {
                                    folderSelected ( root );
                                }
                            } );
                            menu.add ( menuItem );
                            childsCount++;
                        }
                    }
                }
                if ( !SystemUtils.isWindows () && first )
                {
                    setRootsMenu ( menu, childsCount );
                }

                final WebToggleButton childs = new WebToggleButton ();
                childs.setIcon ( ltr ? right : left );
                childs.setSelectedIcon ( down );
                childs.setShadeToggleIcon ( false );
                childs.setRound ( 0 );
                childs.setShadeWidth ( 0 );
                childs.setRolloverDecoratedOnly ( true );
                childs.setRolloverDarkBorderOnly ( false );
                childs.setFocusable ( false );
                childs.setComponentPopupMenu ( menu );
                childs.setMargin ( 0 );
                childs.setLeftRightSpacing ( 0 );
                childs.setEnabled ( childsCount > 0 );
                childs.addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
                    {
                        // todo Apply orientation globally on change, not here
                        WebPathField.this.transferFocus ();
                        SwingUtils.applyOrientation ( menu );
                        menu.show ( childs, childs.getComponentOrientation ().isLeftToRight () ? 0 :
                                childs.getWidth () - menu.getPreferredSize ().width, childs.getHeight () );
                    }
                } );
                contentPanel.add ( childs );

                menu.addPopupMenuListener ( new PopupMenuListener ()
                {
                    public void popupMenuWillBecomeVisible ( PopupMenuEvent e )
                    {
                        //
                    }

                    public void popupMenuWillBecomeInvisible ( PopupMenuEvent e )
                    {
                        childs.setSelected ( false );
                    }

                    public void popupMenuCanceled ( PopupMenuEvent e )
                    {
                        childs.setSelected ( false );
                    }
                } );

                first = false;
            }
        }

        // Filling space
        contentPanel.add ( new JLabel () );

        // Shortening long elemets
        if ( !SystemUtils.isWindows () )
        {
            while ( getRootsMenu ().getComponentCount () > getRootsMenuItemsCount () )
            {
                getRootsMenu ().remove ( 0 );
            }
        }
        if ( canShortenPath () )
        {
            getRootsMenu ().add ( new JPopupMenu.Separator (), 0 );
        }
        while ( canShortenPath () )
        {
            // Andding menu element
            WebButton wb = ( WebButton ) contentPanel.getComponent ( 2 );
            WebMenuItem menuItem = new WebMenuItem ();
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

    protected List<File> getSimilarFileChilds ( File file, String namePart )
    {
        String searchText = namePart.toLowerCase ();
        File[] childs = getFileChilds ( file );
        List<File> similar = new ArrayList<File> ();
        if ( childs != null )
        {
            for ( File child : childs )
            {
                if ( child.getName ().toLowerCase ().contains ( searchText ) )
                {
                    similar.add ( child );
                }
            }
        }
        return similar;
    }

    protected File[] getFileChilds ( File file )
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
            myComputer = WebButton.createIconWebButton ( FileUtils.getMyComputerIcon () );
            myComputer.setRound ( getRound () );
            myComputer.setShadeWidth ( 0 );
            myComputer.setLeftRightSpacing ( 0 );
            myComputer.setRolloverDecoratedOnly ( true );
            myComputer.setRolloverDarkBorderOnly ( false );
            myComputer.setDrawFocus ( false );
            myComputer.setDrawRight ( false );
            myComputer.setDrawRightLine ( true );
            myComputer.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
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

    public void setRootsMenu ( WebPopupMenu rootsMenu, int childsCount )
    {
        this.rootsMenu = rootsMenu;
        this.rootsMenuItemsCount = childsCount;
    }

    protected WebToggleButton getRootsArrowButton ( boolean ltr )
    {
        if ( rootsArrowButton == null )
        {
            rootsMenu = new WebPopupMenu ();

            File[] rootFiles = FileUtils.getDiskRoots ();
            for ( final File root : FileUtils.sortFiles ( rootFiles ) )
            {
                WebMenuItem menuItem = new WebMenuItem ( FileUtils.getDisplayFileName ( root ) );
                menuItem.setIcon ( FileUtils.getFileIcon ( root, false ) );
                menuItem.addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
                    {
                        folderSelected ( root );
                    }
                } );
                rootsMenu.add ( menuItem );
                rootsMenuItemsCount++;
            }

            rootsArrowButton = new WebToggleButton ();
            rootsArrowButton.setIcon ( ltr ? right : left );
            rootsArrowButton.setSelectedIcon ( down );
            rootsArrowButton.setShadeToggleIcon ( false );
            rootsArrowButton.setRound ( 0 );
            rootsArrowButton.setShadeWidth ( 0 );
            rootsArrowButton.setRolloverDecoratedOnly ( true );
            rootsArrowButton.setRolloverDarkBorderOnly ( false );
            rootsArrowButton.setFocusable ( false );
            rootsArrowButton.setMargin ( 0 );
            rootsArrowButton.setLeftRightSpacing ( 0 );
            rootsArrowButton.setComponentPopupMenu ( rootsMenu );
            rootsArrowButton.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    WebPathField.this.transferFocus ();
                    SwingUtils.applyOrientation ( rootsMenu );
                    rootsMenu.show ( rootsArrowButton, rootsArrowButton.getComponentOrientation ().isLeftToRight () ? 0 :
                            rootsArrowButton.getWidth () - rootsMenu.getPreferredSize ().width, rootsArrowButton.getHeight () );
                }
            } );

            rootsMenu.addPopupMenuListener ( new PopupMenuListener ()
            {
                public void popupMenuWillBecomeVisible ( PopupMenuEvent e )
                {

                }

                public void popupMenuWillBecomeInvisible ( PopupMenuEvent e )
                {
                    rootsArrowButton.setSelected ( false );
                }

                public void popupMenuCanceled ( PopupMenuEvent e )
                {
                    rootsArrowButton.setSelected ( false );
                }
            } );
        }
        else
        {
            rootsArrowButton.setIcon ( ltr ? right : left );
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

    public void addPathFieldListener ( PathFieldListener listener )
    {
        listeners.add ( listener );
    }

    public void removePathFieldListener ( PathFieldListener listener )
    {
        listeners.remove ( listener );
    }

    protected void fireDirectoryChanged ( File newDirectory )
    {
        for ( PathFieldListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.directoryChanged ( newDirectory );
        }
    }

    public void applyComponentOrientation ( ComponentOrientation o )
    {
        super.applyComponentOrientation ( o );
        updatePath ();
    }
}
