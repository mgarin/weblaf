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
import com.alee.api.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides a set of utilities to work with URLs and email addresses.
 *
 * @author Mikle Garin
 */
public final class WebUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private WebUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns normalized url path.
     *
     * @param url the url to normalize
     * @return normalized url
     */
    @NotNull
    public static String normalizeUrl ( @NotNull final String url )
    {
        try
        {
            return new URI ( url ).normalize ().toASCIIString ();
        }
        catch ( final URISyntaxException e )
        {
            throw new UtilityException ( "Unable to normalize URL: " + url, e );
        }
    }

    /**
     * Returns url query parameters.
     *
     * @param url the url to parse
     * @return parameters map
     */
    @NotNull
    public static Map<String, List<String>> getUrlParameters ( @NotNull final String url )
    {
        final Map<String, List<String>> params = new HashMap<String, List<String>> ();
        final String[] urlParts = url.split ( "\\?" );
        if ( urlParts.length > 1 )
        {
            final String query = urlParts[ 1 ];
            for ( final String param : query.split ( "&" ) )
            {
                final String[] pair = param.split ( "=" );
                final String key = decodeUrl ( pair[ 0 ] );
                String value = "";
                if ( pair.length > 1 )
                {
                    value = decodeUrl ( pair[ 1 ] );
                }
                List<String> values = params.get ( key );
                if ( values == null )
                {
                    values = new ArrayList<String> ();
                    params.put ( key, values );
                }
                values.add ( value );
            }
        }
        return params;
    }

    /**
     * Returns encoded url path.
     *
     * @param url the url to encode
     * @return encoded url
     */
    @NotNull
    public static String encodeUrl ( @NotNull final String url )
    {
        try
        {
            final URL u = new URL ( url );
            final URI uri = new URI ( u.getProtocol (), u.getHost (), u.getPath (), u.getQuery (), null );
            return uri.toASCIIString ();
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to encore URL: " + url, e );
        }
    }

    /**
     * Returns decoded url path.
     *
     * @param url the url to decode
     * @return decoded url
     */
    @NotNull
    public static String decodeUrl ( @NotNull final String url )
    {
        try
        {
            return URLDecoder.decode ( url, "UTF-8" );
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new UtilityException ( "Unable to decode URL: " + url, e );
        }
    }

    /**
     * Opens site in default system web-browser
     *
     * @param address the address to open
     * @throws URISyntaxException if URI cannot be created for the given address
     * @throws IOException        if browser application is not found or fails to launch
     */
    public static void browseSite ( @NotNull final String address ) throws URISyntaxException, IOException
    {
        Desktop.getDesktop ().browse ( new URI ( address ) );
    }

    /**
     * Opens site in default system web-browser safely
     *
     * @param address the address to open
     */
    public static void browseSiteSafely ( @NotNull final String address )
    {
        try
        {
            browseSite ( address );
        }
        catch ( final Exception ignored )
        {
            //
        }
    }

    /**
     * Opens system default web-browser with Twitter share page.
     *
     * @param address the address to share
     */
    public static void shareOnTwitter ( @NotNull final String address )
    {
        new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                browseSiteSafely ( "http://twitter.com/intent/tweet?text=" + address );
            }
        } ).start ();
    }

    /**
     * Opens system default web-browser with VKontakte share page.
     *
     * @param address the address to share
     */
    public static void shareOnVk ( @NotNull final String address )
    {
        new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                browseSiteSafely ( "http://vkontakte.ru/share.php?url=" + address );
            }
        } ).start ();
    }

    /**
     * Opens system default web-browser with Facebook share page.
     *
     * @param address the address to share
     */
    public static void shareOnFb ( @NotNull final String address )
    {
        new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                browseSiteSafely ( "http://www.facebook.com/sharer.php?u=" + address );
            }
        } ).start ();
    }

    /**
     * Opens file in appropriate system application
     *
     * @param file the file to open
     * @throws IOException if the specified file has no associated application or the associated application fails to be launched
     */
    public static void openFile ( @NotNull final File file ) throws IOException
    {
        Desktop.getDesktop ().open ( file );
    }

    /**
     * Opens file in appropriate system application safely
     *
     * @param file the file to open
     */
    public static void openFileSafely ( @NotNull final File file )
    {
        try
        {
            openFile ( file );
        }
        catch ( final Exception ignored )
        {
            //
        }
    }

    /**
     * Opens system mail agent to compose a new letter
     *
     * @param email the destination email address
     * @throws URISyntaxException if mailing URI cannot be created
     * @throws IOException        if used mail client cannot be found
     */
    public static void writeEmail ( @NotNull final String email ) throws URISyntaxException, IOException
    {
        writeEmail ( email, null, null );
    }

    /**
     * Opens system mail agent to compose a new letter
     *
     * @param email   the destination email address
     * @param subject letter subject
     * @param body    letter text
     * @throws URISyntaxException if mailing URI cannot be created
     * @throws IOException        if used mail client cannot be found
     */
    public static void writeEmail ( @NotNull String email, @Nullable String subject, @Nullable String body )
            throws URISyntaxException, IOException
    {
        final URI uri;
        if ( !email.startsWith ( "mailto:" ) )
        {
            email = "mailto:" + email;
        }
        if ( subject != null && body != null )
        {
            subject = subject.replaceAll ( " ", "%20" );
            body = body.replaceAll ( " ", "%20" );
            uri = new URI ( email + "?subject=" + subject + "&body=" + body );
        }
        else
        {
            uri = new URI ( email );
        }
        Desktop.getDesktop ().mail ( uri );
    }

    /**
     * Opens system mail agent to compose a new letter safely
     *
     * @param email the destination email address
     */
    public static void writeEmailSafely ( @NotNull final String email )
    {
        try
        {
            writeEmail ( email );
        }
        catch ( final Exception ignored )
        {
            //
        }
    }

    /**
     * Opens system mail agent to compose a new letter safely
     *
     * @param email   the destination email address
     * @param subject letter subject
     * @param body    letter text
     */
    public static void writeEmailSafely ( @NotNull final String email, @Nullable final String subject, @Nullable final String body )
    {
        try
        {
            writeEmail ( email, subject, body );
        }
        catch ( final Exception ignored )
        {
            //
        }
    }
}