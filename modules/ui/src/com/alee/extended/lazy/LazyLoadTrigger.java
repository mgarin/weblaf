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

/**
 * {@link LazyContent} trigger types.
 *
 * @author Mikle Garin
 */
public enum LazyLoadTrigger
{
    /**
     * Loading will start right upon initialization.
     */
    onInit,

    /**
     * Loading will start upon {@link java.awt.Container} becoming visible.
     */
    onDisplay,

    /**
     * Loading won't start until manually triggered.
     */
    manual
}