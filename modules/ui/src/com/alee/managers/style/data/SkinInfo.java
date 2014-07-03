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

import com.alee.managers.log.Log;
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
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 */

@XStreamAlias ( "skin" )
@XStreamConverter ( SkinInfoConverter.class )
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
     * Skin class canonical name.
     * Used to locate included resources.
     */
    @XStreamAlias ( "class" )
    private String skinClass;

    /**
     * List of skin styles.
     * This list contains all styling settings and painter directions.
     */
    @XStreamImplicit
    private List<ComponentStyle> styles;

    /**
     * Skin styles cache map.
     */
    private transient Map<SupportedComponent, Map<String, ComponentStyle>> stylesCache;

    /**
     * Constructs new skin information.
     */
    public SkinInfo ()
    {
        super ();
    }

    /**
     * Returns skin ID.
     *
     * @return skin ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Sets skin ID.
     *
     * @param id new skin ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns skin name.
     *
     * @return skin name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets skin name.
     *
     * @param name new skin name
     */
    public void setName ( final String name )
    {
        this.name = name;
    }

    /**
     * Returns skin description.
     *
     * @return skin description
     */
    public String getDescription ()
    {
        return description;
    }

    /**
     * Sets skin description.
     *
     * @param description new skin description
     */
    public void setDescription ( final String description )
    {
        this.description = description;
    }

    /**
     * Returns skin author.
     *
     * @return skin author
     */
    public String getAuthor ()
    {
        return author;
    }

    /**
     * Sets skin author.
     *
     * @param author new skin author
     */
    public void setAuthor ( final String author )
    {
        this.author = author;
    }

    /**
     * Returns supported systems.
     *
     * @return supported systems
     */
    public String getSupportedSystems ()
    {
        return supportedSystems;
    }

    /**
     * Returns supported systems list.
     *
     * @return supported systems list
     */
    public List<String> getSupportedSystemsList ()
    {
        return TextUtils.stringToList ( supportedSystems, "," );
    }

    /**
     * Sets supported systems.
     *
     * @param supportedSystems supported systems
     */
    public void setSupportedSystems ( final String supportedSystems )
    {
        this.supportedSystems = supportedSystems;
    }

    /**
     * Sets supported systems.
     *
     * @param supportedSystems supported systems
     */
    public void setSupportedSystems ( final List<String> supportedSystems )
    {
        this.supportedSystems = TextUtils.listToString ( supportedSystems, "," );
    }

    /**
     * Returns skin class canonical name.
     *
     * @return skin class canonical name
     */
    public String getSkinClass ()
    {
        return skinClass;
    }

    /**
     * Sets skin class canonical name.
     *
     * @param skinClass new skin class canonical name
     */
    public void setSkinClass ( final String skinClass )
    {
        this.skinClass = skinClass;
    }

    /**
     * Returns skin styles.
     *
     * @return skin styles
     */
    public List<ComponentStyle> getStyles ()
    {
        return styles;
    }

    /**
     * Sets skin styles.
     *
     * @param styles new skin styles
     */
    public void setStyles ( final List<ComponentStyle> styles )
    {
        this.styles = styles;
    }

    /**
     * Returns skin styles cache map.
     *
     * @return skin styles cache map
     */
    public Map<SupportedComponent, Map<String, ComponentStyle>> getStylesCache ()
    {
        return stylesCache;
    }

    /**
     * Sets skin styles cache map.
     *
     * @param stylesCache new skin styles cache map
     */
    public void setStylesCache ( final Map<SupportedComponent, Map<String, ComponentStyle>> stylesCache )
    {
        this.stylesCache = stylesCache;
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
            if ( style != null )
            {
                // We have found required style
                return style;
            }
            else
            {
                // Required style cannot be found, using default style
                Log.error ( this, "Unable to find style with ID \"" + styleId + "\" for component: " + component );
                return componentStyles.get ( ComponentStyleConverter.DEFAULT_STYLE_ID );
            }
        }
        else
        {
            return null;
        }
    }
}