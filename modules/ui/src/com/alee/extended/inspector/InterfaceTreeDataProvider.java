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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class InterfaceTreeDataProvider extends AbstractExTreeDataProvider<InterfaceTreeNode>
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
    }

    @Override
    public InterfaceTreeNode getRoot ()
    {
        return new InterfaceTreeNode ( tree, inspected );
    }

    @Override
    public List<InterfaceTreeNode> getChildren ( final InterfaceTreeNode node )
    {
        final Container component = ( Container ) node.getComponent ();
        final List<InterfaceTreeNode> nodes = new ArrayList<InterfaceTreeNode> ( component.getComponentCount () );
        for ( int i = 0; i < component.getComponentCount (); i++ )
        {
            nodes.add ( new InterfaceTreeNode ( tree, component.getComponent ( i ) ) );
        }
        return nodes;
    }

    @Override
    public boolean isLeaf ( final InterfaceTreeNode node )
    {
        return !( node.getComponent () instanceof Container ) || ( ( Container ) node.getComponent () ).getComponentCount () == 0;
    }
}