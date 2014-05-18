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

package com.alee.extended.tab;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.menu.PopupMenuGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public final class PaneData<T extends DocumentData> implements StructureData<T>, SwingConstants
{
    private static final ImageIcon closeTabIcon = new ImageIcon ( PaneData.class.getResource ( "icons/close.png" ) );
    private static final ImageIcon closeTabRolloverIcon = new ImageIcon ( PaneData.class.getResource ( "icons/close-rollover.png" ) );

    protected final WebTabbedPane tabbedPane;
    protected final DefaultFocusTracker focusTracker;

    protected List<T> data = new ArrayList<T> ();

    public PaneData ()
    {
        super ();

        // Creating tabbed pane
        tabbedPane = new WebTabbedPane ( TabbedPaneStyle.attached );
        tabbedPane.putClientProperty ( WebDocumentPane.DATA_KEY, this );

        // Some hotkeys
        HotkeyManager.registerHotkey ( tabbedPane, tabbedPane, Hotkey.CTRL_W, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                closeSelected ();
            }
        } );

        // Tab drag
        DocumentDragHandler.install ( this );

        // Tab menu
        tabbedPane.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isMiddleMouseButton ( e ) )
                {
                    if ( getDocumentPane ().isCloseable () )
                    {
                        final int index = tabbedPane.getTabAt ( e.getPoint () );
                        if ( index != -1 )
                        {
                            close ( get ( index ) );
                        }
                    }
                }
                else if ( SwingUtils.isPopupTrigger ( e ) )
                {
                    // Checking that menu is enabled
                    final WebDocumentPane documentPane = getDocumentPane ();
                    if ( !documentPane.isTabMenuEnabled () )
                    {
                        return;
                    }

                    // Checking that user actually clicked on a tab
                    final int index = tabbedPane.getTabAt ( e.getPoint () );
                    if ( index == -1 )
                    {
                        return;
                    }

                    // Variables
                    final T document = get ( index );
                    final boolean csb = documentPane.isCloseable ();
                    final boolean spb = data.size () > 1;

                    // Creating popup menu
                    final PopupMenuGenerator pmg = new PopupMenuGenerator ( "document-pane-menu" );
                    pmg.setIconSettings ( PaneData.class, "icons/menu/", ".png" );
                    pmg.setLanguagePrefix ( "weblaf.ex.docpane" );
                    pmg.addItem ( "close", "close", Hotkey.CTRL_W, csb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            close ( get ( index ) );
                        }
                    } );
                    pmg.addSeparator ();
                    pmg.addItem ( "left", "left", spb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, LEFT );
                        }
                    } );
                    pmg.addItem ( "right", "right", spb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, RIGHT );
                        }
                    } );
                    pmg.addItem ( "top", "top", spb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, TOP );
                        }
                    } );
                    pmg.addItem ( "bottom", "bottom", spb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, BOTTOM );
                        }
                    } );
                    pmg.addSeparator ();
                    pmg.addItem ( "unsplit", "unsplit", spb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            merge ();
                        }
                    } );
                    pmg.addItem ( "unsplit", "unsplitall", spb, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            mergeAll ();
                        }
                    } );

                    // Displaying popup menu
                    final WebPopupMenu menu = pmg.getPopupMenu ();
                    final Dimension mps = menu.getPreferredSize ();
                    final Rectangle bounds = tabbedPane.getBoundsAt ( index );
                    menu.show ( tabbedPane, bounds.x + bounds.width / 2 - mps.width / 2, bounds.y + bounds.height - menu.getShadeWidth () );
                }
            }
        } );

        // Adding focus tracker
        focusTracker = new DefaultFocusTracker ( true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                if ( focused )
                {
                    activate ();
                }
            }
        };
        FocusManager.addFocusTracker ( tabbedPane, focusTracker );
    }

    @Override
    public Component getComponent ()
    {
        return getTabbedPane ();
    }

    @Override
    public PaneData<T> findClosestPane ()
    {
        return this;
    }

    public WebDocumentPane getDocumentPane ()
    {
        return SwingUtils.getFirstParent ( tabbedPane, WebDocumentPane.class );
    }

    public WebTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    public List<T> getData ()
    {
        return data;
    }

    public int count ()
    {
        return data.size ();
    }

    public void setData ( final List<T> data )
    {
        this.data = data;
    }

    public void add ( final T document )
    {
        add ( document, -1 );
    }

    public void add ( final T document, final int index )
    {
        final int i = index != -1 ? index : tabbedPane.getTabCount ();
        data.add ( i, document );
        tabbedPane.insertTab ( "", document.getIcon (), document.getComponent (), null, i );
        tabbedPane.setTabComponentAt ( i, createTabComponent ( document ) );
    }

    protected JComponent createTabComponent ( final T document )
    {
        final WebPanel tabPanel = new WebPanel ( new BorderLayout ( 2, 0 ) );
        tabPanel.setOpaque ( false );

        tabPanel.add ( document.getTitle () == null ? WebLabel.createTranslatedLabel ( document.getIcon (), document.getId () ) :
                new WebLabel ( document.getTitle (), document.getIcon () ), BorderLayout.CENTER );

        final WebButton closeButton = new WebButton ( closeTabIcon, closeTabRolloverIcon );
        closeButton.setUndecorated ( true );
        closeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                close ( document );
            }
        } );
        tabPanel.add ( closeButton, BorderLayout.LINE_END );

        return tabPanel;
    }

    public T get ( final int index )
    {
        return data.get ( index );
    }

    public T get ( final String id )
    {
        for ( final T document : data )
        {
            if ( document.getId ().equals ( id ) )
            {
                return document;
            }
        }
        return null;
    }

    public T getSelected ()
    {
        final int index = tabbedPane.getSelectedIndex ();
        return index != -1 ? data.get ( index ) : null;
    }

    public void setSelected ( final String id )
    {
        setSelected ( indexOf ( id ) );
    }

    public void setSelected ( final T document )
    {
        setSelected ( indexOf ( document ) );
    }

    public void setSelected ( final int index )
    {
        tabbedPane.setSelectedIndex ( index );
    }

    public int indexOf ( final String id )
    {
        return indexOf ( get ( id ) );
    }

    public int indexOf ( final T document )
    {
        return data.indexOf ( document );
    }

    public void remove ( final int index )
    {
        remove ( get ( index ) );
    }

    public void remove ( final String id )
    {
        remove ( get ( id ) );
    }

    public void remove ( final T document )
    {
        if ( document != null )
        {
            final int index = indexOf ( document );
            if ( index != -1 )
            {
                tabbedPane.remove ( index );
                data.remove ( document );
            }
        }
    }

    public void close ( final int index )
    {
        remove ( index );
        mergeIfEmpty ();
    }

    public void close ( final String id )
    {
        remove ( id );
        mergeIfEmpty ();
    }

    public void close ( final T document )
    {
        remove ( document );
        mergeIfEmpty ();
    }

    public void closeSelected ()
    {
        final T selected = getSelected ();
        if ( selected != null )
        {
            close ( selected );
        }
    }

    public void activate ()
    {
        final WebDocumentPane pane = getDocumentPane ();
        if ( pane != null )
        {
            pane.activate ( PaneData.this );
        }
    }

    public void split ( final T document, final int direction )
    {
        final WebDocumentPane pane = getDocumentPane ();
        if ( pane != null )
        {
            pane.split ( PaneData.this, document, direction );
        }
    }

    public void mergeIfEmpty ()
    {
        if ( count () == 0 )
        {
            merge ();
        }
    }

    public void merge ()
    {
        final WebDocumentPane documentPane = getDocumentPane ();
        if ( documentPane != null )
        {
            documentPane.merge ( this );
        }
    }

    public void mergeAll ()
    {
        final WebDocumentPane pane = getDocumentPane ();
        if ( pane != null )
        {
            pane.merge ( pane.getStructureRoot () );
        }
    }
}