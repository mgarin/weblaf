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

package com.alee.utils.classes;

/**
 * Simple helper interface for class construction tests.
 *
 * @author Mikle Garin
 */
public interface ClassData
{
    /**
     * Success data.
     */
    public static final String SUCCESS = "success";

    /**
     * Returns class construction data or {@code null} if construction failed.
     * This data is used to confirm that class constructor was called and it wasn't some workaround through {@code sun.misc.Unsafe}.
     *
     * @return class construction data or {@code null} if construction failed
     */
    public String get ();
}