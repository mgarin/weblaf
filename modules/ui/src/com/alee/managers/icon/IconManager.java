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

package com.alee.managers.icon;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages customizable component icons.
 * It will eventually be in control of all icons used within WebLaF.
 *
 * @author Mikle Garin
 */

public final class IconManager
{
    /**
     * Icons information kept for lazy loading.
     */
    private static Map<String, IconInfo> lazyIcons;

    /**
     * Loaded icons cache.
     * It uses weak reference to ensure that icons will be removed from memory if not used any longer.
     * This cache is useful to avoid loading the same icon multiple times.
     */
    private static Map<String, WeakReference<Icon>> cache;

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes StyleManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Initializing lazy icons map and cache
            lazyIcons = new HashMap<String, IconInfo> ( 100 );
            cache = new HashMap<String, WeakReference<Icon>> ( 30 );
        }
    }

    /**
     * Registers lazy icon information.
     *
     * @param icon lazy icon information
     */
    public static void add ( final IconInfo icon )
    {
        // Saving lazy icon information
        lazyIcons.put ( icon.getId (), icon );

        // Clearing icon cache
        cache.remove ( icon.getId () );
    }

    /**
     * Returns icon for the specified ID.
     *
     * @param id unique icon ID
     * @return icon for the specified ID
     */
    public static Icon get ( final String id )
    {
        // Checking cache first
        final WeakReference<Icon> reference = cache.get ( id );
        if ( reference != null )
        {
            // Checking referenced icon existance
            final Icon icon = reference.get ();
            if ( icon != null )
            {
                // Returning cached icon
                return icon;
            }
        }

        // Loading lazy icon
        final IconInfo iconInfo = lazyIcons.get ( id );
        if ( iconInfo != null )
        {
            final Icon icon;

            // Retrieving icon
            final Class<? extends Icon> type = iconInfo.getType ();
            if ( type == ImageIcon.class )
            {
                // Loading common ImageIcon
                if ( iconInfo.getNearClass () != null )
                {
                    icon = new ImageIcon ( iconInfo.getNearClass ().getResource ( iconInfo.getPath () ) );
                }
                else
                {
                    icon = new ImageIcon ( iconInfo.getPath () );
                }
            }
            else
            {
                throw new IconException ( "Unknown icon class type: " + type );
            }

            // Caching icon
            cache.put ( id, new WeakReference<Icon> ( icon ) );

            // Returning icon
            return icon;
        }

        // Icon is not provided for the specified ID
        // This is pretty dire in most cases so we will throw an exception
        throw new IconException ( "Icon is not provided for ID: " + id );
    }
}