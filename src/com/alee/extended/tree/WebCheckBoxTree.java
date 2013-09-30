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
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
    protected int checkBoxRendererGap = WebCheckBoxTreeStyle.checkBoxRendererGap;

    /**
     * Whether user can interact with checkboxes to change their check state or not.
     */
    protected boolean checkingEnabled = true;

    /**
     * Whether partially checked node should be checked or unchecked on toggle.
     */
    protected boolean checkMixedOnToggle = true;

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
        if ( renderer instanceof CheckBoxTreeCellRenderer )
        {
            checkBoxCellRenderer = ( CheckBoxTreeCellRenderer ) renderer;
            actualCellRenderer = checkBoxCellRenderer.getActualRenderer ();
        }
        else
        {
            checkBoxCellRenderer = createCheckBoxTreeCellRenderer ( renderer );
            actualCellRenderer = renderer;
        }
        checkBoxCellRenderer.setCheckBoxRendererGap ( checkBoxRendererGap );

        super.setCellRenderer ( checkBoxCellRenderer );
    }

    /**
     * Creates and returns checkbox tree cell renderer.
     *
     * @param renderer actual cell renderer
     * @return checkbox tree cell renderer
     */
    protected WebCheckBoxTreeCellRenderer createCheckBoxTreeCellRenderer ( TreeCellRenderer renderer )
    {
        return new WebCheckBoxTreeCellRenderer ( renderer );
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
     * Returns whether the specified tree node is checked or not.
     *
     * @param node tree node to process
     * @return true if the specified tree node is checked, false otherwise
     */
    public boolean isChecked ( final E node )
    {
        return checkingModel != null && checkingModel.isChecked ( node );
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
    @SuppressWarnings ( "UnusedParameters" )
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
    @SuppressWarnings ( "UnusedParameters" )
    public boolean isCheckBoxVisible ( final E node )
    {
        return true;
    }

    /**
     * Returns whether checked or unchecked node childs should be checked or unchecked recursively or not.
     *
     * @return true if checked or unchecked node childs should be checked or unchecked recursively, false otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
    public boolean isRecursiveCheckingEnabled ()
    {
        return true;
    }

    /**
     * Returns whether user can interact with checkboxes to change their check state or not.
     *
     * @return true if user can interact with checkboxes to change their check state, false otherwise
     */
    public boolean isCheckingEnabled ()
    {
        return checkingEnabled;
    }

    /**
     * Sets whether user can interact with checkboxes to change their check state or not.
     *
     * @param checkingEnabled whether user can interact with checkboxes to change their check state or not
     */
    public void setCheckingEnabled ( final boolean checkingEnabled )
    {
        this.checkingEnabled = checkingEnabled;
    }

    /**
     * Returns whether partially checked node should be checked or unchecked on toggle.
     *
     * @return true if partially checked node should be checked on toggle, false if it should be unchecked
     */
    public boolean isCheckMixedOnToggle ()
    {
        return checkMixedOnToggle;
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
            if ( Hotkey.SPACE.isTriggered ( e ) )
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
            if ( SwingUtils.isLeftMouseButton ( e ) )
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