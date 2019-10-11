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

import com.alee.api.annotations.NotNull;

/**
 * This interface allows you to create customizer for objects of any type.
 * They can be easily passed into any method to customize or modify any component or data when requested.
 * todo Replace with {@link com.alee.api.jdk.Consumer}
 *
 * @param <T> object type
 * @author Mikle Garin
 */
public interface Customizer<T>
{
    /**
     * Customizes the specified object.
     *
     * @param object object to customize
     */
    public void customize ( @NotNull T object );
}