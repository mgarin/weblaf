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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

/**
 * Abstract descriptor for {@link WebOverlay} component.
 * Extend this class for creating custom {@link WebOverlay} descriptors.
 *
 * @param <C> {@link WebOverlay} type
 * @param <U> base {@link WOverlayUI} type
 * @param <P> {@link IOverlayPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractOverlayDescriptor<C extends WebOverlay, U extends WOverlayUI, P extends IOverlayPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractOverlayDescriptor}.
     *
     * @param id                  {@link WebOverlay} identifier
     * @param componentClass      {@link WebOverlay} {@link Class}
     * @param uiClassId           {@link WOverlayUI} {@link Class} identifier
     * @param baseUIClass         base {@link WOverlayUI} {@link Class} applicable to {@link WebOverlay}
     * @param uiClass             {@link WOverlayUI} {@link Class} used for {@link WebOverlay} by default
     * @param painterInterface    {@link IOverlayPainter} interface {@link Class}
     * @param painterClass        {@link IOverlayPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IOverlayPainter}
     * @param defaultStyleId      {@link WebOverlay} default {@link StyleId}
     */
    public AbstractOverlayDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
                                       @NotNull final Class<U> baseUIClass, @NotNull final Class<? extends U> uiClass,
                                       @NotNull final Class<P> painterInterface, @NotNull final Class<? extends P> painterClass,
                                       @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}