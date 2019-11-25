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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.svg.*;
import com.alee.managers.icon.data.IconSource;
import com.alee.managers.icon.data.ImageIconSource;
import com.alee.managers.icon.data.SetIcon;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.icon.set.XmlIconSet;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CollectionUtils;
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
     * Added {@link IconSet}s.
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
            XmlUtils.processAnnotations ( XmlIconSet.class );

            // ImageIcon aliases
            XmlUtils.processAnnotations ( ImageIconSource.class );

            // SvgIcon aliases
            XmlUtils.processAnnotations ( SvgIconSource.class );
            XmlUtils.processAnnotations ( SvgStroke.class );
            XmlUtils.processAnnotations ( SvgFill.class );
            XmlUtils.processAnnotations ( SvgOpacity.class );
            XmlUtils.processAnnotations ( SvgTransform.class );
            XmlUtils.processAnnotations ( SvgGrayscale.class );
            XmlUtils.processAnnotations ( SvgColorOpacity.class );
        }
    }

    /**
     * Returns whether or not {@link IconSet} with the specified identifier exists.
     *
     * @param id {@link IconSet} identifier
     * @return {@code true} if {@link IconSet} with the specified identifier exists, {@code false} otherwise
     */
    public static boolean hasIconSet ( @NotNull final String id )
    {
        return findIconSet ( id ) != null;
    }

    /**
     * Returns {@link IconSet} with the specified identifier.
     *
     * @param id {@link IconSet} identifier
     * @return {@link IconSet} with the specified identifier
     */
    @NotNull
    public static IconSet getIconSet ( @NotNull final String id )
    {
        final IconSet iconSet = findIconSet ( id );
        if ( iconSet == null )
        {
            throw new IconException ( "IconSet with identifier doesn't exist: " + id );
        }
        return iconSet;
    }

    /**
     * Returns {@link IconSet} with the specified identifier or {@code null} if none found.
     * Order of search is irrelevant in this method since {@link IconSet} identifiers are unique.
     *
     * @param id {@link IconSet} identifier
     * @return {@link IconSet} with the specified identifier or {@code null} if none found
     */
    @Nullable
    public static IconSet findIconSet ( @NotNull final String id )
    {
        IconSet iconSet = null;
        for ( final IconSet set : iconSets )
        {
            if ( set.getId ().equals ( id ) )
            {
                iconSet = set;
                break;
            }
        }
        if ( iconSet != null )
        {
            for ( final IconSet set : StyleManager.getSkin ().getIconSets () )
            {
                if ( set.getId ().equals ( id ) )
                {
                    iconSet = set;
                    break;
                }
            }
        }
        return iconSet;
    }

    /**
     * Adds new {@link IconSet}.
     *
     * @param iconSet {@link IconSet} to add
     */
    public static void addIconSet ( @NotNull final IconSet iconSet )
    {
        if ( !hasIconSet ( iconSet.getId () ) )
        {
            // Adding new icon set
            iconSets.add ( iconSet );

            // Clearing cache for any icons from that set
            clearIconSetCache ( iconSet );
        }
        else if ( iconSets.contains ( iconSet ) )
        {
            throw new IconException ( "Specified IconSet is already added: " + iconSet.getId () );
        }
        else
        {
            throw new IconException ( "Different IconSet with the same identifier is already added: " + iconSet.getId () );
        }
    }

    /**
     * Removes {@link IconSet}.
     *
     * @param iconSet {@link IconSet} to remove
     */
    public static void removeIconSet ( @NotNull final IconSet iconSet )
    {
        final IconSet existingIconSet = getIconSet ( iconSet.getId () );
        if ( existingIconSet == iconSet )
        {
            if ( iconSets.contains ( iconSet ) )
            {
                // Removing icon set
                iconSets.remove ( iconSet );

                // Clearing its cache
                clearIconSetCache ( iconSet );
            }
            else
            {
                throw new IconException ( "Skin and SkinExtension IconSets cannot be removed from IconManager: " + iconSet.getId () );
            }
        }
        else
        {
            throw new IconException ( "Specified IconSet is different from existing one with the same identifier: " + iconSet.getId () );
        }
    }

    /**
     * Clears global cache for the specified {@link IconSet}.
     *
     * @param iconSet {@link IconSet} to clear global cache for
     */
    public static void clearIconSetCache ( @NotNull final IconSet iconSet )
    {
        for ( final String id : iconSet.getIds () )
        {
            cache.remove ( id );
        }
    }

    /**
     * Clears global cache for the specified {@link IconSource}.
     *
     * @param icon {@link IconSource} to clear global cache for
     */
    public static void clearIconCache ( @NotNull final IconSource icon )
    {
        cache.remove ( icon.getId () );
    }

    /**
     * Clears global cache for all icons.
     */
    public static void clearIconCaches ()
    {
        cache.clear ();
    }

    /**
     * Returns whether or not {@link Icon} for the specified identifier exists.
     *
     * @param id icon identifier
     * @return {@code true} if {@link Icon} for the specified identifier exists, {@code false} otherwise
     */
    public static boolean hasIcon ( @NotNull final String id )
    {
        return findIcon ( id ) != null;
    }

    /**
     * Returns {@link Icon} for the specified identifier.
     *
     * @param id  {@link Icon} identifier
     * @param <I> {@link Icon} type
     * @return {@link Icon} for the specified identifier
     * @throws IconException if {@link Icon} cannot be found for the specified identifier
     */
    @NotNull
    public static <I extends Icon> I getIcon ( @NotNull final String id )
    {
        final I icon = findIcon ( id );
        if ( icon != null )
        {
            return icon;
        }
        else
        {
            throw new IconException ( "Could not find Icon for identifier: " + id );
        }
    }

    /**
     * Returns {@link Icon} for the specified identifier or {@code null} if it cannot be found.
     *
     * @param id  {@link Icon} identifier
     * @param <I> {@link Icon} type
     * @return {@link Icon} for the specified identifier or {@code null} if it cannot be found
     */
    @Nullable
    public static <I extends Icon> I findIcon ( @NotNull final String id )
    {
        final WeakReference<Icon> reference = cache.get ( id );
        I icon = reference != null ? ( I ) reference.get () : null;
        if ( icon == null )
        {
            icon = findIcon ( id, iconSets );
            if ( icon == null )
            {
                icon = findIcon ( id, StyleManager.getSkin ().getIconSets () );
            }
            cache.put ( id, new WeakReference<Icon> ( icon ) );
        }
        return icon;
    }

    /**
     * Returns {@link Icon} for the specified identifier in the specified {@link IconSet}s or {@code null} if it cannot be found.
     *
     * @param id       {@link Icon} identifier
     * @param iconSets {@link List} of {@link IconSet}s to look in
     * @param <I>      {@link Icon} type
     * @return {@link Icon} for the specified identifier in the specified {@link IconSet}s or {@code null} if it cannot be found
     */
    @Nullable
    private static <I extends Icon> I findIcon ( @NotNull final String id, final List<IconSet> iconSets )
    {
        I icon = null;
        if ( CollectionUtils.notEmpty ( iconSets ) )
        {
            final ListIterator<IconSet> iter = iconSets.listIterator ( iconSets.size () );
            while ( iter.hasPrevious () )
            {
                icon = ( I ) iter.previous ().findIcon ( id );
                if ( icon != null )
                {
                    break;
                }
            }
        }
        return icon;
    }
}