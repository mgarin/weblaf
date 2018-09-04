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

package com.alee.demo.content.data.tree.model;

import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneBehavior;
import com.alee.api.clone.RecursiveClone;
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
        implements TextBridge<TreeNodeParameters<SampleNode, WebAsyncTree<SampleNode>>>, CloneBehavior<SampleNode>
{
    /**
     * Time spent to load node children.
     */
    protected long time;

    /**
     * Constructs new {@link SampleNode}.
     *
     * @param id    {@link SampleNode} identifier
     * @param type  {@link SampleObject} type
     * @param title {@link SampleObject} title
     */
    public SampleNode ( final String id, final SampleObjectType type, final String title )
    {
        this ( id, new SampleObject ( type, title ) );
    }

    /**
     * Constructs new {@link SampleNode}.
     *
     * @param id         {@link SampleNode} identifier
     * @param userObject node {@link Object}
     */
    public SampleNode ( final String id, final SampleObject userObject )
    {
        super ( id, userObject );
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
    public Icon getNodeIcon ( final TreeNodeParameters<SampleNode, WebAsyncTree<SampleNode>> parameters )
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
    public String getText ( final TreeNodeParameters<SampleNode, WebAsyncTree<SampleNode>> parameters )
    {
        return getTitle ();
    }

    @Override
    public SampleNode clone ( final RecursiveClone clone, final int depth )
    {
        final SampleObject userObject = clone.clone ( getUserObject (), depth + 1 );
        final SampleNode nodeCopy = new SampleNode ( getId (), userObject );
        nodeCopy.setTime ( getTime () );
        return nodeCopy;
    }

    @Override
    public SampleNode clone ()
    {
        return Clone.deep ().clone ( this );
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }
}