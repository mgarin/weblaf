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

/**
 * Interface providing access to data loading events.
 *
 * @param <D> data type
 * @author Mikle Garin
 */
public interface DataLoadProgress<D>
{
    /**
     * Returns last provided total amount of data chunks to be loaded or {@code -1} if it was never specified.
     * Actual total value can be between {@code 0} and {@link Integer#MAX_VALUE}.
     *
     * @return last provided total amount of data chunks to be loaded or {@code -1} if it was never specified
     */
    public int getTotal ();

    /**
     * Returns last provided amount of loaded data chunks or {@code -1} if it was never specified.
     * Actual progress value can be between {@code 0} and {@link Integer#MAX_VALUE}.
     *
     * @return last provided amount of loaded data chunks or {@code -1} if it was never specified
     */
    public int getProgress ();

    /**
     * Returns last provided additional progress data or {@code null} if it was never specified.
     *
     * @return last provided additional progress data or {@code null} if it was never specified
     */
    @Nullable
    public Object[] getProgressData ();

    /**
     * Adds specified {@link DataLoadListener}.
     *
     * @param listener {@link DataLoadListener} to add
     */
    public void addListener ( @NotNull DataLoadListener<D> listener );

    /**
     * Removes specified {@link DataLoadListener}.
     *
     * @param listener {@link DataLoadListener} to remove
     */
    public void removeListener ( @NotNull DataLoadListener<D> listener );
}