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

package com.alee.extended.tree.sample;

import com.alee.api.ui.TextBridge;
import com.alee.extended.tree.AsyncUniqueNode;
import com.alee.extended.tree.WebAsyncTree;
import com.alee.laf.tree.TreeNodeParameters;
import com.alee.managers.icon.Icons;

import javax.swing.*;

/**
 * Sample {@link AsyncUniqueNode} usage.
 *
 * @author Mikle Garin
 */

public class SampleNode extends AsyncUniqueNode<SampleNode, SampleObject>
        implements TextBridge<TreeNodeParameters<SampleNode, WebAsyncTree>>
{
    /**
     * Time spent to load node children.
     */
    protected long time;

    /**
     * Constructs new {@link SampleNode}.
     *
     * @param userObject node {@link Object}
     */
    public SampleNode ( final SampleObject userObject )
    {
        super ( userObject );
        this.time = 0;
    }

    /**
     * Returns time spent to load node children.
     *
     * @return time spent to load node children
     */
    public long getTime ()
    {
        return time;
    }

    /**
     * Sets time spent to load node children.
     *
     * @param time new time spent to load node children
     */
    public void setTime ( final long time )
    {
        this.time = time;
    }

    @Override
    public Icon getNodeIcon ( final TreeNodeParameters<SampleNode, WebAsyncTree> parameters )
    {
        switch ( getUserObject ().getType () )
        {
            case root:
            {
                return parameters.isExpanded () ? Icons.rootOpen : Icons.root;
            }
            case folder:
            {
                return parameters.isExpanded () ? Icons.folderOpen : Icons.folder;
            }
            case leaf:
            {
                return Icons.leaf;
            }
            default:
            {
                throw new RuntimeException ( "Unknown node type" );
            }
        }
    }

    @Override
    public String getText ( final TreeNodeParameters<SampleNode, WebAsyncTree> parameters )
    {
        return getTitle ();
    }

    /**
     * Returns node title.
     *
     * @return node title
     */
    public String getTitle ()
    {
        return getUserObject ().getTitle ();
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }

    @Override
    public SampleNode clone ()
    {
        // Cloning object and node
        final SampleObject object = getUserObject ().clone ();
        final SampleNode clone = new SampleNode ( object );

        // Copying settings
        clone.setId ( getId () );
        clone.setTime ( getTime () );

        return clone;
    }
}