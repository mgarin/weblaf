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

import com.alee.api.annotations.NotNull;
import com.alee.utils.swing.extensions.MethodExtension;

import javax.swing.*;

/**
 * This interface provides a set of methods that can be implemented by any components that could provide event listeners.
 * Commonly all implementations are leading to similar {@link UILanguageManager} methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see MethodExtension
 * @see LanguageManager
 * @see LanguageListener
 * @see DictionaryListener
 */
public interface LanguageEventMethods extends MethodExtension
{
    /**
     * Adds new {@link LanguageListener} tied to the {@link JComponent} implementing this interface.
     *
     * Unlike {@link LanguageManager#addLanguageListener(LanguageListener)} using this method will not store hard references
     * to the {@link LanguageListener} outside of the {@link JComponent} to avoid any mermory leaks. So if specified {@link JComponent}
     * will for instance be destroyed provided {@link LanguageListener} will also be destroyed.
     *
     * @param listener {@link LanguageListener} to add
     */
    public void addLanguageListener ( @NotNull LanguageListener listener );

    /**
     * Removes {@link LanguageListener} tied to the {@link JComponent} implementing this interface.
     *
     * @param listener {@link LanguageListener} to remove
     */
    public void removeLanguageListener ( @NotNull LanguageListener listener );

    /**
     * Removes all {@link LanguageListener}s tied to the {@link JComponent} implementing this interface.
     */
    public void removeLanguageListeners ();

    /**
     * Adds new {@link DictionaryListener} tied to the {@link JComponent} implementing this interface.
     *
     * Unlike {@link LanguageManager#addDictionaryListener(DictionaryListener)} using this method will not store hard references
     * to the {@link DictionaryListener} outside of the {@link JComponent} to avoid any mermory leaks. So if specified {@link JComponent}
     * will for instance be destroyed provided {@link DictionaryListener} will also be destroyed.
     *
     * @param listener {@link DictionaryListener} to add
     */
    public void addDictionaryListener ( @NotNull DictionaryListener listener );

    /**
     * Removes {@link DictionaryListener} tied to the {@link JComponent} implementing this interface.
     *
     * @param listener {@link DictionaryListener} to remove
     */
    public void removeDictionaryListener ( @NotNull DictionaryListener listener );

    /**
     * Removes all {@link DictionaryListener}s tied to the {@link JComponent} implementing this interface.
     */
    public void removeDictionaryListeners ();
}