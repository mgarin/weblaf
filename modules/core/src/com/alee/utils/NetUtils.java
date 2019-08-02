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

package com.alee.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class provides a set of utilities to work with URL.
 *
 * @author Mikle Garin
 */
public final class NetUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private NetUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns address represented by the specified URL object.
     *
     * @param url URL object to process
     * @return address represented by the specified URL object
     */
    public static String getAddress ( final URL url )
    {
        try
        {
            return url.toURI ().toASCIIString ();
        }
        catch ( final URISyntaxException e )
        {
            return url.toExternalForm ();
        }
    }

    /**
     * Returns URL for the specified address.
     *
     * @param address address to process
     * @return URL for the specified address
     */
    public static URL getURL ( final String address )
    {
        try
        {
            return new URL ( address );
        }
        catch ( final MalformedURLException e )
        {
            return null;
        }
    }

    /**
     * Returns URI for the specified address.
     *
     * @param address address to process
     * @return URI for the specified address
     */
    public static URI getURI ( final String address )
    {
        final URL url = getURL ( address );
        return url != null ? toURI ( url ) : null;
    }

    /**
     * Returns URI for the specified address.
     *
     * @param url URL object to process
     * @return URI for the specified address
     */
    public static URI toURI ( final URL url )
    {
        try
        {
            return url != null ? url.toURI () : null;
        }
        catch ( final URISyntaxException e )
        {
            return null;
        }
    }

    /**
     * Returns host for the specified address.
     *
     * @param address address to process
     * @return host for the specified address
     */
    public static String getHost ( final String address )
    {
        final URL url = getURL ( address );
        return url != null ? url.getHost () : null;
    }

    /**
     * Returns port for the specified address.
     *
     * @param address address to process
     * @return port for the specified address
     */
    public static int getPort ( final String address )
    {
        final URL url = getURL ( address );
        return url != null ? url.getPort () : -1;
    }

    /**
     * Returns base address for the specified complete address.
     *
     * @param address complete address to process
     * @return base address for the specified complete address
     */
    public static String getBaseAddress ( final String address )
    {
        final URL url = getURL ( address );
        return url != null ? url.getHost () + ( url.getPort () != 80 && url.getPort () != -1 ? ":" + url.getPort () : "" ) : null;
    }

    /**
     * Joins two url paths without leaving any unnecessary path separators.
     *
     * @param part1 first url path
     * @param part2 second url path
     * @return properly joined url paths
     */
    public static String joinUrlPaths ( final String part1, final String part2 )
    {
        final String separator = "/";
        final boolean p1s = part1.endsWith ( separator );
        final boolean p2s = part2.startsWith ( separator );
        if ( p1s && p2s )
        {
            return part1 + part2.substring ( 1 );
        }
        else if ( !p1s && !p2s )
        {
            return part1 + separator + part2;
        }
        else
        {
            return part1 + part2;
        }
    }
}