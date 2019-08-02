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

package com.alee.managers.drag.view;

import com.alee.extended.tree.WebAsyncTree;
import com.alee.extended.tree.WebExTree;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract {@link DragViewHandler} implementation for tree nodes.
 * It can provide visual feedback for multiple tree nodes drag operation.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public abstract class NodesDragViewHandler<N extends MutableTreeNode> implements DragViewHandler<List<N>>
{
    /**
     * Returns maximum amount of nodes displayed when dragged.
     * If there are more nodes than limit allows additional label will be added informing about it.
     *
     * @return maximum amount of nodes displayed when dragged
     */
    public abstract int getNodesViewLimit ();

    /**
     * Retturns additional X offset for "more" text.
     * Might be useful to move this text according to node icons.
     *
     * @return additional X offset for "more" text
     */
    public Insets getMoreTextMargin ()
    {
        return new Insets ( 0, 0, 0, 0 );
    }

    @Override
    public BufferedImage getView ( final List<N> nodes, final DragSourceDragEvent dragSourceDragEvent )
    {
        if ( dragSourceDragEvent.getSource () instanceof DragSourceContext )
        {
            final DragSourceContext dsc = ( DragSourceContext ) dragSourceDragEvent.getSource ();
            if ( dsc.getComponent () instanceof WebTree )
            {
                final WebTree tree = ( WebTree ) dsc.getComponent ();
                final List<N> realNodes;
                if ( nodes.get ( 0 ) instanceof UniqueNode )
                {
                    final List<UniqueNode> uniqueNodes = ( List<UniqueNode> ) nodes;
                    if ( tree instanceof WebExTree )
                    {
                        final WebExTree exTree = ( WebExTree ) tree;
                        realNodes = new ArrayList<N> ();
                        for ( final UniqueNode node : uniqueNodes )
                        {
                            realNodes.add ( ( N ) exTree.findNode ( node.getId () ) );
                        }
                    }
                    else if ( tree instanceof WebExTree )
                    {
                        final WebAsyncTree exTree = ( WebAsyncTree ) tree;
                        realNodes = new ArrayList<N> ();
                        for ( final UniqueNode node : uniqueNodes )
                        {
                            realNodes.add ( ( N ) exTree.findNode ( node.getId () ) );
                        }
                    }
                    else
                    {
                        realNodes = nodes;
                    }
                }
                else
                {
                    realNodes = nodes;
                }

                final FontMetrics fm = tree.getFontMetrics ( tree.getFont () );
                final int fmh = fm.getHeight ();

                final int limit = getNodesViewLimit ();
                final int amount = realNodes.size () - limit;
                final String text = amount > 1 ? "And %s more elements" : "And one more element";
                final String moreText = limit > 0 ? String.format ( text, amount ) : null;
                final Insets moreTextOffset = getMoreTextMargin ();

                int width = 0;
                int height = 0;
                int count = 0;
                for ( final N node : realNodes )
                {
                    if ( limit <= 0 || limit > count )
                    {
                        final Rectangle bounds = tree.getNodeBounds ( node );
                        width = Math.max ( bounds.width, width );
                        height += bounds.height;
                        count++;
                    }
                    else
                    {
                        width = Math.max ( moreTextOffset.left + fm.stringWidth ( moreText ) + moreTextOffset.right, width );
                        height += moreTextOffset.top + fmh + moreTextOffset.bottom;
                        break;
                    }
                }

                final BufferedImage image = ImageUtils.createCompatibleImage ( width, height, BufferedImage.TRANSLUCENT );
                final Graphics2D g2d = image.createGraphics ();
                int y = 0;
                count = 0;
                for ( final N node : realNodes )
                {
                    if ( limit <= 0 || limit > count )
                    {
                        final int row = tree.getRowForNode ( node );
                        final boolean exp = tree.isExpanded ( node );
                        final boolean leaf = tree.getModel ().isLeaf ( node );
                        final Component r =
                                tree.getCellRenderer ().getTreeCellRendererComponent ( tree, node, false, exp, leaf, row, false );
                        final Dimension ps = r.getPreferredSize ();
                        tree.getUI ().getCellRendererPane ().paintComponent ( g2d, r, null, 0, y, ps.width, ps.height );
                        y += ps.height;
                        count++;
                    }
                    else
                    {
                        SwingUtils.setupTextAntialias ( g2d );
                        g2d.setPaint ( Color.BLACK );
                        g2d.drawString ( moreText, moreTextOffset.left,
                                y + moreTextOffset.top + fmh / 2 + LafUtils.getTextCenterShiftY ( fm ) );
                        break;
                    }
                }
                g2d.dispose ();
                return image;
            }
        }
        return null;
    }

    @Override
    public Point getViewRelativeLocation ( final List<N> nodes, final DragSourceDragEvent dragSourceDragEvent, final BufferedImage view )
    {
        return new Point ( 25, 5 );
    }

    @Override
    public void dragEnded ( final List<N> nodes, final DragSourceDropEvent event )
    {
        /**
         * Don't need to do anything on drag end.
         */
    }
}