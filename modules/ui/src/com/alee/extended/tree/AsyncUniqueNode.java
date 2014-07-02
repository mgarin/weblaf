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
    protected transient ImageIcon loaderIcon = null;

    /**
     * Current async node state.
     */
    protected AsyncNodeState state = AsyncNodeState.waiting;

    /**
     * Childs load failure cause.
     */
    protected Throwable failureCause = null;

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
    public AsyncUniqueNode ( final Object userObject )
    {
        super ( userObject );
    }

    /**
     * Constructs AsyncUniqueNode with a custom user object and node ID.
     *
     * @param id         node ID
     * @param userObject custom user object
     */
    public AsyncUniqueNode ( final String id, final Object userObject )
    {
        super ( id, userObject );
    }

    /**
     * Returns async node state.
     *
     * @return async node state
     */
    public AsyncNodeState getState ()
    {
        return state;
    }

    /**
     * Returns whether node is in waiting state.
     *
     * @return true if node is in waiting state, false otherwise
     */
    public boolean isWaiting ()
    {
        return state == AsyncNodeState.waiting;
    }

    /**
     * Returns whether node childs are being loaded or not.
     *
     * @return true if node childs are being loaded, false otherwise
     */
    public boolean isLoading ()
    {
        return state == AsyncNodeState.loading;
    }

    /**
     * Returns whether node childs are loaded or not.
     *
     * @return true if node childs are loaded, false otherwise
     */
    public boolean isLoaded ()
    {
        return state == AsyncNodeState.loaded;
    }

    /**
     * Returns whether node childs load failed or not.
     *
     * @return true if node childs load failed, false otherwise
     */
    public boolean isFailed ()
    {
        return state == AsyncNodeState.failed;
    }

    /**
     * Sets async node state.
     * Do not change this value on your own since that might break the tree.
     *
     * @param state new async node state
     */
    public void setState ( final AsyncNodeState state )
    {
        this.state = state;
    }

    /**
     * Returns childs load failure cause.
     *
     * @return childs load failure cause
     */
    public Throwable getFailureCause ()
    {
        return failureCause;
    }

    /**
     * Sets childs load failure cause.
     *
     * @param failureCause childs load failure cause
     */
    public void setFailureCause ( final Throwable failureCause )
    {
        this.failureCause = failureCause;
    }

    /**
     * Returns loader icon for this node.
     *
     * @return loader icon
     */
    public ImageIcon getLoaderIcon ()
    {
        if ( loaderIcon == null )
        {
            loaderIcon = createLoaderIcon ();
        }
        return loaderIcon;
    }

    /**
     * Returns newly created loader icon for this node.
     *
     * @return loader icon
     */
    protected ImageIcon createLoaderIcon ()
    {
        return WebAsyncTreeStyle.loaderIconType.equals ( LoaderIconType.none ) ? null :
                new ImageIcon ( AsyncUniqueNode.class.getResource ( "icons/" + WebAsyncTreeStyle.loaderIconType + ".gif" ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsyncUniqueNode getParent ()
    {
        return ( AsyncUniqueNode ) super.getParent ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsyncUniqueNode getChildAt ( final int index )
    {
        return ( AsyncUniqueNode ) super.getChildAt ( index );
    }
}