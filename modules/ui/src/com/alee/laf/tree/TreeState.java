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

package com.alee.laf.tree;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This object might contain tree selection and expand states for all loaded tree nodes.
 * Usually this object is constructed through TreeUtils automatically and could be applied to some tree using these utilities aswell.
 *
 * @author Mikle Garin
 * @see TreeUtils
 */
@XStreamAlias ( "TreeState" )
@XStreamConverter ( TreeStateConverter.class )
public class TreeState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link NodeState}s of a single tree.
     */
    protected final Map<String, NodeState> states;

    /**
     * Constructs new {@link TreeState} with empty states.
     */
    public TreeState ()
    {
        states = new LinkedHashMap<String, NodeState> ();
    }

    /**
     * Returns all {@link NodeState}s.
     *
     * @return all {@link NodeState}s
     */
    public Map<String, NodeState> states ()
    {
        return states;
    }

    /**
     * Adds node state.
     *
     * @param nodeId node identifier
     * @param state  {@link NodeState}
     */
    public void addState ( final String nodeId, final NodeState state )
    {
        states.put ( nodeId, state );
    }

    /**
     * Returns whether node with the specified ID is expanded or not.
     *
     * @param nodeId node identifier
     * @return {@code true} if node with the specified ID is expanded, {@code false} otherwise
     */
    public boolean isExpanded ( final String nodeId )
    {
        final NodeState state = states.get ( nodeId );
        return state != null && state.isExpanded ();
    }

    /**
     * Returns whether node with the specified ID is selected or not.
     *
     * @param nodeId node identifier
     * @return {@code true} if node with the specified ID is expanded, {@code false} otherwise
     */
    public boolean isSelected ( final String nodeId )
    {
        final NodeState state = states.get ( nodeId );
        return state != null && state.isSelected ();
    }
}