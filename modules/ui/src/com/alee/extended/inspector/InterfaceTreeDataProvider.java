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
import com.alee.laf.VisibleWindowListener;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.compare.Filter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
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

                final int result;
                if ( component1 instanceof Window && component2 instanceof Window )
                {
                    // No point to compare windows
                    result = 0;
                }
                else
                {
                    // Checking that parent is the same
                    final Container parent1 = component1.getParent ();
                    final Container parent2 = component2.getParent ();
                    if ( parent1 == null || parent2 == null || parent1 == parent2 )
                    {
                        // Comparing component's Z-index
                        final int zIndex1 = parent1 != null ? parent1.getComponentZOrder ( component1 ) : 0;
                        final int zIndex2 = parent2 != null ? parent2.getComponentZOrder ( component2 ) : 0;
                        result = new Integer ( zIndex2 ).compareTo ( zIndex1 );
                    }
                    else
                    {
                        throw new RuntimeException ( "Compared nodes cannot have different parent" );
                    }
                }
                return result;
            }
        } );

        if ( inspected == null )
        {
            WebLookAndFeel.addVisibleWindowListener ( tree, new VisibleWindowListener ()
            {
                @Override
                public void windowDisplayed ( final Window window )
                {
                    if ( window.isShowing () )
                    {
                        final InterfaceTreeNode root = tree.getRootNode ();
                        final InterfaceTreeNode node = new InterfaceTreeNode ( tree, window );
                        tree.addChildNode ( root, node );
                    }
                }

                @Override
                public void windowHidden ( final Window window )
                {
                    final InterfaceTreeNode root = tree.getRootNode ();
                    for ( int i = 0; i < root.getChildCount (); i++ )
                    {
                        final InterfaceTreeNode child = root.getChildAt ( i );
                        if ( child.getUserObject () == window )
                        {
                            child.uninstall ();
                            tree.removeNode ( child );
                            break;
                        }
                    }
                }
            } );
        }
    }

    @Override
    public InterfaceTreeNode getRoot ()
    {
        if ( inspected != null )
        {
            return new InterfaceTreeNode ( tree, inspected );
        }
        else
        {
            return new InterfaceTreeNode ( tree, null );
        }
    }

    @Override
    public List<InterfaceTreeNode> getChildren ( final InterfaceTreeNode parent )
    {
        final List<InterfaceTreeNode> nodes;
        final Component component = parent.getUserObject ();
        if ( component != null )
        {
            if ( !( component instanceof CellRendererPane ) )
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
        else
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
        return nodes;
    }

    @Override
    public boolean accept ( final Component component )
    {
        return !( component instanceof ComponentHighlighter );
    }
}