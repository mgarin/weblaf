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

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Supplier;
import com.alee.painter.decoration.shape.Round;
import com.alee.painter.decoration.shape.ShapeType;
import com.alee.painter.decoration.shape.Sides;

import java.awt.*;
import java.awt.geom.GeneralPath;
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
     * {@link Shape}s cache.
     */
    private static final Map<Component, Map<String, CachedShape>> shapeCache = new WeakHashMap<Component, Map<String, CachedShape>> ();

    /**
     * Private constructor to avoid instantiation.
     */
    private ShapeUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns new border {@link Shape}.
     * This {@link Shape} might have disconnected parts in it, that is why fill-type {@link Shape}s are created separately.
     *
     * @param sw     outer shadow width
     * @param bounds {@link Rectangle} bounds within which {@link Shape} must fit
     * @param round  {@link Shape} corner {@link Round}
     * @param sides  {@link Sides} of the {@link Shape} that are visible
     * @return new border {@link Shape}
     */
    @NotNull
    public static Shape createBorderShape ( final int sw, @NotNull final Rectangle bounds, @NotNull final Round round,
                                            @NotNull final Sides sides )
    {
        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        boolean connect;
        boolean moved = false;
        if ( sides.top )
        {
            shape.moveTo (
                    bounds.x + ( sides.left ? sw + round.topLeft : 0 ),
                    bounds.y + sw
            );
            if ( sides.right )
            {
                shape.lineTo (
                        bounds.x + bounds.width - sw - round.topRight - 1,
                        bounds.y + sw
                );
                shape.quadTo (
                        bounds.x + bounds.width - sw - 1,
                        bounds.y + sw,
                        bounds.x + bounds.width - sw - 1,
                        bounds.y + sw + round.topRight
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x + bounds.width - 1,
                        bounds.y + sw
                );
            }
            connect = true;
        }
        else
        {
            connect = false;
        }
        if ( sides.right )
        {
            if ( !connect )
            {
                shape.moveTo (
                        bounds.x + bounds.width - sw - 1,
                        bounds.y
                );
                moved = true;
            }
            if ( sides.bottom )
            {
                shape.lineTo (
                        bounds.x + bounds.width - sw - 1,
                        bounds.y + bounds.height - sw - round.bottomRight - 1
                );
                shape.quadTo (
                        bounds.x + bounds.width - sw - 1,
                        bounds.y + bounds.height - sw - 1,
                        bounds.x + bounds.width - sw - round.bottomRight - 1,
                        bounds.y + bounds.height - sw - 1
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x + bounds.width - sw - 1,
                        bounds.y + bounds.height - 1
                );
            }
            connect = true;
        }
        else
        {
            connect = false;
        }
        if ( sides.bottom )
        {
            if ( !connect )
            {
                shape.moveTo (
                        bounds.x + bounds.width - 1,
                        bounds.y + bounds.height - sw - 1
                );
                moved = true;
            }
            if ( sides.left )
            {
                shape.lineTo (
                        bounds.x + sw + round.bottomLeft,
                        bounds.y + bounds.height - sw - 1
                );
                shape.quadTo (
                        bounds.x + sw,
                        bounds.y + bounds.height - sw - 1,
                        bounds.x + sw,
                        bounds.y + bounds.height - sw - round.bottomLeft - 1
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x,
                        bounds.y + bounds.height - sw - 1
                );
            }
            connect = true;
        }
        else
        {
            connect = false;
        }
        if ( sides.left )
        {
            if ( !connect )
            {
                shape.moveTo (
                        bounds.x + sw,
                        bounds.y + bounds.height - 1
                );
                moved = true;
            }
            if ( sides.top )
            {
                shape.lineTo (
                        bounds.x + sw,
                        bounds.y + sw + round.topLeft
                );
                shape.quadTo (
                        bounds.x + sw,
                        bounds.y + sw,
                        bounds.x + sw + round.topLeft,
                        bounds.y + sw
                );
                if ( !moved )
                {
                    shape.closePath ();
                }
            }
            else
            {
                shape.lineTo ( bounds.x + sw, bounds.y );
            }
        }
        return shape;
    }

    /**
     * Returns new fill {@link Shape}.
     * This {@link Shape} must always have enclosed area to be filled
     *
     * @param sw     outer shadow width
     * @param bounds {@link Rectangle} bounds within which {@link Shape} must fit
     * @param round  {@link Shape} corner {@link Round}
     * @param sides  {@link Sides} of the {@link Shape} that are visible
     * @param type   {@link ShapeType} used to adjust {@link Shape}
     * @return new fill {@link Shape}
     */
    @NotNull
    public static Shape createFillShape ( final int sw, @NotNull final Rectangle bounds, @NotNull final Round round,
                                          @NotNull final Sides sides, @NotNull final ShapeType type )
    {
        // Special offset for outer shadow to make it connect on the edges of two grouped components
        // Otherwise shadow will appear slightly rounded and bumpy between such components which is not the best case
        final int outerShadowOffset = type.isOuterShadow () ? sw : 0;

        // Special adjustment for round to ensure it doesn't fill any pixels outside of the border
        // Without this background alias on the edges will sometimes be visible outside of the border
        // This is a dirty solution, but the is no better way to do this
        final Round r = new Round (
                round.topLeft + 1,
                round.topRight + 1,
                round.bottomRight + 1,
                round.bottomLeft + 1
        );

        // Constructing fill shape
        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( sides.top )
        {
            shape.moveTo (
                    bounds.x + ( sides.left ? sw + r.topLeft : -outerShadowOffset ),
                    bounds.y + sw
            );
            if ( sides.right )
            {
                shape.lineTo (
                        bounds.x + bounds.width - sw - r.topRight,
                        bounds.y + sw
                );
                shape.quadTo (
                        bounds.x + bounds.width - sw,
                        bounds.y + sw,
                        bounds.x + bounds.width - sw,
                        bounds.y + sw + r.topRight
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x + bounds.width + outerShadowOffset,
                        bounds.y + sw
                );
            }
        }
        else
        {
            shape.moveTo (
                    bounds.x + ( sides.left ? sw : -outerShadowOffset ),
                    bounds.y - outerShadowOffset
            );
            shape.lineTo (
                    bounds.x + bounds.width + ( sides.right ? -sw : outerShadowOffset ),
                    bounds.y - outerShadowOffset
            );
        }
        if ( sides.right )
        {
            if ( sides.bottom )
            {
                shape.lineTo (
                        bounds.x + bounds.width - sw,
                        bounds.y + bounds.height - sw - r.bottomRight
                );
                shape.quadTo (
                        bounds.x + bounds.width - sw,
                        bounds.y + bounds.height - sw,
                        bounds.x + bounds.width - sw - r.bottomRight,
                        bounds.y + bounds.height - sw
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x + bounds.width - sw,
                        bounds.y + bounds.height + outerShadowOffset
                );
            }
        }
        else
        {
            shape.lineTo (
                    bounds.x + bounds.width + outerShadowOffset,
                    bounds.y + bounds.height + ( sides.bottom ? -sw : outerShadowOffset )
            );
        }
        if ( sides.bottom )
        {
            if ( sides.left )
            {
                shape.lineTo (
                        bounds.x + sw + r.bottomLeft,
                        bounds.y + bounds.height - sw
                );
                shape.quadTo (
                        bounds.x + sw,
                        bounds.y + bounds.height - sw,
                        bounds.x + sw,
                        bounds.y + bounds.height - sw - r.bottomLeft
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x - outerShadowOffset,
                        bounds.y + bounds.height - sw
                );
            }
        }
        else
        {
            shape.lineTo (
                    bounds.x + ( sides.left ? sw : -outerShadowOffset ),
                    bounds.y + bounds.height + outerShadowOffset
            );
        }
        if ( sides.left )
        {
            if ( sides.top )
            {
                shape.lineTo (
                        bounds.x + sw,
                        bounds.y + sw + r.topLeft
                );
                shape.quadTo (
                        bounds.x + sw,
                        bounds.y + sw,
                        bounds.x + sw + r.topLeft,
                        bounds.y + sw
                );
            }
            else
            {
                shape.lineTo (
                        bounds.x + sw,
                        bounds.y - outerShadowOffset
                );
            }
        }
        else
        {
            shape.lineTo (
                    bounds.x - outerShadowOffset,
                    bounds.y + ( sides.top ? sw : -outerShadowOffset )
            );
        }
        return shape;
    }

    /**
     * Returns {@link Shape} cached for the specified {@link Component} and identifier.
     * If {@link Shape} is not yet cached it will be created using specified {@link Supplier}.
     * If {@link Shape} settings have changed from the last time it was requested it will also be created again.
     *
     * @param component     {@link Component} for which {@link Shape} is cached
     * @param shapeId       {@link Shape} identifier unique for the {@link Component}
     * @param shapeSupplier {@link Supplier} for the {@link Shape}
     * @param settings      {@link Shape} settings used as a shape key
     * @param <T>           {@link Shape} type
     * @return {@link Shape} cached for the specified {@link Component} and identifier
     */
    @NotNull
    public static <T extends Shape> T getShape ( @NotNull final Component component, @NotNull final String shapeId,
                                                 @NotNull final Supplier<T> shapeSupplier, @NotNull final Object... settings )
    {
        final T shape;
        final String settingsKey = TextUtils.getSettingsKey ( settings );
        Map<String, CachedShape> cacheById = shapeCache.get ( component );
        if ( cacheById == null )
        {
            // Shape is not yet cached
            shape = shapeSupplier.get ();
            cacheById = new HashMap<String, CachedShape> ( 1 );
            cacheById.put ( shapeId, new CachedShape ( settingsKey, shape ) );
            shapeCache.put ( component, cacheById );
        }
        else
        {
            final CachedShape cachedShape = cacheById.get ( shapeId );
            if ( cachedShape == null || !cachedShape.key.equals ( settingsKey ) )
            {
                // Shape is not yet cached or cache entry is outdated
                shape = shapeSupplier.get ();
                cacheById.put ( shapeId, new CachedShape ( settingsKey, shape ) );
            }
            else
            {
                // Returning cached shape
                shape = ( T ) cachedShape.shape;
            }
        }
        return shape;
    }

    /**
     * {@link Shape} cache.
     */
    private static class CachedShape
    {
        /**
         * {@link Shape} cache key.
         */
        @NotNull
        public final String key;

        /**
         * Cached {@link Shape}.
         */
        @NotNull
        public final Shape shape;

        /**
         * Constructs new cache object for the specified key and shape.
         *
         * @param key   {@link Shape} cache key
         * @param shape cached {@link Shape}
         */
        public CachedShape ( @NotNull final String key, @NotNull final Shape shape )
        {
            this.key = key;
            this.shape = shape;
        }
    }
}