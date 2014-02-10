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

import com.alee.managers.style.SupportedComponent;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * Skin information class.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "skin" )
public final class SkinInfo implements Serializable
{
    /**
     * Unique skin ID.
     * Used to collect and manage skins within StyleManager.
     */
    private String id;

    /**
     * Short skin name.
     * Might be used to display skin selection and options.
     */
    private String name;

    /**
     * Skin description.
     * You are free to put here any description you like.
     */
    private String description;

    /**
     * Skin author name.
     */
    private String author;

    /**
     * List of OS supported by this skin separated by "," character.
     * List of OS IDs constants can be found in SystemUtils class.
     * If skin supports all OS you can simply put "all" here.
     */
    private String supportedSystems;

    /**
     * List of skin styles.
     * This list contains all styling settings and painter directions.
     */
    @XStreamImplicit
    private List<ComponentStyle> styles;

    /**
     * Constructs new skin information.
     */
    public SkinInfo ()
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

    public String getName ()
    {
        return name;
    }

    public void setName ( final String name )
    {
        this.name = name;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription ( final String description )
    {
        this.description = description;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor ( final String author )
    {
        this.author = author;
    }

    public String getSupportedSystems ()
    {
        return supportedSystems;
    }

    public void setSupportedSystems ( final String supportedSystems )
    {
        this.supportedSystems = supportedSystems;
    }

    public List<ComponentStyle> getStyles ()
    {
        return styles;
    }

    public void setStyles ( final List<ComponentStyle> styles )
    {
        this.styles = styles;
    }

    /**
     * Returns list of supported OS.
     *
     * @return list of supported OS
     */
    public List<String> getSupportedSystemsList ()
    {
        return TextUtils.stringToList ( supportedSystems, "," );
    }

    /**
     * Returns style for the specified supported component type.
     *
     * @param type supported component type
     * @return component style
     */
    public ComponentStyle getStyle ( final SupportedComponent type )
    {
        for ( final ComponentStyle style : styles )
        {
            if ( style.getId () == type )
            {
                return style;
            }
        }
        return null;
    }
}