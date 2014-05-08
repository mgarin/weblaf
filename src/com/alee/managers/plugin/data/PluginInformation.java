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

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * Plugin information data class.
 * This data will be serialized into XML and placed within plugin as its descriptor.
 * It will be read each time PluginManager attempt to load its plugins.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("PluginInformation")
public class PluginInformation implements Serializable
{
    /**
     * Application-wide unique plugin ID.
     * This should be the same in different version of the same plugin.
     * Will be used to warn that this plugin getting loaded more than once, possibly with different version.
     */
    private String id;

    /**
     * This specific plugin part type.
     * Tells where exactly this plugin part (specific JAR with this descriptor) can be used.
     * This property can be used to limit plugins from loading on different application sides (for example client/server).
     */
    private String type;

    /**
     * All available types of this plugin.
     * Tells where each part of this plugin is placed.
     */
    private String types;

    /**
     * Whether plugin can be disabled or not.
     */
    private boolean disableable;

    /**
     * Plugin main class.
     */
    private String mainClass;

    /**
     * Plugin title.
     */
    private String title;

    /**
     * Short plugin description.
     */
    private String description;

    /**
     * Plugin version.
     */
    private PluginVersion version;

    /**
     * Libraries used by this plugin.
     */
    @XStreamImplicit
    private List<PluginLibrary> libraries;

    /**
     * Constructs new plugin information data object.
     */
    public PluginInformation ()
    {
        super ();
    }

    /**
     * Constructs new plugin information data object with the specified values.
     *
     * @param id          plugin ID
     * @param type        plugin type
     * @param types       available plugin types
     * @param disableable whether plugin can be disabled or not
     * @param mainClass   plugin main class
     * @param title       plugin title
     * @param description plugin short description
     * @param version     plugin version data
     */
    public PluginInformation ( final String id, final String type, final String types, final boolean disableable, final String mainClass,
                               final String title, final String description, final PluginVersion version,
                               final List<PluginLibrary> libraries )
    {
        super ();
        this.id = id;
        this.type = type;
        this.types = types;
        this.disableable = disableable;
        this.mainClass = mainClass;
        this.title = title;
        this.description = description;
        this.version = version;
        this.libraries = libraries;
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType ( final String type )
    {
        this.type = type;
    }

    public String getTypes ()
    {
        return types;
    }

    public List<String> getActualTypes ()
    {
        return TextUtils.stringToList ( types, "," );
    }

    public void setTypes ( final String types )
    {
        this.types = types;
    }

    public boolean isDisableable ()
    {
        return disableable;
    }

    public void setDisableable ( final boolean disableable )
    {
        this.disableable = disableable;
    }

    public String getMainClass ()
    {
        return mainClass;
    }

    public void setMainClass ( final String mainClass )
    {
        this.mainClass = mainClass;
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

    public PluginVersion getVersion ()
    {
        return version;
    }

    public void setVersion ( final PluginVersion version )
    {
        this.version = version;
    }

    public List<PluginLibrary> getLibraries ()
    {
        return libraries;
    }

    public void setLibraries ( final List<PluginLibrary> libraries )
    {
        this.libraries = libraries;
    }

    /**
     * Returns whether plugin uses any libraries or not.
     *
     * @return true if plugin uses any libraries, false otherwise
     */
    public boolean hasLibraries ()
    {
        return libraries != null && libraries.size () > 0;
    }

    /**
     * Returns plugin libraries count.
     *
     * @return plugin libraries count
     */
    public int getLibrariesCount ()
    {
        return libraries != null && libraries.size () > 0 ? libraries.size () : 0;
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