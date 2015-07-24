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
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mikle Garin
 */

public class CoreSwingUtils
{
    /**
     * Returns window ancestor for specified component or null if it doesn't exist.
     *
     * @param component component to process
     * @return window ancestor for specified component or null if it doesn't exist
     */
    public static Window getWindowAncestor ( final Component component )
    {
        if ( component == null )
        {
            return null;
        }
        if ( component instanceof Window )
        {
            return ( Window ) component;
        }
        for ( Container p = component.getParent (); p != null; p = p.getParent () )
        {
            if ( p instanceof Window )
            {
                return ( Window ) p;
            }
        }
        return null;
    }

    /**
     * Will invoke the specified action later in EDT in case it is called from non-EDT thread.
     * Otherwise action will be performed immediately.
     *
     * @param runnable runnable
     */
    public static void invokeLater ( final Runnable runnable )
    {
        if ( SwingUtilities.isEventDispatchThread () )
        {
            runnable.run ();
        }
        else
        {
            SwingUtilities.invokeLater ( runnable );
        }
    }

    /**
     * Will invoke the specified action in EDT in case it is called from non-EDT thread.
     *
     * @param runnable runnable
     * @throws InterruptedException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static void invokeAndWait ( final Runnable runnable ) throws InterruptedException, InvocationTargetException
    {
        if ( SwingUtilities.isEventDispatchThread () )
        {
            runnable.run ();
        }
        else
        {
            SwingUtilities.invokeAndWait ( runnable );
        }
    }

    /**
     * Will invoke the specified action in EDT in case it is called from non-EDT thread.
     * It will also block any exceptions thrown by "invokeAndWait" method.
     *
     * @param runnable runnable
     */
    public static void invokeAndWaitSafely ( final Runnable runnable )
    {
        try
        {
            invokeAndWait ( runnable );
        }
        catch ( final Throwable e )
        {
            //
        }
    }
}