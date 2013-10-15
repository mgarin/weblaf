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
 * @see SizeMethods
 * @see FontMethods
 * @see BorderMethods
 * @see WindowMethods
 */

public interface SwingMethods
{
    // todo To add:
    // todo HotkeyMethods, ButtonHotkeyMethods - add and replace all workarounds
    // todo TooltipMethods, OrientationMethods - add

    // todo To modify:
    // todo SizeMethods - add into all available components
    // todo Move font methods from SwingUtils to FontUtils
}