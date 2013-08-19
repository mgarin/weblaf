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
 * User: mgarin Date: 03.08.12 Time: 16:46
 */

public final class VersionManager
{
    // Library version
    private static VersionInfo libraryVersion = null;

    // Initialization mark
    private static boolean initialized = false;

    /**
     * Manager initialization
     */

    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // VersionManager class alias
            XmlUtils.processAnnotations ( VersionInfo.class );
        }
    }

    /**
     * Current library version
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