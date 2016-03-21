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

import com.alee.extended.tree.WebCheckBoxTree;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * Custom UI for JTree component.
 *
 * @author Mikle Garin
 */

public class WebTreeUI extends BasicTreeUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Expand and collapse control icons.
     */
    public static ImageIcon EXPAND_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/expand.png" ) );
    public static ImageIcon COLLAPSE_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/collapse.png" ) );
    public static ImageIcon DISABLED_EXPAND_ICON = ImageUtils.createDisabledCopy ( EXPAND_ICON );
    public static ImageIcon DISABLED_COLLAPSE_ICON = ImageUtils.createDisabledCopy ( COLLAPSE_ICON );

    /**
     * Default node icons.
     */
    public static ImageIcon ROOT_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/root.png" ) );
    public static ImageIcon CLOSED_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/closed.png" ) );
    public static ImageIcon OPEN_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/open.png" ) );
    public static ImageIcon LEAF_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/leaf.png" ) );

    /**
     * Style settings.
     */
    protected TreeSelectionStyle selectionStyle;
    protected boolean expandSelected;
    protected boolean selectOnHover;

    /**
     * Component painter.
     */
    @DefaultPainter ( TreePainter.class )
    protected ITreePainter painter;

    /**
     * Listeners.
     */
    protected TreePathHoverBehavior hoverNodeTracker;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected int hoverRow = -1;

    /**
     * Returns an instance of the WebTreeUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTreeUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTreeUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // todo Probably completely remove this?
        // Overwrite indent in case WebLookAndFeel is not installed as L&F
        if ( !WebLookAndFeel.isInstalled () )
        {
            setRightChildIndent ( 12 );
            setLeftChildIndent ( 12 );
        }

        // Allow each cell to choose its own preferred height
        tree.setRowHeight ( -1 );

        // Modifying default drop mode
        // USE_SELECTION mode is not preferred since WebLaF provides a better visual drop representation
        tree.setDropMode ( DropMode.ON );

        // Use a moderate amount of visible rows by default
        // BasicTreeUI uses 20 rows by default which is too much for most of cases
        tree.setVisibleRowCount ( 10 );

        // Forces tree to save changes when another tree node is selected instead of cancelling them
        tree.setInvokesStopCellEditing ( true );

        // Hover behavior
        hoverNodeTracker = new TreePathHoverBehavior ( tree, true )
        {
            @Override
            public void hoverChanged ( final TreePath previous, final TreePath current )
            {
                // Updating hover row
                final int previousRow = hoverRow;
                hoverRow = current != null ? tree.getRowForPath ( current ) : -1;

                // Updating selection
                if ( selectOnHover )
                {
                    if ( current != null )
                    {
                        tree.setSelectionPath ( current );
                    }
                    else
                    {
                        tree.clearSelection ();
                    }
                }

                // Repainting nodes according to hover changes
                // This occurs only if hover highlight is enabled
                if ( painter != null && painter.isHoverDecorationSupported () )
                {
                    repaintRow ( previousRow );
                    repaintRow ( hoverRow );
                }

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.hoverCellChanged ( tree, previousRow, 0, hoverRow, 0 );
                }

                // Informing {@link com.alee.laf.tree.WebTree} about hover node change
                // This is performed here to avoid excessive listeners usage for the same purpose
                if ( tree instanceof WebTree )
                {
                    final DefaultMutableTreeNode p = previous != null ? ( DefaultMutableTreeNode ) previous.getLastPathComponent () : null;
                    final DefaultMutableTreeNode c = current != null ? ( DefaultMutableTreeNode ) current.getLastPathComponent () : null;
                    ( ( WebTree ) tree ).fireHoverChanged ( p, c );
                }
            }

            /**
             * Repaints specified row if it exists and it is visible.
             *
             * @param row row to repaint
             */
            private void repaintRow ( final int row )
            {
                if ( row != -1 )
                {
                    final Rectangle rowBounds = getFullRowBounds ( row );
                    if ( rowBounds != null )
                    {
                        tree.repaint ( rowBounds );
                    }
                }
            }
        };
        hoverNodeTracker.install ();

        // Applying skin
        StyleManager.installSkin ( tree );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( tree );

        // Removing custom listeners
        hoverNodeTracker.uninstall ();
        hoverNodeTracker = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( tree );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( tree, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( tree, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns tree painter.
     *
     * @return tree painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets tree painter.
     * Pass null to remove tree painter.
     *
     * @param painter new tree painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( tree, new DataRunnable<ITreePainter> ()
        {
            @Override
            public void run ( final ITreePainter newPainter )
            {
                WebTreeUI.this.painter = newPainter;
            }
        }, this.painter, painter, ITreePainter.class, AdaptiveTreePainter.class );
    }

    /**
     * Returns current hover row.
     *
     * @return current hover row
     */
    public int getHoverRow ()
    {
        return hoverRow;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.prepareToPaint ( drawingCache, currentCellRenderer );
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    //    /**
    //     * Sets tree selection shade width.
    //     *
    //     * @param shadeWidth tree selection shade width
    //     */
    //    public void setSelectionShadeWidth ( final int shadeWidth )
    //    {
    //        if ( this.selectionShadeWidth != shadeWidth )
    //        {
    //            // Saving new selection shade width
    //            this.selectionShadeWidth = shadeWidth;
    //
    //            // Properly updating the whole tree structure since this value might affect renderer size
    //            TreeUtils.updateAllVisibleNodes ( tree );
    //        }
    //    }

    /**
     * Returns row index for specified point on the tree.
     * This method takes selection style into account.
     *
     * @param point point on the tree
     * @return row index for specified point on the tree
     */
    public int getRowForPoint ( final Point point )
    {
        return getRowForPoint ( point, isFullLineSelection () );
    }

    /**
     * Returns row index for specified point on the tree.
     *
     * @param point        point on the tree
     * @param countFullRow whether take the whole row into account or just node renderer rect
     * @return row index for specified point on the tree
     */
    public int getRowForPoint ( final Point point, final boolean countFullRow )
    {
        if ( tree != null )
        {
            final Enumeration<TreePath> visiblePaths = getVisiblePaths ();
            if ( visiblePaths != null )
            {
                while ( visiblePaths.hasMoreElements () )
                {
                    final TreePath treePath = visiblePaths.nextElement ();
                    final Rectangle bounds = getFullPathBounds ( treePath, countFullRow );
                    if ( bounds != null && bounds.contains ( point ) )
                    {
                        return getRowForPath ( tree, treePath );
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Returns row bounds by its index.
     * This method takes selection style into account.
     *
     * @param row row index
     * @return row bounds by its index
     */
    public Rectangle getRowBounds ( final int row )
    {
        return getRowBounds ( row, isFullLineSelection () );
    }

    /**
     * Returns row bounds by its index.
     *
     * @param row          row index
     * @param countFullRow whether take the whole row into account or just node renderer rect
     * @return row bounds by its index
     */
    public Rectangle getRowBounds ( final int row, final boolean countFullRow )
    {
        return countFullRow ? getFullRowBounds ( row ) : getPathBounds ( tree, getPathForRow ( tree, row ) );
    }

    /**
     * Returns full row bounds by its index.
     *
     * @param row row index
     * @return full row bounds by its index
     */
    public Rectangle getFullRowBounds ( final int row )
    {
        return getFullPathBounds ( getPathForRow ( tree, row ) );
    }

    /**
     * Returns full path bounds.
     *
     * @param path         tree path
     * @param countFullRow whether take the whole row into account or just node renderer rect
     * @return full path bounds
     */
    public Rectangle getFullPathBounds ( final TreePath path, final boolean countFullRow )
    {
        return countFullRow ? getFullPathBounds ( path ) : getPathBounds ( tree, path );
    }

    /**
     * Returns full path bounds.
     *
     * @param path tree path
     * @return full path bounds
     */
    private Rectangle getFullPathBounds ( final TreePath path )
    {
        final Rectangle b = getPathBounds ( tree, path );
        if ( b != null )
        {
            final Insets insets = tree.getInsets ();
            b.x = insets.left;
            b.width = tree.getWidth () - insets.left - insets.right;
        }
        return b;
    }

    /**
     * Returns visible paths enumeration.
     * This is just a small method for convenient enumeration retrieval.
     *
     * @return visible paths enumeration
     */
    public Enumeration<TreePath> getVisiblePaths ()
    {
        if ( tree.isShowing () )
        {
            final Rectangle paintBounds = tree.getVisibleRect ();
            final TreePath initialPath = getClosestPathForLocation ( tree, 0, paintBounds.y );
            if ( initialPath != null )
            {
                return treeState.getVisiblePathsFrom ( initialPath );
            }
        }
        return null;
    }

    /**
     * Returns default tree cell editor.
     *
     * @return default tree cell editor
     */
    @Override
    protected TreeCellEditor createDefaultCellEditor ()
    {
        return new WebTreeCellEditor ();
    }

    /**
     * Returns default tree cell renderer.
     *
     * @return default tree cell renderer
     */
    @Override
    protected TreeCellRenderer createDefaultCellRenderer ()
    {
        return new WebTreeCellRenderer ();
    }

    /**
     * Selects tree path for the specified event.
     *
     * @param path tree path to select
     * @param e    event to process
     */
    @Override
    protected void selectPathForEvent ( final TreePath path, final MouseEvent e )
    {
        if ( !isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
        {
            super.selectPathForEvent ( path, e );
        }
    }

    /**
     * Returns whether location is in the checkbox tree checkbox control or not.
     * This method is only used when this UI is applied to {@link com.alee.extended.tree.WebCheckBoxTree}.
     * todo Separate UI for WebCheckBoxTree
     *
     * @param path tree path
     * @param x    location X coordinate
     * @param y    location Y coordinate
     * @return true if location is in the checkbox tree checkbox control, false otherwise
     */
    public boolean isLocationInCheckBoxControl ( final TreePath path, final int x, final int y )
    {
        if ( tree instanceof WebCheckBoxTree )
        {
            final WebCheckBoxTree checkBoxTree = ( WebCheckBoxTree ) tree;
            if ( checkBoxTree.isCheckingByUserEnabled () )
            {
                final DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent ();
                if ( checkBoxTree.isCheckBoxVisible ( node ) && checkBoxTree.isCheckBoxEnabled ( node ) )
                {
                    final Rectangle checkBoxBounds = checkBoxTree.getCheckBoxBounds ( path );
                    return checkBoxBounds != null && checkBoxBounds.contains ( x, y );
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    protected ToolTipProvider<? extends WebTree> getToolTipProvider ()
    {
        return tree instanceof WebTree ? ( ( WebTree ) tree ).getToolTipProvider () : null;
    }

    /**
     * Returns tree structure expanded node icon.
     *
     * @return tree structure expanded node icon
     */
    @Override
    public Icon getExpandedIcon ()
    {
        return tree.isEnabled () ? COLLAPSE_ICON : DISABLED_COLLAPSE_ICON;
    }

    /**
     * Returns tree structure collapsed node icon.
     *
     * @return tree structure collapsed node icon
     */
    @Override
    public Icon getCollapsedIcon ()
    {
        return tree.isEnabled () ? EXPAND_ICON : DISABLED_EXPAND_ICON;
    }

    /**
     * Returns tree cell renderer pane.
     *
     * @return tree cell renderer pane
     */
    public CellRendererPane getCellRendererPane ()
    {
        return rendererPane;
    }

    /**
     * Returns state of tree.
     *
     * @return state of tree
     */
    public AbstractLayoutCache getTreeState ()
    {
        return treeState;
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public TreeSelectionStyle getSelectionStyle ()
    {
        return selectionStyle;
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( final TreeSelectionStyle style )
    {
        this.selectionStyle = style;
    }

    /**
     * Returns whether tree should expand nodes on selection or not.
     *
     * @return true if tree should expand nodes on selection, false otherwise
     */
    public boolean isExpandSelected ()
    {
        return expandSelected;
    }

    /**
     * Sets whether tree should expand nodes on selection or not.
     *
     * @param expandSelected whether tree should expand nodes on selection or not
     */
    public void setExpandSelected ( final boolean expandSelected )
    {
        this.expandSelected = expandSelected;
    }

    /**
     * Returns whether or not nodes should be selected on hover.
     *
     * @return true if nodes should be selected on hover, false otherwise
     */
    public boolean isSelectOnHover ()
    {
        return selectOnHover;
    }

    /**
     * Sets whether or not nodes should be selected on hover.
     *
     * @param select whether or not nodes should be selected on hover
     */
    public void setSelectOnHover ( final boolean select )
    {
        this.selectOnHover = select;
    }

    /**
     * Returns whether tree selection style points that the whole line is a single cell or not.
     *
     * @return true if tree selection style points that the whole line is a single cell, false otherwise
     */
    protected boolean isFullLineSelection ()
    {
        return selectionStyle == TreeSelectionStyle.line;
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}