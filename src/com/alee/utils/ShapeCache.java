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

public class ShapeCache
{
    private static final Map<Component, Map<String, CachedShape>> shapeCache = new WeakHashMap<Component, Map<String, CachedShape>> ( 10 );

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

    private static String combineSettingsKey ( final Object... settings )
    {
        final StringBuilder stringBuilder = new StringBuilder ();
        for ( final Object object : settings )
        {
            stringBuilder.append ( object.toString () );
        }
        return stringBuilder.toString ();
    }

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