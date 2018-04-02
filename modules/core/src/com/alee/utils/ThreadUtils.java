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

/**
 * This class provides a set of utilities to work with threads.
 *
 * @author Mikle Garin
 */
public final class ThreadUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private ThreadUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Causes calling thread to sleep and ignores thrown InterruptedException.
     *
     * @param millis time to sleep
     */
    public static void sleepSafely ( final long millis )
    {
        try
        {
            Thread.sleep ( millis );
        }
        catch ( final InterruptedException ignored )
        {
            //
        }
    }
}