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

import com.alee.api.annotations.Nullable;

import java.util.EventListener;

/**
 * Custom listener that informs about hover object changes.
 * This listener provides ready-to-use functionality in components where it is supported.
 *
 * @param <E> object type
 * @author Mikle Garin
 */
public interface HoverListener<E> extends EventListener
{
    /**
     * Informs when hover object changes.
     *
     * @param previous previous hover object
     * @param current  current hover object
     */
    public void hoverChanged ( @Nullable E previous, @Nullable E current );
}