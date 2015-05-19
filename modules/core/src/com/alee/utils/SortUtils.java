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

package com.alee.utils;

import com.alee.utils.sort.Edge;
import com.alee.utils.sort.GraphDataProvider;
import com.alee.utils.sort.Node;

import java.util.*;

/**
 * This class provides a set of utilities to perform complex sorting operations.
 *
 * @author Mikle Garin
 */

public final class SortUtils
{
    /**
     * Performs topological data sort using the graph data provider to build nodes graph and returns sorted data list.
     *
     * @param graphDataProvider graph data provider
     * @param <T>               data type
     * @return sorted data list
     */
    public static <T> List<T> doTopologicalSort ( final GraphDataProvider<T> graphDataProvider )
    {
        // Building nodes structure
        final List<Node<T>> nodes = new ArrayList<Node<T>> ();
        final Map<T, Node<T>> nodesCache = new HashMap<T, Node<T>> ();
        for ( final T root : graphDataProvider.getRoots () )
        {
            buildNodeStructure ( root, nodes, nodesCache, graphDataProvider );
        }

        // Performing sorting
        final List<Node<T>> sortedNodes = doTopologicalSort ( nodes );

        // Mapping data
        final List<T> sortedData = new ArrayList<T> ( sortedNodes.size () );
        for ( final Node<T> node : sortedNodes )
        {
            sortedData.add ( node.getData () );
        }

        return sortedData;
    }

    /**
     * Builds single node structure using provided data.
     *
     * @param data              structure root node data
     * @param nodes             existing graph nodes
     * @param nodesCache        existing graph nodes cache
     * @param graphDataProvider graph data provider
     * @param <T>               data type
     * @return root structure node
     */
    private static <T> Node<T> buildNodeStructure ( final T data, final List<Node<T>> nodes, final Map<T, Node<T>> nodesCache,
                                                    final GraphDataProvider<T> graphDataProvider )
    {
        final Node<T> node;
        if ( nodesCache.containsKey ( data ) )
        {
            // Retrieving existing node
            node = nodesCache.get ( data );
        }
        else
        {
            // Creating new node
            node = new Node<T> ( data );
            nodes.add ( node );
            nodesCache.put ( data, node );

            // Creating node children
            for ( final T child : graphDataProvider.getChildren ( data ) )
            {
                node.addEdge ( buildNodeStructure ( child, nodes, nodesCache, graphDataProvider ) );
            }
        }
        return node;
    }

    /**
     * Performs topological data sort using provided graph nodes and returns sorted graph nodes list.
     *
     * @param nodes graph nodes to sort
     * @param <T>   data type
     * @return sorted sorted graph nodes list
     */
    public static <T> List<Node<T>> doTopologicalSort ( final List<Node<T>> nodes )
    {
        // L <- Empty list that will contain the sorted elements
        final ArrayList<Node<T>> L = new ArrayList<Node<T>> ();

        // S <- Set of all nodes with no incoming edges
        final HashSet<Node<T>> S = new HashSet<Node<T>> ();
        for ( final Node<T> n : nodes )
        {
            if ( n.inEdges.size () == 0 )
            {
                S.add ( n );
            }
        }

        // while S is non-empty do
        while ( !S.isEmpty () )
        {
            // remove a node n from S
            final Node<T> n = S.iterator ().next ();
            S.remove ( n );

            // insert n into L
            L.add ( n );

            // for each node m with an edge e from n to m do
            for ( final Iterator<Edge> it = n.outEdges.iterator (); it.hasNext (); )
            {
                // remove edge e from the graph
                final Edge e = it.next ();
                final Node<T> m = e.to;

                // Remove edge from n
                it.remove ();

                // Remove edge from m
                m.inEdges.remove ( e );

                // if m has no other incoming edges then insert m into S
                if ( m.inEdges.isEmpty () )
                {
                    S.add ( m );
                }
            }
        }

        // Check to see if all edges are removed
        boolean cycle = false;
        for ( final Node<T> n : nodes )
        {
            if ( !n.inEdges.isEmpty () )
            {
                cycle = true;
                break;
            }
        }

        if ( !cycle )
        {
            return L;
        }
        else
        {
            throw new RuntimeException ( "Cycle present, topological sort not possible" );
        }
    }
}