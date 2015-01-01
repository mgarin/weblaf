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

/**
 * @author Mikle Garin
 */

public class Edge
{
    public final Node from;
    public final Node to;

    public Edge ( final Node from, final Node to )
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals ( final Object obj )
    {
        if ( obj instanceof Edge )
        {
            final Edge e = ( Edge ) obj;
            return e.from == from && e.to == to;
        }
        return false;
    }
}