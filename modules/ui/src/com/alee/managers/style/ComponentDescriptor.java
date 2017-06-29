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

import com.alee.api.IconSupport;
import com.alee.api.Identifiable;
import com.alee.api.TitleSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Interface for various information and behavior providers for {@link JComponent} implementations.
 *
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */

public interface ComponentDescriptor<C extends JComponent> extends Identifiable, IconSupport, TitleSupport
{
    /**
     * Returns component class.
     *
     * @return component class
     */
    public Class<C> getComponentClass ();

    /**
     * Returns UI class ID used by LookAndFeel to store various settings.
     *
     * @return UI class ID
     */
    public String getUIClassId ();

    /**
     * Returns base UI class applicable to this component.
     * This class is required to limit and properly check UIs that can be applied to the component.
     *
     * @return base UI class applicable to this component
     */
    public Class<? extends ComponentUI> getBaseUIClass ();

    /**
     * Returns UI class applied to the component by default.
     * This value is used in {@link com.alee.laf.WebLookAndFeel} to provide default UI classes.
     *
     * @return UI class applied to the component by default
     */
    public Class<? extends ComponentUI> getUIClass ();

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
    @Override
    public String getId ();

    /**
     * Returns component icon.
     *
     * @return component icon
     */
    @Override
    public Icon getIcon ();

    /**
     * Returns component name.
     *
     * @return component name
     */
    @Override
    public String getTitle ();

    /**
     * Asks descriptor to reset component UI to one currently provided by L&F.
     * Some of the components might customize that behavior to update used sub-component UIs.
     *
     * @param component component instance
     */
    public void updateUI ( C component );
}