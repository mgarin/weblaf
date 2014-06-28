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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java version information class.
 *
 * @author Mikle Garin
 */

public class JavaVersion
{
    /**
     * Java version string pattern.
     */
    private static Pattern versionPattern = Pattern.compile ( "(\\d+\\.\\d+)(\\.(\\d+))?(_([^-]+))?(.*)" );

    /**
     * Simple java version string pattern.
     */
    private static Pattern simpleVersionPattern = Pattern.compile ( "(\\d+\\.\\d+)(\\.(\\d+))?(.*)" );

    /**
     * Major java version.
     */
    private double majorVersion;

    /**
     * Minor java version.
     */
    private int minorVersion;

    /**
     * Java update number.
     */
    private int updateNumber;

    /**
     * Java patch.
     */
    private String patch;

    /**
     * Constructs JavaVersion with specified major version and update number.
     *
     * @param major  major java version
     * @param update java update number
     */
    public JavaVersion ( double major, int update )
    {
        super ();
        majorVersion = major;
        minorVersion = 0;
        updateNumber = update;
    }

    /**
     * Constructs JavaVersion with specified major version, minor version and update number.
     *
     * @param major  major java version
     * @param minor  minor java version
     * @param update java update number
     */
    public JavaVersion ( double major, int minor, int update )
    {
        super ();
        majorVersion = major;
        minorVersion = minor;
        updateNumber = update;
    }

    /**
     * Constructs JavaVersion using the specified java version.
     *
     * @param version java version string
     */
    public JavaVersion ( String version )
    {
        super ();
        applyJavaVersion ( version );
    }

    /**
     * Applies specified java version.
     *
     * @param version java version
     */
    public void applyJavaVersion ( String version )
    {
        try
        {
            Matcher matcher = versionPattern.matcher ( version );
            if ( matcher.matches () )
            {
                int groups = matcher.groupCount ();
                majorVersion = Double.parseDouble ( matcher.group ( 1 ) );
                if ( groups >= 3 && matcher.group ( 3 ) != null )
                {
                    minorVersion = Integer.parseInt ( matcher.group ( 3 ) );
                }
                if ( groups >= 5 && matcher.group ( 5 ) != null )
                {
                    try
                    {
                        updateNumber = Integer.parseInt ( matcher.group ( 5 ) );
                    }
                    catch ( NumberFormatException e )
                    {
                        patch = matcher.group ( 5 );
                    }
                }
                if ( groups >= 6 && matcher.group ( 6 ) != null )
                {
                    String s = matcher.group ( 6 );
                    if ( s != null && s.trim ().length () > 0 )
                    {
                        patch = s;
                    }
                }
            }
        }
        catch ( NumberFormatException e )
        {
            try
            {
                Matcher matcher = simpleVersionPattern.matcher ( version );
                if ( matcher.matches () )
                {
                    int groups = matcher.groupCount ();
                    majorVersion = Double.parseDouble ( matcher.group ( 1 ) );
                    if ( groups >= 3 && matcher.group ( 3 ) != null )
                    {
                        minorVersion = Integer.parseInt ( matcher.group ( 3 ) );
                    }
                }
            }
            catch ( NumberFormatException e1 )
            {
                majorVersion = 1.4;
                minorVersion = 0;
                updateNumber = 0;
            }
        }
    }

    /**
     * Returns a negative integer, zero, or a positive integer if this java version is less than, equal to, or greater than the other one.
     *
     * @param major  major java version
     * @param minor  minor java version
     * @param update java update number
     * @return a negative integer, zero, or a positive integer if this java version is less than, equal to, or greater than the other one
     */
    public int compareVersion ( double major, int minor, int update )
    {
        double majorResult = majorVersion - major;
        if ( majorResult != 0 )
        {
            return majorResult < 0 ? -1 : 1;
        }
        int result = minorVersion - minor;
        if ( result != 0 )
        {
            return result;
        }
        return updateNumber - update;
    }

    /**
     * Returns major java version.
     *
     * @return major java version
     */
    public double getMajorVersion ()
    {
        return majorVersion;
    }

    /**
     * Returns minor java version.
     *
     * @return minor java version
     */
    public int getMinorVersion ()
    {
        return minorVersion;
    }

    /**
     * Returns java update number.
     *
     * @return java update number
     */
    public int getUpdateNumber ()
    {
        return updateNumber;
    }

    /**
     * Returns java patch.
     *
     * @return java patch
     */
    public String getPatch ()
    {
        return patch;
    }
}