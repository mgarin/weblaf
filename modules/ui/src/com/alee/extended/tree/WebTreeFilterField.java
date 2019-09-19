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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Function;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.text.WebTextField;
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.TreeUtils;
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.extensions.DocumentEventRunnable;
import com.alee.utils.swing.extensions.KeyEventRunnable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Special filter field that can be attached to any {@link WebExTree} or {@link WebAsyncTree}.
 *
 * @param <N> {@link UniqueNode} type
 * @author Mikle Garin
 */
public class WebTreeFilterField<N extends UniqueNode> extends WebTextField
{
    /**
     * {@link JTree} to which this field should apply filtering.
     */
    protected JTree tree;

    /**
     * {@link TreeModel} that implements {@link FilterableNodes} and currently used in the {@link JTree}.
     */
    protected TreeModel model;

    /**
     * Nodes filter used by this field.
     */
    protected StructuredTreeNodesFilter<N> filter;

    /**
     * Currently listened field document.
     */
    protected Document document;

    /**
     * {@link TreeModel} change listener.
     */
    protected PropertyChangeListener treeModelChangeListener;

    /**
     * Whether should automatically handle tree state on filter changes or not.
     */
    protected boolean defaultTreeStateBehavior;

    /**
     * Last saved tree state.
     */
    protected TreeState treeState;
    protected Rectangle visibleRect;

    /**
     * UI elements.
     */
    protected WebButton filterIcon;
    protected WebPopupMenu settingsMenu;
    protected WebCheckBoxMenuItem matchCaseItem;
    protected WebCheckBoxMenuItem useSpaceAsSeparatorItem;
    protected WebCheckBoxMenuItem searchFromStartItem;

    /**
     * Constructs new tree filter field.
     */
    public WebTreeFilterField ()
    {
        this ( StyleId.auto, null, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param tree tree to which this field applies filtering
     */
    public WebTreeFilterField ( final JTree tree )
    {
        this ( StyleId.auto, tree, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final Function<N, String> textProvider )
    {
        this ( StyleId.auto, null, textProvider );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param tree         tree to which this field applies filtering
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final JTree tree, final Function<N, String> textProvider )
    {
        this ( StyleId.auto, tree, textProvider );
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
     * @param id   style ID
     * @param tree tree to which this field applies filtering
     */
    public WebTreeFilterField ( final StyleId id, final JTree tree )
    {
        this ( id, tree, null );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param id           style ID
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final StyleId id, final Function<N, String> textProvider )
    {
        this ( id, null, textProvider );
    }

    /**
     * Constructs new tree filter field.
     *
     * @param id           style ID
     * @param tree         tree to which this field applies filtering
     * @param textProvider node text provider
     */
    public WebTreeFilterField ( final StyleId id, final JTree tree, final Function<N, String> textProvider )
    {
        super ( id );
        setLanguage ( "weblaf.ex.treefilter.inputprompt" );
        initDefaultFilter ();
        initFilterIcon ();
        initSettingsMenu ();
        initListeners ();
        setDefaultTreeStateBehavior ( true );
        setTextProvider ( textProvider );
        setTree ( tree );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.treefilterfield;
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
        filterIcon = new WebButton ( StyleId.treefilterfieldSettings.at ( this ), Icons.filter, Icons.filterHover );
        filterIcon.setCursor ( Cursor.getDefaultCursor () );
        filterIcon.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                settingsMenu.showBelowStart ( filterIcon );
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

        matchCaseItem = new WebCheckBoxMenuItem ();
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

        useSpaceAsSeparatorItem = new WebCheckBoxMenuItem ();
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

        searchFromStartItem = new WebCheckBoxMenuItem ();
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
        // Updating filtering on text change
        onChange ( new DocumentEventRunnable<WebTextField> ()
        {
            @Override
            public void run ( @NotNull final WebTextField component, @Nullable final DocumentEvent event )
            {
                filter.setSearchText ( component.getText () );
                updateFiltering ();
            }
        } );

        // Clearing filter field on ESCAPE press
        onKeyPress ( Hotkey.ESCAPE, new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                clear ();
            }
        } );

        // Model listener for carrying custom filter over to new model
        treeModelChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // Checking model support
                final TreeModel newModel = ( TreeModel ) evt.getNewValue ();
                checkTreeModel ( tree, newModel );

                // Removing filter from the old model
                removeFieldFilter ();

                // Clearing tree state as it might not be applicable to the new model data
                clearState ();

                // Saving reference to the new model
                model = newModel;

                // Saving tree state before applying the filter
                saveState ();

                // Applying filter to the new model
                applyFieldFilter ();
            }
        };
    }

    /**
     * Sets {@link JTree} to which this field applies filtering.
     *
     * @param tree {@link JTree} to which this field applies filtering
     */
    public void setTree ( final JTree tree )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Checking tree and model support
        checkTreeModel ( tree, tree.getModel () );

        // Removing filter from the tree we are detaching from
        removeFieldFilter ();

        // Restoring state for the tree we are detaching from this filter field
        restoreState ();

        // Saving reference to new tree and its model
        this.tree = tree;
        this.model = tree.getModel ();

        // Saving initial state for the new attached tree before we apply filtering
        saveState ();

        // Installing filter into the new attached tree
        applyFieldFilter ();
    }

    /**
     * Checks {@link JTree} and {@link TreeModel} support.
     *
     * @param tree  {@link JTree} to check
     * @param model {@link TreeModel} to check
     */
    protected void checkTreeModel ( final JTree tree, final TreeModel model )
    {
        if ( tree == null || !( model instanceof FilterableNodes ) )
        {
            throw new RuntimeException ( "WebTreeFilterField only supports tree models that implements FilterableNodes" );
        }
    }

    /**
     * Returns {@link TreeModel} that implements {@link FilterableNodes}.
     *
     * @return {@link TreeModel} that implements {@link FilterableNodes}
     */
    public FilterableNodes<N> getFilterableModel ()
    {
        return ( FilterableNodes<N> ) model;
    }

    /**
     * Returns nodes filter.
     *
     * @return nodes filter
     */
    public StructuredTreeNodesFilter<N> getFilter ()
    {
        return filter;
    }

    /**
     * Sets nodes filter.
     *
     * @param filter new nodes filter
     */
    public void setFilter ( final StructuredTreeNodesFilter<N> filter )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Saving tree state before applying the filter
        saveState ();

        // Removing old filter
        removeFieldFilter ();

        // Saving reference to new filter
        this.filter = filter;

        // Applying new filter
        applyFieldFilter ();

        // Restoring tree state after applying the filter
        restoreState ();
    }

    /**
     * Applies field tree filter.
     */
    protected void applyFieldFilter ()
    {
        // Updating tree filter if possible
        if ( tree != null )
        {
            // Updating tree model filter
            getFilterableModel ().setFilter ( filter );

            // Adding listeners into current tree
            tree.addPropertyChangeListener ( JTree.TREE_MODEL_PROPERTY, treeModelChangeListener );
        }
    }

    /**
     * Removes field tree filter.
     */
    protected void removeFieldFilter ()
    {
        if ( tree != null )
        {
            // Removing listeners from current tree
            tree.removePropertyChangeListener ( JTree.TREE_MODEL_PROPERTY, treeModelChangeListener );

            // Removing tree model filter
            getFilterableModel ().clearFilter ();

            // Cleaning up filter cache
            filter.clearCache ();
        }
    }

    /**
     * Returns node text provider.
     *
     * @return node text provider
     */
    public Function<N, String> getTextProvider ()
    {
        return filter.getTextProvider ();
    }

    /**
     * Sets node text provider.
     *
     * @param textProvider new node text provider
     */
    public void setTextProvider ( final Function<N, String> textProvider )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating filter's text provider
        filter.setTextProvider ( textProvider );

        // Updating filtering
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
     * Updates {@link JTree} filtering on all levels.
     */
    public void updateFiltering ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating tree filtering if possible
        if ( tree != null )
        {
            // Saving tree state before filtering
            saveState ();

            // Cleaning up filter cache
            filter.clearCache ();

            // Updating tree filtering
            getFilterableModel ().filter ();

            // Updating tree nodes state after filtering
            if ( !isEmpty () )
            {
                expandToFilteredNodes ();
            }
            else
            {
                restoreState ();
            }
        }
    }

    /**
     * Saves current {@link TreeState} and visible area {@link Rectangle} if {@link JTree} is attached to this field,
     * default behavior is enabled, filter text is not empty and there is no stored state yet.
     */
    protected void saveState ()
    {
        if ( tree != null && defaultTreeStateBehavior && !isEmpty () && treeState == null )
        {
            treeState = TreeUtils.getTreeState ( tree );
            visibleRect = tree.getVisibleRect ();
        }
    }

    /**
     * Restores previous {@link TreeState} and visible area {@link Rectangle} if {@link JTree} is attached to this field,
     * default behavior is enabled and there is a stored state.
     */
    protected void restoreState ()
    {
        if ( tree != null && defaultTreeStateBehavior && treeState != null )
        {
            TreeUtils.setTreeState ( tree, treeState );
            tree.scrollRectToVisible ( visibleRect );
            treeState = null;
            visibleRect = null;
        }
    }

    /**
     * Clears stored {@link TreeState} and visible area {@link Rectangle}.
     */
    protected void clearState ()
    {
        if ( tree != null && defaultTreeStateBehavior && treeState != null )
        {
            treeState = null;
            visibleRect = null;
        }
    }

    /**
     * Expands tree to display all {@link UniqueNode}s remaining after filtering.
     */
    protected void expandToFilteredNodes ()
    {
        if ( defaultTreeStateBehavior )
        {
            TreeUtils.expandLoaded ( tree );
        }
    }

    /**
     * Performs {@link UniqueNode} acceptance re-check.
     * Might be useful if external tree updates are applied.
     *
     * @param node {@link UniqueNode} that should be re-checked
     */
    public void clearNodeCache ( final N node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating tree filtering
        if ( tree != null )
        {
            // Cleaning up filter cache
            filter.clearCache ( node );

            // Updating tree node filtering
            getFilterableModel ().filter ( ( N ) node.getParent () );
        }
    }
}