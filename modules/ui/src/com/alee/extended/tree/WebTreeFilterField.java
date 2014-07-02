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

package com.alee.extended.tree;

import com.alee.extended.image.WebImage;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.text.WebTextField;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.swing.StringDocumentChangeListener;
import com.alee.utils.text.TextProvider;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 * Special filter field that can be attached to any WebAsyncTree.
 *
 * @param <E> filtered node type
 * @author Mikle Garin
 */

public class WebTreeFilterField<E extends UniqueNode> extends WebTextField
{
    /**
     * todo 1. Listen to tree model changes and update filtering appropriately
     */

    /**
     * Used icons.
     */
    public static final ImageIcon settingsIcon = new ImageIcon ( WebTreeFilterField.class.getResource ( "icons/filter/settings.png" ) );
    public static final ImageIcon matchCaseIcon = new ImageIcon ( WebTreeFilterField.class.getResource ( "icons/filter/matchCase.png" ) );
    public static final ImageIcon useSpaceAsSeparatorIcon =
            new ImageIcon ( WebTreeFilterField.class.getResource ( "icons/filter/useSpaceAsSeparator.png" ) );
    public static final ImageIcon searchFromStartIcon =
            new ImageIcon ( WebTreeFilterField.class.getResource ( "icons/filter/searchFromStart.png" ) );

    /**
     * Async tree to which this field should apply filtering.
     */
    protected WeakReference<WebTree<E>> tree;

    /**
     * Nodes filter used by this field.
     */
    protected StructuredTreeNodesFilter<E> filter;

    /**
     * Currently listened field document.
     */
    protected Document document;

    /**
     * Special document listener that notifies about filter changes.
     */
    protected DocumentListener documentListener;

    /**
     * UI elements.
     */
    protected WebImage filterIcon;
    protected WebPopupMenu settingsMenu;
    protected WebCheckBoxMenuItem matchCaseItem;
    protected WebCheckBoxMenuItem useSpaceAsSeparatorItem;
    protected WebCheckBoxMenuItem searchFromStartItem;

    /**
     * Constructs new tree filter field.
     */
    public WebTreeFilterField ()
    {
        this ( null, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param tree tree to which this field applies filtering
     */
    public WebTreeFilterField ( final WebTree<E> tree )
    {
        this ( tree, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final TextProvider<E> textProvider )
    {
        this ( null, textProvider );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param tree         tree to which this field applies filtering
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final WebTree<E> tree, final TextProvider<E> textProvider )
    {
        super ();
        setTree ( tree );
        setFilter ( new StructuredTreeNodesFilter () );
        setTextProvider ( textProvider );
        init ();
    }

    /**
     * Initializes filter field.
     */
    protected void init ()
    {
        setLanguage ( "weblaf.ex.treefilter.inputprompt" );
        setHideInputPromptOnFocus ( false );

        initFilterIcon ();
        initSettingsMenu ();
        initFieldListeners ();
    }

    /**
     * Initializes filter icon.
     */
    protected void initFilterIcon ()
    {
        filterIcon = new WebImage ( WebTreeFilterField.class, "icons/filter/settings.png" );
        filterIcon.setMargin ( 0, 2, 0, 2 );
        filterIcon.setCursor ( Cursor.getDefaultCursor () );
        filterIcon.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                settingsMenu.showBelowMiddle ( filterIcon );
            }
        } );
        setLeadingComponent ( filterIcon );
    }

    /**
     * Initializes settings menu.
     */
    protected void initSettingsMenu ()
    {
        settingsMenu = new WebPopupMenu ();

        matchCaseItem = new WebCheckBoxMenuItem ( matchCaseIcon );
        matchCaseItem.setLanguage ( "weblaf.filter.matchcase" );
        matchCaseItem.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                filter.setMatchCase ( matchCaseItem.isSelected () );
                updateFiltering ();
            }
        } );
        settingsMenu.add ( matchCaseItem );

        useSpaceAsSeparatorItem = new WebCheckBoxMenuItem ( useSpaceAsSeparatorIcon );
        useSpaceAsSeparatorItem.setLanguage ( "weblaf.filter.spaceseparator" );
        useSpaceAsSeparatorItem.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                filter.setUseSpaceAsSeparator ( useSpaceAsSeparatorItem.isSelected () );
                updateFiltering ();
            }
        } );
        settingsMenu.add ( useSpaceAsSeparatorItem );

        searchFromStartItem = new WebCheckBoxMenuItem ( searchFromStartIcon );
        searchFromStartItem.setLanguage ( "weblaf.filter.frombeginning" );
        searchFromStartItem.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                filter.setSearchFromStart ( searchFromStartItem.isSelected () );
                updateFiltering ();
            }
        } );
        settingsMenu.add ( searchFromStartItem );
    }

    /**
     * Initializes field listeners.
     */
    protected void initFieldListeners ()
    {
        // Field changes listener
        documentListener = new StringDocumentChangeListener ()
        {
            @Override
            public void documentChanged ( final String newValue, final DocumentEvent e )
            {
                filter.setSearchText ( newValue );
                updateFiltering ();
            }
        };
        updateDocumentListener ();

        // Field document change listener
        addPropertyChangeListener ( WebLookAndFeel.DOCUMENT_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                updateDocumentListener ();
            }
        } );

        // Field clear listener
        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( Hotkey.ESCAPE.isTriggered ( e ) )
                {
                    WebTreeFilterField.this.clear ();
                }
            }
        } );
    }

    /**
     * Updates field document listener.
     */
    protected void updateDocumentListener ()
    {
        // Removing listener from old document
        if ( document != null )
        {
            document.removeDocumentListener ( documentListener );
        }

        // Adding listener to new document
        document = getDocument ();
        document.addDocumentListener ( documentListener );
    }

    /**
     * Returns tree to which this field applies filtering.
     *
     * @return tree to which this field applies filtering
     */
    public WebTree<E> getTree ()
    {
        return tree != null ? tree.get () : null;
    }

    /**
     * Sets tree to which this field applies filtering.
     *
     * @param tree tree to which this field applies filtering
     */
    public void setTree ( final WebTree<E> tree )
    {
        clearFilter ();
        this.tree = new WeakReference<WebTree<E>> ( tree );
        applyFilter ();
    }

    /**
     * Returns nodes filter.
     *
     * @return nodes filter
     */
    public StructuredTreeNodesFilter<E> getFilter ()
    {
        return filter;
    }

    /**
     * Sets nodes filter.
     *
     * @param filter new nodes filter
     */
    public void setFilter ( final StructuredTreeNodesFilter<E> filter )
    {
        this.filter = filter;
        applyFilter ();
    }

    /**
     * Applies tree filter.
     */
    protected void applyFilter ()
    {
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                ( ( WebAsyncTree ) tree ).setFilter ( filter );
            }
            else if ( tree instanceof WebExTree )
            {
                ( ( WebExTree ) tree ).setFilter ( filter );
            }
        }
    }

    /**
     * Clears tree filter.
     */
    protected void clearFilter ()
    {
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                ( ( WebAsyncTree ) tree ).clearFilter ();
            }
            else if ( tree instanceof WebExTree )
            {
                ( ( WebExTree ) tree ).clearFilter ();
            }
        }
    }

    /**
     * Returns node text provider.
     *
     * @return node text provider
     */
    public TextProvider<E> getTextProvider ()
    {
        return filter.getTextProvider ();
    }

    /**
     * Sets node text provider.
     *
     * @param textProvider new node text provider
     */
    public void setTextProvider ( final TextProvider<E> textProvider )
    {
        filter.setTextProvider ( textProvider );
        updateFiltering ();
    }

    /**
     * Updates tree filtering.
     */
    public void updateFiltering ()
    {
        // Cleaning up filter cache
        filter.clearCache ();

        // Updating tree filtering if possible
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                ( ( WebAsyncTree ) tree ).updateSortingAndFiltering ();
            }
            else if ( tree instanceof WebExTree )
            {
                ( ( WebExTree ) tree ).updateSortingAndFiltering ();
            }
        }
    }

    /**
     * Performs node acceptance re-check.
     * Might be useful if external tree updates are applied.
     *
     * @param node node that should be re-checked
     */
    public void updateNodeAcceptance ( final E node )
    {
        // Cleaning up filter cache
        filter.clearCache ( node );

        // Updating tree filtering
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                ( ( WebAsyncTree ) tree ).updateSortingAndFiltering ( ( AsyncUniqueNode ) node.getParent () );
            }
            else if ( tree instanceof WebExTree )
            {
                ( ( WebExTree ) tree ).updateSortingAndFiltering ( node.getParent () );
            }
        }
    }
}