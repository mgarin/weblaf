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

package com.alee.laf.filechooser;

/**
 * This enumeration represents all available file chooser types.
 *
 * @author Mikle Garin
 */

public enum FileChooserType
{
    /**
     * Save file chooser type.
     * <p>
     * File chooser of this type has a special input field to enter saved file name.
     * It is also limited to single file selection since there is no point in saving single content into multiply files.
     * It also has an extension chooser combobox if allowed extensions are limited.
     */
    save,

    /**
     * Open file chooser type.
     * <p>
     * File chooser of this type is used to choose single or multiply files.
     */
    open,

    /**
     * Custom file chooser type.
     */
    custom
}
