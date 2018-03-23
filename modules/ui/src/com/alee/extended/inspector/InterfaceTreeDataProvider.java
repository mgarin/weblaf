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

import com.alee.extended.tree.AbstractExTreeDataProvider;
import com.alee.utils.compare.Filter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class InterfaceTreeDataProvider extends AbstractExTreeDataProvider<InterfaceTreeNode> implements Filter<Component>
{
    /**
     * Interface components tree.
     */
    private final InterfaceTree tree;

    /**
     * Interface root component.
     */
    private final Component inspected;

    /**
     * Constructs interface tree data provider for the specified interface root component.
     *
     * @param tree      interface tree
     * @param inspected interface root component
     */
    public InterfaceTreeDataProvider ( final InterfaceTree tree, final Component inspected )
    {
        super ();
        this.tree = tree;
        this.inspected = inspected;

        // Special comparator that keeps nodes in a strict order
        setChildrenComparator ( new Comparator<InterfaceTreeNode> ()
        {
            @Override
            public int compare ( final InterfaceTreeNode node1, final InterfaceTreeNode node2 )
            {
                final Component component1 = node1.getUserObject ();
                final Component component2 = node2.getUserObject ();

                // Checking that parent is the same
                final Container parent = component1.getParent ();
                if ( parent != component2.getParent () )
                {
                    throw new RuntimeException ( "Compared nodes cannot have different parent" );
                }

                // Comparing component's Z-index
                final int zIndex1 = parent.getComponentZOrder ( component1 );
                final int zIndex2 = parent.getComponentZOrder ( component2 );
                return new Integer ( zIndex1 ).compareTo ( zIndex2 );
            }
        } );
    }

    @Override
    public InterfaceTreeNode getRoot ()
    {
        return new InterfaceTreeNode ( tree, inspected );
    }

    @Override
    public List<InterfaceTreeNode> getChildren ( final InterfaceTreeNode parent )
    {
        final Container container = ( Container ) parent.getUserObject ();
        final List<InterfaceTreeNode> nodes;
        if ( !( container instanceof CellRendererPane ) )
        {
            nodes = new ArrayList<InterfaceTreeNode> ( container.getComponentCount () );
            for ( int i = 0; i < container.getComponentCount (); i++ )
            {
                final Component component = container.getComponent ( i );
                if ( accept ( component ) )
                {
                    nodes.add ( new InterfaceTreeNode ( tree, component ) );
                }
            }
        }
        else
        {
            nodes = new ArrayList<InterfaceTreeNode> ( 0 );
        }
        return nodes;
    }

    @Override
    public boolean accept ( final Component component )
    {
        return !( component instanceof ComponentHighlighter );
    }
}