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

package com.alee.managers.hotkey;

import java.awt.event.KeyEvent;

/**
 * This interface provides list of predefined hotkeys which can be easily used to setup any hotkey through WebLaF HotkeyManager system or
 * through standard Web-component methods. More different standard hotkeys will be added to this interface with time to make it even more
 * powerful and useful in various situations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HotkeyManager">How to use HotkeyManager</a>
 * @see HotkeyManager
 */
public final class Hotkey
{
    /**
     * Modifier hotkeys.
     */
    public static final HotkeyData CTRL = new HotkeyData ( KeyEvent.VK_CONTROL );
    public static final HotkeyData ALT = new HotkeyData ( KeyEvent.VK_ALT );
    public static final HotkeyData SHIFT = new HotkeyData ( KeyEvent.VK_SHIFT );

    /**
     * Function hotkeys.
     */
    public static final HotkeyData F1 = new HotkeyData ( KeyEvent.VK_F1 );
    public static final HotkeyData F2 = new HotkeyData ( KeyEvent.VK_F2 );
    public static final HotkeyData F3 = new HotkeyData ( KeyEvent.VK_F3 );
    public static final HotkeyData F4 = new HotkeyData ( KeyEvent.VK_F4 );
    public static final HotkeyData F5 = new HotkeyData ( KeyEvent.VK_F5 );
    public static final HotkeyData F6 = new HotkeyData ( KeyEvent.VK_F6 );
    public static final HotkeyData F7 = new HotkeyData ( KeyEvent.VK_F7 );
    public static final HotkeyData F8 = new HotkeyData ( KeyEvent.VK_F8 );
    public static final HotkeyData F9 = new HotkeyData ( KeyEvent.VK_F9 );
    public static final HotkeyData F10 = new HotkeyData ( KeyEvent.VK_F10 );
    public static final HotkeyData F11 = new HotkeyData ( KeyEvent.VK_F11 );
    public static final HotkeyData F12 = new HotkeyData ( KeyEvent.VK_F12 );

    /**
     * Simple hotkeys.
     */
    public static final HotkeyData A = new HotkeyData ( KeyEvent.VK_A );
    public static final HotkeyData B = new HotkeyData ( KeyEvent.VK_B );
    public static final HotkeyData C = new HotkeyData ( KeyEvent.VK_C );
    public static final HotkeyData D = new HotkeyData ( KeyEvent.VK_D );
    public static final HotkeyData E = new HotkeyData ( KeyEvent.VK_E );
    public static final HotkeyData F = new HotkeyData ( KeyEvent.VK_F );
    public static final HotkeyData G = new HotkeyData ( KeyEvent.VK_G );
    public static final HotkeyData H = new HotkeyData ( KeyEvent.VK_H );
    public static final HotkeyData I = new HotkeyData ( KeyEvent.VK_I );
    public static final HotkeyData J = new HotkeyData ( KeyEvent.VK_J );
    public static final HotkeyData K = new HotkeyData ( KeyEvent.VK_K );
    public static final HotkeyData L = new HotkeyData ( KeyEvent.VK_L );
    public static final HotkeyData M = new HotkeyData ( KeyEvent.VK_M );
    public static final HotkeyData N = new HotkeyData ( KeyEvent.VK_N );
    public static final HotkeyData O = new HotkeyData ( KeyEvent.VK_O );
    public static final HotkeyData P = new HotkeyData ( KeyEvent.VK_P );
    public static final HotkeyData Q = new HotkeyData ( KeyEvent.VK_Q );
    public static final HotkeyData R = new HotkeyData ( KeyEvent.VK_R );
    public static final HotkeyData S = new HotkeyData ( KeyEvent.VK_S );
    public static final HotkeyData T = new HotkeyData ( KeyEvent.VK_T );
    public static final HotkeyData U = new HotkeyData ( KeyEvent.VK_U );
    public static final HotkeyData V = new HotkeyData ( KeyEvent.VK_V );
    public static final HotkeyData W = new HotkeyData ( KeyEvent.VK_W );
    public static final HotkeyData X = new HotkeyData ( KeyEvent.VK_X );
    public static final HotkeyData Y = new HotkeyData ( KeyEvent.VK_Y );
    public static final HotkeyData Z = new HotkeyData ( KeyEvent.VK_Z );

    /**
     * Symbol hotkeys.
     */
    public static final HotkeyData PLUS = new HotkeyData ( KeyEvent.VK_ADD );
    public static final HotkeyData MINUS = new HotkeyData ( KeyEvent.VK_MINUS );
    public static final HotkeyData MULTIPLY = new HotkeyData ( KeyEvent.VK_MULTIPLY );
    public static final HotkeyData DIVIDE = new HotkeyData ( KeyEvent.VK_DIVIDE );

    /**
     * Number hotkeys.
     */
    public static final HotkeyData NUMBER_0 = new HotkeyData ( KeyEvent.VK_0 );
    public static final HotkeyData NUMBER_1 = new HotkeyData ( KeyEvent.VK_1 );
    public static final HotkeyData NUMBER_2 = new HotkeyData ( KeyEvent.VK_2 );
    public static final HotkeyData NUMBER_3 = new HotkeyData ( KeyEvent.VK_3 );
    public static final HotkeyData NUMBER_4 = new HotkeyData ( KeyEvent.VK_4 );
    public static final HotkeyData NUMBER_5 = new HotkeyData ( KeyEvent.VK_5 );
    public static final HotkeyData NUMBER_6 = new HotkeyData ( KeyEvent.VK_6 );
    public static final HotkeyData NUMBER_7 = new HotkeyData ( KeyEvent.VK_7 );
    public static final HotkeyData NUMBER_8 = new HotkeyData ( KeyEvent.VK_8 );
    public static final HotkeyData NUMBER_9 = new HotkeyData ( KeyEvent.VK_9 );

    /**
     * Specific hotkeys.
     */
    public static final HotkeyData UP = new HotkeyData ( KeyEvent.VK_UP );
    public static final HotkeyData DOWN = new HotkeyData ( KeyEvent.VK_DOWN );
    public static final HotkeyData LEFT = new HotkeyData ( KeyEvent.VK_LEFT );
    public static final HotkeyData RIGHT = new HotkeyData ( KeyEvent.VK_RIGHT );
    public static final HotkeyData SPACE = new HotkeyData ( KeyEvent.VK_SPACE );
    public static final HotkeyData BACKSPACE = new HotkeyData ( KeyEvent.VK_BACK_SPACE );
    public static final HotkeyData DELETE = new HotkeyData ( KeyEvent.VK_DELETE );
    public static final HotkeyData ESCAPE = new HotkeyData ( KeyEvent.VK_ESCAPE );
    public static final HotkeyData ENTER = new HotkeyData ( KeyEvent.VK_ENTER );
    public static final HotkeyData PRINTSCREEN = new HotkeyData ( KeyEvent.VK_PRINTSCREEN );
    public static final HotkeyData HOME = new HotkeyData ( KeyEvent.VK_HOME );
    public static final HotkeyData PAGE_UP = new HotkeyData ( KeyEvent.VK_PAGE_UP );
    public static final HotkeyData PAGE_DOWN = new HotkeyData ( KeyEvent.VK_PAGE_DOWN );
    public static final HotkeyData END = new HotkeyData ( KeyEvent.VK_END );
    public static final HotkeyData TAB = new HotkeyData ( KeyEvent.VK_TAB );

    /**
     * Frequently used hotkeys with CTRL modifier.
     */
    public static final HotkeyData CTRL_A = new HotkeyData ( true, false, false, KeyEvent.VK_A );
    public static final HotkeyData CTRL_B = new HotkeyData ( true, false, false, KeyEvent.VK_B );
    public static final HotkeyData CTRL_C = new HotkeyData ( true, false, false, KeyEvent.VK_C );
    public static final HotkeyData CTRL_D = new HotkeyData ( true, false, false, KeyEvent.VK_D );
    public static final HotkeyData CTRL_E = new HotkeyData ( true, false, false, KeyEvent.VK_E );
    public static final HotkeyData CTRL_F = new HotkeyData ( true, false, false, KeyEvent.VK_F );
    public static final HotkeyData CTRL_G = new HotkeyData ( true, false, false, KeyEvent.VK_G );
    public static final HotkeyData CTRL_H = new HotkeyData ( true, false, false, KeyEvent.VK_H );
    public static final HotkeyData CTRL_I = new HotkeyData ( true, false, false, KeyEvent.VK_I );
    public static final HotkeyData CTRL_J = new HotkeyData ( true, false, false, KeyEvent.VK_J );
    public static final HotkeyData CTRL_K = new HotkeyData ( true, false, false, KeyEvent.VK_K );
    public static final HotkeyData CTRL_L = new HotkeyData ( true, false, false, KeyEvent.VK_L );
    public static final HotkeyData CTRL_M = new HotkeyData ( true, false, false, KeyEvent.VK_M );
    public static final HotkeyData CTRL_N = new HotkeyData ( true, false, false, KeyEvent.VK_N );
    public static final HotkeyData CTRL_O = new HotkeyData ( true, false, false, KeyEvent.VK_O );
    public static final HotkeyData CTRL_P = new HotkeyData ( true, false, false, KeyEvent.VK_P );
    public static final HotkeyData CTRL_Q = new HotkeyData ( true, false, false, KeyEvent.VK_Q );
    public static final HotkeyData CTRL_R = new HotkeyData ( true, false, false, KeyEvent.VK_R );
    public static final HotkeyData CTRL_S = new HotkeyData ( true, false, false, KeyEvent.VK_S );
    public static final HotkeyData CTRL_T = new HotkeyData ( true, false, false, KeyEvent.VK_T );
    public static final HotkeyData CTRL_V = new HotkeyData ( true, false, false, KeyEvent.VK_V );
    public static final HotkeyData CTRL_U = new HotkeyData ( true, false, false, KeyEvent.VK_U );
    public static final HotkeyData CTRL_W = new HotkeyData ( true, false, false, KeyEvent.VK_W );
    public static final HotkeyData CTRL_X = new HotkeyData ( true, false, false, KeyEvent.VK_X );
    public static final HotkeyData CTRL_Y = new HotkeyData ( true, false, false, KeyEvent.VK_Y );
    public static final HotkeyData CTRL_Z = new HotkeyData ( true, false, false, KeyEvent.VK_Z );
    public static final HotkeyData CTRL_ENTER = new HotkeyData ( true, false, false, KeyEvent.VK_ENTER );
    public static final HotkeyData CTRL_LEFT = new HotkeyData ( true, false, false, KeyEvent.VK_LEFT );
    public static final HotkeyData CTRL_RIGHT = new HotkeyData ( true, false, false, KeyEvent.VK_RIGHT );
    public static final HotkeyData CTRL_UP = new HotkeyData ( true, false, false, KeyEvent.VK_UP );
    public static final HotkeyData CTRL_DOWN = new HotkeyData ( true, false, false, KeyEvent.VK_DOWN );
    public static final HotkeyData CTRL_TAB = new HotkeyData ( true, false, false, KeyEvent.VK_TAB );
    public static final HotkeyData CTRL_SPACE = new HotkeyData ( true, false, false, KeyEvent.VK_SPACE );

    /**
     * Frequently used hotkeys with ALT modifier.
     */
    public static final HotkeyData ALT_A = new HotkeyData ( false, true, false, KeyEvent.VK_A );
    public static final HotkeyData ALT_B = new HotkeyData ( false, true, false, KeyEvent.VK_B );
    public static final HotkeyData ALT_C = new HotkeyData ( false, true, false, KeyEvent.VK_C );
    public static final HotkeyData ALT_D = new HotkeyData ( false, true, false, KeyEvent.VK_D );
    public static final HotkeyData ALT_E = new HotkeyData ( false, true, false, KeyEvent.VK_E );
    public static final HotkeyData ALT_F = new HotkeyData ( false, true, false, KeyEvent.VK_F );
    public static final HotkeyData ALT_G = new HotkeyData ( false, true, false, KeyEvent.VK_G );
    public static final HotkeyData ALT_H = new HotkeyData ( false, true, false, KeyEvent.VK_H );
    public static final HotkeyData ALT_I = new HotkeyData ( false, true, false, KeyEvent.VK_I );
    public static final HotkeyData ALT_J = new HotkeyData ( false, true, false, KeyEvent.VK_J );
    public static final HotkeyData ALT_K = new HotkeyData ( false, true, false, KeyEvent.VK_K );
    public static final HotkeyData ALT_L = new HotkeyData ( false, true, false, KeyEvent.VK_L );
    public static final HotkeyData ALT_M = new HotkeyData ( false, true, false, KeyEvent.VK_M );
    public static final HotkeyData ALT_N = new HotkeyData ( false, true, false, KeyEvent.VK_N );
    public static final HotkeyData ALT_O = new HotkeyData ( false, true, false, KeyEvent.VK_O );
    public static final HotkeyData ALT_P = new HotkeyData ( false, true, false, KeyEvent.VK_P );
    public static final HotkeyData ALT_Q = new HotkeyData ( false, true, false, KeyEvent.VK_Q );
    public static final HotkeyData ALT_R = new HotkeyData ( false, true, false, KeyEvent.VK_R );
    public static final HotkeyData ALT_S = new HotkeyData ( false, true, false, KeyEvent.VK_S );
    public static final HotkeyData ALT_T = new HotkeyData ( false, true, false, KeyEvent.VK_T );
    public static final HotkeyData ALT_U = new HotkeyData ( false, true, false, KeyEvent.VK_U );
    public static final HotkeyData ALT_V = new HotkeyData ( false, true, false, KeyEvent.VK_V );
    public static final HotkeyData ALT_W = new HotkeyData ( false, true, false, KeyEvent.VK_W );
    public static final HotkeyData ALT_X = new HotkeyData ( false, true, false, KeyEvent.VK_X );
    public static final HotkeyData ALT_Y = new HotkeyData ( false, true, false, KeyEvent.VK_Y );
    public static final HotkeyData ALT_Z = new HotkeyData ( false, true, false, KeyEvent.VK_Z );
    public static final HotkeyData ALT_F4 = new HotkeyData ( false, true, false, KeyEvent.VK_F4 );
    public static final HotkeyData ALT_ENTER = new HotkeyData ( false, true, false, KeyEvent.VK_ENTER );
    public static final HotkeyData ALT_HOME = new HotkeyData ( false, true, false, KeyEvent.VK_HOME );
    public static final HotkeyData ALT_END = new HotkeyData ( false, true, false, KeyEvent.VK_END );
    public static final HotkeyData ALT_LEFT = new HotkeyData ( false, true, false, KeyEvent.VK_LEFT );
    public static final HotkeyData ALT_RIGHT = new HotkeyData ( false, true, false, KeyEvent.VK_RIGHT );
    public static final HotkeyData ALT_UP = new HotkeyData ( false, true, false, KeyEvent.VK_UP );
    public static final HotkeyData ALT_DOWN = new HotkeyData ( false, true, false, KeyEvent.VK_DOWN );
    public static final HotkeyData ALT_TAB = new HotkeyData ( false, true, false, KeyEvent.VK_TAB );

    /**
     * Frequently used hotkeys with SHIFT modifier.
     */
    public static final HotkeyData SHIFT_TAB = new HotkeyData ( false, false, true, KeyEvent.VK_TAB );

    /**
     * Frequently used hotkeys with CTRL &amp; SHIFT modifier.
     */
    public static final HotkeyData CTRL_SHIFT_S = new HotkeyData ( true, false, true, KeyEvent.VK_S );
    public static final HotkeyData CTRL_SHIFT_Z = new HotkeyData ( true, false, true, KeyEvent.VK_Z );
    public static final HotkeyData CTRL_SHIFT_V = new HotkeyData ( true, false, true, KeyEvent.VK_V );
    public static final HotkeyData CTRL_SHIFT_N = new HotkeyData ( true, false, true, KeyEvent.VK_N );
    public static final HotkeyData CTRL_SHIFT_D = new HotkeyData ( true, false, true, KeyEvent.VK_D );
    public static final HotkeyData CTRL_SHIFT_X = new HotkeyData ( true, false, true, KeyEvent.VK_X );
}