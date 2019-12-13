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

package com.alee.laf.toolbar;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Abstract descriptor for {@link javax.swing.JToolBar.Separator} component.
 * Extend this class for creating custom {@link javax.swing.JToolBar.Separator} descriptors.
 *
 * @param <C> {@link javax.swing.JToolBar.Separator} type
 * @param <U> base {@link WToolBarSeparatorUI} type
 * @param <P> {@link IToolBarSeparatorPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractToolBarSeparatorDescriptor<C extends JToolBar.Separator, U extends WToolBarSeparatorUI, P extends IToolBarSeparatorPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractToolBarSeparatorDescriptor}.
     *
     * @param id                  {@link javax.swing.JToolBar.Separator} identifier
     * @param componentClass      {@link javax.swing.JToolBar.Separator} {@link Class}
     * @param uiClassId           {@link WToolBarSeparatorUI} {@link Class} identifier
     * @param baseUIClass         base {@link WToolBarSeparatorUI} {@link Class} applicable to {@link javax.swing.JToolBar.Separator}
     * @param uiClass             {@link WToolBarSeparatorUI} {@link Class} used for {@link javax.swing.JToolBar.Separator} by default
     * @param painterInterface    {@link IToolBarSeparatorPainter} interface {@link Class}
     * @param painterClass        {@link IToolBarSeparatorPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IToolBarSeparatorPainter}
     * @param defaultStyleId      {@link javax.swing.JToolBar.Separator} default {@link StyleId}
     */
    public AbstractToolBarSeparatorDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                                @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                                @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                                @NotNull final Class<? extends P> painterClass,
                                                @NotNull final Class<? extends P> painterAdapterClass,
                                                @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}