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

import com.alee.api.annotations.NotNull;

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
    @NotNull
    public static String getAddress ( @NotNull final URL url )
    {
        try
        {
            return url.toURI ().toASCIIString ();
        }
        catch ( final URISyntaxException e )
        {
            throw new RuntimeException ( "Unable to convert URL to address: " + url, e );
        }
    }

    /**
     * Returns URL for the specified address.
     *
     * @param address address to process
     * @return URL for the specified address
     */
    @NotNull
    public static URL getURL ( @NotNull final String address )
    {
        try
        {
            return new URL ( address );
        }
        catch ( final MalformedURLException e )
        {
            throw new RuntimeException ( "Unable to create URL for address: " + address, e );
        }
    }

    /**
     * Returns URI for the specified address.
     *
     * @param address address to process
     * @return URI for the specified address
     */
    @NotNull
    public static URI getURI ( @NotNull final String address )
    {
        return toURI ( getURL ( address ) );
    }

    /**
     * Returns URI for the specified address.
     *
     * @param url URL object to process
     * @return URI for the specified address
     */
    @NotNull
    public static URI toURI ( @NotNull final URL url )
    {
        try
        {
            return url.toURI ();
        }
        catch ( final URISyntaxException e )
        {
            throw new RuntimeException ( "Unable to convert URL to URI: " + url, e );
        }
    }

    /**
     * Returns host for the specified address.
     *
     * @param address address to process
     * @return host for the specified address
     */
    @NotNull
    public static String getHost ( @NotNull final String address )
    {
        return getURL ( address ).getHost ();
    }

    /**
     * Returns port for the specified address or {@code -1} if it is not set.
     *
     * @param address address to process
     * @return port for the specified address or {@code -1} if it is not set
     */
    public static int getPort ( @NotNull final String address )
    {
        return getURL ( address ).getPort ();
    }

    /**
     * Returns base address for the specified complete address.
     *
     * @param address complete address to process
     * @return base address for the specified complete address
     */
    @NotNull
    public static String getBaseAddress ( @NotNull final String address )
    {
        final URL url = getURL ( address );
        return url.getHost () + ( url.getPort () != 80 && url.getPort () != -1 ? ":" + url.getPort () : "" );
    }

    /**
     * Joins two url paths without leaving any unnecessary path separators.
     *
     * @param part1 first url path
     * @param part2 second url path
     * @return properly joined url paths
     */
    @NotNull
    public static String joinUrlPaths ( @NotNull final String part1, @NotNull final String part2 )
    {
        final String path;
        final String separator = "/";
        final boolean p1s = part1.endsWith ( separator );
        final boolean p2s = part2.startsWith ( separator );
        if ( p1s && p2s )
        {
            path = part1 + part2.substring ( 1 );
        }
        else if ( !p1s && !p2s )
        {
            path = part1 + separator + part2;
        }
        else
        {
            path = part1 + part2;
        }
        return path;
    }
}