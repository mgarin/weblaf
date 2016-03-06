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

import com.alee.api.IconSupport;
import com.alee.laf.tree.UniqueNode;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Custom UniqueNode for WebAsyncTree.
 * In addition to UniqueNode it contains a loader icon and busy state indicator.
 *
 * @author Mikle Garin
 */

public abstract class AsyncUniqueNode extends UniqueNode implements IconSupport, Serializable
{
    /**
     * Special failed state icon.
     */
    protected static final Icon failedStateIcon = new ImageIcon ( AsyncUniqueNode.class.getResource ( "icons/failed.png" ) );

    /**
     * User failed icons cache.
     */
    protected static final Map<Icon, Icon> failedStateIcons = new WeakHashMap<Icon, Icon> ( 5 );

    /**
     * Default loader icon type.
     */
    public static LoaderIconType loaderIconType = LoaderIconType.roller;

    /**
     * Special separate loader icon for each tree node.
     * This is required to provide separate image observers to optimize tree repaints around the animated icon.
     */
    protected transient Icon loaderIcon = null;

    /**
     * Current async node state.
     */
    protected AsyncNodeState state = AsyncNodeState.waiting;

    /**
     * Children load failure cause.
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
     * Returns whether node children are being loaded or not.
     *
     * @return true if node children are being loaded, false otherwise
     */
    public boolean isLoading ()
    {
        return state == AsyncNodeState.loading;
    }

    /**
     * Returns whether node children are loaded or not.
     *
     * @return true if node children are loaded, false otherwise
     */
    public boolean isLoaded ()
    {
        return state == AsyncNodeState.loaded;
    }

    /**
     * Returns whether node children load failed or not.
     *
     * @return true if node children load failed, false otherwise
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
     * Returns children load failure cause.
     *
     * @return children load failure cause
     */
    public Throwable getFailureCause ()
    {
        return failureCause;
    }

    /**
     * Sets children load failure cause.
     *
     * @param failureCause children load failure cause
     */
    public void setFailureCause ( final Throwable failureCause )
    {
        this.failureCause = failureCause;
    }

    @Override
    public Icon getIcon ()
    {
        if ( isLoading () )
        {
            return getLoaderIcon ();
        }
        else
        {
            final Icon icon = getNodeIcon ();
            return icon != null && isFailed () ? getFailedStateIcon ( icon ) : icon;
        }
    }

    /**
     * Returns loader icon for this node.
     * This icon represents node loading state.
     *
     * @return loader icon
     */
    public Icon getLoaderIcon ()
    {
        if ( loaderIcon == null )
        {
            loaderIcon = createLoaderIcon ();
        }
        return loaderIcon;
    }

    /**
     * Returns loader icon for this node.
     *
     * @return loader icon for this node
     */
    public Icon createLoaderIcon ()
    {
        return loaderIconType != null && loaderIconType != LoaderIconType.none ?
                new ImageIcon ( AsyncUniqueNode.class.getResource ( "icons/" + loaderIconType + ".gif" ) ) : null;
    }

    /**
     * Returns specific icon for this node.
     * This icon usually represents node content type or state.
     *
     * @return specific icon for this node
     */
    public abstract Icon getNodeIcon ();

    /**
     * Returns failed state icon for this node.
     *
     * @param icon node icon
     * @return failed state icon for this node
     */
    public Icon getFailedStateIcon ( final Icon icon )
    {
        Icon failedIcon = failedStateIcons.get ( icon );
        if ( failedIcon == null )
        {
            failedIcon = ImageUtils.mergeIcons ( icon, failedStateIcon );
            failedStateIcons.put ( icon, failedIcon );
        }
        return failedIcon;
    }

    @Override
    public AsyncUniqueNode getParent ()
    {
        return ( AsyncUniqueNode ) super.getParent ();
    }

    @Override
    public AsyncUniqueNode getChildAt ( final int index )
    {
        return ( AsyncUniqueNode ) super.getChildAt ( index );
    }
}