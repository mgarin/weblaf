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

package com.alee.managers.icon;

import com.alee.api.Identifiable;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * Icon information class.
 *
 * @author Mikle Garin
 * @see com.alee.managers.icon.IconManager
 */

public final class IconInfo implements Serializable, Cloneable, Identifiable
{
    /**
     * Unique icon ID.
     * Used to access icon through {@link com.alee.managers.icon.IconManager}.
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Icon class type.
     * Used to load the icon.
     */
    @XStreamAsAttribute
    private Class<? extends Icon> type;

    /**
     * Class which specified path is relative to.
     * In case it is not specified when read from skin XML path will be considered relative to skin file.
     * In case it is not specified when provided through the code path will be considered absolute.
     */
    @XStreamAsAttribute
    private Class nearClass;

    /**
     * Icon file path.
     */
    @XStreamAsAttribute
    private String path;

    /**
     * Constructs new empty icon information.
     */
    public IconInfo ()
    {
        super ();
    }

    /**
     * Constructs new icon information.
     *
     * @param id   unique icon ID
     * @param type icon type
     * @param path icon path
     */
    public IconInfo ( final String id, final Class<? extends Icon> type, final String path )
    {
        super ();
        setId ( id );
        setType ( type );
        setPath ( path );
    }

    /**
     * Constructs new icon information.
     *
     * @param id        unique icon ID
     * @param type      icon type
     * @param nearClass class which specified path is relative to
     * @param path      icon path
     */
    public IconInfo ( final String id, final Class<? extends Icon> type, final Class nearClass, final String path )
    {
        super ();
        setId ( id );
        setType ( type );
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
     * Returns icon class type.
     *
     * @return icon class type
     */
    public Class<? extends Icon> getType ()
    {
        return type;
    }

    /**
     * Sets icon class type.
     *
     * @param type icon class type
     */
    public void setType ( final Class<? extends Icon> type )
    {
        this.type = type;
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

    @Override
    public IconInfo clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}