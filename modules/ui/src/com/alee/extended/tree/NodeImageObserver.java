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

import com.alee.utils.CoreSwingUtils;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Custom image observer for {@link WebAsyncTree} loader icons.
 *
 * @author Mikle Garin
 */
public class NodeImageObserver implements ImageObserver
{
    /**
     * {@link WebAsyncTree}.
     */
    protected WebAsyncTree tree;

    /**
     * Observed {@link AsyncUniqueNode}.
     */
    protected AsyncUniqueNode node;

    /**
     * Constructs default node observer.
     *
     * @param tree updated tree
     * @param node observed node
     */
    public NodeImageObserver ( final WebAsyncTree tree, final AsyncUniqueNode node )
    {
        this.tree = tree;
        this.node = node;
    }

    /**
     * Updates loader icon view in tree cell.
     *
     * @param img   image being observed
     * @param flags bitwise inclusive OR of flags
     * @param x     x coordinate
     * @param y     y coordinate
     * @param w     width
     * @param h     height
     * @return {@code false} if the infoflags indicate that the image is completely loaded, {@code true} otherwise
     */
    @Override
    public boolean imageUpdate ( final Image img, final int flags, final int x, final int y, final int w, final int h )
    {
        if ( node.isLoading () && ( flags & ( FRAMEBITS | ALLBITS ) ) != 0 )
        {
            // Extra layer of safety as tree path bounds retrieval might cause issues outside of EDT
            // It can be the case since it asks renderer which might perform some internal UI updates on return
            CoreSwingUtils.invokeLater ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    final Rectangle rect = tree.getPathBounds ( node.getTreePath () );
                    if ( rect != null )
                    {
                        tree.repaint ( rect );
                    }
                }
            } );
        }
        return ( flags & ( ALLBITS | ABORT ) ) == 0;
    }
}