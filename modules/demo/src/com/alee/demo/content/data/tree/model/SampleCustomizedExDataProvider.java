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

import com.alee.api.jdk.Objects;
import com.alee.extended.tree.AbstractExTreeDataProvider;
import com.alee.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Sample {@link com.alee.extended.tree.ExTreeDataProvider} implementation.
 *
 * @author Mikle Garin
 */
public class SampleCustomizedExDataProvider extends AbstractExTreeDataProvider<SampleNode>
{
    @Override
    public SampleNode getRoot ()
    {
        return new SampleNode ( "root", SampleObjectType.root, "Checkbox tree" );
    }

    @Override
    public List<SampleNode> getChildren ( final SampleNode parent )
    {
        final List<SampleNode> children;
        if ( Objects.equals ( parent.getId (), "root" ) )
        {
            children = CollectionUtils.asList (
                    new SampleNode ( "disabled", SampleObjectType.folder, "Disabled" ),
                    new SampleNode ( "hidden", SampleObjectType.folder, "Hidden" ),
                    new SampleNode ( "editable", SampleObjectType.folder, "Editable" )
            );
        }
        else if ( Objects.equals ( parent.getId (), "disabled" ) )
        {
            children = CollectionUtils.asList (
                    new SampleNode ( "disabled1", SampleObjectType.leaf, "Can't check this" ),
                    new SampleNode ( "disabled2", SampleObjectType.leaf, "And this one too" ),
                    new SampleNode ( "disabled3", SampleObjectType.leaf, "Not even this one" )
            );
        }
        else if ( Objects.equals ( parent.getId (), "hidden" ) )
        {
            children = CollectionUtils.asList (
                    new SampleNode ( "hidden1", SampleObjectType.leaf, "No check here" ),
                    new SampleNode ( "hidden2", SampleObjectType.leaf, "And for this one" ),
                    new SampleNode ( "hidden3", SampleObjectType.leaf, "They're all gone" )
            );
        }
        else if ( Objects.equals ( parent.getId (), "editable" ) )
        {
            children = CollectionUtils.asList (
                    new SampleNode ( "editable1", SampleObjectType.leaf, "Edit this node" ),
                    new SampleNode ( "editable2", SampleObjectType.leaf, "Or this one instead" ),
                    new SampleNode ( "editable3", SampleObjectType.leaf, "This one is editable too" )
            );
        }
        else
        {
            children = Collections.emptyList ();
        }
        return children;
    }
}