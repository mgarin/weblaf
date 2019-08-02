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

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;

/**
 * Custom {@link javax.swing.tree.MutableTreeNode} implementation for {@link com.alee.extended.tree.WebExTree}.
 * This node always contains an identifier unique within its tree component model.
 *
 * @param <N> tree node type
 * @param <T> stored object type
 * @author Mikle Garin
 */
public class UniqueNode<N extends UniqueNode<N, T>, T> extends WebTreeNode<N, T> implements Identifiable
{
    /**
     * todo 1. Make a better identifier initialization and ensure it doesn't break things
     */

    /**
     * Prefix for node ID.
     */
    protected static final String ID_PREFIX = "UN";

    /**
     * Unique node ID.
     */
    protected String id;

    /**
     * Costructs a simple node.
     */
    public UniqueNode ()
    {
        super ();
        setId ();
    }

    /**
     * Costructs a node with a specified user object.
     *
     * @param userObject optional node {@link Object}
     */
    public UniqueNode ( final T userObject )
    {
        super ( userObject );
        setId ();
    }

    /**
     * Costructs a node with a specified user object and node ID.
     *
     * @param id         unique node identifier
     * @param userObject optional node {@link Object}
     */
    public UniqueNode ( final String id, final T userObject )
    {
        super ( userObject );
        setId ( id );
    }

    /**
     * Returns node ID and creates it if it doesn't exist.
     *
     * @return node ID
     */
    @NotNull
    @Override
    public String getId ()
    {
        if ( id == null )
        {
            setId ();
        }
        return id;
    }

    /**
     * Changes node ID.
     *
     * @param id new node ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Changes node ID to new random ID.
     */
    protected void setId ()
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
    }

    /**
     * Returns text node representation.
     *
     * @return text node representation
     */
    @Override
    public String toString ()
    {
        return userObject != null && userObject != this ? userObject.toString () : ReflectUtils.getClassName ( this.getClass () );
    }
}