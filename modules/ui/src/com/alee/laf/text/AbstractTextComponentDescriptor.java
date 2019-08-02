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

package com.alee.laf.text;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;

/**
 * Abstract descriptor for {@link JTextComponent} implementations.
 * Extend this class for creating custom {@link JTextComponent} descriptors.
 *
 * @param <C> {@link JTextComponent} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public class AbstractTextComponentDescriptor<C extends JTextComponent, U extends TextUI> extends AbstractComponentDescriptor<C, U>
{
    /**
     * Constructs new {@link AbstractTextComponentDescriptor}.
     *
     * @param id             component identifier
     * @param componentClass component class
     * @param uiClassID      component UI class ID
     * @param baseUIClass    base UI class applicable to this component
     * @param uiClass        UI class applied to the component by default
     * @param defaultStyleId component default style ID
     */
    public AbstractTextComponentDescriptor ( final String id, final Class<C> componentClass, final String uiClassID,
                                             final Class<U> baseUIClass, final Class<? extends U> uiClass, final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassID, baseUIClass, uiClass, defaultStyleId );
    }

    @Override
    public void updateUI ( final C component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Invalidating text component contents
        component.invalidate ();
    }
}