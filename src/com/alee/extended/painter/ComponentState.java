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

package com.alee.extended.painter;

/**
 * User: mgarin Date: 14.12.11 Time: 16:20
 */

public class ComponentState
{
    /**
     * ************************* Shared states ***************************
     */

    // Basic
    public static String normal = "normal";
    public static String disabled = "disabled";

    // Extended states
    public static String rollover = "rollover";
    public static String pressed = "pressed";
    public static String selected = "selected";
    public static String selectedRollover = "selectedRollover";
    public static String selectedDisabled = "selectedDisabled";
    public static String selectedPressed = "selectedPressed";
    public static String floating = "floating";
    public static String floatingDisabled = "floatingDisabled";

    /**
     * *********************** Additional states *************************
     */

    // Basic
    public static String focused = "focused";
}
