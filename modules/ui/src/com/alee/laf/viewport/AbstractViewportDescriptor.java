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

package com.alee.laf.viewport;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Abstract descriptor for {@link JViewport} component.
 * Extend this class for creating custom {@link JViewport} descriptors.
 *
 * @param <C> {@link JViewport} type
 * @param <U> base {@link WViewportUI} type
 * @param <P> {@link IViewportPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractViewportDescriptor<C extends JViewport, U extends WViewportUI, P extends IViewportPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractViewportDescriptor}.
     *
     * @param id                  {@link JViewport} identifier
     * @param componentClass      {@link JViewport} {@link Class}
     * @param uiClassId           {@link WViewportUI} {@link Class} identifier
     * @param baseUIClass         base {@link WViewportUI} {@link Class} applicable to {@link JViewport}
     * @param uiClass             {@link WViewportUI} {@link Class} used for {@link JViewport} by default
     * @param painterInterface    {@link IViewportPainter} interface {@link Class}
     * @param painterClass        {@link IViewportPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IViewportPainter}
     * @param defaultStyleId      {@link JViewport} default {@link StyleId}
     */
    public AbstractViewportDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
                                        @NotNull final Class<U> baseUIClass, @NotNull final Class<? extends U> uiClass,
                                        @NotNull final Class<P> painterInterface, @NotNull final Class<? extends P> painterClass,
                                        @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}