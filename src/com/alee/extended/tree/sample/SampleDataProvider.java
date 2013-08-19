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

import com.alee.extended.tree.AbstractTreeDataProvider;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.ThreadUtils;

import java.util.List;

/**
 * Sample asynchronous tree data provider.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class SampleDataProvider extends AbstractTreeDataProvider<SampleNode>
{
    /**
     * Returns asynchronous tree sample root node.
     *
     * @return root node
     */
    public SampleNode getRoot ()
    {
        // Custom root node
        return new SampleNode ( "Root", SampleNodeType.root );
    }

    /**
     * Returns sample child nodes for specified asynchronous tree node.
     *
     * @param parent childs parent node
     * @return list of child nodes
     */
    public List<SampleNode> getChilds ( SampleNode parent )
    {
        // Sample loading delay to see the loader in progress
        parent.setTime ( 0 );
        final int time = MathUtils.random ( 100, 3000 );
        ThreadUtils.sleepSafely ( time );
        parent.setTime ( time );

        // Sample childs
        switch ( parent.getType () )
        {
            case root:
            {
                // Folder type childs
                final SampleNode folder1 = new SampleNode ( "Folder 1", SampleNodeType.folder );
                final SampleNode folder2 = new SampleNode ( "Folder 2", SampleNodeType.folder );
                final SampleNode folder3 = new SampleNode ( "Folder 3", SampleNodeType.folder );
                return CollectionUtils.copy ( folder1, folder2, folder3 );
            }
            case folder:
            {
                // Leaf type childs
                final SampleNode leaf1 = new SampleNode ( "Leaf 1", SampleNodeType.leaf );
                final SampleNode leaf2 = new SampleNode ( "Leaf 2", SampleNodeType.leaf );
                final SampleNode leaf3 = new SampleNode ( "Leaf 3", SampleNodeType.leaf );
                return CollectionUtils.copy ( leaf1, leaf2, leaf3 );
            }
        }

        // You can return either null or empty list if there are no childs
        return null;
    }

    /**
     * Returns whether the specified sample node is leaf or not.
     *
     * @param node node
     * @return true if the specified node is leaf, false otherwise
     */
    public boolean isLeaf ( SampleNode node )
    {
        // Simply check the node type to determine if it is leaf or not
        return node.getType ().equals ( SampleNodeType.leaf );
    }
}