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
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.compare.Filter;
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
 * Special filter field that can be attached to any WebExTree or WebAsyncTree.
 *
 * @param <E> filtered node type
 * @author Mikle Garin
 */

public class WebTreeFilterField<E extends UniqueNode> extends WebTextField
{
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
     * Data provider change listener.
     */
    protected PropertyChangeListener dataProviderChangeListener;

    /**
     * Tree filter change listener.
     */
    protected PropertyChangeListener filterChangeListener;

    /**
     * Whether should automatically handle tree state on filter changes or not.
     */
    protected boolean defaultTreeStateBehavior = true;

    /**
     * Last saved tree state.
     */
    protected TreeState treeState = null;
    protected Rectangle visibleRect = null;

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
        this ( StyleId.treefilterfield, null, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param id style ID
     */
    public WebTreeFilterField ( final StyleId id )
    {
        this ( id, null, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param tree tree to which this field applies filtering
     */
    public WebTreeFilterField ( final WebTree<E> tree )
    {
        this ( StyleId.treefilterfield, tree, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param id   style ID
     * @param tree tree to which this field applies filtering
     */
    public WebTreeFilterField ( final StyleId id, final WebTree<E> tree )
    {
        this ( id, tree, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final TextProvider<E> textProvider )
    {
        this ( StyleId.treefilterfield, null, textProvider );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param id           style ID
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final StyleId id, final TextProvider<E> textProvider )
    {
        this ( id, null, textProvider );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param tree         tree to which this field applies filtering
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final WebTree<E> tree, final TextProvider<E> textProvider )
    {
        this ( StyleId.treefilterfield, tree, textProvider );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param id           style ID
     * @param tree         tree to which this field applies filtering
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final StyleId id, final WebTree<E> tree, final TextProvider<E> textProvider )
    {
        super ( id );
        setLanguage ( "weblaf.ex.treefilter.inputprompt" );
        checkTree ( tree );
        initDefaultFilter ();
        setTree ( tree );
        setTextProvider ( textProvider );
        initFilterIcon ();
        initSettingsMenu ();
        initListeners ();
    }

    /**
     * Checks whether provided tree type is correct or not.
     *
     * @param tree tree to check
     */
    protected void checkTree ( final WebTree<E> tree )
    {
        if ( !( tree instanceof WebAsyncTree || tree instanceof WebExTree ) )
        {
            throw new RuntimeException ( "WebTreeFilterField is only usable with WebAsyncTree and WebExTree" );
        }
    }

    /**
     * Initializes default field tree filter.
     */
    protected void initDefaultFilter ()
    {
        this.filter = new StructuredTreeNodesFilter ();
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
     * Initializes listeners.
     */
    protected void initListeners ()
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
                    // Clearing filter field on ESCAPE press
                    clear ();
                }
            }
        } );

        // Model change listener to properly update field filter
        dataProviderChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // Reapplying field filter on model change
                applyFieldFilter ();
            }
        };
        getTree ().addPropertyChangeListener ( WebTree.TREE_DATA_PROVIDER_PROPERTY, dataProviderChangeListener );

        // Filter change listener to properly update field filter
        filterChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // Reapplying field filter on filter change
                applyFieldFilter ();
            }
        };
        getTree ().addPropertyChangeListener ( WebTree.TREE_FILTER_PROPERTY, dataProviderChangeListener );
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
     * Sets tree to which this field applies filtering.
     *
     * @param tree tree to which this field applies filtering
     */
    public void setTree ( final WebTree<E> tree )
    {
        // Cleanup the mess we made in previous tree
        final WebTree<E> previousTree = getTree ();
        if ( previousTree != null )
        {
            // Removing listener from previous tree
            previousTree.removePropertyChangeListener ( WebTree.TREE_MODEL_PROPERTY, dataProviderChangeListener );

            // Removing filter from previous tree
            removeFieldFilter ();
        }

        // Installing filtering into new tree
        {
            // Saving reference to new tree
            this.tree = new WeakReference<WebTree<E>> ( tree );

            // Updating filter in current tree
            applyFieldFilter ();

            // Adding listener into current tree
            tree.addPropertyChangeListener ( WebTree.TREE_MODEL_PROPERTY, dataProviderChangeListener );
        }
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
        removeFieldFilter ();
        this.filter = filter;
        applyFieldFilter ();
    }

    /**
     * Applies field tree filter.
     */
    protected void applyFieldFilter ()
    {
        // Updating tree filter if possible
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                final WebAsyncTree asyncTree = ( WebAsyncTree ) tree;

                // Cleaning up filter cache
                filter.clearCache ();

                // Saving original filter
                // Note that we have to check whether field filter is already installed or not here
                final Filter originalFilter = asyncTree.getFilter ();
                filter.setOriginalFilter ( originalFilter instanceof StructuredTreeNodesFilter ?
                        ( ( StructuredTreeNodesFilter ) originalFilter ).getOriginalFilter () : originalFilter );

                // Updating field tree filter
                asyncTree.setFilter ( filter );
            }
            else if ( tree instanceof WebExTree )
            {
                final WebExTree exTree = ( WebExTree ) tree;

                // Cleaning up filter cache
                filter.clearCache ();

                // Saving original filter
                // Note that we have to check whether field filter is already installed or not here
                final Filter originalFilter = exTree.getFilter ();
                filter.setOriginalFilter ( originalFilter instanceof StructuredTreeNodesFilter ?
                        ( ( StructuredTreeNodesFilter ) originalFilter ).getOriginalFilter () : originalFilter );

                // Updating field tree filter
                exTree.setFilter ( filter );
            }
        }
    }

    /**
     * Removes field tree filter.
     */
    protected void removeFieldFilter ()
    {
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            final Filter<E> originalFilter = filter.getOriginalFilter ();
            if ( tree instanceof WebAsyncTree )
            {
                ( ( WebAsyncTree ) tree ).setFilter ( originalFilter );
            }
            else if ( tree instanceof WebExTree )
            {
                ( ( WebExTree ) tree ).setFilter ( originalFilter );
            }
            filter.setOriginalFilter ( null );
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
     * Returns whether should automatically handle tree state on filter changes or not.
     *
     * @return true if should automatically handle tree state on filter changes, false otherwise
     */
    public boolean isDefaultTreeStateBehavior ()
    {
        return defaultTreeStateBehavior;
    }

    /**
     * Sets whether should automatically handle tree state on filter changes or not.
     *
     * @param defaultTreeStateBehavior whether should automatically handle tree state on filter changes or not
     */
    public void setDefaultTreeStateBehavior ( final boolean defaultTreeStateBehavior )
    {
        this.defaultTreeStateBehavior = defaultTreeStateBehavior;
    }

    /**
     * Updates tree filtering.
     */
    public void updateFiltering ()
    {
        // Updating tree filtering if possible
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                // todo Restore/expand behavior

                // Cleaning up filter cache
                filter.clearCache ();

                // Updating tree filtering
                ( ( WebAsyncTree ) tree ).updateSortingAndFiltering ();
            }
            else if ( tree instanceof WebExTree )
            {
                // Save tree state before filtering
                if ( defaultTreeStateBehavior )
                {
                    if ( !isEmpty () && treeState == null )
                    {
                        treeState = tree.getTreeState ();
                        visibleRect = tree.getVisibleRect ();
                    }
                }

                // Cleaning up filter cache
                filter.clearCache ();

                // Updating tree filtering
                ( ( WebExTree ) tree ).updateSortingAndFiltering ();

                // Restore tree state or expand tree
                if ( defaultTreeStateBehavior )
                {
                    if ( isEmpty () )
                    {
                        // Restore tree state
                        if ( treeState != null )
                        {
                            tree.setTreeState ( treeState );
                            tree.scrollRectToVisible ( visibleRect );
                            treeState = null;
                            visibleRect = null;
                        }
                    }
                    else
                    {
                        // Expand all
                        tree.expandAll ();
                    }
                }
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
        // Updating tree filtering
        final WebTree<E> tree = getTree ();
        if ( tree != null )
        {
            if ( tree instanceof WebAsyncTree )
            {
                // Cleaning up filter cache
                filter.clearCache ( node );

                // Updating tree node filtering
                ( ( WebAsyncTree ) tree ).updateSortingAndFiltering ( ( AsyncUniqueNode ) node.getParent () );
            }
            else if ( tree instanceof WebExTree )
            {
                // Cleaning up filter cache
                filter.clearCache ( node );

                // Updating tree node filtering
                ( ( WebExTree ) tree ).updateSortingAndFiltering ( node.getParent () );
            }
        }
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
     * Returns whether this tree filter field is empty or not.
     *
     * @return true if this tree filter field is empty, false otherwise
     */
    public boolean isEmpty ()
    {
        final String text = getText ();
        return text == null || text.equals ( "" );
    }
}