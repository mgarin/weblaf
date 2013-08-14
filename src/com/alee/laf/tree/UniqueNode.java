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
 * @since 1.4
 */

public class UniqueNode extends DefaultMutableTreeNode implements Serializable
{
    /**
     * Prefix for node ID.
     */
    private static final String ID_PREFIX = "UN";

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
    public UniqueNode ( Object userObject )
    {
        super ( userObject );
        setId ();
    }

    /**
     * Returns node ID and creates it if it doesn't exist.
     *
     * @return node ID
     */
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
    public void setId ( String id )
    {
        this.id = id;
    }

    /**
     * Changes node ID to new random ID.
     */
    private void setId ()
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
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
}