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

package com.alee.managers.settings;

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings group data class.
 *
 * @author Mikle Garin
 * @since 1.4
 */

@XStreamAlias ( "SettingsGroup" )
public class SettingsGroup implements Serializable
{
    /**
     * ID prefix.
     */
    private static final String ID_PREFIX = "SG";

    /**
     * Unique ID.
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Unique name.
     */
    @XStreamAsAttribute
    private String name;

    /**
     * Settings map.
     */
    @XStreamImplicit
    // todo @XStreamConverter ( StringMapConverter.class )
    private Map<String, Object> settings;

    /**
     * Constructs unnamed SettingsGroup.
     */
    public SettingsGroup ()
    {
        this ( null );
    }

    /**
     * Constructs SettingsGroup with the specified name.
     *
     * @param name SettingsGroup name
     */
    public SettingsGroup ( String name )
    {
        super ();
        this.id = TextUtils.generateId ( ID_PREFIX );
        this.name = name;
    }

    /**
     * Returns SettingsGroup unique ID.
     *
     * @return SettingsGroup unique ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Sets unique ID.
     *
     * @param id new unique ID
     */
    public void setId ( String id )
    {
        this.id = id;
    }

    /**
     * Returns unique name.
     *
     * @return unique name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets unique name.
     *
     * @param name new unique name
     */
    public void setName ( String name )
    {
        this.name = name;
    }

    /**
     * Returns settings map.
     *
     * @return settings map
     */
    public Map<String, Object> getSettings ()
    {
        if ( settings == null )
        {
            settings = new HashMap<String, Object> ();
        }
        return settings;
    }

    /**
     * Sets settings map.
     *
     * @param settings new settings map
     */
    public void setSettings ( Map<String, Object> settings )
    {
        this.settings = settings;
    }

    /**
     * Returns value for specified key.
     *
     * @param key key
     * @return value for specified key
     */
    public Object get ( String key )
    {
        return getSettings ().get ( key );
    }

    /**
     * Puts value under the specified key.
     *
     * @param key    key
     * @param object value
     * @param <T>    value type
     * @return previous value for the specified key
     */
    public <T> T put ( String key, T object )
    {
        return ( T ) getSettings ().put ( key, object );
    }
}