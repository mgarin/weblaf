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

package com.alee.managers.language;

/**
 * This marker interface can be implemented by any class that provides {@link Language}-sensitive content.
 * It can be used by various components to synchronize visual updates with {@link Language} changes.
 *
 * It is currently supported by UIs of next components:
 * - {@link javax.swing.JList}
 * - {@link javax.swing.JTree}
 * - {@link javax.swing.JTable} and {@link javax.swing.table.JTableHeader}
 *
 * If any of the model values or renderers within those components implement {@link LanguageSensitive} then those components will ensure
 * that all content is updated appropriately upon {@link Language} changes within {@link LanguageManager}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 */
public interface LanguageSensitive
{
    /**
     * This is a marker interface, it doesn't have any methods to implement.
     */
}