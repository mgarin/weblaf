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
import com.alee.utils.zip.UnzipListener;

import java.io.File;
import java.io.FileOutputStream;
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
     */
    public static void unzip ( @NotNull final String archive, @NotNull final String dst )
    {
        unzip ( archive, dst, null );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive  ZIP file path
     * @param dst      destination directory path
     * @param listener extraction process listener
     */
    public static void unzip ( @NotNull final String archive, @NotNull final String dst, @Nullable final UnzipListener listener )
    {
        unzip ( new File ( archive ), new File ( dst ), listener );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive ZIP file
     * @param dst     destination directory
     */
    public static void unzip ( @NotNull final File archive, @NotNull final File dst )
    {
        unzip ( archive, dst, null );
    }

    /**
     * Extracts ZIP archive contents into destination directory.
     * Any folder required for extraction are created in the process.
     *
     * @param archive  ZIP file
     * @param dst      destination directory
     * @param listener extraction process listener
     */
    public static void unzip ( @NotNull final File archive, @NotNull final File dst, @Nullable final UnzipListener listener )
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
                    final File parent = FileUtils.getParent ( extractToFile );
                    if ( parent != null && parent.mkdirs () )
                    {
                        // Creating destination file
                        extractToFile.createNewFile ();

                        // Copying file content
                        IOUtils.copy (
                                zipFile.getInputStream ( entry ),
                                new FileOutputStream ( extractToFile )
                        );
                    }
                }

                // Informing listener about single unzipped file
                if ( listener != null )
                {
                    listener.fileUnzipped ( entry, extractToFile, index );
                    index++;
                }
            }
            zipFile.close ();
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to unzip ZIP archive: " + archive, e );
        }
    }

    /**
     * Returns file name for the specified zip entry.
     *
     * @param zipEntry zip entry to process
     * @return file name for the specified zip entry
     */
    @NotNull
    public static String getFileName ( @NotNull final ZipEntry zipEntry )
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
    @NotNull
    public static String getFileLocation ( @NotNull final ZipEntry zipEntry )
    {
        final String name = zipEntry.getName ();
        return name.substring ( 0, name.lastIndexOf ( "/" ) + 1 );
    }
}