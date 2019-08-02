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

import com.alee.utils.zip.UnzipListener;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class provides a set of utilities to work with ZIP files.
 *
 * @author Mikle Garin
 */
public final class ZipUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private ZipUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive ZIP file path
     * @param dst     destination directory path
     * @return true if archive was extracted successfully
     */
    public static boolean unzip ( final String archive, final String dst )
    {
        return unzip ( archive, dst, null );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive  ZIP file path
     * @param dst      destination directory path
     * @param listener extraction process listener
     * @return true if archive was extracted successfully
     */
    public static boolean unzip ( final String archive, final String dst, final UnzipListener listener )
    {
        return unzip ( new File ( archive ), new File ( dst ), listener );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive ZIP file
     * @param dst     destination directory
     * @return true if archive was extracted successfully
     */
    public static boolean unzip ( final File archive, final File dst )
    {
        return unzip ( archive, dst, null );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive  ZIP file
     * @param dst      destination directory
     * @param listener extraction process listener
     * @return true if archive was extracted successfully
     */
    public static boolean unzip ( final File archive, final File dst, final UnzipListener listener )
    {
        try
        {
            final ZipFile zipFile = new ZipFile ( archive );

            // Informing listener about zip entries amount
            if ( listener != null )
            {
                listener.sizeDetermined ( zipFile.size () );
            }

            // Starting zip extraction
            final Enumeration entries = zipFile.entries ();
            int index = 0;
            while ( entries.hasMoreElements () )
            {
                // Single zip entry
                final ZipEntry entry = ( ZipEntry ) entries.nextElement ();
                final File extractToFile;
                if ( entry.isDirectory () )
                {
                    // Creating directories path
                    extractToFile = new File ( dst, entry.getName () );
                    extractToFile.mkdirs ();
                }
                else
                {
                    // Ensures that destination file and its folders exist
                    extractToFile = new File ( dst, entry.getName () );
                    FileUtils.getParent ( extractToFile ).mkdirs ();
                    extractToFile.createNewFile ();

                    // Copying file
                    copyInputStream ( zipFile.getInputStream ( entry ),
                            new BufferedOutputStream ( new FileOutputStream ( extractToFile ) ) );
                }

                // Informing listener about single unzipped file
                if ( listener != null )
                {
                    listener.fileUnzipped ( entry, extractToFile, index );
                    index++;
                }
            }
            zipFile.close ();
            return true;
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( ZipUtils.class ).error ( e.toString (), e );
            return false;
        }
    }

    /**
     * Returns file name for the specified zip entry.
     *
     * @param zipEntry zip entry to process
     * @return file name for the specified zip entry
     */
    public static String getZipEntryFileName ( final ZipEntry zipEntry )
    {
        final String name = zipEntry.getName ();
        return name.substring ( name.lastIndexOf ( "/" ) + 1 );
    }

    /**
     * Returns file location for the specified zip entry.
     *
     * @param zipEntry zip entry to process
     * @return file location for the specified zip entry
     */
    public static String getZipEntryFileLocation ( final ZipEntry zipEntry )
    {
        final String name = zipEntry.getName ();
        return name.substring ( 0, name.lastIndexOf ( "/" ) + 1 );
    }

    /**
     * Performs data copy from input to output stream.
     *
     * @param in  data input stream
     * @param out data output stream
     * @throws IOException when any IO exceptions occurs
     */
    private static void copyInputStream ( final InputStream in, final OutputStream out ) throws IOException
    {
        final byte[] buffer = new byte[ 1024 ];
        int len;
        while ( ( len = in.read ( buffer ) ) >= 0 )
        {
            out.write ( buffer, 0, len );
        }
        in.close ();
        out.close ();
    }
}