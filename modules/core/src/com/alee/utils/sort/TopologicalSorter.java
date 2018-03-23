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

package com.alee.utils.sort;

import java.util.*;

/**
 * Custom sorter that implements topological data sorting.
 *
 * @param <T> data type
 * @author Mikle Garin
 */

public final class TopologicalSorter<T>
{
    /**
     * {@link TopologicalGraphProvider}.
     */
    private final TopologicalGraphProvider<T> provider;

    /**
     * Constructs new {@link TopologicalSorter}.
     *
     * @param provider {@link TopologicalGraphProvider}
     */
    public TopologicalSorter ( final TopologicalGraphProvider<T> provider )
    {
        super ();
        this.provider = provider;
    }

    /**
     * Performs topological data sort using {@link TopologicalGraphProvider} to build nodes graph and returns sorted data list.
     *
     * @return sorted data list
     */
    public List<T> list ()
    {
        // Building nodes structure
        final List<Node<T>> nodes = new ArrayList<Node<T>> ();
        final Map<T, Node<T>> nodesCache = new HashMap<T, Node<T>> ();
        for ( final T root : provider.getRoots () )
        {
            buildNodeStructure ( root, nodes, nodesCache );
        }

        // Performing sorting
        final List<Node<T>> sortedNodes = doTopologicalSort ( nodes );

        // Mapping data
        final List<T> sortedData = new ArrayList<T> ( sortedNodes.size () );
        for ( final Node<T> node : sortedNodes )
        {
            sortedData.add ( node.data );
        }

        return sortedData;
    }

    /**
     * Builds single node structure using provided data.
     *
     * @param data       structure root node data
     * @param nodes      existing graph nodes
     * @param nodesCache existing graph nodes cache
     * @return root structure node
     */
    private Node<T> buildNodeStructure ( final T data, final List<Node<T>> nodes, final Map<T, Node<T>> nodesCache )
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
            for ( final T child : provider.getChildren ( data ) )
            {
                node.addEdge ( buildNodeStructure ( child, nodes, nodesCache ) );
            }
        }
        return node;
    }

    /**
     * Performs topological data sort using provided graph nodes and returns sorted graph nodes list.
     *
     * @param nodes graph nodes to sort
     * @return sorted sorted graph nodes list
     */
    private List<Node<T>> doTopologicalSort ( final List<Node<T>> nodes )
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
            for ( final Iterator<Edge<T>> it = n.outEdges.iterator (); it.hasNext (); )
            {
                // remove edge e from the graph
                final Edge<T> e = it.next ();
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

    /**
     * Single graph {@link Node} containing data.
     *
     * @param <T> data type
     */
    private static class Node<T>
    {
        /**
         * {@link Node} data.
         */
        public final T data;

        /**
         * Incoming {@link Edge}s of this {@link Node}.
         */
        public final HashSet<Edge<T>> inEdges;

        /**
         * Outgoing {@link Edge}s of this {@link Node}.
         */
        public final HashSet<Edge<T>> outEdges;

        /**
         * Constructs new {@link Node}.
         *
         * @param data {@link Node} data
         */
        public Node ( final T data )
        {
            this.data = data;
            inEdges = new HashSet<Edge<T>> ();
            outEdges = new HashSet<Edge<T>> ();
        }

        /**
         * Adds new outgoing {@link Edge} from this {@link Node} to other {@link Node}.
         *
         * @param node other {@link Node}
         * @return this {@link Node}
         */
        public Node addEdge ( final Node<T> node )
        {
            final Edge<T> edge = new Edge<T> ( this, node );
            outEdges.add ( edge );
            node.inEdges.add ( edge );
            return this;
        }

        @Override
        public String toString ()
        {
            return data != null ? data.toString () : null;
        }
    }

    /**
     * Single {@link Node} edge.
     *
     * @param <T> data type
     */
    private static class Edge<T>
    {
        /**
         * {@link Node} this {@link Edge} is outgoing for.
         */
        public final Node<T> from;

        /**
         * {@link Node} this {@link Edge} is incoming for.
         */
        public final Node<T> to;

        /**
         * Constructs new {@link Edge}.
         *
         * @param from {@link Node} this {@link Edge} is outgoing for
         * @param to   {@link Node} this {@link Edge} is incoming for
         */
        public Edge ( final Node<T> from, final Node<T> to )
        {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals ( final Object object )
        {
            if ( object instanceof TopologicalSorter.Edge )
            {
                final Edge<T> edge = ( Edge<T> ) object;
                return edge.from == from && edge.to == to;
            }
            return false;
        }
    }
}