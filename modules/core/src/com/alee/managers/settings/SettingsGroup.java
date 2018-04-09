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
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings group data class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsManager
 */
@XStreamAlias ( "SettingsGroup" )
@XStreamConverter ( SettingsConverter.class )
public class SettingsGroup implements Serializable
{
    /**
     * Identifier prefix.
     */
    private static final String ID_PREFIX = "SG";

    /**
     * Group identifier.
     */
    private String id;

    /**
     * Group name.
     */
    private String name;

    /**
     * Settings map.
     */
    private HashMap<String, Object> settings;

    /**
     * Constructs new {@link SettingsGroup}.
     */
    public SettingsGroup ()
    {
        this ( null );
    }

    /**
     * Constructs new {@link SettingsGroup}.
     *
     * @param name group name
     */
    public SettingsGroup ( final String name )
    {
        super ();
        this.id = TextUtils.generateId ( ID_PREFIX );
        this.name = name;
    }

    /**
     * Constructs new {@link SettingsGroup}.
     *
     * @param id   group identifier
     * @param name group name
     */
    public SettingsGroup ( final String id, final String name )
    {
        super ();
        this.id = id;
        this.name = name;
    }

    /**
     * Returns group identifier.
     *
     * @return group identifier
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Sets group identifier.
     *
     * @param id new group identifier
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns group name.
     *
     * @return group name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets group name.
     *
     * @param name new group name
     */
    public void setName ( final String name )
    {
        this.name = name;
    }

    /**
     * Returns settings map.
     *
     * @return settings map
     */
    public Map<String, Object> settings ()
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
    public void setSettings ( final HashMap<String, Object> settings )
    {
        this.settings = settings;
    }

    /**
     * Returns value for specified key.
     *
     * @param key key
     * @param <T> value type
     * @return value for specified key
     */
    public <T> T get ( final String key )
    {
        return ( T ) settings ().get ( key );
    }

    /**
     * Removes settings saved under the specified key.
     *
     * @param key settings key
     * @param <T> value type
     * @return settings previously saved under the specified key
     */
    public <T> T remove ( final String key )
    {
        return ( T ) settings ().remove ( key );
    }

    /**
     * Puts value under the specified key.
     *
     * @param key    key
     * @param object value
     * @param <T>    value type
     * @return previous value for the specified key
     */
    public <T> T put ( final String key, final T object )
    {
        return ( T ) settings ().put ( key, object );
    }
}