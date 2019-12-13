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

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Abstract descriptor for {@link JPasswordField} component.
 * Extend this class for creating custom {@link JPasswordField} descriptors.
 *
 * @param <C> {@link JPasswordField} type
 * @param <U> base {@link WPasswordFieldUI} type
 * @param <P> {@link IPasswordFieldPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractPasswordFieldDescriptor<C extends JPasswordField, U extends WPasswordFieldUI, P extends IPasswordFieldPainter>
        extends AbstractTextComponentDescriptor<C, U, P>
{
    /**
     * todo 1. Retrieve echo char from style variables or component settings?
     */

    /**
     * Constructs new {@link AbstractPasswordFieldDescriptor}.
     *
     * @param id                  {@link JPasswordField} identifier
     * @param componentClass      {@link JPasswordField} {@link Class}
     * @param uiClassId           {@link WPasswordFieldUI} {@link Class} identifier
     * @param baseUIClass         base {@link WPasswordFieldUI} {@link Class} applicable to {@link JPasswordField}
     * @param uiClass             {@link WPasswordFieldUI} {@link Class} used for {@link JPasswordField} by default
     * @param painterInterface    {@link IPasswordFieldPainter} interface {@link Class}
     * @param painterClass        {@link IPasswordFieldPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IPasswordFieldPainter}
     * @param defaultStyleId      {@link JPasswordField} default {@link StyleId}
     */
    public AbstractPasswordFieldDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                             @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                             @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                             @NotNull final Class<? extends P> painterClass,
                                             @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }

    @Override
    public void updateUI ( @NotNull final C component )
    {
        // Default echo char
        if ( !component.echoCharIsSet () )
        {
            component.setEchoChar ( '*' );
        }

        // Updating component UI
        super.updateUI ( component );
    }
}