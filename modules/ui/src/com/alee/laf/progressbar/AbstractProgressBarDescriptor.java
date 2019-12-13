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

package com.alee.laf.progressbar;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Abstract descriptor for {@link JProgressBar} component.
 * Extend this class for creating custom {@link JProgressBar} descriptors.
 *
 * @param <C> {@link JProgressBar} type
 * @param <U> base {@link WProgressBarUI} type
 * @param <P> {@link IProgressBarPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractProgressBarDescriptor<C extends JProgressBar, U extends WProgressBarUI, P extends IProgressBarPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractProgressBarDescriptor}.
     *
     * @param id                  {@link JProgressBar} identifier
     * @param componentClass      {@link JProgressBar} {@link Class}
     * @param uiClassId           {@link WProgressBarUI} {@link Class} identifier
     * @param baseUIClass         base {@link WProgressBarUI} {@link Class} applicable to {@link JProgressBar}
     * @param uiClass             {@link WProgressBarUI} {@link Class} used for {@link JProgressBar} by default
     * @param painterInterface    {@link IProgressBarPainter} interface {@link Class}
     * @param painterClass        {@link IProgressBarPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IProgressBarPainter}
     * @param defaultStyleId      {@link JProgressBar} default {@link StyleId}
     */
    public AbstractProgressBarDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                           @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                           @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                           @NotNull final Class<? extends P> painterClass,
                                           @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }
}