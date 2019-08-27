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

package com.alee.api.version;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Semantic version number.
 *
 * @author Mikle Garin
 * @see <a href="https://semver.org/">Semantic Versioning</a>
 */
public final class Version implements Comparable<Version>, Serializable
{
    /**
     * Version name.
     */
    @NotNull
    private final String name;

    /**
     * Major version number.
     */
    private final int major;

    /**
     * Minor version number.
     */
    private final int minor;

    /**
     * Patch number.
     */
    private final int patch;

    /**
     * {@link VersionType}.
     */
    @NotNull
    private final VersionType type;

    /**
     * Constrcuts new {@link Version}.
     *
     * @param cls {@link Class} to retrieve version for
     */
    public Version ( @Nullable final Class cls )
    {
        this ( cls != null ? cls.getPackage () : null );
    }

    /**
     * Constrcuts new {@link Version}.
     *
     * @param pkg {@link Package} to retrieve version for
     */
    public Version ( @Nullable final Package pkg )
    {
        this ( pkg != null ? pkg.getSpecificationTitle () : null, pkg != null ? pkg.getSpecificationVersion () : null );
    }

    /**
     * Constrcuts new {@link Version}.
     *
     * @param name    version name
     * @param version semantic version
     */
    public Version ( @Nullable final String name, @Nullable final String version )
    {
        if ( TextUtils.notEmpty ( name ) && TextUtils.notEmpty ( version ) )
        {
            this.name = name;

            final int typeSeparator = version.indexOf ( "-" );
            final String versionNumber = typeSeparator != -1 ? version.substring ( 0, typeSeparator ) : version;
            final List<String> majorMinorPatch = TextUtils.stringToList ( versionNumber, "." );
            this.major = majorMinorPatch.size () > 0 ? Integer.parseInt ( majorMinorPatch.get ( 0 ) ) : 0;
            this.minor = majorMinorPatch.size () > 1 ? Integer.parseInt ( majorMinorPatch.get ( 1 ) ) : 0;
            this.patch = majorMinorPatch.size () > 2 ? Integer.parseInt ( majorMinorPatch.get ( 2 ) ) : 0;

            final String type = version.substring ( typeSeparator + 1 ).toLowerCase ();
            this.type = typeSeparator != -1 ? VersionType.valueOf ( type ) : VersionType.release;
        }
        else
        {
            this.name = "Unknown";
            this.major = 0;
            this.minor = 0;
            this.patch = 0;
            this.type = VersionType.snapshot;
        }
    }

    /**
     * Constructs new {@link Version}.
     *
     * @param name  version name
     * @param major major version number
     * @param minor minor version number
     * @param patch patch number
     * @param type  {@link VersionType}
     */
    public Version ( @NotNull final String name, final int major, final int minor, final int patch, @NotNull final VersionType type )
    {
        this.name = name;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.type = type;
    }

    /**
     * Returns version name.
     *
     * @return version name
     */
    @NotNull
    public String name ()
    {
        return name;
    }

    /**
     * Returns major version number.
     *
     * @return major version number
     */
    public int major ()
    {
        return major;
    }

    /**
     * Returns minor version number.
     *
     * @return minor version number
     */
    public int minor ()
    {
        return minor;
    }

    /**
     * Returns patch number.
     *
     * @return patch number
     */
    public int patch ()
    {
        return patch;
    }

    /**
     * Returns {@link VersionType}.
     *
     * @return {@link VersionType}
     */
    @NotNull
    public VersionType type ()
    {
        return type;
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash ( major, minor, patch, type );
    }

    @Override
    public int compareTo ( final Version version )
    {
        final int result;
        if ( major () < version.major () )
        {
            result = -1;
        }
        else if ( major () > version.major () )
        {
            result = 1;
        }
        else if ( minor () < version.minor () )
        {
            result = -1;
        }
        else if ( minor () > version.minor () )
        {
            result = 1;
        }
        else if ( patch () < version.patch () )
        {
            result = -1;
        }
        else if ( patch () > version.patch () )
        {
            result = 1;
        }
        else
        {
            result = new Integer ( version.type ().ordinal () ).compareTo ( type ().ordinal () );
        }
        return result;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return "v" + major + "." + minor + "." + patch + ( type != VersionType.release ? "-" + type.name ().toUpperCase () : "" );
    }
}