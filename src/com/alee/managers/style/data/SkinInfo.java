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
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Skin information class.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "skin" )
@XStreamConverter (SkinInfoConverter.class)
public final class SkinInfo implements Serializable
{
    /**
     * Skin styles cache map.
     */
    private transient Map<SupportedComponent, Map<String, ComponentStyle>> stylesCache;

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
     * Sking class canonical name.
     * Used to locate included resources.
     */
    @XStreamAlias ("class")
    private String skinClass;

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

    public Map<SupportedComponent, Map<String, ComponentStyle>> getStylesCache ()
    {
        return stylesCache;
    }

    public void setStylesCache ( final Map<SupportedComponent, Map<String, ComponentStyle>> stylesCache )
    {
        this.stylesCache = stylesCache;
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

    public String getSkinClass ()
    {
        return skinClass;
    }

    public void setSkinClass ( final String skinClass )
    {
        this.skinClass = skinClass;
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
     * Custom style ID can be specified in any Web-component or Web-UI to override default component style.
     * If style for such custom ID is not found in skin descriptor then default style for that component is used.
     *
     * @param component component we are looking style for
     * @param type      supported component type
     * @return component style
     */
    public ComponentStyle getStyle ( final JComponent component, final SupportedComponent type )
    {
        final Map<String, ComponentStyle> componentStyles = stylesCache.get ( type );
        if ( componentStyles != null )
        {
            final String styleId = type.getComponentStyleId ( component );
            final ComponentStyle style = componentStyles.get ( styleId );
            return style != null ? style : componentStyles.get ( ComponentStyleConverter.DEFAULT_STYLE_ID );
        }
        else
        {
            return null;
        }
    }
}