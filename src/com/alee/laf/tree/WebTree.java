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

package com.alee.laf.tree;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.GeometryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * This JTree extension class provides a direct access to WebTreeUI methods.
 * There is also a set of additional methods to simplify some operations with tree.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could stil use that component even if WebLaF is not your application L&F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebTree<E extends DefaultMutableTreeNode> extends JTree implements FontMethods<WebTree<E>>
{
    /**
     * Single selection mode.
     * Only one node can be selected.
     */
    public static final int SINGLE_TREE_SELECTION = TreeSelectionModel.SINGLE_TREE_SELECTION;

    /**
     * Contiguous selection mode.
     * Any amount of nodes can be selected in a row.
     */
    public static final int CONTIGUOUS_TREE_SELECTION = TreeSelectionModel.CONTIGUOUS_TREE_SELECTION;

    /**
     * Discontiguous selection mode.
     * Any amount of nodes can be selected anywhere.
     */
    public static final int DISCONTIGUOUS_TREE_SELECTION = TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION;

    /**
     * Constructs tree with default sample model.
     */
    public WebTree ()
    {
        this ( getDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     */
    public WebTree ( Object[] value )
    {
        this ( createTreeModel ( value ) );
        this.setRootVisible ( false );
        this.setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     */
    public WebTree ( Vector<?> value )
    {
        this ( createTreeModel ( value ) );
        this.setRootVisible ( false );
        this.setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     */
    public WebTree ( Hashtable<?, ?> value )
    {
        this ( createTreeModel ( value ) );
        this.setRootVisible ( false );
        this.setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified root node.
     */
    public WebTree ( E root )
    {
        this ( new WebTreeModel<E> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     */
    public WebTree ( E root, boolean asksAllowsChildren )
    {
        this ( new WebTreeModel<E> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     */
    public WebTree ( TreeModel newModel )
    {
        super ( newModel );
    }

    /**
     * Expands the root path, assuming the current TreeModel has been set.
     */
    public void expandRoot ()
    {
        TreeModel model = getModel ();
        if ( model != null && model.getRoot () != null )
        {
            expandPath ( new TreePath ( model.getRoot () ) );
        }
    }

    /**
     * Expands all tree rows in a single call.
     * It is not recommended to expand large trees this way since that might cause huge interface lags.
     */
    public void expandAll ()
    {
        int i = 0;
        while ( i < getRowCount () )
        {
            expandRow ( i );
            i++;
        }
    }

    /**
     * Expands the specified node.
     *
     * @param node node to expand
     */
    public void expandNode ( E node )
    {
        expandPath ( getPathForNode ( node ) );
    }

    /**
     * Returns tree path for specified node.
     *
     * @param node node to process
     * @return tree path
     */
    public TreePath getPathForNode ( E node )
    {
        if ( node != null )
        {
            return new TreePath ( node.getPath () );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns tree node for specified path.
     *
     * @param path path to process
     * @return tree node
     */
    public E getNodeForPath ( TreePath path )
    {
        if ( path != null )
        {
            return ( E ) path.getLastPathComponent ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns selected node.
     *
     * @return selected node
     */
    public E getSelectedNode ()
    {
        return getNodeForPath ( getSelectionPath () );
    }

    /**
     * Returns selected nodes.
     *
     * @return selected nodes
     */
    public List<E> getSelectedNodes ()
    {
        List<E> selectedNodes = new ArrayList<E> ();
        if ( getSelectionPaths () != null )
        {
            for ( TreePath path : getSelectionPaths () )
            {
                selectedNodes.add ( getNodeForPath ( path ) );
            }
        }
        return selectedNodes;
    }

    /**
     * Sets tree selection mode.
     *
     * @param mode tree selection mode
     */
    public void setSelectionMode ( int mode )
    {
        getSelectionModel ().setSelectionMode ( mode );
    }

    /**
     * Scrolls tree view to selected nodes.
     */
    public void scrollToSelection ()
    {
        TreePath selectionPath = getSelectionPath ();
        Rectangle bounds = getPathBounds ( selectionPath );
        if ( bounds != null )
        {
            scrollRectToVisible ( bounds );
        }
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @return tree expansion and selection states
     */
    public TreeState getTreeState ()
    {
        return TreeUtils.getTreeState ( this );
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param saveSelection whether to save selection states or not
     * @return tree expansion and selection states
     */
    public TreeState getTreeState ( boolean saveSelection )
    {
        return TreeUtils.getTreeState ( this );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState tree expansion and selection states
     */
    public void setTreeState ( TreeState treeState )
    {
        TreeUtils.setTreeState ( this, treeState );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState        tree expansion and selection states
     * @param restoreSelection whether to restore selection states or not
     */
    public void setTreeState ( TreeState treeState, boolean restoreSelection )
    {
        TreeUtils.setTreeState ( this, treeState, restoreSelection );
    }

    /**
     * Returns whether rollover selection is enabled for this list or not.
     *
     * @return true if rollover selection is enabled for this list, false otherwise
     */
    public boolean isRolloverSelectionEnabled ()
    {
        return TreeRolloverSelectionAdapter.isInstalled ( this );
    }

    /**
     * Sets whether rollover selection is enabled for this list or not.
     *
     * @param enabled whether rollover selection is enabled for this list or not
     */
    public void setRolloverSelectionEnabled ( boolean enabled )
    {
        if ( enabled )
        {
            if ( !isRolloverSelectionEnabled () )
            {
                setHighlightRolloverNode ( false );
                TreeRolloverSelectionAdapter.install ( this );
            }
        }
        else
        {
            if ( isRolloverSelectionEnabled () )
            {
                TreeRolloverSelectionAdapter.uninstall ( this );
            }
        }
    }

    /**
     * Returns whether tree should highlight rollover node or not.
     *
     * @return true if tree should highlight rollover, false otherwise
     */
    public boolean isHighlightRolloverNode ()
    {
        return getWebUI ().isHighlightRolloverNode ();
    }

    /**
     * Sets whether tree should highlight rollover node or not.
     *
     * @param highlight whether tree should highlight rollover node or not
     */
    public void setHighlightRolloverNode ( boolean highlight )
    {
        getWebUI ().setHighlightRolloverNode ( highlight );
    }

    /**
     * Returns whether tree should paint structure lines or not.
     *
     * @return true if tree should paint structure lines, false otherwise
     */
    public boolean isPaintLines ()
    {
        return getWebUI ().isPaintLines ();
    }

    /**
     * Sets whether tree should paint structure lines or not.
     *
     * @param paint whether tree should paint structure lines or not
     */
    public void setPaintLines ( boolean paint )
    {
        getWebUI ().setPaintLines ( paint );
    }

    /**
     * Returns tree structure lines color.
     *
     * @return tree structure lines color
     */
    public Color getLinesColor ()
    {
        return getWebUI ().getLinesColor ();
    }

    /**
     * Sets tree structure lines color.
     *
     * @param color tree structure lines color
     */
    public void setLinesColor ( Color color )
    {
        getWebUI ().setLinesColor ( color );
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public TreeSelectionStyle getSelectionStyle ()
    {
        return getWebUI ().getSelectionStyle ();
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( TreeSelectionStyle style )
    {
        getWebUI ().setSelectionStyle ( style );
    }

    /**
     * Returns tree selection rounding.
     *
     * @return tree selection rounding
     */
    public int getSelectionRound ()
    {
        return getWebUI ().getSelectionRound ();
    }

    /**
     * Sets tree selection rounding.
     *
     * @param round tree selection rounding
     */
    public void setSelectionRound ( int round )
    {
        getWebUI ().setSelectionRound ( round );
    }

    /**
     * Returns tree selection shade width.
     *
     * @return tree selection shade width
     */
    public int getSelectionShadeWidth ()
    {
        return getWebUI ().getSelectionShadeWidth ();
    }

    /**
     * Sets tree selection shade width.
     *
     * @param shadeWidth tree selection shade width
     */
    public void setSelectionShadeWidth ( int shadeWidth )
    {
        getWebUI ().setSelectionShadeWidth ( shadeWidth );
    }

    /**
     * Returns whether selector is enabled or not.
     *
     * @return true if selector is enabled, false otherwise
     */
    public boolean isSelectorEnabled ()
    {
        return getWebUI ().isSelectorEnabled ();
    }

    /**
     * Sets whether selector is enabled or not.
     *
     * @param enabled whether selector is enabled or not
     */
    public void setSelectorEnabled ( boolean enabled )
    {
        getWebUI ().setSelectorEnabled ( enabled );
    }

    /**
     * Returns selector color.
     *
     * @return selector color
     */
    public Color getSelectorColor ()
    {
        return getWebUI ().getSelectorColor ();
    }

    /**
     * Sets selector color.
     *
     * @param color selector color
     */
    public void setSelectorColor ( Color color )
    {
        getWebUI ().setSelectorColor ( color );
    }

    /**
     * Returns selector border color.
     *
     * @return selector border color
     */
    public Color getSelectorBorderColor ()
    {
        return getWebUI ().getSelectorBorderColor ();
    }

    /**
     * Sets selector border color.
     *
     * @param color selector border color
     */
    public void setSelectorBorderColor ( Color color )
    {
        getWebUI ().setSelectorBorderColor ( color );
    }

    /**
     * Returns selector rounding.
     *
     * @return selector rounding
     */
    public int getSelectorRound ()
    {
        return getWebUI ().getSelectorRound ();
    }

    /**
     * Sets selector rounding.
     *
     * @param round selector rounding
     */
    public void setSelectorRound ( int round )
    {
        getWebUI ().setSelectorRound ( round );
    }

    /**
     * Returns selector border stroke.
     *
     * @return selector border stroke
     */
    public BasicStroke getSelectorStroke ()
    {
        return getWebUI ().getSelectorStroke ();
    }

    /**
     * Sets selector border stroke.
     *
     * @param stroke selector border stroke
     */
    public void setSelectorStroke ( BasicStroke stroke )
    {
        getWebUI ().setSelectorStroke ( stroke );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebTreeUI getWebUI ()
    {
        return ( WebTreeUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebTreeUI ) )
        {
            try
            {
                setUI ( ( WebTreeUI ) ReflectUtils.createInstance ( WebLookAndFeel.treeUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebTreeUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Repaints specified tree row.
     *
     * @param row row index
     */
    public void repaint ( int row )
    {
        repaint ( getWebUI ().getRowBounds ( row ) );
    }

    /**
     * Repaints all tree rows in specified range.
     *
     * @param from first row index
     * @param to   last row index
     */
    public void repaint ( int from, int to )
    {
        repaint ( GeometryUtils.getContainingRect ( getWebUI ().getRowBounds ( from ), getWebUI ().getRowBounds ( to ) ) );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree changeFontSize ( int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTree setFontName ( String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }

    /**
     * Returns a TreeModel wrapping the specified object.
     * If the object is an array of Object, Hashtable or Vector then a new root node is created with each of the incoming objects as
     * children. Otherwise, a new root is created with the specified object as its value.
     *
     * @param value Object used as the foundation for the TreeModel
     * @return a TreeModel wrapping the specified object
     */
    protected static TreeModel createTreeModel ( Object value )
    {
        DefaultMutableTreeNode root;
        if ( value instanceof Object[] || value instanceof Hashtable || value instanceof Vector )
        {
            root = new DefaultMutableTreeNode ( "root" );
            DynamicUtilTreeNode.createChildren ( root, value );
        }
        else
        {
            root = new DynamicUtilTreeNode ( "root", value );
        }
        return new WebTreeModel<DefaultMutableTreeNode> ( root, false );
    }

    /**
     * Creates and returns a sample TreeModel.
     * Used primarily for beanbuilders to show something interesting.
     *
     * @return the default TreeModel
     */
    protected static TreeModel getDefaultTreeModel ()
    {
        UniqueNode root = new UniqueNode ( "JTree" );
        UniqueNode parent;

        parent = new UniqueNode ( "colors" );
        root.add ( parent );
        parent.add ( new UniqueNode ( "blue" ) );
        parent.add ( new UniqueNode ( "violet" ) );
        parent.add ( new UniqueNode ( "red" ) );
        parent.add ( new UniqueNode ( "yellow" ) );

        parent = new UniqueNode ( "sports" );
        root.add ( parent );
        parent.add ( new UniqueNode ( "basketball" ) );
        parent.add ( new UniqueNode ( "soccer" ) );
        parent.add ( new UniqueNode ( "football" ) );
        parent.add ( new UniqueNode ( "hockey" ) );

        parent = new UniqueNode ( "food" );
        root.add ( parent );
        parent.add ( new UniqueNode ( "hot dogs" ) );
        parent.add ( new UniqueNode ( "pizza" ) );
        parent.add ( new UniqueNode ( "ravioli" ) );
        parent.add ( new UniqueNode ( "bananas" ) );

        return new WebTreeModel<UniqueNode> ( root );
    }
}