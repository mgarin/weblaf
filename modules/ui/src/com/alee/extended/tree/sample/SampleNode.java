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

package com.alee.extended.tree.sample;

import com.alee.extended.tree.AsyncUniqueNode;

/**
 * Sample node.
 *
 * @author Mikle Garin
 */

public class SampleNode extends AsyncUniqueNode
{
    /**
     * Node name to display.
     */
    protected String name;

    /**
     * Node type.
     */
    protected SampleNodeType type;

    /**
     * Time spent to load node childs.
     */
    protected long time;

    /**
     * Constructs sample node.
     *
     * @param name node name
     * @param type node type
     */
    public SampleNode ( final String name, final SampleNodeType type )
    {
        super ();
        this.name = name;
        this.type = type;
        this.time = 0;
    }

    /**
     * Returns node name.
     *
     * @return node name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Changes node name.
     *
     * @param name new node name
     */
    public void setName ( final String name )
    {
        this.name = name;
    }

    /**
     * Returns node type.
     *
     * @return node type
     */
    public SampleNodeType getType ()
    {
        return type;
    }

    /**
     * Changes node type.
     *
     * @param type new node type
     */
    public void setType ( final SampleNodeType type )
    {
        this.type = type;
    }

    /**
     * Returns time spent to load node childs.
     *
     * @return time spent to load node childs
     */
    public long getTime ()
    {
        return time;
    }

    /**
     * Sets time spent to load node childs.
     *
     * @param time new time spent to load node childs
     */
    public void setTime ( final long time )
    {
        this.time = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return name + " (" + type + ")";
    }
}