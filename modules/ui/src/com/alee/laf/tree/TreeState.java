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

import com.alee.utils.MergeUtils;
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

@XStreamAlias ("TreeState")
@XStreamConverter (TreeStateConverter.class)
public class TreeState implements Serializable, Cloneable
{
    /**
     * Tree node states.
     */
    protected Map<String, NodeState> states = new LinkedHashMap<String, NodeState> ();

    /**
     * Constructs new object instance with empty states.
     */
    public TreeState ()
    {
        super ();
    }

    /**
     * Constructs new object instance with specified states.
     *
     * @param states node states
     */
    public TreeState ( final Map<String, NodeState> states )
    {
        super ();
        if ( states != null )
        {
            setStates ( states );
        }
    }

    /**
     * Returns all node states.
     *
     * @return all node states
     */
    public Map<String, NodeState> getStates ()
    {
        return states;
    }

    /**
     * Sets all node states.
     *
     * @param states all node states
     */
    public void setStates ( final Map<String, NodeState> states )
    {
        this.states = states;
    }

    /**
     * Adds node state.
     *
     * @param nodeId   node ID
     * @param expanded expansion state
     * @param selected selection state
     */
    public void addState ( final String nodeId, final boolean expanded, final boolean selected )
    {
        states.put ( nodeId, new NodeState ( expanded, selected ) );
    }

    /**
     * Returns whether node with the specified ID is expanded or not.
     *
     * @param nodeId node ID
     * @return true if node with the specified ID is expanded, false otherwise
     */
    public boolean isExpanded ( final String nodeId )
    {
        final NodeState state = states.get ( nodeId );
        return state != null && state.isExpanded ();
    }

    /**
     * Returns whether node with the specified ID is selected or not.
     *
     * @param nodeId node ID
     * @return true if node with the specified ID is expanded, false otherwise
     */
    public boolean isSelected ( final String nodeId )
    {
        final NodeState state = states.get ( nodeId );
        return state != null && state.isSelected ();
    }

    /**
     * Returns cloned tree state.
     *
     * @return cloned tree state
     */
    @Override
    public TreeState clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}