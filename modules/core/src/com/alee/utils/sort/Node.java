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

import java.util.HashSet;

/**
 * @author Mikle Garin
 */

public class Node<T>
{
    public final T data;
    public final HashSet<Edge> inEdges;
    public final HashSet<Edge> outEdges;

    public Node ( final T data )
    {
        this.data = data;
        inEdges = new HashSet<Edge> ();
        outEdges = new HashSet<Edge> ();
    }

    public Node addEdge ( final Node node )
    {
        final Edge e = new Edge ( this, node );
        outEdges.add ( e );
        node.inEdges.add ( e );
        return this;
    }

    public T getData ()
    {
        return data;
    }

    @Override
    public String toString ()
    {
        return data != null ? data.toString () : null;
    }
}