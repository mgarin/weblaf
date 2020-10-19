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

package com.alee.managers.plugin.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.plugin.Plugin;
import com.alee.managers.plugin.PluginException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Information about single {@link Plugin} library.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 * @see Plugin
 */
@XStreamAlias ( "PluginLibrary" )
public class PluginLibrary implements Serializable
{
    /**
     * Unique library identifier.
     * This identifier must remain the same between different version of the library.
     */
    @Nullable
    private String id;

    /**
     * Library title.
     */
    @Nullable
    private String title;

    /**
     * Library description.
     */
    @Nullable
    private String description;

    /**
     * Library version.
     * Version formats vary between application so it is up to developers which format they will be using.
     */
    @Nullable
    private String version;

    /**
     * Library file path relative to {@link Plugin} file location.
     */
    @Nullable
    private String file;

    /**
     * Constructs new empty {@link PluginLibrary}.
     * This constructor is not intended for public use and only added for XStream deserializer.
     */
    public PluginLibrary ()
    {
        this.id = null;
        this.title = null;
        this.description = null;
        this.version = null;
        this.file = null;
    }

    /**
     * Constructs new empty {@link PluginLibrary} with the specified settings.
     *
     * @param id          unique library identifier
     * @param title       library title
     * @param description library description
     * @param version     library version
     * @param file        library file path relative to {@link Plugin} file location
     */
    public PluginLibrary ( @NotNull final String id, @NotNull final String title, @Nullable final String description,
                           @NotNull final String version, @NotNull final String file )
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.version = version;
        this.file = file;
    }

    /**
     * Returns unique library identifier.
     *
     * @return unique library identifier
     */
    @NotNull
    public String getId ()
    {
        if ( id == null )
        {
            throw new PluginException ( "Plugin library was not properly configured yet" );
        }
        return id;
    }

    /**
     * Sets unique library identifier.
     *
     * @param id new unique library identifier
     */
    public void setId ( @NotNull final String id )
    {
        this.id = id;
    }

    /**
     * Returns library title.
     *
     * @return library title
     */
    @NotNull
    public String getTitle ()
    {
        if ( title == null )
        {
            throw new PluginException ( "Plugin library was not properly configured yet" );
        }
        return title;
    }

    /**
     * Sets library title.
     *
     * @param title new library title
     */
    public void setTitle ( @NotNull final String title )
    {
        this.title = title;
    }

    /**
     * Returns library description.
     *
     * @return library description
     */
    @Nullable
    public String getDescription ()
    {
        return description;
    }

    /**
     * Sets library description.
     *
     * @param description new library description
     */
    public void setDescription ( @Nullable final String description )
    {
        this.description = description;
    }

    /**
     * Returns library version.
     *
     * @return library version
     */
    @NotNull
    public String getVersion ()
    {
        if ( version == null )
        {
            throw new PluginException ( "Plugin library was not properly configured yet" );
        }
        return version;
    }

    /**
     * Sets library version.
     *
     * @param version new library version
     */
    public void setVersion ( @NotNull final String version )
    {
        this.version = version;
    }

    /**
     * Returns library file path relative to {@link Plugin} file location.
     *
     * @return library file path relative to {@link Plugin} file location
     */
    @NotNull
    public String getFile ()
    {
        if ( file == null )
        {
            throw new PluginException ( "Plugin library was not properly configured yet" );
        }
        return file;
    }

    /**
     * Sets library file path relative to {@link Plugin} file location.
     *
     * @param file new library file path relative to {@link Plugin} file location
     */
    public void setFile ( @NotNull final String file )
    {
        this.file = file;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return title + " " + version;
    }
}