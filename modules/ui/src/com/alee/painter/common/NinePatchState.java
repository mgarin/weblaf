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

package com.alee.painter.common;

/**
 * This class contains default state constants supported by NinePatchStatePainter.
 *
 * @author Mikle Garin
 * @see com.alee.painter.common.NinePatchStatePainter
 */

public class NinePatchState
{
    /**
     * Normal state.
     */
    public static String normal = "normal";

    /**
     * Disabled state.
     */
    public static String disabled = "disabled";

    /**
     * Rollover state.
     */
    public static String rollover = "rollover";

    /**
     * Pressed state.
     */
    public static String pressed = "pressed";

    /**
     * Selected state.
     */
    public static String selected = "selected";

    /**
     * Selected and rollover state.
     */
    public static String selectedRollover = "selectedRollover";

    /**
     * Selected and disabled state.
     */
    public static String selectedDisabled = "selectedDisabled";

    /**
     * Selected and pressed state.
     */
    public static String selectedPressed = "selectedPressed";

    /**
     * Floating toolbar state.
     */
    public static String floating = "floating";

    /**
     * Floating and disabled toolbar state.
     */
    public static String floatingDisabled = "floatingDisabled";

    /**
     * Focused component state.
     * This state is rendered differently and not shared with other states.
     * 9-patch icon for this state will be painted atop of other state icons.
     */
    public static String focused = "focused";
}