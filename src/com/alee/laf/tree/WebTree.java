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
import javax.swing.event.CellEditorListener;
import javax.swing.tree.*;
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
 * You could stil use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @param <E> tree nodes type
 * @author Mikle Garin
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
     * Tree cell editor listeners.
     * These listeners act separately from the cell editor and will be moved to new tree cell editor automatically on set.
     */
    protected List<CellEditorListener> cellEditorListeners = new ArrayList<CellEditorListener> ( 1 );

    /**
     * Constructs tree with default sample model.
     */
    public WebTree ()
    {
        this ( getDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebTree ( final Object[] value )
    {
        this ( createTreeModel ( value ) );
        this.setRootVisible ( false );
        this.setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebTree ( final Vector<?> value )
    {
        this ( createTreeModel ( value ) );
        this.setRootVisible ( false );
        this.setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebTree ( final Hashtable<?, ?> value )
    {
        this ( createTreeModel ( value ) );
        this.setRootVisible ( false );
        this.setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebTree ( final E root )
    {
        this ( new WebTreeModel<E> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTree ( final E root, final boolean asksAllowsChildren )
    {
        this ( new WebTreeModel<E> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebTree ( final TreeModel newModel )
    {
        super ( newModel );
        init ();
    }

    /**
     * Initializes additional tree settings.
     */
    protected void init ()
    {
        // You can add your own initialize implementation here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCellEditor ( final TreeCellEditor cellEditor )
    {
        // Removing cell editor listeners from old cell editor
        for ( final CellEditorListener listener : cellEditorListeners )
        {
            this.cellEditor.removeCellEditorListener ( listener );
        }

        super.setCellEditor ( cellEditor );

        // Adding cell editor listeners to new cell editor
        for ( final CellEditorListener listener : cellEditorListeners )
        {
            this.cellEditor.addCellEditorListener ( listener );
        }
    }

    /**
     * Adds tree cell editor listener.
     * These listeners act separately from the cell editor and will be moved to new tree cell editor automatically on set.
     *
     * @param listener cell editor listener to add
     */
    public void addCellEditorListener ( final CellEditorListener listener )
    {
        cellEditorListeners.add ( listener );
        if ( cellEditor != null )
        {
            cellEditor.addCellEditorListener ( listener );
        }
    }

    /**
     * Removes tree cell editor listener.
     *
     * @param listener cell editor listener to remove
     */
    public void removeCellEditorListener ( final CellEditorListener listener )
    {
        cellEditorListeners.remove ( listener );
        if ( cellEditor != null )
        {
            cellEditor.removeCellEditorListener ( listener );
        }
    }

    /**
     * Expands the root path, assuming the current TreeModel has been set.
     */
    public void expandRoot ()
    {
        final TreeModel model = getModel ();
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
    public void expandNode ( final E node )
    {
        expandPath ( getPathForNode ( node ) );
    }

    /**
     * Returns whether node is expanded or not.
     *
     * @param node node to check
     * @return true if node is expanded, false otherwise
     */
    public boolean isExpanded ( final E node )
    {
        return isExpanded ( getPathForNode ( node ) );
    }

    /**
     * Returns node bounds.
     *
     * @param node node to process
     * @return node bounds
     */
    public Rectangle getNodeBounds ( final E node )
    {
        return getPathBounds ( getPathForNode ( node ) );
    }

    /**
     * Returns tree node for the specified row.
     *
     * @param row row to process
     * @return tree node for the specified row
     */
    public E getNodeForRow ( final int row )
    {
        return getNodeForPath ( getPathForRow ( row ) );
    }

    /**
     * Returns tree path for specified node.
     *
     * @param node node to process
     * @return tree path
     */
    public TreePath getPathForNode ( final E node )
    {
        return node != null ? new TreePath ( node.getPath () ) : null;
    }

    /**
     * Returns tree node for specified path.
     *
     * @param path path to process
     * @return tree node for specified path
     */
    public E getNodeForPath ( final TreePath path )
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
     * Returns tree node for the specified location.
     *
     * @param location location to process
     * @return tree node for the specified location
     */
    public E getNodeForLocation ( final Point location )
    {
        return getNodeForLocation ( location.x, location.y );
    }

    /**
     * Returns tree node for the specified location.
     *
     * @param x location X coordinate
     * @param y location Y coordinate
     * @return tree node for the specified location
     */
    public E getNodeForLocation ( final int x, final int y )
    {
        return getNodeForPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Returns the path for the node at the specified location.
     *
     * @param location location to process
     * @return the <code>TreePath</code> for the node at that location
     */
    public TreePath getPathForLocation ( final Point location )
    {
        return getPathForLocation ( location.x, location.y );
    }

    /**
     * Returns closest tree node for the specified location.
     *
     * @param location location to process
     * @return tree node for the specified location
     */
    public E getClosestNodeForLocation ( final Point location )
    {
        return getClosestNodeForLocation ( location.x, location.y );
    }

    /**
     * Returns closest tree node for the specified location.
     *
     * @param x location X coordinate
     * @param y location Y coordinate
     * @return tree node for the specified location
     */
    public E getClosestNodeForLocation ( final int x, final int y )
    {
        return getNodeForPath ( getClosestPathForLocation ( x, y ) );
    }

    /**
     * Returns the path to the node that is closest to the specified location.
     *
     * @param location location to process
     * @return the <code>TreePath</code> for the node closest to that location,
     *         <code>null</code> if nothing is viewable or there is no model
     */
    public TreePath getClosestPathForLocation ( final Point location )
    {
        return getClosestPathForLocation ( location.x, location.y );
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
        final List<E> selectedNodes = new ArrayList<E> ();
        final TreePath[] selectionPaths = getSelectionPaths ();
        if ( selectionPaths != null )
        {
            for ( final TreePath path : selectionPaths )
            {
                selectedNodes.add ( getNodeForPath ( path ) );
            }
        }
        return selectedNodes;
    }

    /**
     * Selects node under the specified point.
     *
     * @param point point to look for node
     */
    public void selectNodeUnderPoint ( final Point point )
    {
        selectNodeUnderPoint ( point.x, point.y );
    }

    /**
     * Selects node under the specified point.
     *
     * @param x point X coordinate
     * @param y point Y coordinate
     */
    public void selectNodeUnderPoint ( final int x, final int y )
    {
        setSelectionPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Sets selected nodes.
     */
    public void setSelectedNode ( final E node )
    {
        final TreePath path = getPathForNode ( node );
        if ( path != null )
        {
            setSelectionPath ( path );
        }
    }

    /**
     * Sets selected nodes.
     */
    public void setSelectedNodes ( final List<E> nodes )
    {
        final TreePath[] paths = new TreePath[ nodes.size () ];
        for ( int i = 0; i < nodes.size (); i++ )
        {
            paths[ i ] = getPathForNode ( nodes.get ( i ) );
        }
        setSelectionPaths ( paths );
    }

    /**
     * Sets selected nodes.
     */
    public void setSelectedNodes ( final E[] nodes )
    {
        final TreePath[] paths = new TreePath[ nodes.length ];
        for ( int i = 0; i < nodes.length; i++ )
        {
            paths[ i ] = getPathForNode ( nodes[ i ] );
        }
        setSelectionPaths ( paths );
    }

    /**
     * Returns tree root node.
     *
     * @return tree root node
     */
    public E getRootNode ()
    {
        return ( E ) getModel ().getRoot ();
    }

    /**
     * Returns list of all nodes added into the tree.
     *
     * @return list of all nodes added into the tree
     */
    public List<E> getAllNodes ()
    {
        final List<E> nodes = new ArrayList<E> ();
        getAllNodesImpl ( nodes, getRootNode () );
        return nodes;
    }

    /**
     * Collects list of all nodes added into the tree.
     *
     * @param nodes list into which all nodes should be collected
     * @param node  node to start collecting from
     */
    private void getAllNodesImpl ( final List<E> nodes, final E node )
    {
        nodes.add ( node );
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            getAllNodesImpl ( nodes, ( E ) node.getChildAt ( i ) );
        }
    }

    /**
     * Sets tree selection mode.
     *
     * @param mode tree selection mode
     */
    public void setSelectionMode ( final int mode )
    {
        getSelectionModel ().setSelectionMode ( mode );
    }

    /**
     * Sets whether multiply nodes selection allowed or not.
     * This call simply changes selection mode according to provided value.
     *
     * @param allowed whether multiply nodes selection allowed or not
     */
    public void setMultiplySelectionAllowed ( final boolean allowed )
    {
        setSelectionMode ( allowed ? DISCONTIGUOUS_TREE_SELECTION : SINGLE_TREE_SELECTION );
    }

    /**
     * Scrolls tree view to the beginning of the tree.
     */
    public void scrollToStart ()
    {
        scrollRectToVisible ( new Rectangle ( 0, 0, 1, 1 ) );
    }

    /**
     * Scrolls tree view to selected nodes.
     */
    public void scrollToSelection ()
    {
        final Rectangle bounds = getPathBounds ( getSelectionPath () );
        if ( bounds != null )
        {
            scrollRectToVisible ( bounds );
        }
    }

    /**
     * Scrolls tree view to specified node.
     *
     * @param node node to scroll to
     */
    public void scrollToNode ( final E node )
    {
        final Rectangle bounds = getNodeBounds ( node );
        if ( bounds != null )
        {
            scrollRectToVisible ( bounds );
        }
    }

    /**
     * Starts editing selected tree node.
     */
    public void startEditingSelectedNode ()
    {
        final TreePath path = getSelectionPath ();
        if ( path != null )
        {
            startEditingAtPath ( path );
        }
    }

    /**
     * Starts editing the specified node.
     *
     * @param node tree node to edit
     */
    public void startEditingNode ( final E node )
    {
        final TreePath path = getPathForNode ( node );
        if ( path != null )
        {
            startEditingAtPath ( path );
        }
    }

    /**
     * Updates all visible nodes.
     * This might be used to update node sizes if renderer has changed.
     */
    public void updateAllVisibleNodes ()
    {
        revalidate ();
        repaint ();
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
    public TreeState getTreeState ( final boolean saveSelection )
    {
        return TreeUtils.getTreeState ( this, saveSelection );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState tree expansion and selection states
     */
    public void setTreeState ( final TreeState treeState )
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
    public void setTreeState ( final TreeState treeState, final boolean restoreSelection )
    {
        TreeUtils.setTreeState ( this, treeState, restoreSelection );
    }

    /**
     * Returns whether tree should expand nodes on selection or not.
     *
     * @return true if tree should expand nodes on selection, false otherwise
     */
    public boolean isAutoExpandSelectedNode ()
    {
        return getWebUI ().isAutoExpandSelectedNode ();
    }

    /**
     * Sets whether tree should expand nodes on selection or not.
     *
     * @param autoExpandSelectedNode whether tree should expand nodes on selection or not
     */
    public void setAutoExpandSelectedNode ( final boolean autoExpandSelectedNode )
    {
        getWebUI ().setAutoExpandSelectedNode ( autoExpandSelectedNode );
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
    public void setRolloverSelectionEnabled ( final boolean enabled )
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
    public void setHighlightRolloverNode ( final boolean highlight )
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
    public void setPaintLines ( final boolean paint )
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
    public void setLinesColor ( final Color color )
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
    public void setSelectionStyle ( final TreeSelectionStyle style )
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
    public void setSelectionRound ( final int round )
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
    public void setSelectionShadeWidth ( final int shadeWidth )
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
    public void setSelectorEnabled ( final boolean enabled )
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
    public void setSelectorColor ( final Color color )
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
    public void setSelectorBorderColor ( final Color color )
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
    public void setSelectorRound ( final int round )
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
    public void setSelectorStroke ( final BasicStroke stroke )
    {
        getWebUI ().setSelectorStroke ( stroke );
    }

    /**
     * Returns drop cell highlight shade width.
     *
     * @return drop cell highlight shade width
     */
    public int getDropCellShadeWidth ()
    {
        return getWebUI ().getDropCellShadeWidth ();
    }

    /**
     * Sets drop cell highlight shade width.
     *
     * @param dropCellShadeWidth new drop cell highlight shade width
     */
    public void setDropCellShadeWidth ( final int dropCellShadeWidth )
    {
        getWebUI ().setDropCellShadeWidth ( dropCellShadeWidth );
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
    @Override
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
    public void repaint ( final int row )
    {
        repaint ( getWebUI ().getRowBounds ( row ) );
    }

    /**
     * Repaints all tree rows in specified range.
     *
     * @param from first row index
     * @param to   last row index
     */
    public void repaint ( final int from, final int to )
    {
        final WebTreeUI webUI = getWebUI ();
        final Rectangle fromBounds = webUI.getRowBounds ( from );
        final Rectangle toBounds = webUI.getRowBounds ( to );
        final Rectangle rect = GeometryUtils.getContainingRect ( fromBounds, toBounds );
        if ( rect != null )
        {
            repaint ( rect );
        }
    }

    /**
     * Repaints specified node.
     *
     * @param node node to repaint
     */
    public void repaint ( final E node )
    {
        if ( node != null )
        {
            repaint ( getNodeBounds ( node ) );
        }
    }

    /**
     * Repaints specified node.
     *
     * @param nodes nodes to repaint
     */
    public void repaint ( final List<E> nodes )
    {
        if ( nodes != null && nodes.size () > 0 )
        {
            Rectangle summ = null;
            for ( final E node : nodes )
            {
                summ = GeometryUtils.getContainingRect ( summ, getNodeBounds ( node ) );
            }
            if ( summ != null )
            {
                repaint ( summ );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTree<E> setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    protected static TreeModel createTreeModel ( final Object value )
    {
        final DefaultMutableTreeNode root;
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
    public static TreeModel getDefaultTreeModel ()
    {
        final UniqueNode root = new UniqueNode ( "JTree" );

        UniqueNode parent = new UniqueNode ( "colors" );
        parent.add ( new UniqueNode ( "blue" ) );
        parent.add ( new UniqueNode ( "violet" ) );
        parent.add ( new UniqueNode ( "red" ) );
        parent.add ( new UniqueNode ( "yellow" ) );
        root.add ( parent );

        parent = new UniqueNode ( "sports" );
        parent.add ( new UniqueNode ( "basketball" ) );
        parent.add ( new UniqueNode ( "soccer" ) );
        parent.add ( new UniqueNode ( "football" ) );
        parent.add ( new UniqueNode ( "hockey" ) );
        root.add ( parent );

        parent = new UniqueNode ( "food" );
        parent.add ( new UniqueNode ( "hot dogs" ) );
        parent.add ( new UniqueNode ( "pizza" ) );
        parent.add ( new UniqueNode ( "ravioli" ) );
        parent.add ( new UniqueNode ( "bananas" ) );
        root.add ( parent );

        return new WebTreeModel<UniqueNode> ( root );
    }
}
