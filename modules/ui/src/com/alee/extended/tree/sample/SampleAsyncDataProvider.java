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
import com.alee.extended.tree.ChildrenListener;
import com.alee.utils.CollectionUtils;

import java.util.Locale;

/**
 * Sample asynchronous tree data provider.
 *
 * @author Mikle Garin
 */

public class SampleAsyncDataProvider extends AbstractAsyncTreeDataProvider<SampleNode>
{
    /**
     * Returns asynchronous tree sample root node.
     *
     * @return root node
     */
    @Override
    public SampleNode getRoot ()
    {
        return new SampleNode ( SampleNodeType.root, "Root" );
    }

    /**
     * Returns sample child nodes for specified asynchronous tree node.
     *
     * @param parent   children parent node
     * @param listener children loading progress listener
     */
    @Override
    public void loadChildren ( final SampleNode parent, final ChildrenListener<SampleNode> listener )
    {
        // Sample loading delay to see the loader in progress
        //        parent.setTime ( 0 );
        //        final int time = MathUtils.random ( 100, 2000 );
        //        ThreadUtils.sleepSafely ( time );
        //        parent.setTime ( time );

        if ( parent.getTitle ().toLowerCase ( Locale.ROOT ).contains ( "fail" ) )
        {
            // Sample load fail
            listener.loadFailed ( new RuntimeException ( "Sample exception cause" ) );
        }
        else
        {
            // Sample children
            switch ( parent.getType () )
            {
                case root:
                {
                    // Folder type children
                    final SampleNode folder1 = new SampleNode ( SampleNodeType.folder, "Folder 1" );
                    final SampleNode folder2 = new SampleNode ( SampleNodeType.folder, "Folder 2" );
                    final SampleNode folder3 = new SampleNode ( SampleNodeType.folder, "Folder 3" );
                    final SampleNode failFolder = new SampleNode ( SampleNodeType.folder, "Fail folder" );
                    listener.loadCompleted ( CollectionUtils.asList ( folder1, folder2, folder3, failFolder ) );
                    break;
                }
                case folder:
                {
                    // Leaf type children
                    final SampleNode leaf1 = new SampleNode ( SampleNodeType.leaf, "Leaf 1" );
                    final SampleNode leaf2 = new SampleNode ( SampleNodeType.leaf, "Leaf 2" );
                    final SampleNode leaf3 = new SampleNode ( SampleNodeType.leaf, "Leaf 3" );
                    listener.loadCompleted ( CollectionUtils.asList ( leaf1, leaf2, leaf3 ) );
                    break;
                }
            }
        }
    }

    /**
     * Returns whether the specified sample node is leaf or not.
     * Simply checks the node type to determine if it is leaf or not.
     *
     * @param node node
     * @return true if the specified node is leaf, false otherwise
     */
    @Override
    public boolean isLeaf ( final SampleNode node )
    {
        return node.getType ().equals ( SampleNodeType.leaf );
    }
}