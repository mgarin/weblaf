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

package com.alee.extended.inspector;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.tree.AbstractExTreeDataProvider;
import com.alee.utils.compare.Filter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data provider implementation for {@link InterfaceTree}.
 *
 * @author Mikle Garin
 */
public class InterfaceTreeDataProvider extends AbstractExTreeDataProvider<InterfaceTreeNode> implements Filter<Component>
{
    /**
     * Interface components tree.
     */
    @NotNull
    protected final InterfaceTree tree;

    /**
     * Interface root component.
     */
    @Nullable
    protected final Component inspected;

    /**
     * Constructs interface tree data provider for the specified interface root component.
     *
     * @param tree      interface tree
     * @param inspected interface root component
     */
    public InterfaceTreeDataProvider ( @NotNull final InterfaceTree tree, @Nullable final Component inspected )
    {
        this.tree = tree;
        this.inspected = inspected;
        setChildrenComparator ( new InterfaceTreeNodeComparator () );
    }

    @NotNull
    @Override
    public InterfaceTreeNode getRoot ()
    {
        return new InterfaceTreeNode ( tree, inspected );
    }

    @NotNull
    @Override
    public List<InterfaceTreeNode> getChildren ( @NotNull final InterfaceTreeNode parent )
    {
        final List<InterfaceTreeNode> nodes;
        final Component component = parent.getUserObject ();
        if ( component == null )
        {
            final Window[] windows = Window.getWindows ();
            nodes = new ArrayList<InterfaceTreeNode> ( windows.length );
            for ( final Window window : windows )
            {
                if ( window.isShowing () )
                {
                    nodes.add ( new InterfaceTreeNode ( tree, window ) );
                }
            }
        }
        else
        {
            if ( component instanceof Container && !( component instanceof CellRendererPane ) )
            {
                final Container container = ( Container ) component;
                nodes = new ArrayList<InterfaceTreeNode> ( container.getComponentCount () );
                for ( int i = 0; i < container.getComponentCount (); i++ )
                {
                    final Component child = container.getComponent ( i );
                    if ( accept ( child ) )
                    {
                        nodes.add ( new InterfaceTreeNode ( tree, child ) );
                    }
                }
            }
            else
            {
                nodes = new ArrayList<InterfaceTreeNode> ( 0 );
            }
        }
        return nodes;
    }

    @Override
    public boolean accept ( @Nullable final Component component )
    {
        return !( component instanceof ComponentHighlighter );
    }
}