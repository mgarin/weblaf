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
import java.lang.reflect.InvocationTargetException;

/**
 * This utility class operates only with proprietary API calls.
 * Their usage is inevitable, otherwise i would have replaced them with something else.
 *
 * @author Mikle Garin
 */

public class ProprietaryUtils
{
    /**
     * Installs some proprietary L&F defaults for proper text rendering.
     * <p/>
     * Basically this method is a workaround for this simple call:
     * <code>
     * table.put ( sun.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY, sun.swing.SwingUtilities2.AATextInfo.getAATextInfo ( true ) );
     * </code>
     * but it doesn't directly use any proprietary API.
     *
     * @param table defaults table
     */
    public static void setupDefaults ( UIDefaults table )
    {
        try
        {
            Class su2 = ReflectUtils.getClass ( "sun.swing.SwingUtilities2" );
            Object aaProperty = ReflectUtils.getStaticFieldValue ( su2, "AA_TEXT_PROPERTY_KEY" );
            Class aaTextInfo = ReflectUtils.getInnerClass ( su2, "AATextInfo" );
            Object aaValue = ReflectUtils.callStaticMethod ( aaTextInfo, "getAATextInfo", true );
            table.put ( aaProperty, aaValue );
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace ();
        }
        catch ( NoSuchFieldException e )
        {
            e.printStackTrace ();
        }
        catch ( IllegalAccessException e )
        {
            e.printStackTrace ();
        }
        catch ( NoSuchMethodException e )
        {
            e.printStackTrace ();
        }
        catch ( InvocationTargetException e )
        {
            e.printStackTrace ();
        }
    }
}