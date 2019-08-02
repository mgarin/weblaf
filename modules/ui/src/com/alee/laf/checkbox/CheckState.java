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

package com.alee.laf.checkbox;

/**
 * Common button selection states.
 *
 * @author Mikle Garin
 */
public enum CheckState
{
    /**
     * When button is not selected.
     */
    unchecked,

    /**
     * When button is selected.
     */
    checked,

    /**
     * When button is partially selected. To make it short - "mixed" state.
     * This is a third custom button state made specifically for {@link com.alee.extended.checkbox.WebTristateCheckBox}.
     */
    mixed
}