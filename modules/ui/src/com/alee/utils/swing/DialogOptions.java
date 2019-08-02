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
 * This interface represents various dialogs return options.
 *
 * @author Mikle Garin
 */
public interface DialogOptions
{
    /**
     * Return value if cancel is chosen.
     */
    public static final int CANCEL_OPTION = 1;

    /**
     * Return value if approve (yes, ok) is chosen.
     */
    public static final int OK_OPTION = 0;

    /**
     * Return value if dialog was closed.
     */
    public static final int CLOSE_OPTION = -1;

    /**
     * Return value if no actions were taken.
     */
    public static final int NONE_OPTION = -2;

    /**
     * Return value if an error occured.
     */
    public static final int ERROR_OPTION = -3;
}