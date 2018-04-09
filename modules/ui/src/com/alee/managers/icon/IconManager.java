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

import com.alee.extended.svg.*;
import com.alee.managers.icon.data.AbstractIconData;
import com.alee.managers.icon.data.ImageIconData;
import com.alee.managers.icon.data.SetIcon;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.icon.set.IconSetData;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * This class manages customizable component {@link Icon}s.
 * All {@link Icon}s are provided through {@link IconSet} implementations that can be added and removed here.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 */
public final class IconManager
{
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

            // Added icon sets
            iconSets = new ArrayList<IconSet> ( 2 );

            // Simple icons cache
            cache = new HashMap<String, WeakReference<Icon>> ( 60 );

            // Base XStream aliases
            XmlUtils.processAnnotations ( SetIcon.class );
            XmlUtils.processAnnotations ( IconSetData.class );
            XmlUtils.processAnnotations ( AbstractIconData.class );

            // ImageIcon aliases
            XmlUtils.processAnnotations ( ImageIconData.class );

            // SvgIcon aliases
            XmlUtils.processAnnotations ( SvgIconData.class );
            XmlUtils.processAnnotations ( SvgStroke.class );
            XmlUtils.processAnnotations ( SvgFill.class );
            XmlUtils.processAnnotations ( SvgTransform.class );
            XmlUtils.processAnnotations ( SvgGrayscale.class );
        }
    }

    /**
     * Returns icon set with the specified identifier.
     *
     * @param id icon set identifier
     * @return icon set with the specified identifier
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
        // Removing existing set with the same identifier
        removeIconSet ( iconSet.getId () );

        // Adding new set
        iconSets.add ( iconSet );
    }

    /**
     * Removes icon set.
     *
     * @param id icon set identifier
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
     * Returns whether or not icon for the specified identifier exists.
     *
     * @param id icon identifier
     * @return icon for the specified identifier
     */
    public static boolean hasIcon ( final String id )
    {
        return getIconImpl ( id ) != null;
    }

    /**
     * Returns {@link Icon} for the specified identifier.
     *
     * @param id  {@link Icon} identifier
     * @param <I> {@link Icon} type
     * @return {@link Icon} for the specified identifier
     * @throws IconException if {@link Icon} cannot be found for the specified identifier
     */
    public static <I extends Icon> I getIcon ( final String id )
    {
        if ( iconSets.size () > 0 )
        {
            // Looking for an icon
            final I icon = getIconImpl ( id );
            if ( icon != null )
            {
                // Returning icon we found
                return icon;
            }
            else
            {
                // No icon found
                final String msg = "Could not find Icon for identifier: %s";
                throw new IconException ( String.format ( msg, id ) );
            }
        }
        else
        {
            // No icon sets added at all
            final String msg = "There are no icon sets added";
            throw new IconException ( msg );
        }
    }

    /**
     * Returns icon for the specified identifier.
     *
     * @param id icon identifier
     * @return icon for the specified identifier
     */
    private static <I extends Icon> I getIconImpl ( final String id )
    {
        // Checking cached icon
        final WeakReference<Icon> reference = cache.get ( id );
        I icon = reference != null ? ( I ) reference.get () : null;
        if ( icon == null )
        {
            // Checking icon sets from the end
            final ListIterator<IconSet> iter = iconSets.listIterator ( iconSets.size () );
            while ( iter.hasPrevious () )
            {
                // Stop looking for an icon as soon as we found one with the specified identifier
                icon = ( I ) iter.previous ().getIcon ( id );
                if ( icon != null )
                {
                    break;
                }
            }
        }
        return icon;
    }
}