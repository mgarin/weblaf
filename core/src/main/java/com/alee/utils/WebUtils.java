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
 * @since 1.3
 */

public class WebUtils
{
    /**
     * Returns normalized url path.
     *
     * @param url the url to normalize
     * @return normalized url
     */
    public static String normalizeUrl ( String url )
    {
        try
        {
            return new URI ( url ).normalize ().toASCIIString ();
        }
        catch ( URISyntaxException e )
        {
            return url;
        }
    }

    /**
     * Returns url query parameters.
     *
     * @param url the url to parse
     * @return parameters map
     */
    public static Map<String, List<String>> getUrlParameters ( String url )
    {
        Map<String, List<String>> params = new HashMap<String, List<String>> ();
        String[] urlParts = url.split ( "\\?" );
        if ( urlParts.length > 1 )
        {
            String query = urlParts[ 1 ];
            for ( String param : query.split ( "&" ) )
            {
                String pair[] = param.split ( "=" );
                String key = decodeUrl ( pair[ 0 ] );
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
    public static String encodeUrl ( String url )
    {
        try
        {
            URL u = new URL ( url );
            URI uri = new URI ( u.getProtocol (), u.getHost (), u.getPath (), u.getQuery (), null );
            return uri.toASCIIString ();
        }
        catch ( Throwable e )
        {
            return url;
        }
    }

    /**
     * Returns decoded url path.
     *
     * @param url the url to decode
     * @return decoded url
     */
    public static String decodeUrl ( String url )
    {
        try
        {
            return URLDecoder.decode ( url, "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            return url;
        }
    }

    /**
     * Opens system default web-browser with Twitter share page.
     *
     * @param address the address to share
     */
    public static void shareOnTwitter ( final String address )
    {
        new Thread ( new Runnable ()
        {
            public void run ()
            {
                try
                {
                    browseSite ( "http://twitter.com/intent/tweet?text=" + address );
                }
                catch ( Throwable ex )
                {
                    //
                }
            }
        } ).start ();
    }

    /**
     * Opens system default web-browser with VKontakte share page.
     *
     * @param address the address to share
     */
    public static void shareOnVk ( final String address )
    {
        new Thread ( new Runnable ()
        {
            public void run ()
            {
                try
                {
                    browseSite ( "http://vkontakte.ru/share.php?url=" + address );
                }
                catch ( Throwable ex )
                {
                    //
                }
            }
        } ).start ();
    }

    /**
     * Opens system default web-browser with Facebook share page.
     *
     * @param address the address to share
     */
    public static void shareOnFb ( final String address )
    {
        new Thread ( new Runnable ()
        {
            public void run ()
            {
                try
                {
                    browseSite ( "http://www.facebook.com/sharer.php?u=" + address );
                }
                catch ( Throwable ex )
                {
                    //
                }
            }
        } ).start ();
    }

    /**
     * Opens site in default system web-browser safely
     *
     * @param address the address to open
     */
    public static void browseSiteSafely ( String address )
    {
        try
        {
            browseSite ( address );
        }
        catch ( Throwable e )
        {
            //
        }
    }

    /**
     * Opens site in default system web-browser
     *
     * @param address the address to open
     */
    public static void browseSite ( String address ) throws URISyntaxException, IOException
    {
        Desktop.getDesktop ().browse ( new URI ( address ) );
    }

    /**
     * Opens file in appropriate system application safely
     *
     * @param file the file to open
     */
    public static void openFileSafely ( File file )
    {
        try
        {
            openFile ( file );
        }
        catch ( Throwable e )
        {
            //
        }
    }

    /**
     * Opens file in appropriate system application
     *
     * @param file the file to open
     */
    public static void openFile ( File file ) throws IOException
    {
        Desktop.getDesktop ().open ( file );
    }

    /**
     * Opens system mail agent to compose a new letter safely
     *
     * @param email the destination email address
     */
    public static void writeEmailSafely ( String email )
    {
        try
        {
            writeEmail ( email );
        }
        catch ( Throwable e )
        {
            //
        }
    }

    /**
     * Opens system mail agent to compose a new letter
     *
     * @param email the destination email address
     */
    public static void writeEmail ( String email ) throws URISyntaxException, IOException
    {
        writeEmail ( email, null, null );
    }

    /**
     * Opens system mail agent to compose a new letter safely
     *
     * @param email   the destination email address
     * @param subject letter subject
     * @param body    letter text
     */
    public static void writeEmailSafely ( String email, String subject, String body )
    {
        try
        {
            writeEmail ( email, subject, body );
        }
        catch ( Throwable e )
        {
            //
        }
    }

    /**
     * Opens system mail agent to compose a new letter
     *
     * @param email   the destination email address
     * @param subject letter subject
     * @param body    letter text
     */
    public static void writeEmail ( String email, String subject, String body ) throws URISyntaxException, IOException
    {
        URI uri;
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
}