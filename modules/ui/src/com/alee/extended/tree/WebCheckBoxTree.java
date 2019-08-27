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
import com.alee.api.jdk.Predicate;
import com.alee.laf.checkbox.CheckState;
import com.alee.laf.tree.NodesAcceptPolicy;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

/**
 * {@link com.alee.laf.tree.WebTree} extension class.
 * It dynamically replaces provided cell renderer to provide an additional check box.
 * This tree uses additional {@link com.alee.extended.tree.TreeCheckingModel} to handle check box states.
 * Structure of this tree can be provided through the same means as in {@link com.alee.laf.tree.WebTree}.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @param <N> {@link MutableTreeNode} type
 * @author Mikle Garin
 * @see com.alee.laf.tree.WebTree
 * @see com.alee.laf.tree.WebTreeUI
 * @see com.alee.laf.tree.TreePainter
 * @see com.alee.extended.tree.TreeCheckingModel
 */
public class WebCheckBoxTree<N extends MutableTreeNode> extends WebTree<N>
{
    /**
     * todo 1. Create separate "checkboxtree" styleable component with its own skin?
     */

    /**
     * Whether or not nodes checking or unchecking should be performed on child nodes recursively.
     */
    protected Boolean recursiveChecking;

    /**
     * Gap between checkbox and actual tree renderer.
     */
    protected Integer checkBoxRendererGap;

    /**
     * Whether or not checkboxes are visible in the tree.
     */
    protected Boolean checkBoxVisible;

    /**
     * Whether or not user can interact with checkboxes to change their check state.
     */
    protected Boolean checkingEnabled;

    /**
     * Whether partially checked node should be checked or unchecked on toggle.
     */
    protected Boolean checkMixedOnToggle;

    /**
     * Custom checking model.
     */
    protected TreeCheckingModel<N> checkingModel;

    /**
     * Checkbox cell renderer.
     */
    protected CheckBoxTreeCellRenderer checkBoxCellRenderer;

    /**
     * Actual content renderer;
     */
    protected TreeCellRenderer actualCellRenderer;

    /**
     * Checkbox enabled state provider.
     */
    protected Predicate<N> enabledStateProvider;

    /**
     * Checkbox visibility state provider.
     */
    protected Predicate<N> visibleStateProvider;

    /**
     * Tree actions handler.
     */
    protected Handler handler;

    /**
     * Checkbox tree check state change listeners.
     */
    protected List<CheckStateChangeListener<N>> checkStateChangeListeners = new ArrayList<CheckStateChangeListener<N>> ( 1 );

    /**
     * Constructs tree with default sample model.
     */
    public WebCheckBoxTree ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebCheckBoxTree ( final Object[] data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebCheckBoxTree ( final Vector<?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebCheckBoxTree ( final Hashtable<?, ?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebCheckBoxTree ( final N root )
    {
        this ( StyleId.auto, root );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren {@code false} if any node can have children,
     *                           {@code true} if each node is asked to see if it can have children
     */
    public WebCheckBoxTree ( final N root, final boolean asksAllowsChildren )
    {
        this ( StyleId.auto, root, asksAllowsChildren );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebCheckBoxTree ( final TreeModel newModel )
    {
        this ( StyleId.auto, newModel );
    }

    /**
     * Constructs tree with default sample model.
     *
     * @param id style ID
     */
    public WebCheckBoxTree ( final StyleId id )
    {
        this ( id, createDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebCheckBoxTree ( final StyleId id, final Object[] data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebCheckBoxTree ( final StyleId id, final Vector<?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebCheckBoxTree ( final StyleId id, final Hashtable<?, ?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param id   style ID
     * @param root tree root node
     */
    public WebCheckBoxTree ( final StyleId id, final N root )
    {
        this ( id, new WebTreeModel<N> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param id                 style ID
     * @param root               tree root node
     * @param asksAllowsChildren {@code false} if any node can have children,
     *                           {@code true} if each node is asked to see if it can have children
     */
    public WebCheckBoxTree ( final StyleId id, final N root, final boolean asksAllowsChildren )
    {
        this ( id, new WebTreeModel<N> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param id       style ID
     * @param newModel tree model
     */
    public WebCheckBoxTree ( final StyleId id, final TreeModel newModel )
    {
        super ( id, newModel );

        // Checking model
        checkingModel = createDefaultCheckingModel ();

        // Actions handler
        handler = new Handler ();
        addMouseListener ( handler );
        addKeyListener ( handler );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.checkboxtree;
    }

    /**
     * Returns checkbox tree  cell renderer.
     *
     * @return checkbox tree cell renderer
     */
    public CheckBoxTreeCellRenderer getCheckBoxCellRenderer ()
    {
        return checkBoxCellRenderer;
    }

    /**
     * Returns actual tree cell renderer.
     *
     * @return actual tree cell renderer
     */
    public TreeCellRenderer getActualRenderer ()
    {
        return actualCellRenderer;
    }

    @Override
    public void setCellRenderer ( final TreeCellRenderer renderer )
    {
        actualCellRenderer = renderer;
        if ( checkBoxCellRenderer == null )
        {
            checkBoxCellRenderer = createCheckBoxTreeCellRenderer ();
        }
        super.setCellRenderer ( checkBoxCellRenderer );
    }

    /**
     * Sets special checkbox tree cell renderer.
     *
     * @param renderer checkbox tree cell renderer
     */
    public void setCheckBoxTreeCellRenderer ( final CheckBoxTreeCellRenderer renderer )
    {
        checkBoxCellRenderer = renderer;
        super.setCellRenderer ( checkBoxCellRenderer );
    }

    /**
     * Creates and returns checkbox tree cell renderer.
     *
     * @return checkbox tree cell renderer
     */
    protected WebCheckBoxTreeCellRenderer createCheckBoxTreeCellRenderer ()
    {
        return new WebCheckBoxTreeCellRenderer.UIResource<N, WebCheckBoxTree<N>, CheckBoxTreeNodeParameters<N, WebCheckBoxTree<N>>> ();
    }

    /**
     * Returns gap between checkbox and actual tree renderer.
     *
     * @return gap between checkbox and actual tree renderer
     */
    public int getCheckBoxRendererGap ()
    {
        return checkBoxRendererGap != null ? checkBoxRendererGap : 0;
    }

    /**
     * Sets gap between checkbox and actual tree renderer.
     *
     * @param gap gap between checkbox and actual tree renderer
     */
    public void setCheckBoxRendererGap ( final int gap )
    {
        this.checkBoxRendererGap = gap;
    }

    /**
     * Returns specified tree node check state.
     *
     * @param node tree node to process
     * @return specified tree node check state
     */
    public CheckState getCheckState ( final N node )
    {
        return checkingModel != null ? checkingModel.getCheckState ( node ) : CheckState.unchecked;
    }

    /**
     * Returns whether the specified tree node is unchecked or not.
     *
     * @param node tree node to process
     * @return {@code true} if the specified tree node is unchecked, {@code false} otherwise
     */
    public boolean isUnchecked ( final N node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.unchecked;
    }

    /**
     * Returns whether the specified tree node is checked or not.
     *
     * @param node tree node to process
     * @return {@code true} if the specified tree node is checked, {@code false} otherwise
     */
    public boolean isChecked ( final N node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.checked;
    }

    /**
     * Returns whether the specified tree node is partially checked or not.
     *
     * @param node tree node to process
     * @return {@code true} if the specified tree node is partially checked, {@code false} otherwise
     */
    public boolean isMixed ( final N node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.mixed;
    }

    /**
     * Returns list of nodes for the specified state.
     *
     * @param state  {@link CheckState} to return nodes for
     * @param policy {@link NodesAcceptPolicy} that defines a way to filter nodes
     * @return list of nodes for the specified state
     */
    public List<N> getNodes ( final CheckState state, final NodesAcceptPolicy policy )
    {
        return checkingModel != null ? checkingModel.getNodes ( state, policy ) : new ArrayList<N> ();
    }

    /**
     * Sets whether the specified tree node is checked or not.
     *
     * @param node    tree node to process
     * @param checked whether the specified tree node is checked or not
     */
    public void setChecked ( final N node, final boolean checked )
    {
        if ( checkingModel != null )
        {
            checkingModel.setChecked ( node, checked );
        }
    }

    /**
     * Sets specified nodes state to checked.
     *
     * @param nodes   nodes to check
     * @param checked whether the specified tree nodes should be checked or not
     */
    public void setChecked ( final Collection<N> nodes, final boolean checked )
    {
        if ( checkingModel != null )
        {
            checkingModel.setChecked ( nodes, checked );
        }
    }

    /**
     * Invert tree node check.
     *
     * @param node tree node to process
     */
    public void invertCheck ( final N node )
    {
        if ( checkingModel != null )
        {
            checkingModel.invertCheck ( node );
        }
    }

    /**
     * Invert tree node check.
     *
     * @param nodes tree node to process
     */
    public void invertCheck ( final List<N> nodes )
    {
        if ( checkingModel != null )
        {
            checkingModel.invertCheck ( nodes );
        }
    }

    /**
     * Check all tree nodes.
     */
    public void checkAll ()
    {
        if ( checkingModel != null )
        {
            checkingModel.checkAll ();
        }
    }

    /**
     * Uncheck all tree nodes.
     */
    public void uncheckAll ()
    {
        if ( checkingModel != null )
        {
            checkingModel.uncheckAll ();
        }
    }

    /**
     * Returns tree checking model.
     *
     * @return tree checking model
     */
    public TreeCheckingModel<N> getCheckingModel ()
    {
        return checkingModel;
    }

    /**
     * Sets tree checking model.
     *
     * @param checkingModel tree checking model
     */
    public void setCheckingModel ( final TreeCheckingModel<N> checkingModel )
    {
        // Removing check state change listeners from old model
        for ( final CheckStateChangeListener<N> listener : checkStateChangeListeners )
        {
            this.checkingModel.removeCheckStateChangeListener ( listener );
        }

        this.checkingModel = checkingModel;

        // Updating nodes view due to possible check state changes
        updateVisibleNodes ();

        // Restoring check state change listeners
        for ( final CheckStateChangeListener<N> listener : checkStateChangeListeners )
        {
            checkingModel.addCheckStateChangeListener ( listener );
        }
    }

    /**
     * Creates and returns new default checking model for the specified checkbox tree.
     *
     * @return new default checking model for the specified checkbox tree
     */
    protected TreeCheckingModel<N> createDefaultCheckingModel ()
    {
        return new DefaultTreeCheckingModel<N, WebCheckBoxTree<N>> ( this );
    }

    /**
     * Returns whether checkbox for the specified node should be enabled or not.
     *
     * @param node tree node to process
     * @return {@code true} if checkbox for the specified node should be enabled, {@code false} otherwise
     */
    public boolean isCheckBoxEnabled ( final N node )
    {
        return enabledStateProvider == null || enabledStateProvider.test ( node );
    }

    /**
     * Sets enabled state provider.
     * It defines whether checkboxes for specific nodes should be enabled or not.
     *
     * @param provider enabled state provider
     */
    public void setCheckBoxEnabledStateProvider ( final Predicate<N> provider )
    {
        this.enabledStateProvider = provider;
    }

    /**
     * Returns whether checkbox for the specified node should be visible or not.
     *
     * @param node tree node to process
     * @return {@code true} if checkbox for the specified node should be visible, {@code false} otherwise
     */
    public boolean isCheckBoxVisible ( final N node )
    {
        return visibleStateProvider == null || visibleStateProvider.test ( node );
    }

    /**
     * Sets visibility state provider.
     * It defines whether checkboxes for specific nodes should be visible or not.
     *
     * @param provider new visibility state provider
     */
    public void setCheckBoxVisibleStateProvider ( final Predicate<N> provider )
    {
        this.visibleStateProvider = provider;
    }

    /**
     * Returns whether or not nodes checking or unchecking should be performed on child nodes recursively.
     *
     * @return {@code true} if nodes checking or unchecking should be performed on child nodes recursively, {@code false} otherwise
     */
    public boolean isRecursiveCheckingEnabled ()
    {
        return recursiveChecking == null || recursiveChecking;
    }

    /**
     * Sets whether or not nodes checking or unchecking should be performed on child nodes recursively.
     *
     * @param recursive whether or not nodes checking or unchecking should be performed on child nodes recursively
     */
    public void setRecursiveChecking ( final boolean recursive )
    {
        if ( recursive != isRecursiveCheckingEnabled () )
        {
            recursiveChecking = recursive;
            if ( checkingModel != null )
            {
                checkingModel.checkingModeChanged ( recursiveChecking );
            }
        }
    }

    /**
     * Returns whether checkboxes are visible in the tree or not.
     *
     * @return {@code true} if checkboxes are visible in the tree, {@code false} otherwise
     */
    public boolean isCheckBoxVisible ()
    {
        return checkBoxVisible == null || checkBoxVisible;
    }

    /**
     * Sets whether checkboxes are visible in the tree or not.
     *
     * @param visible whether checkboxes are visible in the tree or not
     */
    public void setCheckBoxVisible ( final boolean visible )
    {
        this.checkBoxVisible = visible;
        updateVisibleNodes ();
    }

    /**
     * Returns whether or not user can interact with checkboxes to change their check state.
     *
     * @return {@code true} if user can interact with checkboxes to change their check state, {@code false} otherwise
     */
    public boolean isCheckingEnabled ()
    {
        return checkingEnabled == null || checkingEnabled;
    }

    /**
     * Sets whether or not user can interact with checkboxes to change their check state.
     *
     * @param enabled whether user can interact with checkboxes to change their check state or not
     */
    public void setCheckingEnabled ( final boolean enabled )
    {
        this.checkingEnabled = enabled;
        repaint ();
    }

    /**
     * Returns whether partially checked node should be checked or unchecked on toggle.
     *
     * @return {@code true} if partially checked node should be checked on toggle, {@code false} if it should be unchecked
     */
    public boolean isCheckMixedOnToggle ()
    {
        return checkMixedOnToggle == null || checkMixedOnToggle;
    }

    /**
     * Sets whether partially checked node should be checked or unchecked on toggle.
     *
     * @param checkMixedOnToggle whether partially checked node should be checked or unchecked on toggle
     */
    public void setCheckMixedOnToggle ( final boolean checkMixedOnToggle )
    {
        this.checkMixedOnToggle = checkMixedOnToggle;
    }

    /**
     * Returns checkbox bounds for the specified tree node.
     *
     * @param node tree node to process
     * @return checkbox bounds for the specified tree node
     */
    public Rectangle getCheckBoxBounds ( final N node )
    {
        return getCheckBoxBounds ( getPathForNode ( node ) );
    }

    /**
     * Returns checkbox bounds for the specified tree path.
     * todo This should also take cell renderer margin/padding into account
     *
     * @param treePath tree path to process
     * @return checkbox bounds for the specified tree path
     */
    public Rectangle getCheckBoxBounds ( final TreePath treePath )
    {
        if ( checkBoxCellRenderer != null )
        {
            final Rectangle pathBounds = getPathBounds ( treePath );
            final Dimension cbSize = checkBoxCellRenderer.getCheckBox ().getPreferredSize ();
            if ( getComponentOrientation ().isLeftToRight () )
            {
                pathBounds.width = cbSize.width;
            }
            else
            {
                pathBounds.x += pathBounds.width - cbSize.width;
                pathBounds.width = cbSize.width;
            }
            return pathBounds;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns whether user can change checkbox states or not.
     *
     * @return {@code true} if user can change checkbox states, {@code false} otherwise
     */
    public boolean isCheckingByUserEnabled ()
    {
        return isEnabled () && isCheckBoxVisible () && isCheckingEnabled ();
    }

    /**
     * Adds check state change listener.
     *
     * @param listener check state change listener to add
     */
    public void addCheckStateChangeListener ( final CheckStateChangeListener<N> listener )
    {
        checkStateChangeListeners.add ( listener );
        if ( checkingModel != null )
        {
            checkingModel.addCheckStateChangeListener ( listener );
        }
    }

    /**
     * Removes check state change listener.
     *
     * @param listener check state change listener to remove
     */
    public void removeCheckStateChangeListener ( final CheckStateChangeListener listener )
    {
        checkStateChangeListeners.remove ( listener );
        if ( checkingModel != null )
        {
            checkingModel.removeCheckStateChangeListener ( listener );
        }
    }

    /**
     * Informs about single or multiple check state changes.
     *
     * @param stateChanges check state changes list
     */
    public void fireCheckStateChanged ( final List<CheckStateChange<N>> stateChanges )
    {
        for ( final CheckStateChangeListener<N> listener : CollectionUtils.copy ( checkStateChangeListeners ) )
        {
            listener.checkStateChanged ( this, stateChanges );
        }
    }

    /**
     * WebCheckBoxTree mouse and key actions handler.
     */
    protected class Handler implements MouseListener, KeyListener
    {
        @Override
        public void keyPressed ( final KeyEvent e )
        {
            if ( isCheckingByUserEnabled () && Hotkey.SPACE.isTriggered ( e ) )
            {
                final List<N> nodes = getSelectedNodes ();

                // Removing invisible nodes from checking list
                final Iterator<N> nodesIterator = nodes.iterator ();
                while ( nodesIterator.hasNext () )
                {
                    final N node = nodesIterator.next ();
                    if ( !isCheckBoxVisible ( node ) || !isCheckBoxEnabled ( node ) )
                    {
                        nodesIterator.remove ();
                    }
                }

                // Performing checking
                if ( nodes.size () > 0 )
                {
                    invertCheck ( nodes );
                }
            }
        }

        @Override
        public void mousePressed ( final MouseEvent e )
        {
            if ( isCheckingByUserEnabled () && SwingUtils.isLeftMouseButton ( e ) )
            {
                final N node = getNodeForLocation ( e.getPoint () );
                if ( node != null && isCheckBoxVisible ( node ) && isCheckBoxEnabled ( node ) )
                {
                    final Rectangle checkBoxBounds = getCheckBoxBounds ( node );
                    if ( checkBoxBounds.contains ( e.getPoint () ) )
                    {
                        invertCheck ( node );
                    }
                }
            }
        }

        @Override
        public void keyTyped ( final KeyEvent e )
        {
            // Ignored
        }

        @Override
        public void keyReleased ( final KeyEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseClicked ( final MouseEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseReleased ( final MouseEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseEntered ( final MouseEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseExited ( final MouseEvent e )
        {
            // Ignored
        }
    }
}