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
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This utility class can be used to implement shape caching withing any painter or component.
 * This might be useful to improve component painting performance in case it uses complex shapes.
 *
 * @author Mikle Garin
 */

public final class ShapeUtils
{
    /**
     * Shapes cache map.
     */
    private static final Map<Component, Map<String, CachedShape>> shapeCache = new WeakHashMap<Component, Map<String, CachedShape>> ();

    /**
     * Returns shape with rounded corners created using specified points.
     *
     * @param round  corners round
     * @param points shape points
     * @return shape with rounded corners created using specified points
     */
    public static Shape createRoundedShape ( final int round, final Point... points )
    {
        if ( round > 0 )
        {
            final int[] r = new int[ points.length ];
            Arrays.fill ( r, round );
            return createRoundedShape ( points, r );
        }
        else
        {
            return createRoundedShape ( points, null );
        }
    }

    /**
     * Returns shape with rounded corners created using specified points.
     *
     * @param points shape points
     * @param round  corners round
     * @return shape with rounded corners created using specified points
     */
    public static Shape createRoundedShape ( final Point[] points, final int[] round )
    {
        if ( points == null || points.length < 3 )
        {
            throw new RuntimeException ( "There should be at least three points presented" );
        }
        if ( round != null && round.length != points.length )
        {
            throw new RuntimeException ( "Round array size should fit points array size" );
        }

        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        for ( int i = 0; i < points.length; i++ )
        {
            final Point p = points[ i ];
            if ( i == 0 )
            {
                // Start part
                final Point beforePoint = points[ points.length - 1 ];
                if ( round != null && round[ points.length - 1 ] <= 0 )
                {
                    gp.moveTo ( beforePoint.x, beforePoint.y );
                }
                else
                {
                    final Point actualBeforePoint = getRoundSidePoint ( round[ points.length - 1 ], beforePoint, p );
                    gp.moveTo ( actualBeforePoint.x, actualBeforePoint.y );
                }
                if ( round != null && round[ i ] <= 0 )
                {
                    gp.lineTo ( p.x, p.y );
                }
                else
                {
                    final Point before = getRoundSidePoint ( round[ i ], p, beforePoint );
                    final Point after = getRoundSidePoint ( round[ i ], p, points[ i + 1 ] );
                    gp.lineTo ( before.x, before.y );
                    gp.quadTo ( p.x, p.y, after.x, after.y );
                }
            }
            else
            {
                // Proceeding to next point
                if ( round != null && round[ i ] <= 0 )
                {
                    gp.lineTo ( p.x, p.y );
                }
                else
                {
                    final Point before = getRoundSidePoint ( round[ i ], p, points[ i - 1 ] );
                    final Point after = getRoundSidePoint ( round[ i ], p, points[ i < points.length - 1 ? i + 1 : 0 ] );
                    gp.lineTo ( before.x, before.y );
                    gp.quadTo ( p.x, p.y, after.x, after.y );
                }
            }
        }
        return gp;
    }

    /**
     * Returns rounded shape sequence point.
     *
     * @param round corner round
     * @param from  start point
     * @param to    end point
     * @return rounded shape sequence point
     */
    private static Point getRoundSidePoint ( final int round, final Point from, final Point to )
    {
        if ( from.y == to.y )
        {
            if ( from.x < to.x )
            {
                return new Point ( from.x + Math.min ( round, ( to.x - from.x ) / 2 ), from.y );
            }
            else
            {
                return new Point ( from.x - Math.min ( round, ( from.x - to.x ) / 2 ), from.y );
            }
        }
        else if ( from.x == to.x )
        {
            if ( from.y < to.y )
            {
                return new Point ( from.x, from.y + Math.min ( round, ( to.y - from.y ) / 2 ) );
            }
            else
            {
                return new Point ( from.x, from.y - Math.min ( round, ( from.y - to.y ) / 2 ) );
            }
        }
        else
        {
            // todo Add non-90-degree angles support
            throw new RuntimeException ( "Non-90-degree corners are not supported" );
        }
    }

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
        final String settingsKey = TextUtils.getSettingsKey ( settings );
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
     * Shape cache object.
     */
    private static class CachedShape
    {
        private final String key;
        private final Shape shape;

        /**
         * Constructs new cache object for the specified key and shape.
         *
         * @param key   shape cache key
         * @param shape cached shape
         */
        public CachedShape ( final String key, final Shape shape )
        {
            super ();
            this.key = key;
            this.shape = shape;
        }

        /**
         * Returns shape cache key.
         *
         * @return shape cache key
         */
        private String getKey ()
        {
            return key;
        }

        /**
         * Returns cached shape.
         *
         * @return cached shape
         */
        private Shape getShape ()
        {
            return shape;
        }
    }
}