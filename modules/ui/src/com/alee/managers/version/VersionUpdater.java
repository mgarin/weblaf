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

package com.alee.managers.version;

import com.alee.utils.XmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is only needed to update library version files during the build process.
 *
 * @author Mikle Garin
 */
public final class VersionUpdater
{
    /**
     * File separator.
     */
    private static final String s = File.separator;

    /**
     * Properties file path.
     */
    private static final String propertiesPath = "build" + s + "version.properties";

    /**
     * XML file path.
     */
    private static final String versionPath =
            "modules" + s + "ui" + s + "src" + s + "com" + s + "alee" + s + "laf" + s + "resources" + s + "version.xml";

    /**
     * Increments library build version and updates build date.
     *
     * @param args arguments
     * @throws IOException when fails to access some of the files
     */
    public static void main ( final String[] args ) throws IOException
    {
        VersionManager.initialize ();
        if ( args.length == 0 || args[ 0 ].equals ( "increment" ) || args[ 0 ].equals ( "decrement" ) )
        {
            updateVersion ( args.length == 0 || args[ 0 ].equals ( "increment" ) ? 1 : -1 );
        }
        else if ( args[ 0 ].equals ( "update" ) )
        {
            updateVersion ( -1 );
            updateVersion ( +1 );
        }
    }

    /**
     * Performs version updates.
     *
     * @param change version change amount
     * @throws IOException when fails to access some of the files
     */
    private static void updateVersion ( final int change ) throws IOException
    {
        // Updating version in properties
        final File propertiesFile = new File ( propertiesPath );
        final Properties properties = new Properties ();
        final FileInputStream inputStream = new FileInputStream ( propertiesFile );
        properties.load ( inputStream );
        inputStream.close ();
        properties.setProperty ( "build.number", "" + ( Integer.parseInt ( properties.getProperty ( "build.number" ) ) + change ) );
        final FileOutputStream outputStream = new FileOutputStream ( propertiesFile );
        properties.store ( outputStream, null );
        outputStream.close ();

        // Updating version in XML
        final File versionFile = new File ( versionPath );
        final VersionInfo versionInfo = XmlUtils.fromXML ( versionFile );
        versionInfo.setBuild ( versionInfo.getBuild () + change );
        versionInfo.setDate ( System.currentTimeMillis () );
        XmlUtils.toXML ( versionInfo, versionFile );
    }
}