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

package com.alee.demo.content.data.tree.model;

import java.io.Serializable;

/**
 * {@link SampleNode} object type.
 *
 * @author Mikle Garin
 */
public class SampleObject implements Cloneable, Serializable
{
    /**
     * {@link SampleObject} type.
     */
    protected SampleObjectType type;

    /**
     * {@link SampleObject} title.
     */
    protected String title;

    /**
     * Constructs new {@link SampleObject}.
     *
     * @param type  {@link SampleObject} type
     * @param title {@link SampleObject} title
     */
    public SampleObject ( final SampleObjectType type, final String title )
    {
        this.type = type;
        this.title = title;
    }

    /**
     * Returns {@link SampleObject} type.
     *
     * @return {@link SampleObject} type
     */
    public SampleObjectType getType ()
    {
        return type;
    }

    /**
     * Changes {@link SampleObject} type.
     *
     * @param type new {@link SampleObject} type
     */
    public void setType ( final SampleObjectType type )
    {
        this.type = type;
    }

    /**
     * Returns {@link SampleObject} title.
     *
     * @return {@link SampleObject} title
     */
    public String getTitle ()
    {
        return title;
    }

    /**
     * Changes {@link SampleObject} title.
     *
     * @param title new {@link SampleObject} title
     */
    public void setTitle ( final String title )
    {
        this.title = title;
    }
}