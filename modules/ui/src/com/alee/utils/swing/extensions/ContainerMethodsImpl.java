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

import com.alee.api.jdk.BiConsumer;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.util.List;

/**
 * Common implementations for {@link com.alee.utils.swing.extensions.ContainerMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.ContainerMethods
 */
public final class ContainerMethodsImpl
{
    /**
     * Returns whether the specified {@link Component} belongs to this {@link Container} or not.
     *
     * @param container {@link Container}
     * @param component {@link Component} to process
     * @param <C>       {@link Container} type
     * @return {@code true} if the specified {@link Component} belongs to this {@link Container}, {@code false} otherwise
     */
    public static <C extends Container> boolean contains ( final C container, final Component component )
    {
        return component != null && component.getParent () == container;
    }

    /**
     * Adds all {@link Component}s from the list into the {@link Container} under the specified index.
     *
     * @param container  {@link Container}
     * @param components {@link Component}s to add into this {@link Container}
     * @param index      index where {@link Component}s should be placed
     * @param <C>        provided {@link Container} type
     * @param <T>        actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T add ( final C container, final List<? extends Component> components,
                                                             final int index )
    {
        if ( components != null )
        {
            int skipped = 0;
            for ( int i = 0; i < components.size (); i++ )
            {
                final Component component = components.get ( i );
                if ( component != null )
                {
                    container.add ( component, index + i - skipped );
                }
                else
                {
                    skipped++;
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Adds all {@link Component}s from the list into the {@link Container} under the specified constraints.
     *
     * @param container   {@link Container}
     * @param components  components to add into this {@link Container}
     * @param constraints constraints for all {@link Component}s
     * @param <C>         provided {@link Container} type
     * @param <T>         actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T add ( final C container, final List<? extends Component> components,
                                                             final Object constraints )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    container.add ( component, constraints );
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Adds all {@link Component}s from the list into the {@link Container}.
     *
     * @param container  {@link Container}
     * @param components {@link Component}s to add into this {@link Container}
     * @param <C>        provided {@link Container} type
     * @param <T>        actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T add ( final C container, final List<? extends Component> components )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    container.add ( component );
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Adds all specified {@link Component}s into this {@link Container}.
     *
     * @param container  {@link Container}
     * @param components {@link Component}s to add into this {@link Container}
     * @param <C>        provided {@link Container} type
     * @param <T>        actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T add ( final C container, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    container.add ( component );
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Removes all {@link Component}s from the list from this {@link Container}.
     *
     * @param container  {@link Container}
     * @param components {@link Component}s to remove from {@link Container}
     * @param <C>        provided {@link Container} type
     * @param <T>        actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T remove ( final C container, final List<? extends Component> components )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    container.remove ( component );
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Removes all specified {@link Component}s from this {@link Container}.
     *
     * @param container  {@link Container}
     * @param components {@link Component}s to remove from {@link Container}
     * @param <C>        provided {@link Container} type
     * @param <T>        actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T remove ( final C container, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    container.remove ( component );
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Removes all children with the specified {@link Component} {@link Class} type from this {@link Container}.
     *
     * @param container      {@link Container}
     * @param componentClass {@link Class} type of child {@link Component}s to be removed
     * @param <C>            provided {@link Container} type
     * @param <T>            actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T removeAll ( final C container, final Class<? extends Component> componentClass )
    {
        if ( componentClass != null )
        {
            for ( int i = 0; i < container.getComponentCount (); i++ )
            {
                final Component component = container.getComponent ( i );
                if ( componentClass.isAssignableFrom ( component.getClass () ) )
                {
                    container.remove ( component );
                }
            }
        }
        return ( T ) container;
    }

    /**
     * Returns first {@link Component} contained in this {@link Container}.
     *
     * @param container {@link Container}
     * @param <C>       {@link Container} type
     * @return first {@link Component} contained in this {@link Container}
     */
    public static <C extends Container> Component getFirstComponent ( final C container )
    {
        return container.getComponentCount () > 0 ? container.getComponent ( 0 ) : null;
    }

    /**
     * Returns last {@link Component} contained in this {@link Container}.
     *
     * @param container {@link Container}
     * @param <C>       {@link Container} type
     * @return last {@link Component} contained in this {@link Container}
     */
    public static <C extends Container> Component getLastComponent ( final C container )
    {
        return container.getComponentCount () > 0 ? container.getComponent ( container.getComponentCount () - 1 ) : null;
    }

    /**
     * Makes all {@link Container} child {@link Component} widths equal.
     *
     * @param container {@link Container}
     * @param <C>       provided {@link Container} type
     * @param <T>       actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T equalizeComponentsWidth ( final C container )
    {
        SwingUtils.equalizeComponentsWidth ( container.getComponents () );
        return ( T ) container;
    }

    /**
     * Makes all {@link Container} child {@link Component} heights equal.
     *
     * @param container {@link Container}
     * @param <C>       provided {@link Container} type
     * @param <T>       actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T equalizeComponentsHeight ( final C container )
    {
        SwingUtils.equalizeComponentsHeight ( container.getComponents () );
        return ( T ) container;
    }

    /**
     * Makes all {@link Container} child {@link Component} sizes equal.
     *
     * @param container {@link Container}
     * @param <C>       provided {@link Container} type
     * @param <T>       actual {@link Container} type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C> T equalizeComponentsSize ( final C container )
    {
        SwingUtils.equalizeComponentsSize ( container.getComponents () );
        return ( T ) container;
    }

    /**
     * Provides all child {@link Component}s into the specified {@link BiConsumer}.
     *
     * @param container {@link Container}
     * @param consumer  child {@link Component}s {@link BiConsumer}
     * @param <C>       provided {@link Container} type
     * @param <T>       actual {@link Container} type
     * @param <V>       child {@link Component}s type
     * @return this {@link Container}
     */
    public static <C extends Container, T extends C, V extends Component> T forEach ( final C container,
                                                                                      final BiConsumer<Integer, V> consumer )
    {
        final int count = container.getComponentCount ();
        for ( int index = 0; index < count; index++ )
        {
            consumer.accept ( index, ( V ) container.getComponent ( index ) );
        }
        return ( T ) container;
    }
}