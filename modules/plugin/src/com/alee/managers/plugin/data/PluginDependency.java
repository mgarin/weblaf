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
 * Plugin dependency data class.
 * It points to specific plugin which is used by pointing plugin.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "PluginDependency" )
public class PluginDependency implements Serializable
{
    /**
     * Identifier of the {@link Plugin} this dependency is pointing at.
     */
    @Nullable
    private String pluginId;

    /**
     * Minimum {@link Plugin} version.
     * If null minimum version is ignored.
     */
    @Nullable
    private PluginVersion minVersion;

    /**
     * Maximum {@link Plugin} version.
     * If null maximum version is ignored.
     */
    @Nullable
    private PluginVersion maxVersion;

    /**
     * Whether or not this dependency is mandatory.
     * If this dependency is mandatory then plugin will not be loaded unless dependency is provided.
     */
    @Nullable
    private Boolean optional;

    /**
     * Constructs new empty {@link PluginDependency}.
     * This constructor is not intended for public use and only added for XStream deserializer.
     */
    public PluginDependency ()
    {
        this.pluginId = null;
        this.minVersion = null;
        this.maxVersion = null;
        this.optional = null;
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId identifier of the {@link Plugin} this dependency is pointing at
     */
    public PluginDependency ( @NotNull final String pluginId )
    {
        this ( pluginId, null, null, null );
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId   identifier of the {@link Plugin} this dependency is pointing at
     * @param minVersion minimum {@link Plugin} version
     */
    public PluginDependency ( @NotNull final String pluginId, @Nullable final PluginVersion minVersion )
    {
        this ( pluginId, minVersion, null, null );
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId   identifier of the {@link Plugin} this dependency is pointing at
     * @param minVersion minimum {@link Plugin} version
     * @param maxVersion maximum {@link Plugin} version
     */
    public PluginDependency ( final String pluginId, @Nullable final PluginVersion minVersion, @Nullable final PluginVersion maxVersion )
    {
        this ( pluginId, minVersion, maxVersion, null );
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId   identifier of the {@link Plugin} this dependency is pointing at
     * @param minVersion minimum {@link Plugin} version
     * @param maxVersion maximum {@link Plugin} version
     * @param optional   whether this dependency is mandatory or not
     */
    public PluginDependency ( @NotNull final String pluginId, @Nullable final PluginVersion minVersion,
                              @Nullable final PluginVersion maxVersion, @Nullable final Boolean optional )
    {
        this.pluginId = pluginId;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.optional = optional;
    }

    /**
     * Returns identifier of the {@link Plugin} this dependency is pointing at.
     *
     * @return identifier of the {@link Plugin} this dependency is pointing at
     */
    @NotNull
    public String getPluginId ()
    {
        if ( pluginId == null )
        {
            throw new PluginException ( "Plugin dependency was not properly configured yet" );
        }
        return pluginId;
    }

    /**
     * Sets identifier of the {@link Plugin} this dependency is pointing at.
     *
     * @param pluginId new identifier of the {@link Plugin} this dependency is pointing at
     */
    public void setPluginId ( @NotNull final String pluginId )
    {
        this.pluginId = pluginId;
    }

    /**
     * Returns minimum {@link Plugin} version.
     *
     * @return minimum {@link Plugin} version
     */
    @Nullable
    public PluginVersion getMinVersion ()
    {
        return minVersion;
    }

    /**
     * Sets minimum {@link Plugin} version.
     *
     * @param minVersion minimum {@link Plugin} version
     */
    public void setMinVersion ( @Nullable final PluginVersion minVersion )
    {
        this.minVersion = minVersion;
    }

    /**
     * Returns maximum {@link Plugin} version.
     *
     * @return maximum {@link Plugin} version
     */
    @Nullable
    public PluginVersion getMaxVersion ()
    {
        return maxVersion;
    }

    /**
     * Sets maximum {@link Plugin} version.
     *
     * @param maxVersion maximum {@link Plugin} version
     */
    public void setMaxVersion ( @Nullable final PluginVersion maxVersion )
    {
        this.maxVersion = maxVersion;
    }

    /**
     * Returns whether or not this dependency is mandatory.
     *
     * @return {@code true} or {@code null} if this dependency is mandatory, {@code false} otherwise
     */
    @Nullable
    public Boolean getOptional ()
    {
        return optional;
    }

    /**
     * Returns whether this dependency is mandatory or not.
     *
     * @return {@code true} if this dependency is mandatory, {@code false} otherwise
     */
    public boolean isOptional ()
    {
        return optional != null && optional;
    }

    /**
     * Sets whether or not this dependency is mandatory.
     *
     * @param optional whether or not this dependency is mandatory
     */
    public void setOptional ( @Nullable final Boolean optional )
    {
        this.optional = optional;
    }

    /**
     * Returns whether or not {@link Plugin} with the specified information is accepted by this dependency.
     *
     * @param plugin {@link PluginInformation} to check
     * @return {@code true} if {@link Plugin} with the specified information is accepted by this dependency, {@code false} otherwise
     */
    public boolean accept ( @Nullable final PluginInformation plugin )
    {
        boolean accept = false;
        if ( plugin != null && plugin.getId ().equals ( pluginId ) )
        {
            final PluginVersion pv = plugin.getVersion ();
            accept = ( minVersion == null || pv.isNewerOrSame ( minVersion ) )
                    && ( maxVersion == null || pv.isOlderOrSame ( maxVersion ) );
        }
        return accept;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return pluginId
                + ( minVersion != null || maxVersion != null ? " " : "" )
                + getVersionString ( "min", minVersion )
                + ( minVersion != null ? " " : "" )
                + getVersionString ( "max", maxVersion );
    }

    /**
     * Returns version string.
     *
     * @param prefix        version string prefix
     * @param pluginVersion plugin version
     * @return version string
     */
    @NotNull
    public String getVersionString ( @NotNull final String prefix, @Nullable final PluginVersion pluginVersion )
    {
        return pluginVersion != null ? "[ " + prefix + ": " + pluginVersion + " ]" : "";
    }
}