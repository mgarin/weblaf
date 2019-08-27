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

package com.alee.managers.style;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Interface for various information and behavior providers for {@link JComponent} implementations.
 *
 * @param <C> {@link JComponent} type
 * @param <U> base {@link ComponentUI} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see StyleManager#registerComponentDescriptor(ComponentDescriptor)
 * @see StyleManager#unregisterComponentDescriptor(ComponentDescriptor)
 * @see AbstractComponentDescriptor
 */
public interface ComponentDescriptor<C extends JComponent, U extends ComponentUI> extends Identifiable
{
    /**
     * Returns {@link JComponent} class.
     *
     * @return {@link JComponent} class
     */
    public Class<C> getComponentClass ();

    /**
     * Returns UI class ID used by LookAndFeel to store various settings.
     *
     * @return UI class ID
     */
    @NotNull
    public String getUIClassId ();

    /**
     * Returns base UI class applicable to this {@link JComponent}.
     * This class is required to limit and properly check UIs that can be applied to specific {@link JComponent} implementations.
     *
     * @return base UI class applicable to this component
     */
    public Class<U> getBaseUIClass ();

    /**
     * Returns UI class applied to the component by default.
     * This value is used in {@link com.alee.laf.WebLookAndFeel} to provide default UI classes.
     *
     * @return UI class applied to the component by default
     */
    public Class<? extends U> getUIClass ();

    /**
     * Returns component default style ID.
     *
     * @return component default style ID
     */
    public StyleId getDefaultStyleId ();

    /**
     * Returns component instance-specific default style ID.
     *
     * @param component component instance to retrieve default style ID for
     * @return component instance-specific default style ID
     */
    public StyleId getDefaultStyleId ( JComponent component );

    /**
     * Returns component identifier.
     * It is used as component style type and in most cases as default style identifier.
     *
     * @return component identifier
     */
    @Nullable
    @Override
    public String getId ();

    /**
     * Returns component {@link Icon}.
     *
     * @return component {@link Icon}
     */
    public Icon getIcon ();

    /**
     * Returns component name.
     *
     * @return component name
     */
    public String getTitle ();

    /**
     * Performs {@link UIDefaults} table updates.
     * Normally you should put {@link ComponentUI} class mapping here, but other settings can also be updated if needed.
     *
     * @param table {@link UIDefaults}
     */
    public void updateDefaults ( UIDefaults table );

    /**
     * Asks descriptor to update {@link ComponentUI} instance for the specified {@link JComponent}.
     * It is up to implementation whether {@link ComponentUI} instance will be preserved or changed to the one currently provided by LaF.
     * Various {@link ComponentDescriptor} implementations might customize this behavior to update used sub-component UIs.
     *
     * @param component component instance
     */
    public void updateUI ( C component );
}