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
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.Serializable;

/**
 * This class provides a custom node with a specific ID.
 * This node is used in various WebLookAndFeel tree components to properly save selections and expansion states.
 * This node might also be used for some advanced cases like asynchronous tree.
 *
 * @author Mikle Garin
 */

public class UniqueNode extends DefaultMutableTreeNode implements Identifiable, Serializable
{
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
     * @param userObject custom user object
     */
    public UniqueNode ( final Object userObject )
    {
        super ( userObject );
        setId ();
    }

    /**
     * Costructs a node with a specified user object and node ID.
     *
     * @param id         node ID
     * @param userObject custom user object
     */
    public UniqueNode ( final String id, final Object userObject )
    {
        super ( userObject );
        setId ( id );
    }

    /**
     * Returns node ID and creates it if it doesn't exist.
     *
     * @return node ID
     */
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

    @Override
    public UniqueNode getParent ()
    {
        return ( UniqueNode ) super.getParent ();
    }

    /**
     * Returns TreePath for this node.
     *
     * @return TreePath for this node
     */
    public TreePath getTreePath ()
    {
        return new TreePath ( getPath () );
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