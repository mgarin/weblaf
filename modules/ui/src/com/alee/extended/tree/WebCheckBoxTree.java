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
import com.alee.api.jdk.Predicate;
import com.alee.laf.checkbox.CheckState;
import com.alee.laf.tree.NodesAcceptPolicy;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
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
import java.io.Serializable;
import java.util.List;
import java.util.*;

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
     * Component properties.
     */
    public static final String RECURSIVE_CHECKING_PROPERTY = "recursiveChecking";
    public static final String CHECK_BOX_RENDERER_GAP_PROPERTY = "checkBoxRendererGap";
    public static final String CHECK_BOX_VISIBLE_PROPERTY = "checkBoxVisible";
    public static final String CHECKING_ENABLED_PROPERTY = "checkingEnabled";
    public static final String CHECK_MIXED_ON_TOGGLE_PROPERTY = "checkMixedOnToggle";
    public static final String CHECKING_MODEL_PROPERTY = "checkingModel";
    public static final String CHECK_BOX_CELL_RENDERER_PROPERTY = "checkBoxCellRenderer";
    public static final String ENABLED_STATE_PROVIDER_PROPERTY = "enabledStateProvider";
    public static final String VISIBLE_STATE_PROVIDER_PROPERTY = "visibleStateProvider";

    /**
     * Whether or not nodes checking or unchecking should be performed on child nodes recursively.
     */
    @Nullable
    protected Boolean recursiveChecking;

    /**
     * Gap between checkbox and actual tree renderer.
     */
    @Nullable
    protected Integer checkBoxRendererGap;

    /**
     * Whether or not checkboxes are visible in the tree.
     */
    @Nullable
    protected Boolean checkBoxVisible;

    /**
     * Whether or not user can interact with checkboxes to change their check state.
     */
    @Nullable
    protected Boolean checkingEnabled;

    /**
     * Whether partially checked node should be checked or unchecked on toggle.
     */
    @Nullable
    protected Boolean checkMixedOnToggle;

    /**
     * Custom checking model.
     */
    @Nullable
    protected TreeCheckingModel<N> checkingModel;

    /**
     * Checkbox cell renderer.
     */
    @Nullable
    protected CheckBoxTreeCellRenderer checkBoxCellRenderer;

    /**
     * Actual content renderer;
     */
    @Nullable
    protected TreeCellRenderer actualCellRenderer;

    /**
     * Checkbox enabled state provider.
     */
    @Nullable
    protected Predicate<N> checkBoxEnabledStateProvider;

    /**
     * Checkbox visibility state provider.
     */
    @Nullable
    protected Predicate<N> checkBoxVisibleStateProvider;

    /**
     * Tree actions handler.
     */
    @NotNull
    protected Handler handler;

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
    public WebCheckBoxTree ( @NotNull final Object[] data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebCheckBoxTree ( @NotNull final Vector<?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebCheckBoxTree ( @NotNull final Hashtable<?, ?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebCheckBoxTree ( @Nullable final N root )
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
    public WebCheckBoxTree ( @Nullable final N root, final boolean asksAllowsChildren )
    {
        this ( StyleId.auto, root, asksAllowsChildren );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebCheckBoxTree ( @Nullable final TreeModel newModel )
    {
        this ( StyleId.auto, newModel );
    }

    /**
     * Constructs tree with default sample model.
     *
     * @param id {@link StyleId}
     */
    public WebCheckBoxTree ( @NotNull final StyleId id )
    {
        this ( id, createDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   {@link StyleId}
     * @param data tree data
     */
    public WebCheckBoxTree ( @NotNull final StyleId id, @NotNull final Object[] data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   {@link StyleId}
     * @param data tree data
     */
    public WebCheckBoxTree ( @NotNull final StyleId id, @NotNull final Vector<?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   {@link StyleId}
     * @param data tree data
     */
    public WebCheckBoxTree ( @NotNull final StyleId id, @NotNull final Hashtable<?, ?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param id   {@link StyleId}
     * @param root tree root node
     */
    public WebCheckBoxTree ( @NotNull final StyleId id, @Nullable final N root )
    {
        this ( id, new WebTreeModel<N> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param id                 {@link StyleId}
     * @param root               tree root node
     * @param asksAllowsChildren {@code false} if any node can have children,
     *                           {@code true} if each node is asked to see if it can have children
     */
    public WebCheckBoxTree ( @NotNull final StyleId id, @Nullable final N root, final boolean asksAllowsChildren )
    {
        this ( id, new WebTreeModel<N> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param id       {@link StyleId}
     * @param newModel tree model
     */
    public WebCheckBoxTree ( @NotNull final StyleId id, @Nullable final TreeModel newModel )
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
     * Returns actual tree cell renderer.
     *
     * @return actual tree cell renderer
     */
    @Nullable
    public TreeCellRenderer getActualRenderer ()
    {
        return actualCellRenderer;
    }

    @Override
    public void setCellRenderer ( @Nullable final TreeCellRenderer renderer )
    {
        actualCellRenderer = renderer;
        if ( checkBoxCellRenderer == null )
        {
            checkBoxCellRenderer = createCheckBoxTreeCellRenderer ();
        }
        super.setCellRenderer ( checkBoxCellRenderer );
    }

    /**
     * Returns checkbox tree  cell renderer.
     *
     * @return checkbox tree cell renderer
     */
    @Nullable
    public CheckBoxTreeCellRenderer getCheckBoxCellRenderer ()
    {
        return checkBoxCellRenderer;
    }

    /**
     * Sets special checkbox tree cell renderer.
     *
     * @param renderer checkbox tree cell renderer
     */
    public void setCheckBoxTreeCellRenderer ( @Nullable final CheckBoxTreeCellRenderer renderer )
    {
        final CheckBoxTreeCellRenderer old = this.checkBoxCellRenderer;
        checkBoxCellRenderer = renderer;
        super.setCellRenderer ( checkBoxCellRenderer );
        firePropertyChange ( CHECK_BOX_CELL_RENDERER_PROPERTY, old, renderer );
    }

    /**
     * Creates and returns checkbox tree cell renderer.
     * todo Move to custom UI implementation
     *
     * @return checkbox tree cell renderer
     */
    @NotNull
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
        final Object old = this.checkBoxRendererGap;
        this.checkBoxRendererGap = gap;
        firePropertyChange ( CHECK_BOX_RENDERER_GAP_PROPERTY, old, gap );
    }

    /**
     * Returns specified tree node check state.
     *
     * @param node tree node to process
     * @return specified tree node check state
     */
    public CheckState getCheckState ( @NotNull final N node )
    {
        return checkingModel != null ? checkingModel.getCheckState ( node ) : CheckState.unchecked;
    }

    /**
     * Returns whether the specified tree node is unchecked or not.
     *
     * @param node tree node to process
     * @return {@code true} if the specified tree node is unchecked, {@code false} otherwise
     */
    public boolean isUnchecked ( @NotNull final N node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.unchecked;
    }

    /**
     * Returns whether the specified tree node is checked or not.
     *
     * @param node tree node to process
     * @return {@code true} if the specified tree node is checked, {@code false} otherwise
     */
    public boolean isChecked ( @NotNull final N node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.checked;
    }

    /**
     * Returns whether the specified tree node is partially checked or not.
     *
     * @param node tree node to process
     * @return {@code true} if the specified tree node is partially checked, {@code false} otherwise
     */
    public boolean isMixed ( @NotNull final N node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.mixed;
    }

    /**
     * Returns list of nodes for the specified state.
     *
     * @param state {@link CheckState} to return nodes for
     * @return list of nodes for the specified state
     */
    @NotNull
    public List<N> getNodes ( @NotNull final CheckState state )
    {
        return getNodes ( state, NodesAcceptPolicy.all );
    }

    /**
     * Returns list of nodes for the specified state.
     *
     * @param state  {@link CheckState} to return nodes for
     * @param policy {@link NodesAcceptPolicy} that defines a way to filter nodes
     * @return list of nodes for the specified state
     */
    @NotNull
    public List<N> getNodes ( @NotNull final CheckState state, @NotNull final NodesAcceptPolicy policy )
    {
        return checkingModel != null ? checkingModel.getNodes ( state, policy ) : new ArrayList<N> ();
    }

    /**
     * Sets whether the specified tree node is checked or not.
     *
     * @param node    tree node to process
     * @param checked whether the specified tree node is checked or not
     */
    public void setChecked ( @NotNull final N node, final boolean checked )
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
    public void setChecked ( @NotNull final Collection<N> nodes, final boolean checked )
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
    public void invertCheck ( @NotNull final N node )
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
    public void invertCheck ( @NotNull final List<N> nodes )
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
    @Nullable
    public TreeCheckingModel<N> getCheckingModel ()
    {
        return checkingModel;
    }

    /**
     * Sets tree checking model.
     *
     * @param checkingModel tree checking model
     */
    public void setCheckingModel ( @Nullable final TreeCheckingModel<N> checkingModel )
    {
        // Removing check state change listeners from old model
        if ( this.checkingModel != null )
        {
            for ( final CheckStateChangeListener<N> listener : listenerList.getListeners ( CheckStateChangeListener.class ) )
            {
                this.checkingModel.removeCheckStateChangeListener ( listener );
            }
        }

        this.checkingModel = checkingModel;

        // Updating nodes view due to possible check state changes
        updateVisibleNodes ();

        // Restoring check state change listeners
        if ( checkingModel != null )
        {
            for ( final CheckStateChangeListener<N> listener : listenerList.getListeners ( CheckStateChangeListener.class ) )
            {
                checkingModel.addCheckStateChangeListener ( listener );
            }
        }
    }

    /**
     * Creates and returns new default checking model for the specified checkbox tree.
     *
     * @return new default checking model for the specified checkbox tree
     */
    @NotNull
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
    public boolean isCheckBoxEnabled ( @NotNull final N node )
    {
        return checkBoxEnabledStateProvider == null || checkBoxEnabledStateProvider.test ( node );
    }

    /**
     * Returns enabled state provider.
     * It defines whether checkboxes for specific nodes should be enabled or not.
     *
     * @return enabled state provider
     */
    @Nullable
    public Predicate<N> getCheckBoxEnabledStateProvider ()
    {
        return checkBoxEnabledStateProvider;
    }

    /**
     * Sets enabled state provider.
     * It defines whether checkboxes for specific nodes should be enabled or not.
     *
     * @param provider enabled state provider
     */
    public void setCheckBoxEnabledStateProvider ( @Nullable final Predicate<N> provider )
    {
        if ( provider != getCheckBoxEnabledStateProvider () )
        {
            final Object old = this.checkBoxEnabledStateProvider;
            this.checkBoxEnabledStateProvider = provider;
            firePropertyChange ( ENABLED_STATE_PROVIDER_PROPERTY, old, provider );
        }
    }

    /**
     * Returns whether checkbox for the specified node should be visible or not.
     *
     * @param node tree node to process
     * @return {@code true} if checkbox for the specified node should be visible, {@code false} otherwise
     */
    public boolean isCheckBoxVisible ( @NotNull final N node )
    {
        return checkBoxVisibleStateProvider == null || checkBoxVisibleStateProvider.test ( node );
    }

    /**
     * Returns visibility state provider.
     * It defines whether checkboxes for specific nodes should be visible or not.
     *
     * @return visibility state provider
     */
    @Nullable
    public Predicate<N> getCheckBoxVisibleStateProvider ()
    {
        return checkBoxVisibleStateProvider;
    }

    /**
     * Sets visibility state provider.
     * It defines whether checkboxes for specific nodes should be visible or not.
     *
     * @param provider new visibility state provider
     */
    public void setCheckBoxVisibleStateProvider ( @Nullable final Predicate<N> provider )
    {
        if ( provider != getCheckBoxVisibleStateProvider () )
        {
            final Object old = this.checkBoxVisibleStateProvider;
            this.checkBoxVisibleStateProvider = provider;
            firePropertyChange ( VISIBLE_STATE_PROVIDER_PROPERTY, old, provider );
        }
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
            final Object old = this.recursiveChecking;
            recursiveChecking = recursive;
            if ( checkingModel != null )
            {
                checkingModel.checkingModeChanged ( recursiveChecking );
            }
            firePropertyChange ( RECURSIVE_CHECKING_PROPERTY, old, recursive );
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
        if ( visible != isCheckBoxVisible () )
        {
            final Object old = this.checkBoxVisible;
            this.checkBoxVisible = visible;
            firePropertyChange ( CHECK_BOX_VISIBLE_PROPERTY, old, visible );
            updateVisibleNodes ();
        }
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
        if ( enabled != isCheckingEnabled () )
        {
            final Object old = this.checkingEnabled;
            this.checkingEnabled = enabled;
            firePropertyChange ( CHECKING_ENABLED_PROPERTY, old, enabled );
            repaint ();
        }
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
        if ( checkMixedOnToggle != isCheckMixedOnToggle () )
        {
            final Object old = this.checkMixedOnToggle;
            this.checkMixedOnToggle = checkMixedOnToggle;
            firePropertyChange ( CHECK_MIXED_ON_TOGGLE_PROPERTY, old, checkMixedOnToggle );
        }
    }

    /**
     * Returns checkbox bounds for the specified tree node.
     *
     * @param node tree node to process
     * @return checkbox bounds for the specified tree node
     */
    @Nullable
    public Rectangle getCheckBoxBounds ( @Nullable final N node )
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
    @Nullable
    public Rectangle getCheckBoxBounds ( @Nullable final TreePath treePath )
    {
        Rectangle checkBoxBounds = null;
        if ( checkBoxCellRenderer != null )
        {
            final Rectangle pathBounds = getPathBounds ( treePath );
            if ( pathBounds != null )
            {
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
                checkBoxBounds = pathBounds;
            }
        }
        return checkBoxBounds;
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
    public void addCheckStateChangeListener ( @NotNull final CheckStateChangeListener<N> listener )
    {
        listenerList.add ( CheckStateChangeListener.class, listener );
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
    public void removeCheckStateChangeListener ( @NotNull final CheckStateChangeListener listener )
    {
        listenerList.remove ( CheckStateChangeListener.class, listener );
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
    public void fireCheckStateChanged ( @NotNull final List<CheckStateChange<N>> stateChanges )
    {
        for ( final CheckStateChangeListener<N> listener : listenerList.getListeners ( CheckStateChangeListener.class ) )
        {
            listener.checkStateChanged ( this, stateChanges );
        }
    }

    /**
     * WebCheckBoxTree mouse and key actions handler.
     */
    protected class Handler implements MouseListener, KeyListener, Serializable
    {
        @Override
        public void keyPressed ( @NotNull final KeyEvent e )
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
        public void mousePressed ( @NotNull final MouseEvent e )
        {
            if ( isCheckingByUserEnabled () && SwingUtils.isLeftMouseButton ( e ) )
            {
                final N node = getNodeForLocation ( e.getPoint () );
                if ( node != null && isCheckBoxVisible ( node ) && isCheckBoxEnabled ( node ) )
                {
                    final Rectangle checkBoxBounds = getCheckBoxBounds ( node );
                    if ( checkBoxBounds != null && checkBoxBounds.contains ( e.getPoint () ) )
                    {
                        invertCheck ( node );
                    }
                }
            }
        }

        @Override
        public void keyTyped ( @NotNull final KeyEvent e )
        {
            // Ignored
        }

        @Override
        public void keyReleased ( @NotNull final KeyEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseClicked ( @NotNull final MouseEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseReleased ( @NotNull final MouseEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseEntered ( @NotNull final MouseEvent e )
        {
            // Ignored
        }

        @Override
        public void mouseExited ( @NotNull final MouseEvent e )
        {
            // Ignored
        }
    }
}