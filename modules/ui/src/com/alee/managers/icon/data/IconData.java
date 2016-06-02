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

package com.alee.managers.icon.data;

import com.alee.api.Identifiable;
import com.alee.utils.MergeUtils;
import com.alee.utils.xml.ClassConverter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import java.io.Serializable;

/**
 * Basic icon information.
 *
 * @author Mikle Garin
 * @see com.alee.managers.icon.IconManager
 */

public abstract class IconData implements Serializable, Cloneable, Identifiable
{
    /**
     * Unique icon ID.
     * Used to access icon through {@link com.alee.managers.icon.IconManager}.
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Class which specified path is relative to.
     */
    @XStreamAsAttribute
    @XStreamConverter ( ClassConverter.class )
    private Class nearClass;

    /**
     * Icon file path.
     */
    @XStreamAsAttribute
    private String path;

    /**
     * Constructs new empty icon information.
     */
    public IconData ()
    {
        super ();
    }

    /**
     * Constructs new icon information.
     *
     * @param id   unique icon ID
     * @param path icon path
     */
    public IconData ( final String id, final String path )
    {
        super ();
        setId ( id );
        setPath ( path );
    }

    /**
     * Constructs new icon information.
     *
     * @param id        unique icon ID
     * @param nearClass class which specified path is relative to
     * @param path      icon path
     */
    public IconData ( final String id, final Class nearClass, final String path )
    {
        super ();
        setId ( id );
        setNearClass ( nearClass );
        setPath ( path );
    }

    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Sets unique icon ID.
     *
     * @param id unique icon ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns class which specified path is relative to.
     *
     * @return class which specified path is relative to
     */
    public Class getNearClass ()
    {
        return nearClass;
    }

    /**
     * Sets class which specified path is relative to.
     *
     * @param nearClass class which specified path is relative to
     */
    public void setNearClass ( final Class nearClass )
    {
        this.nearClass = nearClass;
    }

    /**
     * Returns icon file path.
     *
     * @return icon file path
     */
    public String getPath ()
    {
        return path;
    }

    /**
     * Sets icon file path.
     *
     * @param path icon file path
     */
    public void setPath ( final String path )
    {
        this.path = path;
    }

    /**
     * Returns loaded icon.
     *
     * @return loaded icon
     */
    public abstract Icon loadIcon ();

    @Override
    public IconData clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}