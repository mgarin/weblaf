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

import com.alee.utils.swing.SizeMethods;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class provides a set of utilities to modify component sizes.
 *
 * @author Mikle Garin
 */

public final class SizeUtils
{
    /**
     * todo 1. Add maximum size options
     */

    /**
     * Weak hash map for keeping component sizes.
     */
    private static final Map<Component, Dimension> preferredSizeCache = new WeakHashMap<Component, Dimension> ();
    private static final Map<Component, Dimension> minimumSizeCache = new WeakHashMap<Component, Dimension> ();
    private static final Map<Component, Dimension> maximumSizeCache = new WeakHashMap<Component, Dimension> ();

    /**
     * Returns component preferred width.
     *
     * @param component component to process
     * @param <C>       component type
     * @return component preferred width
     */
    public static <C extends Component> int getPreferredWidth ( final C component )
    {
        final Dimension ps = preferredSizeCache.get ( component );
        return ps != null ? ps.width : SizeMethods.UNDEFINED;
    }

    /**
     * Sets component preferred width.
     * Pass {@link com.alee.utils.swing.SizeMethods#UNDEFINED} to let component choose preferred width on its own.
     *
     * @param component      component to process
     * @param preferredWidth new component preferred width
     * @param <C>            component type
     * @return modified component
     */
    public static <C extends Component> C setPreferredWidth ( final C component, final int preferredWidth )
    {
        Dimension ps = preferredSizeCache.get ( component );
        if ( ps == null )
        {
            ps = new Dimension ( preferredWidth, SizeMethods.UNDEFINED );
            preferredSizeCache.put ( component, ps );
        }
        else
        {
            ps.width = preferredWidth;
        }
        return component;
    }

    /**
     * Returns component preferred height.
     *
     * @param component component to process
     * @param <C>       component type
     * @return component preferred height
     */
    public static <C extends Component> int getPreferredHeight ( final C component )
    {
        final Dimension ps = preferredSizeCache.get ( component );
        return ps != null ? ps.height : SizeMethods.UNDEFINED;
    }

    /**
     * Sets component preferred height.
     * Pass {@link com.alee.utils.swing.SizeMethods#UNDEFINED} to let component choose preferred height on its own.
     *
     * @param component       component to process
     * @param preferredHeight new component preferred height
     * @param <C>             component type
     * @return modified component
     */
    public static <C extends Component> C setPreferredHeight ( final C component, final int preferredHeight )
    {
        Dimension ps = preferredSizeCache.get ( component );
        if ( ps == null )
        {
            ps = new Dimension ( SizeMethods.UNDEFINED, preferredHeight );
            preferredSizeCache.put ( component, ps );
        }
        else
        {
            ps.height = preferredHeight;
        }
        return component;
    }

    /**
     * Returns component minimum width.
     *
     * @param component component to process
     * @param <C>       component type
     * @return component minimum width
     */
    public static <C extends Component> int getMinimumWidth ( final C component )
    {
        final Dimension ms = minimumSizeCache.get ( component );
        return ms != null ? ms.width : SizeMethods.UNDEFINED;
    }

    /**
     * Sets component minimum width.
     * Pass {@link com.alee.utils.swing.SizeMethods#UNDEFINED} to let component choose minimum width on its own.
     *
     * @param component    component to process
     * @param minimumWidth new component minimum width
     * @param <C>          component type
     * @return modified component
     */
    public static <C extends Component> C setMinimumWidth ( final C component, final int minimumWidth )
    {
        Dimension ms = minimumSizeCache.get ( component );
        if ( ms == null )
        {
            ms = new Dimension ( minimumWidth, SizeMethods.UNDEFINED );
            minimumSizeCache.put ( component, ms );
        }
        else
        {
            ms.width = minimumWidth;
        }
        return component;
    }

    /**
     * Returns component minimum height.
     *
     * @param component component to process
     * @param <C>       component type
     * @return component minimum height
     */
    public static <C extends Component> int getMinimumHeight ( final C component )
    {
        final Dimension ms = minimumSizeCache.get ( component );
        return ms != null ? ms.height : SizeMethods.UNDEFINED;
    }

    /**
     * Sets component minimum height.
     * Pass {@link com.alee.utils.swing.SizeMethods#UNDEFINED} to let component choose minimum height on its own.
     *
     * @param component     component to process
     * @param minimumHeight new component minimum height
     * @param <C>           component type
     * @return modified component
     */
    public static <C extends Component> C setMinimumHeight ( final C component, final int minimumHeight )
    {
        Dimension ms = minimumSizeCache.get ( component );
        if ( ms == null )
        {
            ms = new Dimension ( SizeMethods.UNDEFINED, minimumHeight );
            minimumSizeCache.put ( component, ms );
        }
        else
        {
            ms.height = minimumHeight;
        }
        return component;
    }

    /**
     * Returns component maximum width.
     *
     * @param component component to process
     * @param <C>       component type
     * @return component maximum width
     */
    public static <C extends Component> int getMaximumWidth ( final C component )
    {
        final Dimension ms = maximumSizeCache.get ( component );
        return ms != null ? ms.width : SizeMethods.UNDEFINED;
    }

    /**
     * Sets component maximum width.
     * Pass {@link com.alee.utils.swing.SizeMethods#UNDEFINED} to let component choose maximum width on its own.
     *
     * @param component    component to process
     * @param maximumWidth new component maximum width
     * @param <C>          component type
     * @return modified component
     */
    public static <C extends Component> C setMaximumWidth ( final C component, final int maximumWidth )
    {
        Dimension ms = maximumSizeCache.get ( component );
        if ( ms == null )
        {
            ms = new Dimension ( maximumWidth, SizeMethods.UNDEFINED );
            maximumSizeCache.put ( component, ms );
        }
        else
        {
            ms.width = maximumWidth;
        }
        return component;
    }

    /**
     * Returns component maximum height.
     *
     * @param component component to process
     * @param <C>       component type
     * @return component maximum height
     */
    public static <C extends Component> int getMaximumHeight ( final C component )
    {
        final Dimension ms = maximumSizeCache.get ( component );
        return ms != null ? ms.height : SizeMethods.UNDEFINED;
    }

    /**
     * Sets component maximum height.
     * Pass {@link com.alee.utils.swing.SizeMethods#UNDEFINED} to let component choose maximum height on its own.
     *
     * @param component     component to process
     * @param maximumHeight new component maximum height
     * @param <C>           component type
     * @return modified component
     */
    public static <C extends Component> C setMaximumHeight ( final C component, final int maximumHeight )
    {
        Dimension ms = maximumSizeCache.get ( component );
        if ( ms == null )
        {
            ms = new Dimension ( SizeMethods.UNDEFINED, maximumHeight );
            maximumSizeCache.put ( component, ms );
        }
        else
        {
            ms.height = maximumHeight;
        }
        return component;
    }

    /**
     * Returns component preferred size.
     *
     * @param component           component to process
     * @param actualPreferredSize actual component preferred size
     * @param <C>                 component type
     * @return component preferred size
     */
    public static <C extends Component> Dimension getPreferredSize ( final C component, final Dimension actualPreferredSize )
    {
        final Dimension ps = preferredSizeCache.get ( component );
        final Dimension min = minimumSizeCache.get ( component );
        final Dimension max = maximumSizeCache.get ( component );
        final Dimension preferredSize = new Dimension ( actualPreferredSize );
        if ( ps != null && ps.width != SizeMethods.UNDEFINED )
        {
            preferredSize.width = ps.width;
        }
        else
        {
            if ( min != null && min.width != SizeMethods.UNDEFINED )
            {
                preferredSize.width = Math.max ( min.width, preferredSize.width );
            }
            if ( max != null && max.width != SizeMethods.UNDEFINED )
            {
                preferredSize.width = Math.min ( preferredSize.width, max.width );
            }
        }
        if ( ps != null && ps.height != SizeMethods.UNDEFINED )
        {
            preferredSize.height = ps.height;
        }
        else
        {
            if ( min != null && min.height != SizeMethods.UNDEFINED )
            {
                preferredSize.height = Math.max ( min.height, preferredSize.height );
            }
            if ( max != null && max.height != SizeMethods.UNDEFINED )
            {
                preferredSize.height = Math.min ( preferredSize.height, max.height );
            }
        }
        return preferredSize;
    }

    /**
     * Sets component preferred size.
     * This method is a simple bridge for JComponent#setPreferredSize method.
     *
     * @param component component to process
     * @param width     component preferred width
     * @param height    component preferred height
     * @return modified component
     */
    public static <C extends Component> C setPreferredSize ( final C component, final int width, final int height )
    {
        component.setPreferredSize ( new Dimension ( width, height ) );
        return component;
    }
}