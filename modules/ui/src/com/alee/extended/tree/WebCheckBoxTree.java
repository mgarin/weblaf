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

import com.alee.laf.checkbox.CheckState;
import com.alee.laf.tree.WebTree;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.StateProvider;

import javax.swing.tree.DefaultMutableTreeNode;
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
 * This WebTree extension class provides additional checkbox tree functionality.
 * Basically this tree puts its own rendered behind the one specified by developer and uses it to render checkboxes.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxTree<E extends DefaultMutableTreeNode> extends WebTree<E>
{
    /**
     * todo 1. Create separate "checkboxtree" styleable component with its own skin
     */

    /**
     * Style settings.
     */
    protected Boolean recursiveChecking;
    protected Integer checkBoxRendererGap;
    protected Boolean checkBoxVisible;
    protected Boolean checkingEnabled;
    protected Boolean checkMixedOnToggle;

    /**
     * Custom checking model.
     */
    protected TreeCheckingModel<E> checkingModel;

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
    protected StateProvider<E> enabledStateProvider;

    /**
     * Checkbox visibility state provider.
     */
    protected StateProvider<E> visibleStateProvider;

    /**
     * Tree actions handler.
     */
    protected Handler handler;

    /**
     * Checkbox tree check state change listeners.
     */
    protected List<CheckStateChangeListener<E>> checkStateChangeListeners = new ArrayList<CheckStateChangeListener<E>> ( 1 );

    /**
     * Constructs tree with default sample model.
     */
    public WebCheckBoxTree ()
    {
        super ( StyleId.checkboxtree );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebCheckBoxTree ( final Object[] value )
    {
        super ( StyleId.checkboxtree, value );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebCheckBoxTree ( final Vector<?> value )
    {
        super ( StyleId.checkboxtree, value );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebCheckBoxTree ( final Hashtable<?, ?> value )
    {
        super ( StyleId.checkboxtree, value );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebCheckBoxTree ( final E root )
    {
        super ( StyleId.checkboxtree, root );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebCheckBoxTree ( final E root, final boolean asksAllowsChildren )
    {
        super ( StyleId.checkboxtree, root, asksAllowsChildren );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebCheckBoxTree ( final TreeModel newModel )
    {
        super ( StyleId.checkboxtree, newModel );
    }

    /**
     * Constructs tree with default sample model.
     *
     * @param id style ID
     */
    public WebCheckBoxTree ( final StyleId id )
    {
        super ( id );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id    style ID
     * @param value tree data
     */
    public WebCheckBoxTree ( final StyleId id, final Object[] value )
    {
        super ( id, value );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id    style ID
     * @param value tree data
     */
    public WebCheckBoxTree ( final StyleId id, final Vector<?> value )
    {
        super ( id, value );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id    style ID
     * @param value tree data
     */
    public WebCheckBoxTree ( final StyleId id, final Hashtable<?, ?> value )
    {
        super ( id, value );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param id   style ID
     * @param root tree root node
     */
    public WebCheckBoxTree ( final StyleId id, final E root )
    {
        super ( id, root );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param id                 style ID
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebCheckBoxTree ( final StyleId id, final E root, final boolean asksAllowsChildren )
    {
        super ( id, root, asksAllowsChildren );
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
    }

    @Override
    protected void init ()
    {
        // Initializing checking model
        checkingModel = createDefaultCheckingModel ( this );

        // Initializing actions handler
        handler = new Handler ();
        addMouseListener ( handler );
        addKeyListener ( handler );
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
            checkBoxCellRenderer.setCheckBoxRendererGap ( getCheckBoxRendererGap () );
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
        checkBoxCellRenderer.setCheckBoxRendererGap ( getCheckBoxRendererGap () );
        super.setCellRenderer ( checkBoxCellRenderer );
    }

    /**
     * Creates and returns checkbox tree cell renderer.
     *
     * @return checkbox tree cell renderer
     */
    protected WebCheckBoxTreeCellRenderer createCheckBoxTreeCellRenderer ()
    {
        return new WebCheckBoxTreeCellRenderer ( WebCheckBoxTree.this );
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
    public CheckState getCheckState ( final E node )
    {
        return checkingModel != null ? checkingModel.getCheckState ( node ) : CheckState.unchecked;
    }

    /**
     * Returns whether the specified tree nod is unchecked or not.
     *
     * @param node tree node to process
     * @return true if the specified tree nod is unchecked, false otherwise
     */
    public boolean isUnchecked ( final E node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.unchecked;
    }

    /**
     * Returns whether the specified tree node is checked or not.
     *
     * @param node tree node to process
     * @return true if the specified tree node is checked, false otherwise
     */
    public boolean isChecked ( final E node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.checked;
    }

    /**
     * Returns whether the specified tree node is partially checked or not.
     *
     * @param node tree node to process
     * @return true if the specified tree node is partially checked, false otherwise
     */
    public boolean isMixed ( final E node )
    {
        return checkingModel != null && checkingModel.getCheckState ( node ) == CheckState.mixed;
    }

    /**
     * Returns optimized list of checked nodes.
     *
     * @return optimized list of checked nodes
     */
    public List<E> getCheckedNodes ()
    {
        return getCheckedNodes ( true );
    }

    /**
     * Returns list of checked nodes.
     *
     * @param optimize whether should optimize the resulting list by removing checked node children or not
     * @return list of checked nodes
     */
    public List<E> getCheckedNodes ( final boolean optimize )
    {
        return checkingModel != null ? checkingModel.getCheckedNodes ( optimize ) : new ArrayList<E> ( 0 );
    }

    /**
     * Returns list of nodes in mixed state.
     *
     * @return list of nodes in mixed state
     */
    public List<E> getMixedNodes ()
    {
        return checkingModel != null ? checkingModel.getMixedNodes () : new ArrayList<E> ( 0 );
    }

    /**
     * Sets specified nodes state to checked.
     *
     * @param nodes   nodes to check
     * @param checked whether the specified tree nodes should be checked or not
     */
    public void setChecked ( final Collection<E> nodes, final boolean checked )
    {
        if ( checkingModel != null )
        {
            checkingModel.setChecked ( nodes, checked );
        }
    }

    /**
     * Sets whether the specified tree node is checked or not.
     *
     * @param node    tree node to process
     * @param checked whether the specified tree node is checked or not
     */
    public void setChecked ( final E node, final boolean checked )
    {
        if ( checkingModel != null )
        {
            checkingModel.setChecked ( node, checked );
        }
    }

    /**
     * Invert tree node check.
     *
     * @param node tree node to process
     */
    public void invertCheck ( final E node )
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
    public void invertCheck ( final List<E> nodes )
    {
        if ( checkingModel != null )
        {
            checkingModel.invertCheck ( nodes );
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
     * Returns tree checking model.
     *
     * @return tree checking model
     */
    public TreeCheckingModel<E> getCheckingModel ()
    {
        return checkingModel;
    }

    /**
     * Sets tree checking model.
     *
     * @param checkingModel tree checking model
     */
    public void setCheckingModel ( final TreeCheckingModel<E> checkingModel )
    {
        // Removing check state change listeners from old model
        for ( final CheckStateChangeListener<E> listener : checkStateChangeListeners )
        {
            this.checkingModel.removeCheckStateChangeListener ( listener );
        }

        this.checkingModel = checkingModel;

        // Updating nodes view due to possible check state changes
        updateAllVisibleNodes ();

        // Restoring check state change listeners
        for ( final CheckStateChangeListener<E> listener : checkStateChangeListeners )
        {
            checkingModel.addCheckStateChangeListener ( listener );
        }
    }

    /**
     * Creates and returns new default checking model for the specified checkbox tree.
     *
     * @param checkBoxTree checkbox tree to process
     * @return new default checking model for the specified checkbox tree
     */
    protected TreeCheckingModel<E> createDefaultCheckingModel ( final WebCheckBoxTree<E> checkBoxTree )
    {
        return new DefaultTreeCheckingModel<E> ( checkBoxTree );
    }

    /**
     * Returns whether checkbox for the specified node should be enabled or not.
     *
     * @param node tree node to process
     * @return true if checkbox for the specified node should be enabled, false otherwise
     */
    public boolean isCheckBoxEnabled ( final E node )
    {
        return enabledStateProvider == null || enabledStateProvider.provide ( node );
    }

    /**
     * Sets enabled state provider.
     * It defines whether checkboxes for specific nodes should be enabled or not.
     *
     * @param provider enabled state provider
     */
    public void setCheckBoxEnabledStateProvider ( final StateProvider<E> provider )
    {
        this.enabledStateProvider = provider;
    }

    /**
     * Returns whether checkbox for the specified node should be visible or not.
     *
     * @param node tree node to process
     * @return true if checkbox for the specified node should be visible, false otherwise
     */
    public boolean isCheckBoxVisible ( final E node )
    {
        return visibleStateProvider == null || visibleStateProvider.provide ( node );
    }

    /**
     * Sets visibility state provider.
     * It defines whether checkboxes for specific nodes should be visible or not.
     *
     * @param provider new visibility state provider
     */
    public void setCheckBoxVisibleStateProvider ( final StateProvider<E> provider )
    {
        this.visibleStateProvider = provider;
    }

    /**
     * Returns whether checked or unchecked node children should be checked or unchecked recursively or not.
     *
     * @return true if checked or unchecked node children should be checked or unchecked recursively, false otherwise
     */
    public boolean isRecursiveCheckingEnabled ()
    {
        return recursiveChecking != null ? recursiveChecking : true;
    }

    /**
     * Sets whether checked or unchecked node children should be checked or unchecked recursively or not.
     *
     * @param recursive whether checked or unchecked node children should be checked or unchecked recursively or not
     */
    public void setRecursiveChecking ( final boolean recursive )
    {
        final boolean modified = recursiveChecking != recursive;
        recursiveChecking = recursive;
        if ( modified && checkingModel != null )
        {
            checkingModel.checkingModeChanged ( recursiveChecking );
        }
    }

    /**
     * Returns whether checkboxes are visible in the tree or not.
     *
     * @return true if checkboxes are visible in the tree, false otherwise
     */
    public boolean isCheckBoxVisible ()
    {
        return checkBoxVisible != null ? checkBoxVisible : true;
    }

    /**
     * Sets whether checkboxes are visible in the tree or not.
     *
     * @param visible whether checkboxes are visible in the tree or not
     */
    public void setCheckBoxVisible ( final boolean visible )
    {
        this.checkBoxVisible = visible;
        updateAllVisibleNodes ();
    }

    /**
     * Returns whether user can interact with checkboxes to change their check state or not.
     *
     * @return true if user can interact with checkboxes to change their check state, false otherwise
     */
    public boolean isCheckingEnabled ()
    {
        return checkingEnabled != null ? checkingEnabled : true;
    }

    /**
     * Sets whether user can interact with checkboxes to change their check state or not.
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
     * @return true if partially checked node should be checked on toggle, false if it should be unchecked
     */
    public boolean isCheckMixedOnToggle ()
    {
        return checkMixedOnToggle != null ? checkMixedOnToggle : true;
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
    public Rectangle getCheckBoxBounds ( final E node )
    {
        return getCheckBoxBounds ( getPathForNode ( node ) );
    }

    /**
     * Returns checkbox bounds for the specified tree path.
     *
     * @param treePath tree path to process
     * @return checkbox bounds for the specified tree path
     */
    public Rectangle getCheckBoxBounds ( final TreePath treePath )
    {
        if ( checkBoxCellRenderer != null )
        {
            final int checkBoxWidth = checkBoxCellRenderer.getCheckBoxWidth ();
            final Rectangle pathBounds = getPathBounds ( treePath );
            if ( getComponentOrientation ().isLeftToRight () )
            {
                pathBounds.width = checkBoxWidth;
            }
            else
            {
                pathBounds.x += pathBounds.width - checkBoxWidth;
                pathBounds.width = checkBoxWidth;
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
     * @return true if user can change checkbox states, false otherwise
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
    public void addCheckStateChangeListener ( final CheckStateChangeListener<E> listener )
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
     * Informs about single or multiply check state changes.
     *
     * @param stateChanges check state changes list
     */
    public void fireCheckStateChanged ( final List<CheckStateChange<E>> stateChanges )
    {
        for ( final CheckStateChangeListener<E> listener : CollectionUtils.copy ( checkStateChangeListeners ) )
        {
            listener.checkStateChanged ( stateChanges );
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
                final List<E> nodes = getSelectedNodes ();

                // Removing invisible nodes from checking list
                final Iterator<E> nodesIterator = nodes.iterator ();
                while ( nodesIterator.hasNext () )
                {
                    final E node = nodesIterator.next ();
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
                final E node = getNodeForLocation ( e.getPoint () );
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