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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.ui.IconBridge;
import com.alee.api.ui.TextBridge;
import com.alee.demo.api.example.Example;
import com.alee.demo.api.example.ExampleElement;
import com.alee.demo.api.example.ExampleGroup;
import com.alee.laf.tree.TreeNodeParameters;
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
 * {@link UniqueNode} representing single {@link ExampleElement}.
 *
 * @author Mikle Garin
 */
public final class ExamplesTreeNode extends UniqueNode<ExamplesTreeNode, ExampleElement>
        implements Stateful, IconBridge<TreeNodeParameters<ExamplesTreeNode, JTree>>,
        TextBridge<TreeNodeParameters<ExamplesTreeNode, JTree>>
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

    @NotNull
    @Override
    public String getId ()
    {
        return type == group ? getExampleGroup ().getId () : type == example ? getExample ().getId () : ROOT_ID;
    }

    @NotNull
    @Override
    public List<String> getStates ()
    {
        final List<String> states;
        switch ( type )
        {
            case group:
                states = CollectionUtils.asList ( getExampleGroup ().getFeatureState ().name () );
                break;

            case example:
                states = CollectionUtils.asList ( getExample ().getFeatureState ().name () );
                break;

            default:
                states = Collections.emptyList ();
                break;
        }
        return states;
    }

    @Nullable
    @Override
    public Icon getIcon ( @NotNull final TreeNodeParameters<ExamplesTreeNode, JTree> parameters )
    {
        final Icon icon;
        switch ( type )
        {
            case group:
                icon = getExampleGroup ().getIcon ();
                break;

            case example:
                icon = getExample ().getIcon ();
                break;

            default:
                icon = null;
                break;
        }
        return icon;
    }

    @Override
    public String getText ( @NotNull final TreeNodeParameters<ExamplesTreeNode, JTree> parameters )
    {
        return getTitle ();
    }

    /**
     * Returns node title.
     *
     * @return node title
     */
    @NotNull
    private String getTitle ()
    {
        final String title;
        switch ( type )
        {
            case group:
                title = getExampleGroup ().getTitle ();
                break;

            case example:
                title = getExample ().getTitle ();
                break;

            default:
                title = null;
                break;
        }
        return LM.get ( title );
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