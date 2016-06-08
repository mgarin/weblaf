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

package com.alee.managers.icon.set;

import com.alee.api.Identifiable;
import com.alee.managers.icon.data.IconData;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * Basic icon set (collection) information.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("IconSet")
public final class IconSetData implements Identifiable, Serializable
{
    /**
     * Icon set ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Class which specified icon paths are relative to.
     */
    @XStreamAsAttribute
    private Class nearClass;

    /**
     * Icon files base path.
     */
    @XStreamAsAttribute
    private String base;

    /**
     * List of set icons.
     */
    @XStreamImplicit
    protected List<IconData> icons;

    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Sets icon set ID.
     *
     * @param id icon set ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns class which specified icon paths are relative to.
     *
     * @return class which specified icon paths are relative to
     */
    public Class getNearClass ()
    {
        return nearClass;
    }

    /**
     * Sets class which specified icon paths are relative to.
     *
     * @param nearClass class which specified icon paths are relative to
     */
    public void setNearClass ( final Class nearClass )
    {
        this.nearClass = nearClass;
    }

    /**
     * Returns icon files base path.
     *
     * @return icon files base path
     */
    public String getBase ()
    {
        return base;
    }

    /**
     * Sets icon files base path.
     *
     * @param base icon files base path
     */
    public void setBase ( final String base )
    {
        this.base = base;
    }

    /**
     * Returns list of set icons.
     *
     * @return list of set icons
     */
    public List<IconData> getIcons ()
    {
        return icons;
    }

    /**
     * Sets list of set icons.
     *
     * @param icons list of set icons
     */
    public void setIcons ( final List<IconData> icons )
    {
        this.icons = icons;
    }
}