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

import com.alee.extended.tree.AbstractAsyncTreeDataProvider;
import com.alee.extended.tree.NodesLoadCallback;
import com.alee.utils.CollectionUtils;

import java.util.Locale;

/**
 * Sample {@link com.alee.extended.tree.AsyncTreeDataProvider} implementation.
 *
 * @author Mikle Garin
 */
public class SampleAsyncDataProvider extends AbstractAsyncTreeDataProvider<SampleNode>
{
    @Override
    public SampleNode getRoot ()
    {
        return new SampleNode ( new SampleObject ( SampleObjectType.root, "Root" ) );
    }

    @Override
    public void loadChildren ( final SampleNode parent, final NodesLoadCallback<SampleNode> listener )
    {
        if ( parent.getTitle ().toLowerCase ( Locale.ROOT ).contains ( "fail" ) )
        {
            listener.failed ( new RuntimeException ( "Sample exception cause" ) );
        }
        else
        {
            switch ( parent.getUserObject ().getType () )
            {
                case root:
                {
                    final SampleNode folder1 = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Folder 1" ) );
                    final SampleNode folder2 = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Folder 2" ) );
                    final SampleNode folder3 = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Folder 3" ) );
                    final SampleNode failFolder = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Fail folder" ) );
                    listener.completed ( CollectionUtils.asList ( folder1, folder2, folder3, failFolder ) );
                    break;
                }
                case folder:
                {
                    final SampleNode leaf1 = new SampleNode ( new SampleObject ( SampleObjectType.leaf, "Leaf 1" ) );
                    final SampleNode leaf2 = new SampleNode ( new SampleObject ( SampleObjectType.leaf, "Leaf 2" ) );
                    final SampleNode leaf3 = new SampleNode ( new SampleObject ( SampleObjectType.leaf, "Leaf 3" ) );
                    listener.completed ( CollectionUtils.asList ( leaf1, leaf2, leaf3 ) );
                    break;
                }
                default:
                {
                    listener.failed ( new RuntimeException ( "Unknown parent node type" ) );
                }
            }
        }
    }

    @Override
    public boolean isLeaf ( final SampleNode node )
    {
        return node.getUserObject ().getType ().equals ( SampleObjectType.leaf );
    }
}