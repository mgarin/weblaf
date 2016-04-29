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

import java.awt.*;
import java.util.List;

/**
 * This interface provides a set of methods that should be added into containers.
 *
 * @author Mikle Garin
 * @see MethodExtension
 * @see com.alee.utils.swing.extensions.ContainerMethodsImpl
 */

public interface ContainerMethods<C extends Container> extends MethodExtension
{
    /**
     * Returns whether the specified component belongs to this container or not.
     *
     * @param component component to process
     * @return true if the specified component belongs to this container, false otherwise
     */
    public boolean contains ( Component component );

    /**
     * Adds all components from the list into the container.
     *
     * @param components components to add into container
     * @return this container
     */
    public C add ( List<? extends Component> components );

    /**
     * Adds all components from the list into the container under the specified index.
     *
     * @param components components to add into container
     * @param index      index where components should be placed
     * @return this container
     */
    public C add ( List<? extends Component> components, int index );

    /**
     * Adds all components from the list into the container under the specified constraints.
     *
     * @param components  components to add into container
     * @param constraints constraints for all components
     * @return this container
     */
    public C add ( List<? extends Component> components, Object constraints );

    /**
     * Adds all specified components into the panel.
     * Useful for layouts like FlowLayout and some others.
     * This method is a fix for {@link #add(java.awt.Component...)} method usage in case of two components.
     *
     * @param component1 component to add into panel
     * @param component2 component to add into panel
     * @return this panel
     */
    public C add ( Component component1, Component component2 );

    /**
     * Adds all specified components into the container.
     * Useful for layouts like FlowLayout and some others.
     *
     * @param components components to add into container
     * @return this container
     */
    public C add ( Component... components );

    /**
     * Removes all components from the list from the container.
     *
     * @param components components to remove from container
     * @return this container
     */
    public C remove ( List<? extends Component> components );

    /**
     * Removes all specified components from the container.
     *
     * @param components components to remove from container
     * @return this container
     */
    public C remove ( Component... components );

    /**
     * Removes all children with the specified component class type.
     *
     * @param componentClass class type of child components to be removed
     * @return this container
     */
    public C removeAll ( Class<? extends Component> componentClass );

    /**
     * Returns first component contained in this container.
     *
     * @return first component contained in this container
     */
    public Component getFirstComponent ();

    /**
     * Returns last component contained in this container.
     *
     * @return last component contained in this container
     */
    public Component getLastComponent ();

    /**
     * Makes all container child component widths equal.
     *
     * @return this container
     */
    public C equalizeComponentsWidth ();

    /**
     * Makes all container child component heights equal.
     *
     * @return this container
     */
    public C equalizeComponentsHeight ();

    /**
     * Makes all container child component sizes equal.
     *
     * @return this container
     */
    public C equalizeComponentsSize ();
}