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

import java.util.EventListener;

/**
 * Data loading listener for {@link LazyContent}.
 *
 * @param <D> data type
 * @author Mikle Garin
 */
public interface DataLoadListener<D> extends EventListener
{
    /**
     * Informs about data loading start.
     */
    public void loadingStarted ();

    /**
     * Informs about total amount of data chunks to be loaded change.
     *
     * @param total total amount of data chunks to be loaded
     */
    public void totalChanged ( int total );

    /**
     * Informs about amount of loaded data chunks change.
     *
     * @param progress current amount of loaded data chunks
     * @param data     additional progress data
     */
    public void progressChanged ( int progress, @Nullable Object[] data );

    /**
     * Informs about data loading completion.
     *
     * @param data loaded data
     */
    public void loaded ( @Nullable D data );

    /**
     * Informs about data loading failure.
     *
     * @param cause {@link Throwable} cause
     */
    public void failed ( @NotNull Throwable cause );
}