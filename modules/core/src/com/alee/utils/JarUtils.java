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

import com.alee.utils.file.FileDownloadListener;
import com.alee.utils.jar.JarEntry;
import com.alee.utils.jar.JarEntryType;
import com.alee.utils.jar.JarStructure;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class provides a set of utilities to retrieve complete JAR archive structure.
 *
 * @author Mikle Garin
 */
public final class JarUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private JarUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns JAR archive structure.
     *
     * @param jarClass any class within the JAR
     * @return JAR archive structure
     */
    public static JarStructure getJarStructure ( final Class jarClass )
    {
        return getJarStructure ( jarClass, null, null );
    }

    /**
     * Returns JAR archive structure.
     *
     * @param jarClass          any class within the JAR
     * @param allowedExtensions list of extension filters
     * @param allowedPackages   list of allowed packages
     * @return JAR archive structure
     */
    public static JarStructure getJarStructure ( final Class jarClass, final List<String> allowedExtensions,
                                                 final List<String> allowedPackages )
    {
        return getJarStructure ( jarClass, allowedExtensions, allowedPackages, null );
    }

    /**
     * Returns JAR archive structure.
     *
     * @param jarClass          any class within the JAR
     * @param allowedExtensions list of extension filters
     * @param allowedPackages   list of allowed packages
     * @param listener          jar download listener
     * @return JAR archive structure
     */
    public static JarStructure getJarStructure ( final Class jarClass, final List<String> allowedExtensions,
                                                 final List<String> allowedPackages, final FileDownloadListener listener )
    {
        try
        {
            final CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
            if ( src != null )
            {
                // Creating structure

                // Source url
                final URL jarUrl = src.getLocation ();
                final URI uri = jarUrl.toURI ();

                // Source file
                final File jarFile;
                final String scheme = uri.getScheme ();
                if ( scheme != null && scheme.equalsIgnoreCase ( "file" ) )
                {
                    // Local jar-file
                    jarFile = new File ( uri );
                }
                else
                {
                    // Remote jar-file
                    jarFile = FileUtils.downloadFile ( jarUrl.toString (), File.createTempFile ( jarUrl.getFile (), ".tmp" ), listener );
                }

                // Creating JAR structure
                final JarStructure jarStructure = new JarStructure ();
                jarStructure.setJarLocation ( jarFile.getAbsolutePath () );

                // Updating root element
                final JarEntry rootEntry = new JarEntry ( jarStructure, JarEntryType.jarEntry, jarFile.getName () );
                jarStructure.setRoot ( rootEntry );

                // Reading all entries and parsing them into structure
                final ZipInputStream zip = new ZipInputStream ( jarUrl.openStream () );
                ZipEntry zipEntry;
                while ( ( zipEntry = zip.getNextEntry () ) != null )
                {
                    final String entryName = zipEntry.getName ();
                    if ( isAllowedPackage ( entryName, allowedPackages ) &&
                            ( zipEntry.isDirectory () || isAllowedExtension ( entryName, allowedExtensions ) ) )
                    {
                        parseElement ( jarStructure, rootEntry, entryName, zipEntry );
                    }
                }
                zip.close ();

                return jarStructure;
            }
        }
        catch ( final IOException e )
        {
            LoggerFactory.getLogger ( JarUtils.class ).error ( e.toString (), e );
        }
        catch ( final URISyntaxException e )
        {
            LoggerFactory.getLogger ( JarUtils.class ).error ( e.toString (), e );
        }
        return null;
    }

    /**
     * Returns JAR location URL for the specified class.
     *
     * @param jarClass any class from that JAR
     * @return JAR location URL
     */
    public static URL getJarLocationURL ( final Class jarClass )
    {
        final CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
        return src != null ? src.getLocation () : null;
    }

    /**
     * Returns JAR location File for the specified class.
     *
     * @param jarClass any class from that JAR
     * @return JAR location File
     */
    public static File getJarLocationFile ( final Class jarClass )
    {
        try
        {
            final CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
            if ( src != null )
            {
                final URL jarUrl = src.getLocation ();
                final URI uri = jarUrl.toURI ();
                final String scheme = uri.getScheme ();
                if ( scheme != null && scheme.equalsIgnoreCase ( "file" ) )
                {
                    return new File ( uri );
                }
            }
        }
        catch ( final URISyntaxException e )
        {
            LoggerFactory.getLogger ( JarUtils.class ).error ( e.toString (), e );
        }
        return null;
    }

    /**
     * Returns whether JAR entry with the specified name is allowed by the extensions list or not.
     *
     * @param entryName         JAR entry name
     * @param allowedExtensions list of allowed extensions
     * @return true if JAR entry with the specified name is allowed by the extensions list, false otherwise
     */
    private static boolean isAllowedExtension ( final String entryName, final List<String> allowedExtensions )
    {
        if ( allowedExtensions == null || allowedExtensions.size () == 0 )
        {
            return true;
        }
        else
        {
            final String entryExt = FileUtils.getFileExtPart ( entryName, true ).toLowerCase ( Locale.ROOT );
            return allowedExtensions.contains ( entryExt );
        }
    }

    /**
     * Returns whether JAR entry with the specified name is allowed by the packages list or not.
     *
     * @param entryName       JAR entry name
     * @param allowedPackages list of allowed packages
     * @return true if JAR entry with the specified name is allowed by the packages list, false otherwise
     */
    private static boolean isAllowedPackage ( final String entryName, final List<String> allowedPackages )
    {
        if ( allowedPackages == null || allowedPackages.size () == 0 )
        {
            return true;
        }
        else
        {
            for ( final String packageStart : allowedPackages )
            {
                if ( entryName.startsWith ( packageStart ) )
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Parses single JAR entry with the specified name.
     *
     * @param jarStructure JAR structure
     * @param jarEntry     JAR entry
     * @param entryName    JAR entry name
     * @param zipEntry     ZIP entry
     */
    private static void parseElement ( final JarStructure jarStructure, final JarEntry jarEntry, final String entryName,
                                       final ZipEntry zipEntry )
    {
        final String[] path = entryName.split ( "/" );
        JarEntry currentLevel = jarEntry;
        for ( int i = 0; i < path.length; i++ )
        {
            if ( i < path.length - 1 )
            {
                // We are getting deeper into packages
                JarEntry child = currentLevel.getChildByName ( path[ i ] );
                if ( child == null )
                {
                    child = new JarEntry ( jarStructure, JarEntryType.packageEntry, path[ i ], currentLevel );
                    child.setZipEntry ( zipEntry );
                    currentLevel.addChild ( child );
                }
                currentLevel = child;
            }
            else
            {
                // We reached last element
                final JarEntry newEntry = new JarEntry ( jarStructure, getJarEntryType ( path[ i ] ), path[ i ], currentLevel );
                newEntry.setZipEntry ( zipEntry );
                currentLevel.addChild ( newEntry );
            }
        }
    }

    /**
     * Returns JAR entry type.
     *
     * @param file file to process
     * @return JAR entry type
     */
    private static JarEntryType getJarEntryType ( final String file )
    {
        final String ext = FileUtils.getFileExtPart ( file, false );
        if ( ext.equals ( "java" ) )
        {
            return JarEntryType.javaEntry;
        }
        else if ( ext.equals ( "class" ) )
        {
            return JarEntryType.classEntry;
        }
        else if ( !ext.isEmpty () )
        {
            return JarEntryType.fileEntry;
        }
        else
        {
            return JarEntryType.packageEntry;
        }
    }
}