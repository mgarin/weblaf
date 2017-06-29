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
import com.alee.managers.style.ComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.plaf.ComponentUI;
import javax.swing.text.JTextComponent;

/**
 * {@link ComponentDescriptor} implementation that provides UI update operation common for all {@link JTextComponent}s.
 *
 * @param <C> {@link JTextComponent} type
 * @author Mikle Garin
 */

public class AbstractTextComponentDescriptor<C extends JTextComponent> extends AbstractComponentDescriptor<C>
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
                                             final Class<? extends ComponentUI> baseUIClass,
                                             final Class<? extends ComponentUI> uiClass,
                                             final StyleId defaultStyleId )
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