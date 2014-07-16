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

import com.alee.utils.swing.DataProvider;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This utility class can be used to implement shape caching withing any painter or component.
 * This might be useful to improve component painting performance in case it uses complex shapes.
 *
 * @author Mikle Garin
 */

public final class ShapeCache
{
    /**
     * todo 1. Improve shape settings key generation/usage performance
     */

    /**
     * Separator for settings cached within single key.
     */
    private static final String settingsSeparator = ";";

    /**
     * Shapes cache map.
     */
    private static final Map<Component, Map<String, CachedShape>> shapeCache = new WeakHashMap<Component, Map<String, CachedShape>> ();

    /**
     * Returns cached component shape.
     * If shape is not yet cached it will be created.
     * If shape settings are changed from the last time it was queued it will be re-created.
     *
     * @param component     component for which shape is cached
     * @param shapeId       unique shape ID
     * @param shapeProvider shape provider
     * @param settings      shape settings used as a shape key
     * @param <T>           shape type
     * @return cached component shape
     */
    public static <T extends Shape> T getShape ( final Component component, final String shapeId, final DataProvider<T> shapeProvider,
                                                 final Object... settings )
    {
        final String settingsKey = combineSettingsKey ( settings );
        Map<String, CachedShape> cacheById = shapeCache.get ( component );
        if ( cacheById == null )
        {
            // Shape is not yet cached
            final Shape shape = shapeProvider.provide ();
            cacheById = new HashMap<String, CachedShape> ( 1 );
            cacheById.put ( shapeId, new CachedShape ( settingsKey, shape ) );
            shapeCache.put ( component, cacheById );
            return ( T ) shape;
        }
        else
        {
            final CachedShape cachedShape = cacheById.get ( shapeId );
            if ( cachedShape == null || !cachedShape.getKey ().equals ( settingsKey ) )
            {
                // Shape is not yet cached or cache entry is outdated
                final Shape shape = shapeProvider.provide ();
                cacheById.put ( shapeId, new CachedShape ( settingsKey, shape ) );
                return ( T ) shape;
            }
            else
            {
                // Returning cached shape
                return ( T ) cachedShape.getShape ();
            }
        }
    }

    /**
     * Combines shape settings into a single key for cache map and returns it.
     *
     * @param settings settings to combine
     * @return key for the specified shape settings
     */
    private static String combineSettingsKey ( final Object... settings )
    {
        final StringBuilder stringBuilder = new StringBuilder ();
        for ( final Object object : settings )
        {
            if ( stringBuilder.length () > 0 )
            {
                stringBuilder.append ( settingsSeparator );
            }
            stringBuilder.append ( getSettingKey ( object ) );
        }
        return stringBuilder.toString ();
    }

    /**
     * Returns setting string representation.
     *
     * @param setting setting to be converted
     * @return setting string representation
     */
    private static String getSettingKey ( final Object setting )
    {
        if ( setting instanceof Insets )
        {
            final Insets i = ( Insets ) setting;
            return i.top + "," + i.left + "," + i.bottom + "," + i.right;
        }
        else if ( setting instanceof Rectangle )
        {
            final Rectangle r = ( Rectangle ) setting;
            return r.x + "," + r.y + "," + r.width + "," + r.height;
        }
        else if ( setting instanceof Point )
        {
            final Point p = ( Point ) setting;
            return p.x + "," + p.y;
        }
        else
        {
            return setting.toString ();
        }
    }

    /**
     * Cached shape class.
     */
    private static class CachedShape
    {
        private final String key;
        private final Shape shape;

        public CachedShape ( final String key, final Shape shape )
        {
            super ();
            this.key = key;
            this.shape = shape;
        }

        private String getKey ()
        {
            return key;
        }

        private Shape getShape ()
        {
            return shape;
        }
    }
}