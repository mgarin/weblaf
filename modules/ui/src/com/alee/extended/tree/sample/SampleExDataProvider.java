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
        return new SampleNode ( SampleNodeType.root, "Root" );
    }

    /**
     * Returns sample child nodes for specified asynchronous tree node.
     *
     * @param parent children parent node
     * @return sample child nodes for specified asynchronous tree node
     */
    @Override
    public List<SampleNode> getChildren ( final SampleNode parent )
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
                return CollectionUtils.asList ( folder1, folder2, folder3 );
            }
            case folder:
            {
                // Leaf type children
                final SampleNode leaf1 = new SampleNode ( SampleNodeType.leaf, "Leaf 1" );
                final SampleNode leaf2 = new SampleNode ( SampleNodeType.leaf, "Leaf 2" );
                final SampleNode leaf3 = new SampleNode ( SampleNodeType.leaf, "Leaf 3" );
                return CollectionUtils.asList ( leaf1, leaf2, leaf3 );
            }
            default:
            {
                // Empty children
                return Collections.EMPTY_LIST;
            }
        }
    }
}