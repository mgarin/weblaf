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

package com.alee.extended.lazy;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import java.util.EventListener;

/**
 * {@link LazyContent} events listener.
 *
 * @param <D> data type
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */
public interface LazyContentListener<D, C extends JComponent> extends EventListener
{
    /**
     * Informs about {@link LazyContent} data load completion.
     *
     * @param data loaded data
     */
    public void dataLoaded ( @Nullable D data );

    /**
     * Informs about {@link LazyContent} data load failure.
     *
     * @param cause {@link Throwable}
     */
    public void dataFailed ( @NotNull Throwable cause );

    /**
     * Informs about {@link LazyContent} UI elements load completion.
     *
     * @param data      loaded data
     * @param component loaded {@link JComponent}
     */
    public void uiLoaded ( @Nullable D data, @NotNull C component );

    /**
     * Informs about {@link LazyContent} UI elements load failure.
     *
     * @param data  loaded data
     * @param cause {@link Throwable}
     */
    public void uiFailed ( @Nullable D data, @NotNull Throwable cause );
}