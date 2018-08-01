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
 * Sample {@link com.alee.extended.tree.ExTreeDataProvider} implementation.
 *
 * @author Mikle Garin
 */
public class SampleExDataProvider extends AbstractExTreeDataProvider<SampleNode>
{
    @Override
    public SampleNode getRoot ()
    {
        return new SampleNode ( new SampleObject ( SampleObjectType.root, "Root" ) );
    }

    @Override
    public List<SampleNode> getChildren ( final SampleNode parent )
    {
        switch ( parent.getUserObject ().getType () )
        {
            case root:
            {
                final SampleNode folder1 = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Folder 1" ) );
                final SampleNode folder2 = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Folder 2" ) );
                final SampleNode folder3 = new SampleNode ( new SampleObject ( SampleObjectType.folder, "Folder 3" ) );
                return CollectionUtils.asList ( folder1, folder2, folder3 );
            }
            case folder:
            {
                final SampleNode leaf1 = new SampleNode ( new SampleObject ( SampleObjectType.leaf, "Leaf 1" ) );
                final SampleNode leaf2 = new SampleNode ( new SampleObject ( SampleObjectType.leaf, "Leaf 2" ) );
                final SampleNode leaf3 = new SampleNode ( new SampleObject ( SampleObjectType.leaf, "Leaf 3" ) );
                return CollectionUtils.asList ( leaf1, leaf2, leaf3 );
            }
            default:
            {
                return Collections.emptyList ();
            }
        }
    }
}