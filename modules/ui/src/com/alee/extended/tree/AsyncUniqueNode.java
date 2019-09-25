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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.ui.IconBridge;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.TreeNodeParameters;
import com.alee.laf.tree.UniqueNode;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.BroadcastImageObserver;
import com.alee.utils.swing.LoadIconType;

import javax.swing.*;
import java.awt.image.ImageObserver;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Custom {@link javax.swing.tree.MutableTreeNode} implementation for {@link WebAsyncTree}.
 * In addition to {@link UniqueNode} capabilities it can provide busy state indicator icon.
 *
 * @param <N> tree node type
 * @param <T> stored object type
 * @author Mikle Garin
 */
public abstract class AsyncUniqueNode<N extends AsyncUniqueNode<N, T>, T>
        extends UniqueNode<N, T> implements IconBridge<TreeNodeParameters<N, WebAsyncTree<N>>>
{
    /**
     * todo 1. Provide an easy way to customize failed state icon
     * todo 2. Move failed icon into icon set (and make it SVG?)
     */

    /**
     * Special failed state icon.
     */
    protected static final Icon failedStateIcon = new ImageIcon ( AsyncUniqueNode.class.getResource ( "icons/failed.png" ) );

    /**
     * User failed icons cache.
     */
    protected static final Map<Icon, Icon> failedStateIcons = new WeakHashMap<Icon, Icon> ( 5 );

    /**
     * Default load icon type.
     */
    public static LoadIconType loadIconType = LoadIconType.roller;

    /**
     * Current async node state.
     */
    protected AsyncNodeState state = AsyncNodeState.waiting;

    /**
     * Children load failure cause.
     */
    protected Throwable failureCause = null;

    /**
     * Load icon observer.
     */
    protected transient ImageObserver observer = null;

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
    public AsyncUniqueNode ( final T userObject )
    {
        super ( userObject );
    }

    /**
     * Constructs AsyncUniqueNode with a custom user object and node ID.
     *
     * @param id         node ID
     * @param userObject custom user object
     */
    public AsyncUniqueNode ( final String id, final T userObject )
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

    @Nullable
    @Override
    public Icon getIcon ( @NotNull final TreeNodeParameters<N, WebAsyncTree<N>> parameters )
    {
        final Icon icon;
        if ( isLoading () )
        {
            icon = getLoadIcon ( parameters );
        }
        else
        {
            final Icon nodeIcon = getNodeIcon ( parameters );
            icon = nodeIcon != null && isFailed () ? getFailedStateIcon ( parameters, nodeIcon ) : nodeIcon;
        }
        return icon;
    }

    /**
     * Returns load {@link Icon} for this node.
     * This {@link Icon} represents node loading state.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return load {@link Icon}
     */
    public Icon getLoadIcon ( final TreeNodeParameters<N, WebAsyncTree<N>> parameters )
    {
        return loadIconType != null ? loadIconType.getIcon () : null;
    }

    /**
     * Attaches {@link ImageObserver} to the load icon of this {@link AsyncUniqueNode}.
     * todo Perform this somewhere globally for all trees?
     *
     * @param tree {@link WebAsyncTree}
     */
    public void attachLoadIconObserver ( final WebAsyncTree tree )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Proceed only if icon actually exists
        final Icon icon = getLoadIcon ( new TreeNodeParameters<N, WebAsyncTree<N>> ( tree, ( N ) this ) );
        if ( icon != null && icon instanceof ImageIcon )
        {
            final ImageIcon imageIcon = ( ImageIcon ) icon;

            // Make sure we have broadcas observer in the image icon
            // This is necessary to ensure all updates are properly preserved
            final BroadcastImageObserver broadcast;
            final ImageObserver existing = imageIcon.getImageObserver ();
            if ( existing == null )
            {
                // Creating new broadcast image observer
                broadcast = new BroadcastImageObserver ();
                imageIcon.setImageObserver ( broadcast );
            }
            else if ( existing instanceof BroadcastImageObserver )
            {
                // Using existing broadcast image observer
                broadcast = ( BroadcastImageObserver ) existing;
            }
            else
            {
                // Creating new broadcast image observer
                // Adding previously added image observer to broadcast list
                broadcast = new BroadcastImageObserver ();
                broadcast.addObserver ( existing );
                imageIcon.setImageObserver ( broadcast );
            }

            // Adding node observer
            if ( observer == null )
            {
                observer = new NodeImageObserver ( tree, this );
            }
            broadcast.addObserver ( tree, observer );
        }
    }

    /**
     * Detaches {@link ImageObserver} from the load icon of this node.
     * todo Perform this somewhere globally for all trees?
     *
     * @param tree {@link WebAsyncTree}
     */
    public void detachLoadIconObserver ( final WebAsyncTree tree )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Proceed only if icon actually exists
        final Icon icon = getLoadIcon ( new TreeNodeParameters<N, WebAsyncTree<N>> ( tree, ( N ) this ) );
        if ( icon != null && icon instanceof ImageIcon )
        {
            final ImageIcon imageIcon = ( ImageIcon ) icon;

            // Removing node observer
            // Since observer could have been changed externally we need to check it here
            final ImageObserver existing = imageIcon.getImageObserver ();
            if ( existing instanceof BroadcastImageObserver )
            {
                final BroadcastImageObserver broadcast = ( BroadcastImageObserver ) existing;
                broadcast.removeObserver ( tree, observer );
            }
        }
    }

    /**
     * Returns specific icon for this node.
     * This icon usually represents node content type or state.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return specific icon for this node
     */
    public abstract Icon getNodeIcon ( TreeNodeParameters<N, WebAsyncTree<N>> parameters );

    /**
     * Returns failed state icon for this node.
     *
     * @param parameters {@link TreeNodeParameters}
     * @param icon       node icon
     * @return failed state icon for this node
     */
    public Icon getFailedStateIcon ( final TreeNodeParameters<N, WebAsyncTree<N>> parameters, final Icon icon )
    {
        Icon failedIcon = failedStateIcons.get ( icon );
        if ( failedIcon == null )
        {
            failedIcon = ImageUtils.mergeIcons ( icon, failedStateIcon );
            failedStateIcons.put ( icon, failedIcon );
        }
        return failedIcon;
    }
}