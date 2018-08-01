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

package com.alee.demo.frames.examples;

import com.alee.demo.api.example.ExampleElement;
import com.alee.demo.api.example.ExampleGroup;
import com.alee.demo.content.ExamplesManager;
import com.alee.extended.tree.AbstractExTreeDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikle Garin
 */
public final class ExamplesTreeDataProvider extends AbstractExTreeDataProvider<ExamplesTreeNode>
{
    @Override
    public ExamplesTreeNode getRoot ()
    {
        return new ExamplesTreeNode ();
    }

    @Override
    public List<ExamplesTreeNode> getChildren ( final ExamplesTreeNode parent )
    {
        switch ( parent.getType () )
        {
            case root:
            {
                return toNodes ( ExamplesManager.getGroups () );
            }
            case group:
            {
                final ExampleGroup group = parent.getExampleGroup ();
                final List<ExamplesTreeNode> children = new ArrayList<ExamplesTreeNode> ();
                children.addAll ( toNodes ( group.getGroups () ) );
                children.addAll ( toNodes ( group.getExamples () ) );
                return children;
            }
            default:
            {
                return Collections.emptyList ();
            }
        }
    }

    /**
     * Returns tree nodes for the specified elements.
     *
     * @param elements elements to provide nodes for
     * @return tree nodes for the specified elements
     */
    protected List<ExamplesTreeNode> toNodes ( final List<? extends ExampleElement> elements )
    {
        final List<ExamplesTreeNode> nodes = new ArrayList<ExamplesTreeNode> ();
        for ( final ExampleElement element : elements )
        {
            nodes.add ( new ExamplesTreeNode ( element ) );
        }
        return nodes;
    }
}