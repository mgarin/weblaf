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

import com.alee.api.jdk.Objects;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * {@link com.alee.managers.plugin.Plugin} version data class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see <a href="http://semver.org/">Semantic Versioning</a>
 * @see com.alee.managers.plugin.PluginManager
 * @see com.alee.managers.plugin.Plugin
 */

@XStreamAlias ( "PluginVersion" )
public class PluginVersion implements Serializable
{
    /**
     * Simple default v1.0.0 version.
     */
    public static final PluginVersion DEFAULT = new PluginVersion ( 1, 0, 0, null );

    /**
     * Major plugin version.
     * Version when you make incompatible API changes.
     */
    @XStreamAsAttribute
    private int major;

    /**
     * Minor plugin version.
     * Version when you add functionality in a backwards-compatible manner.
     */
    @XStreamAsAttribute
    private int minor;

    /**
     * Plugin patch version.
     * Version when you make backwards-compatible bug fixes.
     */
    @XStreamAsAttribute
    private Integer patch;

    /**
     * Plugin build version.
     * Additional labels for pre-release and build metadata.
     */
    @XStreamAsAttribute
    private String build;

    /**
     * Constructs new plugin version data object.
     */
    public PluginVersion ()
    {
        this ( DEFAULT.major, DEFAULT.minor, DEFAULT.patch, DEFAULT.build );
    }

    /**
     * Constructs new plugin version data object with the specified major and minor version numbers.
     *
     * @param major major version number
     * @param minor minor version number
     */
    public PluginVersion ( final int major, final int minor )
    {
        this ( major, minor, null, null );
    }

    /**
     * Constructs new plugin version data object with the specified major and minor version numbers.
     *
     * @param major major version number
     * @param minor minor version number
     * @param patch patch version number
     */
    public PluginVersion ( final int major, final int minor, final Integer patch )
    {
        this ( major, minor, patch, null );
    }

    /**
     * Constructs new plugin version data object with the specified major and minor version numbers.
     *
     * @param major major version number
     * @param minor minor version number
     * @param patch patch version number
     * @param build build
     */
    public PluginVersion ( final int major, final int minor, final Integer patch, final String build )
    {
        super ();
        this.major = major;
        this.minor = minor;
        this.patch = patch;
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
     * Returns plugin patch version.
     *
     * @return plugin patch version
     */
    public Integer getPatch ()
    {
        return patch;
    }

    /**
     * Sets plugin patch version.
     *
     * @param patch plugin patch version
     */
    public void setPatch ( final Integer patch )
    {
        this.patch = patch;
    }

    /**
     * Returns plugin build version.
     * Might return null in case build is not specified.
     *
     * @return plugin build version
     */
    public String getBuild ()
    {
        return build;
    }

    /**
     * Sets plugin build version.
     * You might want to set it to null in case you don't need build number.
     *
     * @param build plugin build version
     */
    public void setBuild ( final String build )
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
                this.major == ov.major && this.minor == ov.minor && this.patch != null && ov.patch == null ||
                this.major == ov.major && this.minor == ov.minor && this.patch == null && ov.patch != null && this.patch > ov.patch;
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
                ov.major == this.major && ov.minor == this.minor && ov.patch != null && this.patch == null ||
                ov.major == this.major && ov.minor == this.minor && ov.patch != null && this.patch != null && ov.patch > this.patch;
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
        return ov.major == this.major && ov.minor == this.minor && Objects.equals ( ov.patch, this.patch );
    }

    @Override
    public boolean equals ( final Object obj )
    {
        return obj != null && obj instanceof PluginVersion && isSame ( ( PluginVersion ) obj ) &&
                Objects.equals ( ( ( PluginVersion ) obj ).build, this.patch );
    }

    @Override
    public String toString ()
    {
        return "v" + major + "." + minor + ( patch != null ? "." + patch : "" ) + ( build != null ? build : "" );
    }
}