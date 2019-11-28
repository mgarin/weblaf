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

/**
 * Data loading progress callback for {@link LazyContent}.
 *
 * @author Mikle Garin
 */
public interface ProgressCallback
{
    /**
     * Changes total amount of data chunks to be loaded.
     * Total value can be between {@code 0} and {@link Integer#MAX_VALUE}.
     *
     * @param total total amount of data chunks to be loaded
     */
    public void setTotal ( int total );

    /**
     * Changes current amount of loaded data chunks.
     * Progress value can be between {@code 0} and {@link Integer#MAX_VALUE}.
     *
     * @param progress current amount of loaded data chunks
     * @param data     additional progress data
     */
    public void setProgress ( int progress, @NotNull Object... data );
}