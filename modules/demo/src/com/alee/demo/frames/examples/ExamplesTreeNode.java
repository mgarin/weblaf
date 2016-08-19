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

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.demo.api.example.Example;
import com.alee.demo.api.example.ExampleElement;
import com.alee.demo.api.example.ExampleGroup;
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.language.LM;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import static com.alee.demo.frames.examples.ExamplesTreeNodeType.example;
import static com.alee.demo.frames.examples.ExamplesTreeNodeType.group;

/**
 * @author Mikle Garin
 */

public final class ExamplesTreeNode extends UniqueNode implements Stateful, IconSupport, TitleSupport
{
    /**
     * Static root node ID.
     */
    private static final String ROOT_ID = "examples.root.id";

    /**
     * Node type.
     */
    private final ExamplesTreeNodeType type;

    /**
     * Constructs examples tree root node.
     */
    public ExamplesTreeNode ()
    {
        super ();
        this.type = ExamplesTreeNodeType.root;
    }

    /**
     * Constructs examples tree node that reflect specified element.
     *
     * @param element element to construct node for
     */
    public ExamplesTreeNode ( final ExampleElement element )
    {
        super ( element );
        this.type = element instanceof ExampleGroup ? group : example;
    }

    /**
     * Returns node type.
     *
     * @return node type
     */
    public ExamplesTreeNodeType getType ()
    {
        return type;
    }

    /**
     * Returns example group.
     * Make sure to call this only if type of the node is {@link com.alee.demo.frames.examples.ExamplesTreeNodeType#group}.
     *
     * @return example group
     */
    public ExampleGroup getExampleGroup ()
    {
        return ( ExampleGroup ) getUserObject ();
    }

    /**
     * Returns example.
     * Make sure to call this only if type of the node is {@link com.alee.demo.frames.examples.ExamplesTreeNodeType#example}.
     *
     * @return example
     */
    public Example getExample ()
    {
        return ( Example ) getUserObject ();
    }

    @Override
    public String getId ()
    {
        return type == group ? getExampleGroup ().getId () : type == example ? getExample ().getId () : ROOT_ID;
    }

    @Override
    public List<String> getStates ()
    {
        if ( type == group )
        {
            return CollectionUtils.asList ( getExampleGroup ().getFeatureState ().name () );
        }
        else if ( type == example )
        {
            return CollectionUtils.asList ( getExample ().getFeatureState ().name () );
        }
        else
        {
            return Collections.emptyList ();
        }
    }

    @Override
    public Icon getIcon ()
    {
        return type == group ? getExampleGroup ().getIcon () : type == example ? getExample ().getIcon () : null;
    }

    @Override
    public String getTitle ()
    {
        return LM.get ( type == group ? getExampleGroup ().getTitle () : type == example ? getExample ().getTitle () : null );
    }

    /**
     * Returns proper node text representation.
     * Used to filter nodes in tree and to display its name.
     *
     * @return proper node text representation
     */
    @Override
    public String toString ()
    {
        return getTitle ();
    }
}