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

import com.alee.extended.checkbox.CheckState;
import com.alee.laf.tree.WebTree;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.SwingUtils;

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
     * Style settings.
     */
    protected Integer checkBoxRendererGap = WebCheckBoxTreeStyle.checkBoxRendererGap;
    protected Boolean checkBoxVisible = WebCheckBoxTreeStyle.checkBoxVisible;
    protected Boolean checkingEnabled = WebCheckBoxTreeStyle.checkingEnabled;
    protected Boolean checkMixedOnToggle = WebCheckBoxTreeStyle.checkMixedOnToggle;

    /**
     * Cusstom checking model.
     */
    protected TreeCheckingModel checkingModel;

    /**
     * Checkbox cell renderer.
     */
    protected CheckBoxTreeCellRenderer checkBoxCellRenderer;

    /**
     * Actual content renderer;
     */
    protected TreeCellRenderer actualCellRenderer;

    /**
     * Tree actions handler.
     */
    protected Handler handler;

    /**
     * Constructs tree with default sample model.
     */
    public WebCheckBoxTree ()
    {
        super ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebCheckBoxTree ( final Object[] value )
    {
        super ( value );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebCheckBoxTree ( final Vector<?> value )
    {
        super ( value );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebCheckBoxTree ( final Hashtable<?, ?> value )
    {
        super ( value );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebCheckBoxTree ( final E root )
    {
        super ( root );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebCheckBoxTree ( final E root, final boolean asksAllowsChildren )
    {
        super ( root, asksAllowsChildren );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebCheckBoxTree ( final TreeModel newModel )
    {
        super ( newModel );
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
        return checkBoxRendererGap != null ? checkBoxRendererGap : WebCheckBoxTreeStyle.checkBoxRendererGap;
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
     * Returns list of checked nodes.
     *
     * @return list of checked nodes
     */
    public List<E> getCheckedNodes ()
    {
        return checkingModel != null ? checkingModel.getCheckedNodes () : new ArrayList<E> ( 0 );
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
     * @param nodes nodes to check
     */
    public void setChecked ( final Collection<E> nodes )
    {
        if ( checkingModel != null )
        {
            checkingModel.setChecked ( nodes );
        }
    }

    /**
     * Sets specified nodes state to unchecked.
     *
     * @param nodes nodes to uncheck
     */
    public void setUnchecked ( final Collection<E> nodes )
    {
        if ( checkingModel != null )
        {
            checkingModel.setUnchecked ( nodes );
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
        checkingModel.setChecked ( node, checked );
    }

    /**
     * Inverts tree node check.
     *
     * @param node tree node to process
     */
    public void invertCheck ( final E node )
    {
        checkingModel.invertCheck ( node );
    }

    /**
     * Inverts tree node check.
     *
     * @param nodes tree node to process
     */
    public void invertCheck ( final List<E> nodes )
    {
        checkingModel.invertCheck ( nodes );
    }

    /**
     * Unchecks all tree nodes.
     */
    public void uncheckAll ()
    {
        checkingModel.uncheckAll ();
    }

    /**
     * Checks all tree nodes.
     */
    public void checkAll ()
    {
        checkingModel.checkAll ();
    }

    /**
     * Returns tree checking model.
     *
     * @return tree checking model
     */
    public TreeCheckingModel getCheckingModel ()
    {
        return checkingModel;
    }

    /**
     * Sets tree checking model.
     *
     * @param checkingModel tree checking model
     */
    public void setCheckingModel ( final TreeCheckingModel checkingModel )
    {
        this.checkingModel = checkingModel;
    }

    /**
     * Creates and returns new default checking model for the specified checkbox tree.
     *
     * @param checkBoxTree checkbox tree to process
     * @return new default checking model for the specified checkbox tree
     */
    protected TreeCheckingModel createDefaultCheckingModel ( final WebCheckBoxTree checkBoxTree )
    {
        return new DefaultTreeCheckingModel ( checkBoxTree );
    }

    /**
     * Returns whether checkbox for the specified node should be enabled or not.
     *
     * @param node tree node to process
     * @return true if checkbox for the specified node should be enabled, false otherwise
     */
    @SuppressWarnings ("UnusedParameters")
    public boolean isCheckBoxEnabled ( final E node )
    {
        return true;
    }

    /**
     * Returns whether checkbox for the specified node should be visible or not.
     *
     * @param node tree node to process
     * @return true if checkbox for the specified node should be visible, false otherwise
     */
    @SuppressWarnings ("UnusedParameters")
    public boolean isCheckBoxVisible ( final E node )
    {
        return true;
    }

    /**
     * Returns whether checked or unchecked node childs should be checked or unchecked recursively or not.
     *
     * @return true if checked or unchecked node childs should be checked or unchecked recursively, false otherwise
     */
    @SuppressWarnings ("UnusedParameters")
    public boolean isRecursiveCheckingEnabled ()
    {
        return true;
    }

    /**
     * Returns whether checkboxes are visible in the tree or not.
     *
     * @return true if checkboxes are visible in the tree, false otherwise
     */
    public boolean getCheckBoxVisible ()
    {
        return checkBoxVisible != null ? checkBoxVisible : WebCheckBoxTreeStyle.checkBoxVisible;
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
        return checkingEnabled != null ? checkingEnabled : WebCheckBoxTreeStyle.checkingEnabled;
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
        return checkMixedOnToggle != null ? checkMixedOnToggle : WebCheckBoxTreeStyle.checkMixedOnToggle;
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
        return isEnabled () && getCheckBoxVisible () && isCheckingEnabled ();
    }

    /**
     * WebCheckBoxTree mouse and key actions handler.
     */
    protected class Handler implements MouseListener, KeyListener
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void keyPressed ( final KeyEvent e )
        {
            if ( isCheckingByUserEnabled () && Hotkey.SPACE.isTriggered ( e ) )
            {
                final List<E> nodes = getSelectedNodes ();
                if ( nodes.size () > 0 )
                {
                    invertCheck ( nodes );
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed ( final MouseEvent e )
        {
            if ( isCheckingByUserEnabled () && SwingUtils.isLeftMouseButton ( e ) )
            {
                final E node = getNodeForLocation ( e.getPoint () );
                if ( node != null )
                {
                    final Rectangle checkBoxBounds = getCheckBoxBounds ( node );
                    if ( checkBoxBounds.contains ( e.getPoint () ) )
                    {
                        invertCheck ( node );
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void keyTyped ( final KeyEvent e )
        {
            // Ignored
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void keyReleased ( final KeyEvent e )
        {
            // Ignored
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked ( final MouseEvent e )
        {
            // Ignored
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased ( final MouseEvent e )
        {
            // Ignored
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered ( final MouseEvent e )
        {
            // Ignored
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited ( final MouseEvent e )
        {
            // Ignored
        }
    }
}