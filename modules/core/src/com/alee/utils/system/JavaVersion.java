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

import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;

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
    public JavaVersion ( final double major, final int minor, final int update, final String patch )
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
        final Object version = ReflectUtils.callStaticMethodSafely ( Runtime.class, "version" );
        if ( version != null )
        {
            /**
             * Using {@link Runtime} public API introduced in JDK 9.
             */
            major = ( Integer ) ReflectUtils.callMethodSafely ( version, "major" );
            minor = ( Integer ) ReflectUtils.callMethodSafely ( version, "minor" );
            update = ( Integer ) ReflectUtils.callMethodSafely ( version, "security" );
            patch = null;
        }
        else
        {
            /**
             * Manually parsing {@code "java.version"} system property string.
             */
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
                }
                catch ( final NumberFormatException e1 )
                {
                    major = 1.4;
                    minor = 0;
                    update = 0;
                }
            }
        }
        if ( major == 0.0 )
        {
            throw new RuntimeException ( "Unable to determine Java runtime version" );
        }
        this.major = major;
        this.minor = minor;
        this.update = update;
        this.patch = patch;
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
        final double majorResult = this.major - major;
        if ( majorResult != 0 )
        {
            return majorResult < 0 ? -1 : 1;
        }
        final int result = this.minor - minor;
        if ( result != 0 )
        {
            return result;
        }
        return this.update - update;
    }

    @Override
    public String toString ()
    {
        return "Java " + major () + "." + minor () + " u" + update ();
    }
}