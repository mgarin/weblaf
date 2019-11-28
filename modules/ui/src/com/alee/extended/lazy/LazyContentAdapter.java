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

/**
 * {@link LazyContentListener} adapter.
 *
 * @param <D> data type
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */
public abstract class LazyContentAdapter<D, C extends JComponent> implements LazyContentListener<D, C>
{
    @Override
    public void dataLoaded ( @Nullable final D data )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void dataFailed ( @NotNull final Throwable exception )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void uiLoaded ( @Nullable final D data, @NotNull final C component )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void uiFailed ( @Nullable final D data, @NotNull final Throwable cause )
    {
        /**
         * Do nothing by default.
         */
    }
}