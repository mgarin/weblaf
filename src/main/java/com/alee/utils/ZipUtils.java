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

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class provides a set of utilities to work with ZIP files.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class ZipUtils
{
    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive ZIP file path
     * @param dst     destination directory path
     * @return true if archive was extracted successfully
     */
    public static boolean unzip ( String archive, String dst )
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
    public static boolean unzip ( String archive, String dst, UnzipListener listener )
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
    public static boolean unzip ( File archive, File dst )
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
    public static boolean unzip ( File archive, File dst, UnzipListener listener )
    {
        try
        {
            ZipFile zipFile = new ZipFile ( archive );

            // Informing listener about zip entries amount
            if ( listener != null )
            {
                listener.sizeDetermined ( zipFile.size () );
            }

            // Starting zip extraction
            Enumeration entries = zipFile.entries ();
            int index = 0;
            while ( entries.hasMoreElements () )
            {
                // Single zip entry
                ZipEntry entry = ( ZipEntry ) entries.nextElement ();
                File extractToFile;
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
                    extractToFile.getParentFile ().mkdirs ();
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
        catch ( Throwable e )
        {
            e.printStackTrace ();
            return false;
        }
    }

    /**
     * Performs data copy from input to output stream.
     *
     * @param in  data input stream
     * @param out data output stream
     * @throws IOException
     */
    private static void copyInputStream ( InputStream in, OutputStream out ) throws IOException
    {
        byte[] buffer = new byte[ 1024 ];
        int len;
        while ( ( len = in.read ( buffer ) ) >= 0 )
        {
            out.write ( buffer, 0, len );
        }
        in.close ();
        out.close ();
    }
}