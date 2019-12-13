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
 * Abstract descriptor for {@link JFormattedTextField} component.
 * Extend this class for creating custom {@link JFormattedTextField} descriptors.
 *
 * @param <C> {@link JFormattedTextField} type
 * @param <U> base {@link WFormattedTextFieldUI} type
 * @param <P> {@link IFormattedTextFieldPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractFormattedTextFieldDescriptor<C extends JFormattedTextField, U extends WFormattedTextFieldUI, P extends IFormattedTextFieldPainter>
        extends AbstractTextComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractFormattedTextFieldDescriptor}.
     *
     * @param id                  {@link JFormattedTextField} identifier
     * @param componentClass      {@link JFormattedTextField} {@link Class}
     * @param uiClassId           {@link WFormattedTextFieldUI} {@link Class} identifier
     * @param baseUIClass         base {@link WFormattedTextFieldUI} {@link Class} applicable to {@link JFormattedTextField}
     * @param uiClass             {@link WFormattedTextFieldUI} {@link Class} used for {@link JFormattedTextField} by default
     * @param painterInterface    {@link IFormattedTextFieldPainter} interface {@link Class}
     * @param painterClass        {@link IFormattedTextFieldPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IFormattedTextFieldPainter}
     * @param defaultStyleId      {@link JFormattedTextField} default {@link StyleId}
     */
    public AbstractFormattedTextFieldDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                                  @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                                  @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                                  @NotNull final Class<? extends P> painterClass,
                                                  @NotNull final Class<? extends P> painterAdapterClass,
                                                  @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}