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

import com.alee.utils.CompareUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Plugin version data class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */

@XStreamAlias ( "PluginVersion" )
public class PluginVersion implements Serializable
{
    /**
     * Simple default v1.0.0 version.
     */
    public static final PluginVersion DEFAULT = new PluginVersion ( 1, 0, 0 );

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
     * Plugin build version.
     */
    @XStreamAsAttribute
    private Integer build;

    /**
     * Constructs new plugin version data object.
     */
    public PluginVersion ()
    {
        super ();
        this.major = DEFAULT.major;
        this.minor = DEFAULT.minor;
        this.build = DEFAULT.build;
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
        this.build = null;
    }

    /**
     * Constructs new plugin version data object with the specified major and minor version numbers.
     *
     * @param major major version number
     * @param minor minor version number
     * @param build build version number
     */
    public PluginVersion ( final int major, final int minor, final Integer build )
    {
        super ();
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    /**
     * Returns major plugin version.
     *
     * @return major plugin version
     */
    public int getMajor ()
    {
        return major;
    }

    /**
     * Sets major plugin version.
     *
     * @param major new major plugin version
     */
    public void setMajor ( final int major )
    {
        this.major = major;
    }

    /**
     * Returns minor plugin version.
     *
     * @return minor plugin version
     */
    public int getMinor ()
    {
        return minor;
    }

    /**
     * Sets minor plugin version
     *
     * @param minor new minor plugin version
     */
    public void setMinor ( final int minor )
    {
        this.minor = minor;
    }

    /**
     * Returns plugin build version.
     * Might return null in case build is not specified.
     *
     * @return plugin build version
     */
    public Integer getBuild ()
    {
        return build;
    }

    /**
     * Sets plugin build version.
     * You might want to set it to null in case you don't need build number.
     *
     * @param build plugin build version
     */
    public void setBuild ( final Integer build )
    {
        this.build = build;
    }

    /**
     * Returns whether this plugin version is newer than the specified one or not.
     *
     * @param ov other plugin version
     * @return true if this plugin version is newer than the specified one, false otherwise
     */
    public boolean isNewerThan ( final PluginVersion ov )
    {
        return this.major > ov.major ||
                this.major == ov.major && this.minor > ov.minor ||
                this.major == ov.major && this.minor == ov.minor && this.build != null && ov.build == null ||
                this.major == ov.major && this.minor == ov.minor && this.build == null && ov.build != null && this.build > ov.build;
    }

    /**
     * Returns whether this plugin version is newer or the same as the specified one or not.
     *
     * @param ov other plugin version
     * @return true if this plugin version is newer or the same as the specified one, false otherwise
     */
    public boolean isNewerOrSame ( final PluginVersion ov )
    {
        return isSame ( ov ) || isNewerThan ( ov );
    }

    /**
     * Returns whether this plugin version is older than the specified one or not.
     *
     * @param ov other plugin version
     * @return true if this plugin version is older than the specified one, false otherwise
     */
    public boolean isOlderThan ( final PluginVersion ov )
    {
        return ov.major > this.major ||
                ov.major == this.major && ov.minor > this.minor ||
                ov.major == this.major && ov.minor == this.minor && ov.build != null && this.build == null ||
                ov.major == this.major && ov.minor == this.minor && ov.build != null && this.build != null && ov.build > this.build;
    }

    /**
     * Returns whether this plugin version is older or the same as the specified one or not.
     *
     * @param ov other plugin version
     * @return true if this plugin version is older or the same as the specified one, false otherwise
     */
    public boolean isOlderOrSame ( final PluginVersion ov )
    {
        return isSame ( ov ) || isOlderThan ( ov );
    }

    /**
     * Returns whether this plugin version is the same as the specified one or not.
     *
     * @param ov other plugin version
     * @return true if this plugin version is the same as the specified one, false otherwise
     */
    public boolean isSame ( final PluginVersion ov )
    {
        return ov.major == this.major && ov.minor == this.minor && CompareUtils.equals ( ov.build, this.build );
    }

    @Override
    public String toString ()
    {
        return "v" + major + "." + minor + ( build != null ? ( "." + build ) : "" );
    }
}