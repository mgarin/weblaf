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

import java.util.EventListener;

/**
 * Base listener for any chooser component.
 *
 * @param <T> chooser data type
 * @author Mikle Garin
 */
public interface ChooserListener<T> extends EventListener
{
    /**
     * Informs that chooser's selected data has changed.
     *
     * @param oldData previously selected data
     * @param newData new selected data
     */
    public void selected ( T oldData, T newData );
}