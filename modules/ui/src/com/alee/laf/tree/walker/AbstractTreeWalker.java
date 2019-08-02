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

package com.alee.laf.tree.walker;

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Predicate;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * Abstract {@link TreeWalker} implementation that provides basic methods to walking through the trees.
 * You only need to implement {@link #nextNode()} and {@link #reset()} methods in extending class to create a working {@link TreeWalker}.
 *
 * @param <N> node type
 * @param <M> tree model type
 * @author Mikle Garin
 */
public abstract class AbstractTreeWalker<N extends TreeNode, M extends TreeModel> implements TreeWalker<N>
{
    /**
     * {@link JTree} to walk through.
     */
    protected JTree tree;

    /**
     * Current parent within walking sequence.
     */
    protected N parent;

    /**
     * Current child index within walking sequence.
     */
    protected int index;

    /**
     * Constructs new {@link AbstractTreeWalker} for the specified {@link JTree}.
     *
     * @param tree {@link JTree} to walk through
     */
    public AbstractTreeWalker ( final JTree tree )
    {
        super ();
        this.tree = tree;
    }

    @Override
    public void forEach ( final Consumer<N> action )
    {
        N node;
        while ( ( node = nextNode () ) != null )
        {
            action.accept ( node );
        }
        reset ();
    }

    @Override
    public boolean anyMatch ( final Predicate<N> predicate )
    {
        boolean anyMatch = false;
        N node;
        while ( ( node = nextNode () ) != null )
        {
            if ( predicate.test ( node ) )
            {
                anyMatch = true;
                break;
            }
        }
        return anyMatch;
    }

    @Override
    public boolean allMatch ( final Predicate<N> predicate )
    {
        boolean allMatch = true;
        N node;
        while ( ( node = nextNode () ) != null )
        {
            if ( !predicate.test ( node ) )
            {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    @Override
    public boolean noneMatch ( final Predicate<N> predicate )
    {
        boolean noneMatch = true;
        N node;
        while ( ( node = nextNode () ) != null )
        {
            if ( predicate.test ( node ) )
            {
                noneMatch = false;
                break;
            }
        }
        return noneMatch;
    }

    /**
     * Returns next tree node from the tree.
     *
     * @return next tree node from the tree
     */
    protected N nextNode ()
    {
        final N node;
        final M model = ( M ) tree.getModel ();
        if ( parent == null )
        {
            node = getRootNode ( model );
            parent = node;
            index = 0;
        }
        else
        {
            if ( getChildCount ( model, parent ) > index )
            {
                node = getChild ( model, parent, index );
                parent = node;
                index = 0;
            }
            else
            {
                final N child = parent;
                parent = ( N ) parent.getParent ();
                if ( parent != null )
                {
                    final int childIndex = getIndexOfChild ( model, parent, child );
                    if ( childIndex != -1 )
                    {
                        index = childIndex + 1;
                        node = nextNode ();
                    }
                    else
                    {
                        throw new TreeWalkerException ( "Provided child is not contained within its parent" );
                    }
                }
                else
                {
                    node = null;
                }
            }
        }
        return node;
    }

    /**
     * Resets tree walking to initial position.
     */
    protected void reset ()
    {
        parent = null;
        index = -1;
    }

    /**
     * Returns tree root node.
     *
     * @param model {@link TreeModel}
     * @return tree root node
     */
    protected abstract N getRootNode ( M model );

    /**
     * Returns child count for the specified {@link TreeNode}.
     *
     * @param model  {@link TreeModel}
     * @param parent {@link TreeNode} to return child count for
     * @return child count for the specified {@link TreeNode}
     */
    protected abstract int getChildCount ( M model, N parent );

    /**
     * Returns child {@link TreeNode} under parent {@link TreeNode} at the specified index.
     *
     * @param model  {@link TreeModel}
     * @param parent parent {@link TreeNode}
     * @param index  child {@link TreeNode} index
     * @return child {@link TreeNode} under parent {@link TreeNode} at the specified index
     */
    protected abstract N getChild ( M model, N parent, int index );

    /**
     * Returns child {@link TreeNode} index within specified parent {@link TreeNode}.
     *
     * @param model  {@link TreeModel}
     * @param parent parent {@link TreeNode}
     * @param child  child {@link TreeNode}
     * @return child {@link TreeNode} index within specified parent {@link TreeNode}
     */
    protected abstract int getIndexOfChild ( M model, N parent, N child );
}