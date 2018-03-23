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

package com.alee.utils.swing.extensions;

import com.alee.api.jdk.BiFunction;
import com.alee.api.jdk.Function;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import java.awt.*;

/**
 * Common implementations for {@link SizeMethods} interface methods.
 *
 * @author Mikle Garin
 * @see SizeMethods
 */

public final class SizeMethodsImpl
{
    /**
     * {@link JComponent} minimum sizes.
     */
    private static final WeakComponentData<JComponent, Dimension> minimumSizeCache =
            new WeakComponentData<JComponent, Dimension> ( "SizeMethodsImpl.minimumSize", 5 );

    /**
     * {@link JComponent} maximum sizes.
     */
    private static final WeakComponentData<JComponent, Dimension> maximumSizeCache =
            new WeakComponentData<JComponent, Dimension> ( "SizeMethodsImpl.maximumSize", 5 );

    /**
     * {@link JComponent} preferred sizes.
     */
    private static final WeakComponentData<JComponent, Dimension> preferredSizeCache =
            new WeakComponentData<JComponent, Dimension> ( "SizeMethodsImpl.preferredSize", 50 );

    /**
     * Returns {@link JComponent} preferred width.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} preferred width
     */
    public static int getPreferredWidth ( final JComponent component )
    {
        final Dimension ps = preferredSizeCache.get ( component );
        return ps != null ? ps.width : SizeMethods.UNDEFINED;
    }

    /**
     * Sets {@link JComponent} preferred width.
     * Pass {@link SizeMethods#UNDEFINED} to let {@link JComponent} choose preferred width on its own.
     *
     * @param component      {@link JComponent} to process
     * @param preferredWidth new {@link JComponent} preferred width
     * @param <C>            {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setPreferredWidth ( final JComponent component, final int preferredWidth )
    {
        preferredSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component, final Dimension dimension )
            {
                return new Dimension ( preferredWidth, dimension.height );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component )
            {
                return new Dimension ( preferredWidth, SizeMethods.UNDEFINED );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} preferred height.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} preferred height
     */
    public static int getPreferredHeight ( final JComponent component )
    {
        final Dimension ps = preferredSizeCache.get ( component );
        return ps != null ? ps.height : SizeMethods.UNDEFINED;
    }

    /**
     * Sets {@link JComponent} preferred height.
     * Pass {@link SizeMethods#UNDEFINED} to let {@link JComponent} choose preferred height on its own.
     *
     * @param component       {@link JComponent} to process
     * @param preferredHeight new {@link JComponent} preferred height
     * @param <C>             {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setPreferredHeight ( final JComponent component, final int preferredHeight )
    {
        preferredSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component, final Dimension dimension )
            {
                return new Dimension ( dimension.width, preferredHeight );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component )
            {
                return new Dimension ( SizeMethods.UNDEFINED, preferredHeight );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} minimum width.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} minimum width
     */
    public static int getMinimumWidth ( final JComponent component )
    {
        final Dimension ms = minimumSizeCache.get ( component );
        return ms != null ? ms.width : SizeMethods.UNDEFINED;
    }

    /**
     * Sets {@link JComponent} minimum width.
     * Pass {@link SizeMethods#UNDEFINED} to let {@link JComponent} choose minimum width on its own.
     *
     * @param component    {@link JComponent} to process
     * @param minimumWidth new {@link JComponent} minimum width
     * @param <C>          {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setMinimumWidth ( final JComponent component, final int minimumWidth )
    {
        minimumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component, final Dimension dimension )
            {
                return new Dimension ( minimumWidth, dimension.height );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component )
            {
                return new Dimension ( minimumWidth, SizeMethods.UNDEFINED );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} minimum height.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} minimum height
     */
    public static int getMinimumHeight ( final JComponent component )
    {
        final Dimension ms = minimumSizeCache.get ( component );
        return ms != null ? ms.height : SizeMethods.UNDEFINED;
    }

    /**
     * Sets {@link JComponent} minimum height.
     * Pass {@link SizeMethods#UNDEFINED} to let {@link JComponent} choose minimum height on its own.
     *
     * @param component     {@link JComponent} to process
     * @param minimumHeight new {@link JComponent} minimum height
     * @param <C>           {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setMinimumHeight ( final JComponent component, final int minimumHeight )
    {
        minimumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component, final Dimension dimension )
            {
                return new Dimension ( dimension.width, minimumHeight );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component )
            {
                return new Dimension ( SizeMethods.UNDEFINED, minimumHeight );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} maximum width.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} maximum width
     */
    public static int getMaximumWidth ( final JComponent component )
    {
        final Dimension ms = maximumSizeCache.get ( component );
        return ms != null ? ms.width : SizeMethods.UNDEFINED;
    }

    /**
     * Sets {@link JComponent} maximum width.
     * Pass {@link SizeMethods#UNDEFINED} to let {@link JComponent} choose maximum width on its own.
     *
     * @param component    {@link JComponent} to process
     * @param maximumWidth new {@link JComponent} maximum width
     * @param <C>          {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setMaximumWidth ( final JComponent component, final int maximumWidth )
    {
        maximumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component, final Dimension dimension )
            {
                return new Dimension ( maximumWidth, dimension.height );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component )
            {
                return new Dimension ( maximumWidth, SizeMethods.UNDEFINED );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} maximum height.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} maximum height
     */
    public static int getMaximumHeight ( final JComponent component )
    {
        final Dimension ms = maximumSizeCache.get ( component );
        return ms != null ? ms.height : SizeMethods.UNDEFINED;
    }

    /**
     * Sets {@link JComponent} maximum height.
     * Pass {@link SizeMethods#UNDEFINED} to let {@link JComponent} choose maximum height on its own.
     *
     * @param component     {@link JComponent} to process
     * @param maximumHeight new {@link JComponent} maximum height
     * @param <C>           {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setMaximumHeight ( final JComponent component, final int maximumHeight )
    {
        maximumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component, final Dimension dimension )
            {
                return new Dimension ( dimension.width, maximumHeight );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( final JComponent component )
            {
                return new Dimension ( SizeMethods.UNDEFINED, maximumHeight );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} preferred size.
     * This size is already adjusted according to min/max width and height settings.
     *
     * @param component             {@link JComponent} to process
     * @param originalPreferredSize original {@link JComponent} preferred size
     * @return {@link JComponent} preferred size
     */
    public static Dimension getPreferredSize ( final JComponent component, final Dimension originalPreferredSize )
    {
        final Dimension ps = preferredSizeCache.get ( component );
        final Dimension min = minimumSizeCache.get ( component );
        final Dimension max = maximumSizeCache.get ( component );
        final Dimension preferredSize = new Dimension ( originalPreferredSize );
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
     * Returns original {@link JComponent} preferred size before any adjustments.
     * This might be useful for various calculations involving {@link JComponent} size.
     *
     * @param component             {@link JComponent} to process
     * @param originalPreferredSize original {@link JComponent} preferred size
     * @return original {@link JComponent} preferred size before any adjustments
     */
    public static Dimension getOriginalPreferredSize ( final JComponent component, final Dimension originalPreferredSize )
    {
        return originalPreferredSize;
    }

    /**
     * Sets {@link JComponent} preferred size.
     * This method is a simple bridge for JComponent#setPreferredSize method.
     *
     * @param component {@link JComponent} to process
     * @param width     {@link JComponent} preferred width
     * @param height    {@link JComponent} preferred height
     * @param <C>       {@link JComponent} type
     * @return modified {@link JComponent}
     */
    public static <C extends JComponent> C setPreferredSize ( final JComponent component, final int width, final int height )
    {
        component.setPreferredSize ( new Dimension ( width, height ) );
        return ( C ) component;
    }
}