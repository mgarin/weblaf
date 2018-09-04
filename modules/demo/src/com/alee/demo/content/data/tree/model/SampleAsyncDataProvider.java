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

import com.alee.extended.tree.AbstractAsyncTreeDataProvider;
import com.alee.extended.tree.NodesLoadCallback;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.ThreadUtils;

/**
 * Sample {@link com.alee.extended.tree.AsyncTreeDataProvider} implementation.
 * Adds extra loading time to showcase asynchronous data loading.
 *
 * @author Mikle Garin
 */
public class SampleAsyncDataProvider extends AbstractAsyncTreeDataProvider<SampleNode>
{
    @Override
    public SampleNode getRoot ()
    {
        return new SampleNode ( "root", SampleObjectType.root, "Root" );
    }

    @Override
    public void loadChildren ( final SampleNode parent, final NodesLoadCallback<SampleNode> listener )
    {
        // Excluding root node children to avoid awkward demo startup
        if ( parent.getUserObject ().getType () != SampleObjectType.root )
        {
            // Stalling the thread here won't hurt the UI since it is always executed on non-EDT thread
            ThreadUtils.sleepSafely ( MathUtils.random ( 100, 2000 ) );
        }

        // Loading actual children
        if ( !parent.getId ().equals ( "failFolder" ) )
        {
            switch ( parent.getUserObject ().getType () )
            {
                case root:
                {
                    listener.completed ( CollectionUtils.asList (
                            new SampleNode ( "folder1", SampleObjectType.folder, "Folder 1" ),
                            new SampleNode ( "folder2", SampleObjectType.folder, "Folder 2" ),
                            new SampleNode ( "folder3", SampleObjectType.folder, "Folder 3" ),
                            new SampleNode ( "failFolder", SampleObjectType.folder, "Fail folder" )
                    ) );
                    break;
                }
                case folder:
                {
                    listener.completed ( CollectionUtils.asList (
                            new SampleNode ( parent.getId () + ".leaf1", SampleObjectType.leaf, "Leaf 1" ),
                            new SampleNode ( parent.getId () + ".leaf2", SampleObjectType.leaf, "Leaf 2" ),
                            new SampleNode ( parent.getId () + ".leaf3", SampleObjectType.leaf, "Leaf 3" )
                    ) );
                    break;
                }
                default:
                {
                    listener.failed ( new RuntimeException ( "Unknown parent node type" ) );
                    break;
                }
            }
        }
        else
        {
            listener.failed ( new RuntimeException ( "Sample exception cause" ) );
        }
    }

    @Override
    public boolean isLeaf ( final SampleNode node )
    {
        return node.getUserObject ().getType ().equals ( SampleObjectType.leaf );
    }
}