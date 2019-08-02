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
 * Plugin dependency data class.
 * It points to specific plugin which is used by pointing plugin.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "PluginDependency" )
public class PluginDependency implements Serializable
{
    /**
     * Dependency plugin ID.
     */
    private String pluginId;

    /**
     * Minimum plugin version.
     * If null minimum version is ignored.
     */
    private PluginVersion minVersion;

    /**
     * Maximum plugin version.
     * If null maximum version is ignored.
     */
    private PluginVersion maxVersion;

    /**
     * Whether this dependency is mandatory or not.
     * If this dependency is mandatory then plugin will not be loaded unless dependency is provided.
     */
    private Boolean optional;

    /**
     * Constructs new empty dependency.
     */
    public PluginDependency ()
    {
        super ();
        this.pluginId = null;
        this.minVersion = null;
        this.maxVersion = null;
        this.optional = null;
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId plugin ID
     */
    public PluginDependency ( final String pluginId )
    {
        super ();
        this.pluginId = pluginId;
        this.minVersion = null;
        this.maxVersion = null;
        this.optional = null;
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId   plugin ID
     * @param minVersion minimum plugin version
     */
    public PluginDependency ( final String pluginId, final PluginVersion minVersion )
    {
        super ();
        this.pluginId = pluginId;
        this.minVersion = minVersion;
        this.maxVersion = null;
        this.optional = null;
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId   plugin ID
     * @param minVersion minimum plugin version
     * @param maxVersion maximum plugin version
     */
    public PluginDependency ( final String pluginId, final PluginVersion minVersion, final PluginVersion maxVersion )
    {
        super ();
        this.pluginId = pluginId;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.optional = null;
    }

    /**
     * Constructs new dependency.
     *
     * @param pluginId   plugin ID
     * @param minVersion minimum plugin version
     * @param maxVersion maximum plugin version
     * @param optional   whether this dependency is mandatory or not
     */
    public PluginDependency ( final String pluginId, final PluginVersion minVersion, final PluginVersion maxVersion,
                              final Boolean optional )
    {
        super ();
        this.pluginId = pluginId;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.optional = optional;
    }

    /**
     * Returns plugin ID.
     *
     * @return plugin ID
     */
    public String getPluginId ()
    {
        return pluginId;
    }

    /**
     * Sets plugin ID.
     *
     * @param pluginId plugin ID
     */
    public void setPluginId ( final String pluginId )
    {
        this.pluginId = pluginId;
    }

    /**
     * Returns minimum plugin version.
     *
     * @return minimum plugin version
     */
    public PluginVersion getMinVersion ()
    {
        return minVersion;
    }

    /**
     * Sets minimum plugin version.
     *
     * @param minVersion minimum plugin version
     */
    public void setMinVersion ( final PluginVersion minVersion )
    {
        this.minVersion = minVersion;
    }

    /**
     * Returns maximum plugin version.
     *
     * @return maximum plugin version
     */
    public PluginVersion getMaxVersion ()
    {
        return maxVersion;
    }

    /**
     * Sets maximum plugin version.
     *
     * @param maxVersion maximum plugin version
     */
    public void setMaxVersion ( final PluginVersion maxVersion )
    {
        this.maxVersion = maxVersion;
    }

    /**
     * Returns whether this dependency is mandatory or not.
     *
     * @return true or null if this dependency is mandatory, false otherwise
     */
    public Boolean getOptional ()
    {
        return optional;
    }

    /**
     * Returns whether this dependency is mandatory or not.
     *
     * @return true if this dependency is mandatory, false otherwise
     */
    public boolean isOptional ()
    {
        return optional != null && optional;
    }

    /**
     * Sets whether this dependency is mandatory or not.
     *
     * @param optional whether this dependency is mandatory or not
     */
    public void setOptional ( final Boolean optional )
    {
        this.optional = optional;
    }

    /**
     * Returns whether plugin with the specified information is accepted by this dependency or not.
     *
     * @param plugin plugin information to check
     * @return {@code true} if plugin with the specified information is accepted by this dependency, {@code false} otherwise
     */
    public boolean accept ( final PluginInformation plugin )
    {
        if ( plugin != null && plugin.getId ().equals ( pluginId ) )
        {
            final PluginVersion pv = plugin.getVersion ();
            return ( minVersion == null || pv.isNewerOrSame ( minVersion ) ) && ( maxVersion == null || pv.isOlderOrSame ( maxVersion ) );
        }
        return false;
    }

    @Override
    public String toString ()
    {
        return pluginId + ( minVersion != null || maxVersion != null ? " " : "" ) + getVersionString ( "min", minVersion ) +
                ( minVersion != null ? " " : "" ) + getVersionString ( "max", maxVersion );
    }

    /**
     * Returns version string.
     *
     * @param prefix        version string prefix
     * @param pluginVersion plugin version
     * @return version string
     */
    public String getVersionString ( final String prefix, final PluginVersion pluginVersion )
    {
        return pluginVersion != null ? "[ " + prefix + ": " + pluginVersion + " ]" : "";
    }
}