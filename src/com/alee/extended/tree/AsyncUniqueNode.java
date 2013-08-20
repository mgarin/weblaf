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

import com.alee.laf.tree.UniqueNode;

import javax.swing.*;
import java.io.Serializable;

/**
 * Custom UniqueNode for WebAsyncTree.
 * In addition to UniqueNode it contains a loader icon and busy state indicator.
 *
 * @author Mikle Garin
 */

public abstract class AsyncUniqueNode extends UniqueNode implements Serializable
{
    /**
     * Special separate loader icon for each tree node.
     * This is required to provide separate image observers to optimize tree repaints around the animated icon.
     */
    private transient ImageIcon loaderIcon = createLoaderIcon ();

    /**
     * Represents busy state of the node.
     * Node is busy when its childs are in process of loading from some kind of source.
     */
    private boolean busy = false;

    /**
     * Costructs default node.
     */
    public AsyncUniqueNode ()
    {
        super ();
    }

    /**
     * Constructs AsyncUniqueNode with a custom user object.
     *
     * @param userObject custom user object
     */
    public AsyncUniqueNode ( Object userObject )
    {
        super ( userObject );
    }

    /**
     * Returns whether node is busy or not.
     *
     * @return true if node is busy, false otherwise
     */
    public boolean isBusy ()
    {
        return busy;
    }

    /**
     * Changes node busy state.
     * Do not change this value on your own since that might break the tree.
     *
     * @param busy new node busy state
     */
    public void setBusy ( boolean busy )
    {
        this.busy = busy;
    }

    /**
     * Returns loader icon for this node.
     *
     * @return loader icon
     */
    public ImageIcon getLoaderIcon ()
    {
        return loaderIcon;
    }

    /**
     * Returns newly created loader icon for this node.
     *
     * @return loader icon
     */
    private ImageIcon createLoaderIcon ()
    {
        return WebAsyncTreeStyle.loaderIconType.equals ( LoaderIconType.none ) ? null :
                new ImageIcon ( AsyncUniqueNode.class.getResource ( "icons/" + WebAsyncTreeStyle.loaderIconType + ".gif" ) );
    }

    /**
     * {@inheritDoc}
     */
    public AsyncUniqueNode getParent ()
    {
        return ( AsyncUniqueNode ) super.getParent ();
    }

    /**
     * {@inheritDoc}
     */
    public AsyncUniqueNode getChildAt ( int index )
    {
        return ( AsyncUniqueNode ) super.getChildAt ( index );
    }
}