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
 * Abstract {@link TreeWalker} that provides implementation for walking methods.
 * You only need to implement {@link #nextNode()} and {@link #reset()} methods in extending class to create a complete {@link TreeWalker}.
 *
 * @param <E> tree node type
 * @param <M> tree model type
 * @author Mikle Garin
 */

public abstract class AbstractTreeWalker<E extends TreeNode, M extends TreeModel> implements TreeWalker<E>
{
    /**
     * {@link JTree} to walk through.
     */
    protected JTree tree;

    /**
     * Current parent within walking sequence.
     */
    protected E parent;

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
    public void forEach ( final Consumer<E> action )
    {
        E node;
        while ( ( node = nextNode () ) != null )
        {
            action.accept ( node );
        }
        reset ();
    }

    @Override
    public boolean anyMatch ( final Predicate<E> predicate )
    {
        boolean anyMatch = false;
        E node;
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
    public boolean allMatch ( final Predicate<E> predicate )
    {
        boolean allMatch = true;
        E node;
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
    public boolean noneMatch ( final Predicate<E> predicate )
    {
        boolean noneMatch = true;
        E node;
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
    protected E nextNode ()
    {
        final E node;
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
                final E child = parent;
                parent = ( E ) parent.getParent ();
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
    protected abstract E getRootNode ( M model );

    /**
     * Returns child count for the specified {@link TreeNode}.
     *
     * @param model  {@link TreeModel}
     * @param parent {@link TreeNode} to return child count for
     * @return child count for the specified {@link TreeNode}
     */
    protected abstract int getChildCount ( M model, E parent );

    /**
     * Returns child {@link TreeNode} under parent {@link TreeNode} at the specified index.
     *
     * @param model  {@link TreeModel}
     * @param parent parent {@link TreeNode}
     * @param index  child {@link TreeNode} index
     * @return child {@link TreeNode} under parent {@link TreeNode} at the specified index
     */
    protected abstract E getChild ( M model, E parent, int index );

    /**
     * Returns child {@link TreeNode} index within specified parent {@link TreeNode}.
     *
     * @param model  {@link TreeModel}
     * @param parent parent {@link TreeNode}
     * @param child  child {@link TreeNode}
     * @return child {@link TreeNode} index within specified parent {@link TreeNode}
     */
    protected abstract int getIndexOfChild ( M model, E parent, E child );
}