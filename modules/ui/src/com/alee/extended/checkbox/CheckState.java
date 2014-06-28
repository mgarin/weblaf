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

package com.alee.extended.checkbox;

/**
 * Tristate checkbox states.
 *
 * @author Mikle Garin
 */

public enum CheckState
{
    /**
     * When checkbox is not checked.
     */
    unchecked,

    /**
     * When checkbox is checked.
     */
    checked,

    /**
     * When checkbox is partially checked. Shortly - mixed state.
     * This is a third custom checkbox state specially for WebTristateCheckBox.
     */
    mixed
}