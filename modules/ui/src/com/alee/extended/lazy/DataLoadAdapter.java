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
 * Adapter for {@link DataLoadListener}.
 *
 * @param <D> data type
 * @author Mikle Garin
 */
public abstract class DataLoadAdapter<D> implements DataLoadListener<D>
{
    @Override
    public void loadingStarted ()
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void totalChanged ( final int total )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void progressChanged ( final int progress, @Nullable final Object[] data )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void loaded ( @Nullable final D data )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void failed ( @NotNull final Throwable cause )
    {
        /**
         * Do nothing by default.
         */
    }
}