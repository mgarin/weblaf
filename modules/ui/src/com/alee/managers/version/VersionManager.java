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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.XmlUtils;

/**
 * Application version manager.
 *
 * @author Mikle Garin
 */

public class VersionManager
{
    /**
     * todo 1. Extend this manager to allow custom versions management.
     * todo 2. Implement custom version for WebLaF
     */

    /**
     * Library version.
     */
    protected static VersionInfo libraryVersion = null;

    /**
     * Initialization mark.
     */
    protected static boolean initialized = false;

    /**
     * Initializes VersionManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // VersionManager class alias
            XmlUtils.processAnnotations ( VersionInfo.class );
        }
    }

    /**
     * Returns library version.
     *
     * @return library version
     */
    public static VersionInfo getLibraryVersion ()
    {
        if ( libraryVersion == null )
        {
            libraryVersion = XmlUtils.fromXML ( WebLookAndFeel.class.getResource ( "resources/version.xml" ) );
        }
        return libraryVersion;
    }
}