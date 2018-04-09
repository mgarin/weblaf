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

import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.Customizer;
import com.alee.utils.swing.menu.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.alee.laf.splitpane.WebSplitPane.HORIZONTAL_SPLIT;

/**
 * Data for single tabbed pane within document pane.
 * It basically contains tabbed pane and opened documents list.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 */
public final class PaneData<T extends DocumentData> implements StructureData<T>, SwingConstants
{
    /**
     * {@link WebDocumentPane} this {@link PaneData} belongs to.
     * Referenced to properly act when {@link WebDocumentPane} is required to retrieve customizers or perform any operation.
     */
    protected WebDocumentPane<T> documentPane;

    /**
     * Actual tabbed pane component.
     */
    protected final WebTabbedPane tabbedPane;

    /**
     * Pane focus tracker.
     */
    protected final DefaultFocusTracker focusTracker;

    /**
     * Pane documents.
     */
    protected List<T> data = new ArrayList<T> ();

    /**
     * Constructs new {@link PaneData} for the specified {@link WebDocumentPane}.
     *
     * @param documentPane parent {@link WebDocumentPane}
     */
    public PaneData ( final WebDocumentPane<T> documentPane )
    {
        super ();

        // Parent document pane reference
        this.documentPane = documentPane;

        // Creating tabbed pane
        // todo This style parent might change on drag into other document pane
        tabbedPane = new WebTabbedPane ( StyleId.documentpaneTabbedPane.at ( documentPane ) );
        tabbedPane.putClientProperty ( WebDocumentPane.DATA_KEY, this );

        // Customizing tabbed pane
        updateTabbedPaneCustomizer ( documentPane );

        // Some hotkeys
        HotkeyManager.registerHotkey ( tabbedPane, tabbedPane, Hotkey.CTRL_W, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                if ( getDocumentPane ().isClosable () )
                {
                    final T selected = getSelected ();
                    if ( selected.isClosable () )
                    {
                        close ( selected );
                    }
                }
            }
        } );

        // Tabs drag & drop
        DocumentTransferHandler.install ( this );

        // Activating document pane on
        tabbedPane.addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                checkSelection ();
            }
        } );

        // Tab menu
        tabbedPane.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isMiddleMouseButton ( e ) )
                {
                    if ( getDocumentPane ().isClosable () )
                    {
                        final int index = tabbedPane.getTabAt ( e.getPoint () );
                        if ( index != -1 )
                        {
                            final T document = get ( index );
                            if ( document.isClosable () )
                            {
                                close ( document );
                            }
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
                    final boolean close = documentPane.isClosable () && document.isClosable ();
                    final boolean closeOthers = documentPane.isClosable () && data.size () > 1;
                    final boolean split = data.size () > 1 && documentPane.isSplitEnabled ();
                    final boolean unsplit = tabbedPane.getParent () instanceof WebSplitPane;
                    final boolean hor = unsplit && ( ( WebSplitPane ) tabbedPane.getParent () ).getOrientation () == HORIZONTAL_SPLIT;

                    // Creating popup menu
                    final PopupMenuGenerator pmg = new PopupMenuGenerator ( StyleId.documentpaneMenu.at ( tabbedPane ) );
                    pmg.setIconSettings ( PaneData.class, "icons/menu/", ".png" );
                    pmg.setLanguagePrefix ( "weblaf.ex.docpane" );
                    pmg.addItem ( "close", "close", Hotkey.CTRL_W, close, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            close ( get ( index ) );
                        }
                    } );
                    pmg.addItem ( "closeAll", "closeAll", close, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            closeAll ();
                        }
                    } );
                    pmg.addItem ( "closeOthers", "closeOthers", closeOthers, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            closeOthers ( get ( index ) );
                        }
                    } );
                    pmg.addSeparator ();
                    pmg.addItem ( "left", "left", split, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, LEFT );
                        }
                    } );
                    pmg.addItem ( "right", "right", split, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, RIGHT );
                        }
                    } );
                    pmg.addItem ( "top", "top", split, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, TOP );
                        }
                    } );
                    pmg.addItem ( "bottom", "bottom", split, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            split ( document, BOTTOM );
                        }
                    } );
                    pmg.addSeparator ();
                    pmg.addItem ( "rotate", "rotate", unsplit, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            rotate ();
                        }
                    } );
                    pmg.addItem ( hor ? "swapHor" : "swapVer", "swap", unsplit, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            swap ();
                        }
                    } );
                    pmg.addItem ( hor ? "unsplitHor" : "unsplitVer", "unsplit", unsplit, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            merge ();
                        }
                    } );
                    pmg.addItem ( "unsplit", "unsplitall", unsplit, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            mergeAll ();
                        }
                    } );

                    // Displaying popup menu
                    final WebPopupMenu menu = pmg.getMenu ();
                    final Dimension mps = menu.getPreferredSize ();
                    final Rectangle bounds = tabbedPane.getBoundsAt ( index );
                    menu.show ( tabbedPane, bounds.x + bounds.width / 2 - mps.width / 2, bounds.y + bounds.height + 5 );
                }
            }
        } );

        // Adding focus tracker
        focusTracker = new DefaultFocusTracker ( tabbedPane, true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                if ( focused )
                {
                    activate ();
                }
                checkSelection ();
            }
        };
        FocusManager.addFocusTracker ( tabbedPane, focusTracker );
    }

    /**
     * Checks document pane selection.
     */
    public void checkSelection ()
    {
        final WebDocumentPane documentPane = getDocumentPane ();
        if ( documentPane != null )
        {
            documentPane.checkSelection ();
        }
    }

    /**
     * Updates tabbed pane customizer.
     *
     * @param documentPane parent WebDocumentPane
     */
    protected void updateTabbedPaneCustomizer ( final WebDocumentPane<T> documentPane )
    {
        final Customizer<WebTabbedPane> customizer = documentPane.getTabbedPaneCustomizer ();
        if ( customizer != null )
        {
            customizer.customize ( tabbedPane );
        }
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

    @Override
    public DocumentPaneState getDocumentPaneState ()
    {
        return new DocumentPaneState ( this );
    }

    /**
     * Returns parent WebDocumentPane.
     *
     * @return parent WebDocumentPane
     */
    public WebDocumentPane getDocumentPane ()
    {
        return documentPane;
    }

    /**
     * Sets parent WebDocumentPane reference.
     *
     * @param documentPane parent WebDocumentPane reference
     */
    public void setDocumentPane ( final WebDocumentPane<T> documentPane )
    {
        this.documentPane = documentPane;
        updateTabbedPaneCustomizer ( documentPane );
        updateTabTitleComponents ();
    }

    /**
     * Returns actual tabbed pane component.
     *
     * @return actual tabbed pane component
     */
    public WebTabbedPane getTabbedPane ()
    {
        return tabbedPane;
    }

    /**
     * Returns pane documents.
     *
     * @return pane documents
     */
    public List<T> getData ()
    {
        return data;
    }

    /**
     * Returns pane document IDs.
     *
     * @return pane document IDs
     */
    public List<String> getDocumentIds ()
    {
        final List<String> ids = new ArrayList<String> ( data.size () );
        for ( final T document : data )
        {
            ids.add ( document.getId () );
        }
        return ids;
    }

    /**
     * Returns pane documents count.
     *
     * @return pane documents count
     */
    public int count ()
    {
        return data.size ();
    }

    /**
     * Returns whether specified document is in this pane or not.
     *
     * @param document document to look for
     * @return true if specified document is in this pane, false otherwise
     */
    public boolean contains ( final T document )
    {
        return contains ( document.getId () );
    }

    /**
     * Returns whether document with the specified ID is in this pane or not.
     *
     * @param documentId ID of the document to look for
     * @return true if document with the specified ID is in this pane, false otherwise
     */
    public boolean contains ( final String documentId )
    {
        for ( final T document : data )
        {
            if ( document.getId ().equals ( documentId ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds new document into this pane.
     *
     * @param document document to add
     */
    public void add ( final T document )
    {
        add ( document, -1 );
    }

    /**
     * Adds new document into this pane at the specified index.
     *
     * @param document document to add
     * @param index    index to add at
     */
    public void add ( final T document, final int index )
    {
        final int i = index != -1 ? index : tabbedPane.getTabCount ();
        data.add ( i, document );
        tabbedPane.insertTab ( "", document.getIcon (), document.getComponent (), null, i );
        tabbedPane.setBackgroundAt ( i, document.getBackground () );
        tabbedPane.setTabComponentAt ( i, createTabComponent ( document ) );

        // Listening to document data changes
        document.addListener ( new PaneUpdater<T> ( this ) );
    }

    /**
     * Returns new tab component.
     *
     * @param document document to create tab component for
     * @return new tab component
     */
    protected JComponent createTabComponent ( final T document )
    {
        final WeakReference<T> weakDocument = new WeakReference<T> ( document );
        final MouseAdapter tabSelector = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                redirectMouseEvent ( e );
            }

            /**
             * Redirects mouse event from custom tab component to tabbed pane.
             * That allows tabbed pane to handle selection, menu and drag events properly.
             *
             * @param e mouse event
             */
            protected void redirectMouseEvent ( final MouseEvent e )
            {
                final PaneData paneData = getDocumentPane ().getPane ( weakDocument.get () );
                if ( paneData != null )
                {
                    final WebTabbedPane tabbedPane = paneData.getTabbedPane ();
                    tabbedPane.dispatchEvent ( SwingUtilities.convertMouseEvent ( e.getComponent (), e, tabbedPane ) );
                }
            }
        };
        return getDocumentPane ().getTabTitleComponentProvider ().createTabTitleComponent ( this, document, tabSelector );
    }

    /**
     * Updates all tab title components.
     */
    public void updateTabTitleComponents ()
    {
        for ( final T document : getData () )
        {
            updateTabTitleComponent ( document );
        }
    }

    /**
     * Updates tab title component for the specified document.
     *
     * @param document document to update tab title component for
     */
    public void updateTabTitleComponent ( final T document )
    {
        getTabbedPane ().setTabComponentAt ( indexOf ( document ), createTabComponent ( document ) );
    }

    /**
     * Updates tab background for the specified document.
     *
     * @param document document to update tab background for
     */
    public void updateTabBackground ( final T document )
    {
        getTabbedPane ().setBackgroundAt ( indexOf ( document ), document.getBackground () );
    }

    /**
     * Updates tab view for the specified document.
     *
     * @param document document to update tab view for
     */
    public void updateTabComponent ( final T document )
    {
        getTabbedPane ().setComponentAt ( indexOf ( document ), document.getComponent () );
    }

    /**
     * Opens document in this pane.
     * Unlike add method this one informs listeners about opened document.
     *
     * @param document document to open
     */
    public void open ( final T document )
    {
        // Opening document
        add ( document );

        // Informing listeners about document open event
        getDocumentPane ().fireDocumentOpened ( document, this, indexOf ( document ) );
    }

    /**
     * Returns pane document at the specified index.
     *
     * @param index document index
     * @return pane document at the specified index
     */
    public T get ( final int index )
    {
        return data.get ( index );
    }

    /**
     * Returns pane document with the specified ID.
     *
     * @param id document ID
     * @return pane document with the specified ID
     */
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

    /**
     * Returns pane's selected document.
     *
     * @return pane's selected document
     */
    public T getSelected ()
    {
        final int index = tabbedPane.getSelectedIndex ();
        return index != -1 ? data.get ( index ) : null;
    }

    /**
     * Returns pane's selected document index.
     *
     * @return pane's selected document index
     */
    public int getSelectedIndex ()
    {
        return tabbedPane.getSelectedIndex ();
    }

    /**
     * Sets pane's selected document ID.
     *
     * @param id ID of the document to select
     */
    public void setSelected ( final String id )
    {
        setSelected ( indexOf ( id ) );
    }

    /**
     * Sets selected document.
     *
     * @param document document to select
     */
    public void setSelected ( final T document )
    {
        setSelected ( indexOf ( document ) );
    }

    /**
     * Sets selected document index.
     *
     * @param index index of the document to select
     */
    public void setSelected ( final int index )
    {
        if ( 0 <= index && index < tabbedPane.getTabCount () )
        {
            tabbedPane.setSelectedIndex ( index );
        }
        else if ( tabbedPane.getTabCount () > 0 )
        {
            tabbedPane.setSelectedIndex ( 0 );
        }
    }

    /**
     * Decrements selected document index inside the active pane.
     */
    public void selectPrevious ()
    {
        final int count = count ();
        if ( count > 1 )
        {
            final int selected = getSelectedIndex () - 1;
            setSelected ( selected == -1 ? count - 1 : selected );
        }
    }

    /**
     * Increments selected document index inside the active pane.
     */
    public void selectNext ()
    {
        final int count = count ();
        if ( count > 1 )
        {
            final int selected = getSelectedIndex () + 1;
            setSelected ( selected == count ? 0 : selected );
        }
    }

    /**
     * Returns index of the document with the specified ID.
     *
     * @param id document ID
     * @return index of the document with the specified ID
     */
    public int indexOf ( final String id )
    {
        return indexOf ( get ( id ) );
    }

    /**
     * Returns index of the specified document.
     *
     * @param document document
     * @return index of the specified document
     */
    public int indexOf ( final T document )
    {
        return data.indexOf ( document );
    }

    /**
     * Closes document at the specified index in the active pane.
     *
     * @param index index of the document to close
     * @return true if document was successfully closed, false otherwise
     */
    public boolean remove ( final int index )
    {
        return remove ( get ( index ) );
    }

    /**
     * Closes document with the specified ID.
     *
     * @param id ID of the document to close
     * @return true if document was successfully closed, false otherwise
     */
    public boolean remove ( final String id )
    {
        return remove ( get ( id ) );
    }

    /**
     * Closes the specified document.
     *
     * @param document document to close
     * @return true if document was successfully closed, false otherwise
     */
    public boolean remove ( final T document )
    {
        if ( document != null )
        {
            final int index = indexOf ( document );
            if ( index != -1 )
            {
                document.removeListener ( findDocumentListener ( document ) );
                tabbedPane.remove ( index );
                data.remove ( document );
                return true;
            }
        }
        return false;
    }

    /**
     * Returns document listener attached by this pane data class.
     *
     * @param document listened document
     * @return document listener attached by this pane data class
     */
    protected DocumentDataListener<T> findDocumentListener ( final T document )
    {
        final List<DocumentDataListener<T>> listeners = document.getListeners ();
        for ( final DocumentDataListener<T> listener : listeners )
        {
            if ( listener instanceof PaneUpdater )
            {
                final PaneData paneData = ( ( PaneUpdater ) listener ).getPaneData ();
                if ( paneData == this )
                {
                    return listener;
                }
            }
        }
        return null;
    }

    /**
     * Closes all document in this group.
     *
     * @return {@code true} if all documents were successfully closed, {@code false} otherwise
     */
    public boolean closeAll ()
    {
        boolean success = true;
        for ( final T document : CollectionUtils.copy ( data ) )
        {
            success &= close ( document );
        }

        return success;
    }

    /**
     * Closes document at the specified index in the active pane.
     *
     * @param index index of the document to close
     * @return {@code true} if document was successfully closed, {@code false} otherwise
     */
    public boolean close ( final int index )
    {
        return close ( get ( index ) );
    }

    /**
     * Closes document with the specified ID.
     *
     * @param id ID of the document to close
     * @return {@code true} if document was successfully closed, {@code false} otherwise
     */
    public boolean close ( final String id )
    {
        return close ( get ( id ) );
    }

    /**
     * Closes the specified document.
     *
     * @param document document to close
     * @return {@code true} if document was successfully closed, {@code false} otherwise
     */
    public boolean close ( final T document )
    {
        if ( document != null )
        {
            final WebDocumentPane documentPane = getDocumentPane ();
            final int index = indexOf ( document );

            // Informing listeners about document closing event
            if ( documentPane.fireDocumentClosing ( document, this, index ) )
            {
                // Closing document and fixing view
                final boolean removed = remove ( document );
                mergeIfEmpty ();

                // todo Fire additional event when all documents are closed?
                // if ( documentPane.getDocumentsCount () == 0 )

                // Informing listeners about document close event
                documentPane.fireDocumentClosed ( document, this, index );

                return removed;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Closes all documents except the specified one.
     *
     * @param document document to keep opened
     * @return {@code true} if all documents were successfully closed, {@code false} otherwise
     */
    public boolean closeOthers ( final T document )
    {
        boolean success = true;
        for ( final T doc : CollectionUtils.copy ( data ) )
        {
            if ( doc != document && doc.isClosable () )
            {
                success &= close ( doc );
            }
        }
        return success;
    }

    /**
     * Closes selected document.
     *
     * @return {@code true} if selected document was successfully closed, {@code false} otherwise
     */
    public boolean closeSelected ()
    {
        final T selected = getSelected ();
        return selected != null && close ( selected );
    }

    /**
     * Activates this pane within WebDocumentPane.
     */
    public void activate ()
    {
        final WebDocumentPane pane = getDocumentPane ();
        if ( pane != null )
        {
            pane.activate ( PaneData.this );
        }
    }

    /**
     * Returns whether this pane is active one within WebDocumentPane or not.
     *
     * @return true if this pane is active one within WebDocumentPane, false otherwise
     */
    public boolean isActive ()
    {
        return getDocumentPane ().getActivePane () == this;
    }

    /**
     * Splits the specified document into a separate pane.
     *
     * @param document  document to split
     * @param direction split direction
     */
    public void split ( final T document, final int direction )
    {
        final WebDocumentPane pane = getDocumentPane ();
        if ( pane != null )
        {
            pane.split ( PaneData.this, document, direction );
        }
    }

    /**
     * Merges this pane with its neighbour if it is empty.
     */
    public void mergeIfEmpty ()
    {
        if ( count () == 0 )
        {
            merge ();
        }
    }

    /**
     * Changes parent split orientation if this pane is located within a split.
     */
    public void rotate ()
    {
        final Container parent = tabbedPane.getParent ();
        if ( parent instanceof WebSplitPane )
        {
            final SplitData<T> splitData = WebDocumentPane.getData ( ( WebSplitPane ) parent );
            splitData.changeOrientation ();
        }
    }

    /**
     * Changes parent split sides if this pane is located within a split.
     */
    public void swap ()
    {
        final Container parent = tabbedPane.getParent ();
        if ( parent instanceof WebSplitPane )
        {
            final SplitData<T> splitData = WebDocumentPane.getData ( ( WebSplitPane ) parent );
            splitData.swapSides ();
        }
    }

    /**
     * Merges this pane with its neighbour.
     */
    public void merge ()
    {
        final WebDocumentPane documentPane = getDocumentPane ();
        if ( documentPane != null )
        {
            documentPane.merge ( this );
        }
    }

    /**
     * Merges all panes within WebDocumentPane.
     */
    public void mergeAll ()
    {
        final WebDocumentPane pane = getDocumentPane ();
        if ( pane != null )
        {
            pane.merge ( pane.getStructureRoot () );
        }
    }
}