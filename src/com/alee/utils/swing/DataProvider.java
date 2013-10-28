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

package com.alee.utils.swing;

/**
 * This interface provides a single method for data request of any type.
 * <p>
 * It is used within the WebLaF library for many purposes, for example:
 * - Providing popup location and bounds
 * - Providing data for language string formatters
 * - Providing specific component state value
 * and there are many others.
 *
 * @param <T> provided data type
 * @author Mikle Garin
 */
public interface DataProvider<T>
{
    /**
     * Returns data of the specified type.
     *
     * @return data
     */
    public T provide ();
}
