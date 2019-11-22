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

package com.alee.utils.system;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java version information.
 *
 * @author Mikle Garin
 */
public final class JavaVersion
{
    /**
     * Version string pattern.
     */
    private static final Pattern versionPattern = Pattern.compile ( "(\\d+\\.\\d+)(\\.(\\d+))?(_([^-]+))?(.*)" );

    /**
     * Simple version string pattern.
     */
    private static final Pattern simpleVersionPattern = Pattern.compile ( "(\\d+\\.\\d+)(\\.(\\d+))?(.*)" );

    /**
     * Major version.
     */
    private final double major;

    /**
     * Minor version.
     */
    private final int minor;

    /**
     * Java update number.
     */
    private final int update;

    /**
     * Java patch.
     */
    @Nullable
    private final String patch;

    /**
     * Constructs new {@link JavaVersion} for the specified major version and update number.
     *
     * @param major  major version
     * @param update update number
     */
    public JavaVersion ( final double major, final int update )
    {
        this ( major, 0, update );
    }

    /**
     * Constructs new {@link JavaVersion} for the specified major version, minor version and update number.
     *
     * @param major  major version
     * @param minor  minor version
     * @param update update number
     */
    public JavaVersion ( final double major, final int minor, final int update )
    {
        this ( major, minor, update, null );
    }

    /**
     * Constructs new {@link JavaVersion} for the specified major version, minor version and update number.
     *
     * @param major  major version
     * @param minor  minor version
     * @param update update number
     * @param patch  patch
     */
    public JavaVersion ( final double major, final int minor, final int update, @Nullable final String patch )
    {
        this.major = major;
        this.minor = minor;
        this.update = update;
        this.patch = patch;
    }

    /**
     * Constructs current runtime {@link JavaVersion}.
     */
    public JavaVersion ()
    {
        double major = 0.0;
        int minor = 0;
        int update = 0;
        String patch = null;
        boolean read = false;

        /**
         * Using {@link Runtime} public API introduced in JDK 9.
         */
        try
        {
            final Object version = ReflectUtils.callStaticMethod ( Runtime.class, "version" );
            try
            {
                final Object majorNumber = ReflectUtils.callMethod ( version, "major" );
                major = majorNumber instanceof Integer ? ( Integer ) majorNumber : 0.0;
                final Object minorNumber = ReflectUtils.callMethod ( version, "minor" );
                minor = minorNumber instanceof Integer ? ( Integer ) minorNumber : 0;
                final Object updateNumber = ReflectUtils.callMethod ( version, "security" );
                update = updateNumber instanceof Integer ? ( Integer ) updateNumber : 0;
                final Object optional = ReflectUtils.callMethod ( version, "optional" );
                patch = optional != null ? ( String ) ReflectUtils.callMethod ( optional, "orElse", ( Object ) null ) : null;
                read = true;
            }
            catch ( final Exception e )
            {
                LoggerFactory.getLogger ( JavaVersion.class ).error ( "Unable to read Runtime version", e );
            }
        }
        catch ( final Exception ignored )
        {
            /**
             * Ignoring any exceptions here.
             */
        }

        /**
         * Manually parsing {@code "java.version"} system property string.
         */
        if ( !read )
        {
            final String versionString = SystemUtils.getJavaVersionString ();
            try
            {
                final Matcher matcher = versionPattern.matcher ( versionString );
                if ( matcher.matches () )
                {
                    final int groups = matcher.groupCount ();
                    major = Double.parseDouble ( matcher.group ( 1 ) );
                    if ( groups >= 3 && matcher.group ( 3 ) != null )
                    {
                        minor = Integer.parseInt ( matcher.group ( 3 ) );
                    }
                    if ( groups >= 5 && matcher.group ( 5 ) != null )
                    {
                        try
                        {
                            update = Integer.parseInt ( matcher.group ( 5 ) );
                        }
                        catch ( final NumberFormatException e )
                        {
                            patch = matcher.group ( 5 );
                        }
                    }
                    if ( groups >= 6 && matcher.group ( 6 ) != null )
                    {
                        final String s = matcher.group ( 6 );
                        if ( s != null && s.trim ().length () > 0 )
                        {
                            patch = s;
                        }
                    }
                    read = true;
                }
            }
            catch ( final NumberFormatException e )
            {
                try
                {
                    final Matcher matcher = simpleVersionPattern.matcher ( versionString );
                    if ( matcher.matches () )
                    {
                        final int groups = matcher.groupCount ();
                        major = Double.parseDouble ( matcher.group ( 1 ) );
                        if ( groups >= 3 && matcher.group ( 3 ) != null )
                        {
                            minor = Integer.parseInt ( matcher.group ( 3 ) );
                        }
                    }
                    read = true;
                }
                catch ( final NumberFormatException e1 )
                {
                    major = 1.4;
                    minor = 0;
                    update = 0;
                    read = true;
                }
            }
        }

        /**
         * Updating resulting values.
         */
        if ( read )
        {
            this.major = major;
            this.minor = minor;
            this.update = update;
            this.patch = patch;
        }
        else
        {
            throw new RuntimeException ( "Unable to determine Java runtime version" );
        }
    }

    /**
     * Returns major version.
     *
     * @return major version
     */
    public double major ()
    {
        return major;
    }

    /**
     * Returns minor version.
     *
     * @return minor version
     */
    public int minor ()
    {
        return minor;
    }

    /**
     * Returns update number.
     *
     * @return update number
     */
    public int update ()
    {
        return update;
    }

    /**
     * Returns patch.
     *
     * @return patch
     */
    @Nullable
    public String patch ()
    {
        return patch;
    }

    /**
     * Returns a negative integer, zero, or a positive integer if this version is less than, equal to, or greater than the other one.
     *
     * @param version {@link JavaVersion}
     * @return a negative integer, zero, or a positive integer if this version is less than, equal to, or greater than the other one
     */
    public int compareTo ( final JavaVersion version )
    {
        return compareTo ( version.major (), version.minor (), version.update () );
    }

    /**
     * Returns a negative integer, zero, or a positive integer if this version is less than, equal to, or greater than the other one.
     *
     * @param major  major version
     * @param minor  minor version
     * @param update update number
     * @return a negative integer, zero, or a positive integer if this version is less than, equal to, or greater than the other one
     */
    public int compareTo ( final double major, final int minor, final int update )
    {
        final int result;
        final double majorResult = this.major - major;
        if ( majorResult != 0 )
        {
            result = majorResult < 0 ? -1 : 1;
        }
        else
        {
            final int minorResult = this.minor - minor;
            if ( minorResult != 0 )
            {
                result = minorResult;
            }
            else
            {
                result = this.update - update;
            }
        }
        return result;
    }

    /**
     * Returns version {@link String} only.
     *
     * @return version {@link String} only
     */
    @NotNull
    public String versionString ()
    {
        final StringBuilder version = new StringBuilder ();
        if ( major () < 9.0 )
        {
            version.append ( major () );
            version.append ( "." );
            version.append ( minor () );
            version.append ( " u" ).append ( update () );
        }
        else
        {
            version.append ( Math.round ( major () ) );
            version.append ( "." );
            version.append ( minor () );
            version.append ( "." );
            version.append ( update () );
        }
        return version.toString ();
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash ( major (), minor (), update (), patch () );
    }

    @NotNull
    @Override
    public String toString ()
    {
        final StringBuilder version = new StringBuilder ( "Java " );
        version.append ( versionString () );
        if ( patch () != null )
        {
            version.append ( " (" );
            version.append ( patch () );
            version.append ( ")" );
        }
        return version.toString ();
    }
}