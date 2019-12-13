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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Abstract descriptor for {@link JMenuItem} component.
 * Extend this class for creating custom {@link JMenuItem} descriptors.
 *
 * @param <C> {@link JMenuItem} type
 * @param <U> base {@link WebMenuItemUI} type
 * @param <P> {@link WebMenuItemUI} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractMenuItemDescriptor<C extends JMenuItem, U extends WebMenuItemUI, P extends IMenuItemPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractMenuItemDescriptor}.
     *
     * @param id                  {@link JMenuItem} identifier
     * @param componentClass      {@link JMenuItem} {@link Class}
     * @param uiClassId           {@link WebMenuItemUI} {@link Class} identifier
     * @param baseUIClass         base {@link WebMenuItemUI} {@link Class} applicable to {@link JMenuItem}
     * @param uiClass             {@link WebMenuItemUI} {@link Class} used for {@link JMenuItem} by default
     * @param painterInterface    {@link WebMenuItemUI} interface {@link Class}
     * @param painterClass        {@link WebMenuItemUI} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link WebMenuItemUI}
     * @param defaultStyleId      {@link JMenuItem} default {@link StyleId}
     */
    public AbstractMenuItemDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
                                        @NotNull final Class<U> baseUIClass, @NotNull final Class<? extends U> uiClass,
                                        @NotNull final Class<P> painterInterface, @NotNull final Class<? extends P> painterClass,
                                        @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}