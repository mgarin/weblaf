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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Plugin version data class.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("PluginVersion")
public class PluginVersion implements Serializable
{
    /**
     * Major plugin version.
     */
    @XStreamAsAttribute
    private int major;

    /**
     * Minor plugin version.
     */
    @XStreamAsAttribute
    private int minor;

    /**
     * Constructs new plugin version data object.
     */
    public PluginVersion ()
    {
        super ();
    }

    /**
     * Constructs new plugin version data object with the specified major and minor version numbers.
     *
     * @param major major version number
     * @param minor minor version number
     */
    public PluginVersion ( final int major, final int minor )
    {
        super ();
        this.major = major;
        this.minor = minor;
    }

    public int getMajor ()
    {
        return major;
    }

    public void setMajor ( final int major )
    {
        this.major = major;
    }

    public int getMinor ()
    {
        return minor;
    }

    public void setMinor ( final int minor )
    {
        this.minor = minor;
    }

    public boolean newerThan ( final PluginVersion otherVersion )
    {
        return this.major > otherVersion.major || this.major == otherVersion.major && this.minor > otherVersion.minor;
    }

    public boolean olderThan ( final PluginVersion otherVersion )
    {
        return otherVersion.major > this.major || otherVersion.major == this.major && otherVersion.minor > this.minor;
    }

    public boolean same ( final PluginVersion otherVersion )
    {
        return otherVersion.major == this.major && otherVersion.minor == this.minor;
    }

    @Override
    public String toString ()
    {
        return "v" + major + "." + minor;
    }
}