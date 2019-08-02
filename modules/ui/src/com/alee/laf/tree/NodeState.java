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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Single node state data class.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "NodeState" )
public class NodeState implements Cloneable, Serializable
{
    /**
     * Whether node is expanded or not.
     */
    @XStreamAsAttribute
    protected boolean expanded;

    /**
     * Whether node is selected or not.
     */
    @XStreamAsAttribute
    protected boolean selected;

    /**
     * Constructs new {@link NodeState} with default state.
     */
    public NodeState ()
    {
        this ( false, false );
    }

    /**
     * Constructs new {@link NodeState} with the specified expansion and selection states.
     *
     * @param expanded expansion state
     * @param selected selection state
     */
    public NodeState ( final boolean expanded, final boolean selected )
    {
        this.expanded = expanded;
        this.selected = selected;
    }

    /**
     * Returns whether node is expanded or not.
     *
     * @return true if node is expanded, false otherwise
     */
    public boolean isExpanded ()
    {
        return expanded;
    }

    /**
     * Sets whether node is expanded or not.
     *
     * @param expanded whether node is expanded or not
     */
    public void setExpanded ( final boolean expanded )
    {
        this.expanded = expanded;
    }

    /**
     * Returns whether node is selected or not.
     *
     * @return true if node is selected, false otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Sets whether node is selected or not.
     *
     * @param selected whether node is selected or not
     */
    public void setSelected ( final boolean selected )
    {
        this.selected = selected;
    }
}