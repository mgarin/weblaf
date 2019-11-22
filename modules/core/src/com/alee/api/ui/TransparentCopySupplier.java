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

package com.alee.api.ui;

import com.alee.api.annotations.NotNull;

/**
 * Special interface for any class providing support for semi-transparent state copy of itself.
 * todo Potentially replace with TransparentIcon implementation?
 *
 * @param <T> copy type
 * @author Mikle Garin
 */
public interface TransparentCopySupplier<T>
{
    /**
     * Returns semi-transparent state copy of implementing class instance.
     * It is not guaranteed that returned instance will be of the same type as original class.
     *
     * @param opacity opacity value, must be between 0 and 1
     * @return semi-transparent state copy of implementing class instance
     */
    @NotNull
    public T createTransparentCopy ( float opacity );
}