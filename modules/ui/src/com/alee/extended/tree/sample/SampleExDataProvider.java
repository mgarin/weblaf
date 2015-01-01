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

import com.alee.extended.tree.AbstractExTreeDataProvider;
import com.alee.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class SampleExDataProvider extends AbstractExTreeDataProvider<SampleNode>
{
    /**
     * Returns asynchronous tree sample root node.
     *
     * @return root node
     */
    @Override
    public SampleNode getRoot ()
    {
        return new SampleNode ( "Root", SampleNodeType.root );
    }

    /**
     * Returns sample child nodes for specified asynchronous tree node.
     *
     * @param parent childs parent node
     * @return sample child nodes for specified asynchronous tree node
     */
    @Override
    public List<SampleNode> getChilds ( final SampleNode parent )
    {
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
            default:
            {
                // Empty childs
                return Collections.EMPTY_LIST;
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