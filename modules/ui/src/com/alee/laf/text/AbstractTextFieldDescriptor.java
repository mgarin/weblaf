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
 * Abstract descriptor for {@link JTextField} component.
 * Extend this class for creating custom {@link JTextField} descriptors.
 *
 * @param <C> {@link JTextField} type
 * @param <U> base {@link WTextFieldUI} type
 * @param <P> {@link ITextFieldPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractTextFieldDescriptor<C extends JTextField, U extends WTextFieldUI, P extends ITextFieldPainter>
        extends AbstractTextComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractTextFieldDescriptor}.
     *
     * @param id                  {@link JTextField} identifier
     * @param componentClass      {@link JTextField} {@link Class}
     * @param uiClassId           {@link WTextFieldUI} {@link Class} identifier
     * @param baseUIClass         base {@link WTextFieldUI} {@link Class} applicable to {@link JTextField}
     * @param uiClass             {@link WTextFieldUI} {@link Class} used for {@link JTextField} by default
     * @param painterInterface    {@link ITextFieldPainter} interface {@link Class}
     * @param painterClass        {@link ITextFieldPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link ITextFieldPainter}
     * @param defaultStyleId      {@link JTextField} default {@link StyleId}
     */
    public AbstractTextFieldDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
                                         @NotNull final Class<U> baseUIClass, @NotNull final Class<? extends U> uiClass,
                                         @NotNull final Class<P> painterInterface, @NotNull final Class<? extends P> painterClass,
                                         @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}