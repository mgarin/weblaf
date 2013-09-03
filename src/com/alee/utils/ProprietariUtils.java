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

import javax.swing.*;

/**
 * This utility class operates only with proprietari API calls.
 * Their usage is inevitable, otherwise i would have replaced them with something else.
 *
 * @author Mikle Garin
 */

public class ProprietariUtils
{
    /**
     * Installs some proprietari L&F defaults.
     *
     * @param table defaults table
     */
    public static void setupDefaults ( UIDefaults table )
    {
        // Required for proper text antialias
        table.put ( sun.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY, sun.swing.SwingUtilities2.AATextInfo.getAATextInfo ( true ) );
    }
}