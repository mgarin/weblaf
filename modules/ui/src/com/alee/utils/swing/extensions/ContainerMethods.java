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

import java.awt.*;
import java.util.List;

/**
 * This interface provides a set of methods that should be added into {@link Container}s.
 *
 * @param <C> {@link Container} type
 * @author Mikle Garin
 * @see MethodExtension
 * @see com.alee.utils.swing.extensions.ContainerMethodsImpl
 */
public interface ContainerMethods<C extends Container> extends MethodExtension
{
    /**
     * Returns whether the specified {@link Component} belongs to this {@link Container} or not.
     *
     * @param component {@link Component} to process
     * @return {@code true} if the specified {@link Component} belongs to this {@link Container}, {@code false} otherwise
     */
    public boolean contains ( Component component );

    /**
     * Adds all {@link Component}s from the list into this {@link Container}.
     *
     * @param components {@link Component}s to add into this {@link Container}
     * @return this {@link Container}
     */
    public C add ( List<? extends Component> components );

    /**
     * Adds all {@link Component}s from the list into this {@link Container} under the specified index.
     *
     * @param components {@link Component}s to add into this {@link Container}
     * @param index      index where {@link Component}s should be placed
     * @return this {@link Container}
     */
    public C add ( List<? extends Component> components, int index );

    /**
     * Adds all {@link Component}s from the list into this {@link Container} under the specified constraints.
     *
     * @param components  {@link Component}s to add into this {@link Container}
     * @param constraints constraints for all {@link Component}s
     * @return this {@link Container}
     */
    public C add ( List<? extends Component> components, Object constraints );

    /**
     * Adds all specified {@link Component}s into this {@link Container}.
     * This method is a fix for {@link #add(java.awt.Component...)} method usage in case of two {@link Component}s.
     *
     * @param component1 {@link Component} to add into this {@link Container}
     * @param component2 {@link Component} to add into this {@link Container}
     * @return this {@link Container}
     */
    public C add ( Component component1, Component component2 );

    /**
     * Adds all specified {@link Component}s into this {@link Container}.
     *
     * @param components {@link Component}s to add into this {@link Container}
     * @return this {@link Container}
     */
    public C add ( Component... components );

    /**
     * Removes all {@link Component}s from the list from this {@link Container}.
     *
     * @param components components to remove from {@link Container}
     * @return this {@link Container}
     */
    public C remove ( List<? extends Component> components );

    /**
     * Removes all specified {@link Component}s from this {@link Container}.
     *
     * @param components components to remove from {@link Container}
     * @return this {@link Container}
     */
    public C remove ( Component... components );

    /**
     * Removes all children with the specified {@link Component} {@link Class} type from this {@link Container}.
     *
     * @param componentClass {@link Class} type of child {@link Component}s to be removed
     * @return this {@link Container}
     */
    public C removeAll ( Class<? extends Component> componentClass );

    /**
     * Returns first {@link Component} contained in this {@link Container}.
     *
     * @return first {@link Component} contained in this {@link Container}
     */
    public Component getFirstComponent ();

    /**
     * Returns last {@link Component} contained in this {@link Container}.
     *
     * @return last {@link Component} contained in this {@link Container}
     */
    public Component getLastComponent ();

    /**
     * Makes all {@link Container} child {@link Component} widths equal.
     *
     * @return this {@link Container}
     */
    public C equalizeComponentsWidth ();

    /**
     * Makes all {@link Container} child {@link Component} heights equal.
     *
     * @return this {@link Container}
     */
    public C equalizeComponentsHeight ();

    /**
     * Makes all {@link Container} child {@link Component} sizes equal.
     *
     * @return this {@link Container}
     */
    public C equalizeComponentsSize ();

    /**
     * Provides all child {@link Component}s into the specified {@link BiConsumer}.
     *
     * @param consumer child {@link Component}s {@link BiConsumer}
     * @param <T>      child {@link Component}s type
     * @return this {@link Container}
     */
    public <T extends Component> C forEach ( BiConsumer<Integer, T> consumer );
}