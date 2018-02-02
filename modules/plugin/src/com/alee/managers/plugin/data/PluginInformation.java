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
 * {@link com.alee.managers.plugin.Plugin} information data class.
 * This data will be serialized into XML and placed within plugin as its descriptor.
 * It will be read each time PluginManager attempt to load its plugins.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 * @see com.alee.managers.plugin.Plugin
 */

@XStreamAlias ( "PluginInformation" )
public class PluginInformation implements Serializable
{
    /**
     * Application-wide unique plugin ID.
     * This should be the same in different version of the same plugin.
     * Will be used to warn that this plugin getting loaded more than once, possibly with different version.
     */
    private String id;

    /**
     * This specific plugin custom type.
     * Tells where exactly this plugin (loaded from specific JAR with this descriptor) can be used.
     * This property can be used to limit plugins from loading on different application sides (for example client/server/web/etc.).
     */
    private String type;

    /**
     * All available custom types of this plugin.
     * Tells where different parts of this plugin can be placed.
     * It might be useful to know on each application side where specific plugin can be placed.
     */
    private String types;

    /**
     * Whether plugin can be disabled or not.
     * Note that plugin enabling/disabling implementation is up to plugin developer.
     */
    private boolean disableable;

    /**
     * Plugin main class canonical name.
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
     * These are additional libraries which are loaded only for this plugin.
     * They should not affect the general application since used in custom sub-classloader.
     */
    @XStreamImplicit
    private List<PluginLibrary> libraries;

    /**
     * Other plugins used required to run this one.
     * Other plugins must be loaded first so this can initialize properly.
     */
    @XStreamImplicit
    private List<PluginDependency> dependencies;

    /**
     * Constructs new plugin information data object.
     * This constructor might be used by XStream in some cases to create information instance from plugin XML descriptor.
     */
    public PluginInformation ()
    {
        super ();
    }

    /**
     * Constructs new plugin information data object with the specified values.
     * This constructor can be used to create programmatical plugin information.
     *
     * @param id          plugin ID
     * @param disableable whether plugin can be disabled or not
     * @param mainClass   plugin main class
     * @param title       plugin title
     * @param description plugin short description
     */
    public PluginInformation ( final String id, final boolean disableable, final String mainClass, final String title,
                               final String description )
    {
        this ( id, null, null, disableable, mainClass, title, description, PluginVersion.DEFAULT, null, null );
    }

    /**
     * Constructs new plugin information data object with the specified values.
     * This constructor can be used to create programmatical plugin information.
     *
     * @param id          plugin ID
     * @param disableable whether plugin can be disabled or not
     * @param mainClass   plugin main class
     * @param title       plugin title
     * @param description plugin short description
     * @param version     plugin version data
     */
    public PluginInformation ( final String id, final boolean disableable, final String mainClass, final String title,
                               final String description, final PluginVersion version )
    {
        this ( id, null, null, disableable, mainClass, title, description, version, null, null );
    }

    /**
     * Constructs new plugin information data object with the specified values.
     * This constructor can be used to create programmatical plugin information.
     *
     * @param id          plugin ID
     * @param type        plugin type
     * @param types       available plugin types
     * @param disableable whether plugin can be disabled or not
     * @param mainClass   plugin main class
     * @param title       plugin title
     * @param description plugin short description
     * @param version     plugin version data
     * @param libraries   plugin libraries
     */
    public PluginInformation ( final String id, final String type, final String types, final boolean disableable, final String mainClass,
                               final String title, final String description, final PluginVersion version,
                               final List<PluginLibrary> libraries )
    {
        this ( id, type, types, disableable, mainClass, title, description, version, libraries, null );
    }

    /**
     * Constructs new plugin information data object with the specified values.
     * This constructor can be used to create programmatical plugin information.
     *
     * @param id           plugin ID
     * @param type         plugin type
     * @param types        available plugin types
     * @param disableable  whether plugin can be disabled or not
     * @param mainClass    plugin main class
     * @param title        plugin title
     * @param description  plugin short description
     * @param version      plugin version data
     * @param libraries    plugin libraries
     * @param dependencies plugin dependencies
     */
    public PluginInformation ( final String id, final String type, final String types, final boolean disableable, final String mainClass,
                               final String title, final String description, final PluginVersion version,
                               final List<PluginLibrary> libraries, final List<PluginDependency> dependencies )
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
        this.dependencies = dependencies;
    }

    /**
     * Returns plugin ID.
     *
     * @return plugin ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Sets plugin ID.
     *
     * @param id new plugin ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns custom plugin type.
     *
     * @return custom plugin type
     */
    public String getType ()
    {
        return type;
    }

    /**
     * Sets custom plugin type.
     *
     * @param type new custom plugin type
     */
    public void setType ( final String type )
    {
        this.type = type;
    }

    /**
     * Returns all custom plugin types.
     *
     * @return all custom plugin types
     */
    public String getTypes ()
    {
        return types;
    }

    /**
     * Returns all custom plugin types list.
     *
     * @return all custom plugin types list
     */
    public List<String> getTypesList ()
    {
        return TextUtils.stringToList ( types, "," );
    }

    /**
     * Sets custom plugin types.
     *
     * @param types new custom plugin types
     */
    public void setTypes ( final String types )
    {
        this.types = types;
    }

    /**
     * Sets custom plugin types.
     *
     * @param types new custom plugin types
     */
    public void setTypes ( final List<String> types )
    {
        this.types = TextUtils.listToString ( types, "," );
    }

    /**
     * Returns whether this plugin is disableable or not.
     *
     * @return true if this plugin is disableable, false otherwise
     */
    public boolean isDisableable ()
    {
        return disableable;
    }

    /**
     * Sets whether this plugin is disableable or not.
     *
     * @param disableable whether this plugin is disableable or not
     */
    public void setDisableable ( final boolean disableable )
    {
        this.disableable = disableable;
    }

    /**
     * Returns plugin main class canonical name.
     *
     * @return plugin main class canonical name
     */
    public String getMainClass ()
    {
        return mainClass;
    }

    /**
     * Sets plugin main class canonical name.
     *
     * @param mainClass new plugin main class canonical name
     */
    public void setMainClass ( final String mainClass )
    {
        this.mainClass = mainClass;
    }

    /**
     * Returns plugin title.
     *
     * @return plugin title
     */
    public String getTitle ()
    {
        return title;
    }

    /**
     * Sets plugin title.
     *
     * @param title new plugin title
     */
    public void setTitle ( final String title )
    {
        this.title = title;
    }

    /**
     * Returns plugin description.
     *
     * @return plugin description
     */
    public String getDescription ()
    {
        return description;
    }

    /**
     * Sets plugin description.
     *
     * @param description new plugin description
     */
    public void setDescription ( final String description )
    {
        this.description = description;
    }

    /**
     * Returns plugin version.
     *
     * @return plugin version
     */
    public PluginVersion getVersion ()
    {
        return version;
    }

    /**
     * Sets plugin version.
     *
     * @param version new plugin version
     */
    public void setVersion ( final PluginVersion version )
    {
        this.version = version;
    }

    /**
     * Returns plugin libraries list.
     *
     * @return plugin libraries list
     */
    public List<PluginLibrary> getLibraries ()
    {
        return libraries;
    }

    /**
     * Sets plugin libraries list.
     *
     * @param libraries new plugin libraries list
     */
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
     * Returns plugins required to run this one.
     *
     * @return plugins required to run this one
     */
    public List<PluginDependency> getDependencies ()
    {
        return dependencies;
    }

    /**
     * Sets plugins required to run this one.
     *
     * @param dependencies plugins required to run this one
     */
    public void setDependencies ( final List<PluginDependency> dependencies )
    {
        this.dependencies = dependencies;
    }

    /**
     * Returns whether plugin has any plugin dependencies or not.
     *
     * @return true if plugin has any plugin dependencies, false otherwise
     */
    public boolean hasDependencies ()
    {
        return dependencies != null && dependencies.size () > 0;
    }

    /**
     * Returns plugin dependencies count.
     *
     * @return plugin dependencies count
     */
    public int getDependenciesCount ()
    {
        return dependencies != null && dependencies.size () > 0 ? dependencies.size () : 0;
    }

    @Override
    public String toString ()
    {
        return title + " " + version;
    }
}