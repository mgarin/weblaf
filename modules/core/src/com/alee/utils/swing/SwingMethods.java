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
 * This interface is extended by all interfaces that provide custom methods for multiply Web-components.
 * All those methods are mostly bridges between Web-components and various managers and utility classes.
 *
 * @author Mikle Garin
 * @see com.alee.managers.settings.SettingsMethods
 * @see com.alee.managers.language.LanguageMethods
 * @see com.alee.managers.language.LanguageContainerMethods
 * @see com.alee.managers.hotkey.HotkeyMethods
 * @see com.alee.managers.hotkey.ButtonHotkeyMethods
 * @see com.alee.managers.tooltip.ToolTipMethods
 * @see com.alee.utils.swing.SizeMethods
 * @see com.alee.utils.swing.FontMethods
 * @see com.alee.utils.swing.BorderMethods
 * @see com.alee.utils.swing.EventMethods
 * @see com.alee.utils.swing.WindowMethods
 * @see com.alee.utils.swing.WindowEventMethods
 * @see com.alee.utils.swing.DocumentEventMethods
 * @see com.alee.laf.tree.TreeEventMethods
 * @see com.alee.extended.window.PopOverEventMethods
 * @see com.alee.extended.tab.DocumentPaneEventMethods
 */

@SuppressWarnings ("JavadocReference")
public interface SwingMethods
{
    /**
     * todo To add:
     * todo HotkeyMethods, ButtonHotkeyMethods - add and replace all workarounds
     * todo OrientationMethods - add
     *
     * todo To modify:
     * todo SizeMethods - add into all available components
     * todo Move font methods from SwingUtils to FontUtils
     * todo TooltipMethods, EventMethods - add more methods and inject them into various components
     */
}