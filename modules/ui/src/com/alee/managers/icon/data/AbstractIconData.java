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
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Overwriting;
import com.alee.extended.svg.SvgIconData;
import com.alee.utils.xml.ClassConverter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract {@link AbstractIconData} containing basic {@link Icon} information.
 * Implements {@link Overwriting} instead of {@link com.alee.api.merge.Mergeable} to avoid merging any icon data ever.
 *
 * @param <T> icon type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see com.alee.managers.icon.IconManager
 * @see com.alee.managers.icon.data.ImageIconData
 * @see SvgIconData
 */
public abstract class AbstractIconData<T extends Icon> implements Identifiable, Overwriting, Cloneable, Serializable
{
    /**
     * Unique icon identifier.
     * Used to access icon through {@link com.alee.managers.icon.IconManager}.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Class which specified path is relative to.
     */
    @XStreamAsAttribute
    @XStreamConverter ( ClassConverter.class )
    protected Class nearClass;

    /**
     * Icon file path.
     */
    @XStreamAsAttribute
    protected String path;

    /**
     * Customizable icon adjustments.
     */
    @XStreamImplicit
    protected List<IconAdjustment<T>> adjustments;

    /**
     * Constructs new empty icon information.
     */
    public AbstractIconData ()
    {
        super ();
    }

    /**
     * Constructs new icon information.
     *
     * @param id   unique icon identifier
     * @param path icon path
     */
    public AbstractIconData ( final String id, final String path )
    {
        super ();
        setId ( id );
        setPath ( path );
    }

    /**
     * Constructs new icon information.
     *
     * @param id        unique icon identifier
     * @param nearClass class which specified path is relative to
     * @param path      icon path
     */
    public AbstractIconData ( final String id, final Class nearClass, final String path )
    {
        super ();
        setId ( id );
        setNearClass ( nearClass );
        setPath ( path );
    }

    @Nullable
    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    /**
     * Sets unique icon identifier.
     *
     * @param id unique icon identifier
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
     * Returns icon described by this data class.
     *
     * @return icon described by this data class
     */
    public T getIcon ()
    {
        final T icon = loadIcon ();
        if ( adjustments != null )
        {
            for ( final IconAdjustment<T> adjustment : adjustments )
            {
                adjustment.apply ( icon );
            }
        }
        return icon;
    }

    /**
     * Returns loaded icon.
     *
     * @return loaded icon
     */
    protected abstract T loadIcon ();
}