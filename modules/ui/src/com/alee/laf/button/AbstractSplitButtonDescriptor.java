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

package com.alee.laf.button;

import com.alee.api.annotations.NotNull;
import com.alee.extended.button.ISplitButtonPainter;
import com.alee.extended.button.WSplitButtonUI;
import com.alee.extended.button.WebSplitButton;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

/**
 * Abstract descriptor for {@link WebSplitButton} component.
 * Extend this class for creating custom {@link WebSplitButton} descriptors.
 *
 * @param <C> {@link WebSplitButton} type
 * @param <U> base {@link WSplitButtonUI} type
 * @param <P> {@link ISplitButtonPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractSplitButtonDescriptor<C extends WebSplitButton, U extends WSplitButtonUI, P extends ISplitButtonPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractSplitButtonDescriptor}.
     *
     * @param id                  {@link WebSplitButton} identifier
     * @param componentClass      {@link WebSplitButton} {@link Class}
     * @param uiClassId           {@link WSplitButtonUI} {@link Class} identifier
     * @param baseUIClass         base {@link WSplitButtonUI} {@link Class} applicable to {@link WebSplitButton}
     * @param uiClass             {@link WSplitButtonUI} {@link Class} used for {@link WebSplitButton} by default
     * @param painterInterface    {@link ISplitButtonPainter} interface {@link Class}
     * @param painterClass        {@link ISplitButtonPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link ISplitButtonPainter}
     * @param defaultStyleId      {@link WebSplitButton} default {@link StyleId}
     */
    public AbstractSplitButtonDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                           @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                           @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                           @NotNull final Class<? extends P> painterClass,
                                           @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}