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

import com.alee.managers.icon.data.*;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.icon.set.IconSetData;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * todo 1. Move default icon sets into styles?
     */

    /**
     * Added icon sets.
     */
    private static List<IconSet> iconSets;

    /**
     * Global icons cache.
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

            // Icon sets
            iconSets = new ArrayList<IconSet> ( 2 );
            cache = new HashMap<String, WeakReference<Icon>> ( 60 );

            // Base XStream aliases
            XmlUtils.processAnnotations ( SetIconContent.class );
            XmlUtils.processAnnotations ( IconSetData.class );
            XmlUtils.processAnnotations ( IconData.class );

            // ImageIcon aliases
            XmlUtils.processAnnotations ( ImageIconData.class );

            // SvgIcon aliases
            XmlUtils.processAnnotations ( SvgIconData.class );
            XmlUtils.processAnnotations ( SvgStroke.class );
            XmlUtils.processAnnotations ( SvgFill.class );
            XmlUtils.processAnnotations ( SvgTransform.class );
        }
    }

    /**
     * Returns icon set with the specified ID.
     *
     * @param id icon set ID
     * @return icon set with the specified ID
     */
    public static IconSet getIconSet ( final String id )
    {
        for ( final IconSet iconSet : iconSets )
        {
            if ( iconSet.getId ().equals ( id ) )
            {
                return iconSet;
            }
        }
        return null;
    }

    /**
     * Adds new icon set.
     *
     * @param iconSet icon set to add
     */
    public static void addIconSet ( final IconSet iconSet )
    {
        // Removing existing set with the same ID
        removeIconSet ( iconSet.getId () );

        // Adding new set
        iconSets.add ( iconSet );
    }

    /**
     * Removes icon set.
     *
     * @param id icon set ID
     */
    public static void removeIconSet ( final String id )
    {
        final IconSet iconSet = getIconSet ( id );
        if ( iconSet != null )
        {
            // Removing icon set
            iconSets.remove ( iconSet );

            // Clearing its cache
            clearIconSetCache ( iconSet );
        }
    }

    /**
     * Removes icon set.
     *
     * @param iconSet icon set to remove
     */
    public static void removeIconSet ( final IconSet iconSet )
    {
        removeIconSet ( iconSet.getId () );
    }

    /**
     * Clears global cache for the specified icon set.
     *
     * @param iconSet icon set to clear global cache for
     */
    private static void clearIconSetCache ( final IconSet iconSet )
    {
        for ( final String id : iconSet.getIds () )
        {
            cache.remove ( id );
        }
    }

    /**
     * Returns icon for the specified ID.
     *
     * @param id icon ID
     * @return icon for the specified ID
     */
    public static Icon getIcon ( final String id )
    {
        if ( iconSets.size () > 0 )
        {
            // Checking cached icon
            final WeakReference<Icon> reference = cache.get ( id );
            Icon icon = reference != null ? reference.get () : null;
            if ( icon != null )
            {
                return icon;
            }

            // Checking icon sets
            for ( final IconSet iconSet : iconSets )
            {
                icon = iconSet.getIcon ( id );
                if ( icon != null )
                {
                    return icon;
                }
            }

            // No icon found
            throw new IconException ( "Could not find Icon for ID: " + id );
        }
        else
        {
            throw new IconException ( "There are no icon sets added" );
        }
    }
}