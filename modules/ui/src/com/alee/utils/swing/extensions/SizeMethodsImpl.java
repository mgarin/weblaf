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

import com.alee.api.annotations.NotNull;
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
     * Returns {@link JComponent} preferred width set in {@link #setPreferredWidth(JComponent, int)} or {@link SizeMethods#UNDEFINED}.
     * To retrieve actual {@link JComponent} preferred width use {@link #getPreferredSize(JComponent, Dimension)} method instead.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} preferred width set in {@link #setPreferredWidth(JComponent, int)} or {@link SizeMethods#UNDEFINED}
     */
    public static int getPreferredWidth ( @NotNull final JComponent component )
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
    @NotNull
    public static <C extends JComponent> C setPreferredWidth ( @NotNull final JComponent component, final int preferredWidth )
    {
        preferredSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component, @NotNull final Dimension dimension )
            {
                return new Dimension ( preferredWidth, dimension.height );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component )
            {
                return new Dimension ( preferredWidth, SizeMethods.UNDEFINED );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} preferred height set in {@link #setPreferredHeight(JComponent, int)} or {@link SizeMethods#UNDEFINED}.
     * To retrieve actual {@link JComponent} preferred height use {@link #getPreferredSize(JComponent, Dimension)} method instead.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} preferred height set in {@link #setPreferredHeight(JComponent, int)} or {@link SizeMethods#UNDEFINED}
     */
    public static int getPreferredHeight ( @NotNull final JComponent component )
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
    @NotNull
    public static <C extends JComponent> C setPreferredHeight ( @NotNull final JComponent component, final int preferredHeight )
    {
        preferredSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component, @NotNull final Dimension dimension )
            {
                return new Dimension ( dimension.width, preferredHeight );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component )
            {
                return new Dimension ( SizeMethods.UNDEFINED, preferredHeight );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} preferred size.
     * This size is already adjusted according to min/max width and height settings.
     * Use {@link #getOriginalPreferredSize(JComponent, Dimension)} method to retrieve original {@link JComponent} preferred size.
     *
     * @param component             {@link JComponent} to process
     * @param originalPreferredSize original {@link JComponent} preferred size
     * @return {@link JComponent} preferred size
     */
    @NotNull
    public static Dimension getPreferredSize ( @NotNull final JComponent component, @NotNull final Dimension originalPreferredSize )
    {
        final Dimension preferredSize = new Dimension ( originalPreferredSize );
        final Dimension ps = preferredSizeCache.get ( component );
        final Dimension min = minimumSizeCache.get ( component );
        final Dimension max = maximumSizeCache.get ( component );
        if ( ps != null && ps.width != SizeMethods.UNDEFINED )
        {
            preferredSize.width = ps.width;
        }
        else
        {
            // todo Should it actually account for known min/max values?
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
            // todo Should it actually account for known min/max values?
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
     * In most cases this is the size provided by UI implementation of the {@link JComponent}.
     *
     * @param component             {@link JComponent} to process
     * @param originalPreferredSize original {@link JComponent} preferred size
     * @return original {@link JComponent} preferred size before any adjustments
     */
    @NotNull
    public static Dimension getOriginalPreferredSize ( @NotNull final JComponent component, @NotNull final Dimension originalPreferredSize )
    {
        return originalPreferredSize;
    }

    /**
     * Sets {@link JComponent} preferred size.
     * This method is a simple bridge for {@link JComponent#setPreferredSize(Dimension)} method.
     *
     * @param component {@link JComponent} to process
     * @param width     {@link JComponent} preferred width
     * @param height    {@link JComponent} preferred height
     * @param <C>       {@link JComponent} type
     * @return modified {@link JComponent}
     */
    @NotNull
    public static <C extends JComponent> C setPreferredSize ( @NotNull final JComponent component, final int width, final int height )
    {
        component.setPreferredSize ( new Dimension ( width, height ) );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} maximum width set in {@link #setMaximumWidth(JComponent, int)} or {@link SizeMethods#UNDEFINED}.
     * To retrieve actual {@link JComponent} maximum width use {@link #getMaximumSize(JComponent, Dimension)} method instead.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} maximum width set in {@link #setMaximumWidth(JComponent, int)} or {@link SizeMethods#UNDEFINED}
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
    @NotNull
    public static <C extends JComponent> C setMaximumWidth ( @NotNull final JComponent component, final int maximumWidth )
    {
        maximumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component, @NotNull final Dimension dimension )
            {
                return new Dimension ( maximumWidth, dimension.height );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component )
            {
                return new Dimension ( maximumWidth, SizeMethods.UNDEFINED );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} maximum height set in {@link #setMaximumHeight(JComponent, int)} or {@link SizeMethods#UNDEFINED}.
     * To retrieve actual {@link JComponent} maximum height use {@link #getMaximumSize(JComponent, Dimension)} method instead.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} maximum height set in {@link #setMaximumHeight(JComponent, int)} or {@link SizeMethods#UNDEFINED}
     */
    public static int getMaximumHeight ( @NotNull final JComponent component )
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
    @NotNull
    public static <C extends JComponent> C setMaximumHeight ( @NotNull final JComponent component, final int maximumHeight )
    {
        maximumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component, @NotNull final Dimension dimension )
            {
                return new Dimension ( dimension.width, maximumHeight );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component )
            {
                return new Dimension ( SizeMethods.UNDEFINED, maximumHeight );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} maximum size.
     * This size is already adjusted according to min/max width and height settings.
     * Use {@link #getOriginalMaximumSize(JComponent, Dimension)} method to retrieve original {@link JComponent} maximum size.
     *
     * @param component           {@link JComponent} to process
     * @param originalMaximumSize original {@link JComponent} maximum size
     * @return {@link JComponent} maximum size
     */
    @NotNull
    public static Dimension getMaximumSize ( @NotNull final JComponent component, @NotNull final Dimension originalMaximumSize )
    {
        final Dimension maximumSize = new Dimension ( originalMaximumSize );
        final Dimension max = maximumSizeCache.get ( component );
        if ( max != null && max.width != SizeMethods.UNDEFINED )
        {
            maximumSize.width = max.width;
        }
        if ( max != null && max.height != SizeMethods.UNDEFINED )
        {
            maximumSize.height = max.height;
        }
        return maximumSize;
    }

    /**
     * Returns original {@link JComponent} maximum size before any adjustments.
     * This might be useful for various calculations involving {@link JComponent} size.
     * In most cases this is the size provided by UI implementation of the {@link JComponent}.
     *
     * @param component           {@link JComponent} to process
     * @param originalMaximumSize original {@link JComponent} maximum size
     * @return original {@link JComponent} maximum size before any adjustments
     */
    @NotNull
    public static Dimension getOriginalMaximumSize ( @NotNull final JComponent component, @NotNull final Dimension originalMaximumSize )
    {
        return originalMaximumSize;
    }

    /**
     * Sets {@link JComponent} maximum size.
     * This method is a simple bridge for {@link JComponent#setMaximumSize(Dimension)} method.
     *
     * @param component {@link JComponent} to process
     * @param width     {@link JComponent} maximum width
     * @param height    {@link JComponent} maximum height
     * @param <C>       {@link JComponent} type
     * @return modified {@link JComponent}
     */
    @NotNull
    public static <C extends JComponent> C setMaximumSize ( @NotNull final JComponent component, final int width, final int height )
    {
        component.setMaximumSize ( new Dimension ( width, height ) );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} minimum width set in {@link #setMinimumWidth(JComponent, int)} or {@link SizeMethods#UNDEFINED}.
     * To retrieve actual {@link JComponent} minimum width use {@link #getMinimumSize(JComponent, Dimension)} method instead.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} minimum width set in {@link #setMinimumWidth(JComponent, int)} or {@link SizeMethods#UNDEFINED}
     */
    public static int getMinimumWidth ( @NotNull final JComponent component )
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
    @NotNull
    public static <C extends JComponent> C setMinimumWidth ( @NotNull final JComponent component, final int minimumWidth )
    {
        minimumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component, @NotNull final Dimension dimension )
            {
                return new Dimension ( minimumWidth, dimension.height );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component )
            {
                return new Dimension ( minimumWidth, SizeMethods.UNDEFINED );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} minimum height set in {@link #setMinimumHeight(JComponent, int)} or {@link SizeMethods#UNDEFINED}.
     * To retrieve actual {@link JComponent} minimum height use {@link #getMinimumSize(JComponent, Dimension)} method instead.
     *
     * @param component {@link JComponent} to process
     * @return {@link JComponent} minimum height set in {@link #setMinimumHeight(JComponent, int)} or {@link SizeMethods#UNDEFINED}
     */
    public static int getMinimumHeight ( @NotNull final JComponent component )
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
    @NotNull
    public static <C extends JComponent> C setMinimumHeight ( @NotNull final JComponent component, final int minimumHeight )
    {
        minimumSizeCache.modify ( component, new BiFunction<JComponent, Dimension, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component, @NotNull final Dimension dimension )
            {
                return new Dimension ( dimension.width, minimumHeight );
            }
        }, new Function<JComponent, Dimension> ()
        {
            @Override
            public Dimension apply ( @NotNull final JComponent component )
            {
                return new Dimension ( SizeMethods.UNDEFINED, minimumHeight );
            }
        } );
        return ( C ) component;
    }

    /**
     * Returns {@link JComponent} minimum size.
     * This size is already adjusted according to min/max width and height settings.
     * Use {@link #getOriginalMinimumSize(JComponent, Dimension)} method to retrieve original {@link JComponent} minimum size.
     *
     * @param component           {@link JComponent} to process
     * @param originalMinimumSize original {@link JComponent} minimum size
     * @return {@link JComponent} minimum size
     */
    @NotNull
    public static Dimension getMinimumSize ( @NotNull final JComponent component, @NotNull final Dimension originalMinimumSize )
    {
        final Dimension minimumSize = new Dimension ( originalMinimumSize );
        final Dimension min = minimumSizeCache.get ( component );
        if ( min != null && min.width != SizeMethods.UNDEFINED )
        {
            minimumSize.width = min.width;
        }
        if ( min != null && min.height != SizeMethods.UNDEFINED )
        {
            minimumSize.height = min.height;
        }
        return minimumSize;
    }

    /**
     * Returns original {@link JComponent} minimum size before any adjustments.
     * This might be useful for various calculations involving {@link JComponent} size.
     * In most cases this is the size provided by UI implementation of the {@link JComponent}.
     *
     * @param component           {@link JComponent} to process
     * @param originalMinimumSize original {@link JComponent} minimum size
     * @return original {@link JComponent} minimum size before any adjustments
     */
    @NotNull
    public static Dimension getOriginalMinimumSize ( @NotNull final JComponent component, @NotNull final Dimension originalMinimumSize )
    {
        return originalMinimumSize;
    }

    /**
     * Sets {@link JComponent} minimum size.
     * This method is a simple bridge for {@link JComponent#setMinimumSize(Dimension)} method.
     *
     * @param component {@link JComponent} to process
     * @param width     {@link JComponent} minimum width
     * @param height    {@link JComponent} minimum height
     * @param <C>       {@link JComponent} type
     * @return modified {@link JComponent}
     */
    @NotNull
    public static <C extends JComponent> C setMinimumSize ( @NotNull final JComponent component, final int width, final int height )
    {
        component.setMinimumSize ( new Dimension ( width, height ) );
        return ( C ) component;
    }
}