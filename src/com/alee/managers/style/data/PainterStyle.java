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

package com.alee.managers.style.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Painter style information class.
 *
 * @author Mikle Garin
 */

public final class PainterStyle implements Serializable
{
    /**
     * Painter ID.
     * Refers to painter type.
     * It is required since a lot of components has more than just one painter.
     */
    private String id;

    /**
     * Painter class canonical name.
     * Used for painter instantiation.
     */
    private String painterClass;

    /**
     * Painter properties.
     * Contains parsed paintr settings.
     */
    private Map<String, Object> properties;

    /**
     * Constructs new painter style information.
     */
    public PainterStyle ()
    {
        super ();
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public String getPainterClass ()
    {
        return painterClass;
    }

    public void setPainterClass ( final String painterClass )
    {
        this.painterClass = painterClass;
    }

    public Map<String, Object> getProperties ()
    {
        return properties;
    }

    public void setProperties ( final Map<String, Object> properties )
    {
        this.properties = properties;
    }
}