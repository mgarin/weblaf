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

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Plugin library information data class.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("PluginLibrary")
public class PluginLibrary implements Serializable
{
    /**
     * Application-wide unique library ID.
     * This should be the same in different version of the same library.
     * Will be used to warn that this library getting loaded more than once, possibly with different version.
     */
    private String id;

    /**
     * Library title.
     */
    private String title;

    /**
     * Short library description.
     * Will be useful to understand what the hell this library is doing in this plugin.
     */
    private String description;

    /**
     * Plugin version.
     * Made string as different plugins might use lots of different versioning types.
     * Simply put any plugin version string here so version can be compared.
     */
    private String version;

    /**
     * Library file path relative to plugin file location.
     */
    private String file;

    /**
     * Constructs new plugin library information data object.
     */
    public PluginLibrary ()
    {
        super ();
    }

    /**
     * Constructs new plugin library information data object with the specified values.
     *
     * @param id          application-wide unique library ID
     * @param title       library title
     * @param description library description
     * @param version     library version
     * @param file        library file path
     */
    public PluginLibrary ( final String id, final String title, final String description, final String version, final String file )
    {
        super ();
        this.id = id;
        this.title = title;
        this.description = description;
        this.version = version;
        this.file = file;
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle ( final String title )
    {
        this.title = title;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription ( final String description )
    {
        this.description = description;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion ( final String version )
    {
        this.version = version;
    }

    public String getFile ()
    {
        return file;
    }

    public void setFile ( final String file )
    {
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return title + " " + version;
    }
}