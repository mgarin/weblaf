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
        return new SampleNode ( "root", SampleObjectType.root, "Root" );
    }

    @Override
    public List<SampleNode> getChildren ( final SampleNode parent )
    {
        final List<SampleNode> children;
        switch ( parent.getUserObject ().getType () )
        {
            case root:
            {
                children = CollectionUtils.asList (
                        new SampleNode ( "folder1", SampleObjectType.folder, "Folder 1" ),
                        new SampleNode ( "folder2", SampleObjectType.folder, "Folder 2" ),
                        new SampleNode ( "folder3", SampleObjectType.folder, "Folder 3" )
                );
                break;
            }
            case folder:
            {
                children = CollectionUtils.asList (
                        new SampleNode ( parent.getId () + ".leaf1", SampleObjectType.leaf, "Leaf 1" ),
                        new SampleNode ( parent.getId () + ".leaf2", SampleObjectType.leaf, "Leaf 2" ),
                        new SampleNode ( parent.getId () + ".leaf3", SampleObjectType.leaf, "Leaf 3" )
                );
                break;
            }
            default:
            {
                children = Collections.emptyList ();
                break;
            }
        }
        return children;
    }
}