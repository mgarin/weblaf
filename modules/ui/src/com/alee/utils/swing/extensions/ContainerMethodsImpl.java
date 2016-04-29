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

import com.alee.utils.SwingUtils;

import java.awt.*;
import java.util.List;

/**
 * Common implementations for {@link com.alee.utils.swing.extensions.ContainerMethods} interface methods.
 *
 * @author Mikle Garin
 */

public final class ContainerMethodsImpl
{
    /**
     * Returns whether the specified component belongs to this container or not.
     *
     * @param container container
     * @param component component to process
     * @param <C>       provided container type
     * @return true if the specified component belongs to this container, false otherwise
     */
    public static <C extends Container> boolean contains ( final C container, final Component component )
    {
        return component != null && component.getParent () == container;
    }

    /**
     * Adds all components from the list into the container under the specified index.
     *
     * @param container  container
     * @param components components to add into container
     * @param index      index where components should be placed
     * @param <C>        provided container type
     * @param <T>        actual container type
     * @return this container
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
     * Adds all components from the list into the container under the specified constraints.
     *
     * @param container   container
     * @param components  components to add into container
     * @param constraints constraints for all components
     * @param <C>         provided container type
     * @param <T>         actual container type
     * @return this container
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
     * Adds all components from the list into the container.
     *
     * @param container  container
     * @param components components to add into container
     * @param <C>        provided container type
     * @param <T>        actual container type
     * @return this container
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
     * Adds all specified components into the container.
     * Useful for layouts like FlowLayout and some others.
     *
     * @param container  container
     * @param components components to add into container
     * @param <C>        provided container type
     * @param <T>        actual container type
     * @return this container
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
     * Removes all components from the list from the container.
     *
     * @param container  container
     * @param components components to remove from container
     * @param <C>        provided container type
     * @param <T>        actual container type
     * @return this container
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
     * Removes all specified components from the container.
     *
     * @param container  container
     * @param components components to remove from container
     * @param <C>        provided container type
     * @param <T>        actual container type
     * @return this container
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
     * Removes all children with the specified component class type.
     *
     * @param container      container
     * @param componentClass class type of child components to be removed
     * @param <C>            provided container type
     * @param <T>            actual container type
     * @return this container
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
     * Returns first component contained in this container.
     *
     * @param container container
     * @param <C>       provided container type
     * @return first component contained in this container
     */
    public static <C extends Container> Component getFirstComponent ( final C container )
    {
        return container.getComponentCount () > 0 ? container.getComponent ( 0 ) : null;
    }

    /**
     * Returns last component contained in this container.
     *
     * @param container container
     * @param <C>       provided container type
     * @return last component contained in this container
     */
    public static <C extends Container> Component getLastComponent ( final C container )
    {
        return container.getComponentCount () > 0 ? container.getComponent ( container.getComponentCount () - 1 ) : null;
    }

    /**
     * Makes all container child component widths equal.
     *
     * @param container container
     * @param <C>       provided container type
     * @param <T>       actual container type
     * @return this container
     */
    public static <C extends Container, T extends C> T equalizeComponentsWidth ( final C container )
    {
        SwingUtils.equalizeComponentsWidth ( container.getComponents () );
        return ( T ) container;
    }

    /**
     * Makes all container child component heights equal.
     *
     * @param container container
     * @param <C>       provided container type
     * @param <T>       actual container type
     * @return this container
     */
    public static <C extends Container, T extends C> T equalizeComponentsHeight ( final C container )
    {
        SwingUtils.equalizeComponentsHeight ( container.getComponents () );
        return ( T ) container;
    }

    /**
     * Makes all container child component sizes equal.
     *
     * @param container container
     * @param <C>       provided container type
     * @param <T>       actual container type
     * @return this container
     */
    public static <C extends Container, T extends C> T equalizeComponentsSize ( final C container )
    {
        SwingUtils.equalizeComponentsSize ( container.getComponents () );
        return ( T ) container;
    }
}