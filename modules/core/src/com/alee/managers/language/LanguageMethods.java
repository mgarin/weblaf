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

import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.swing.SwingMethods;

/**
 * This interface provides a set of methods that should be added into components that has translate-able text and support translation.
 * Basically all these methods are already implemented in LanguageManager but it is much easier to call them directly from component.
 *
 * @author Mikle Garin
 * @see SwingMethods
 * @see LanguageManager
 */

public interface LanguageMethods extends SwingMethods
{
    /**
     * Registers component in LanguageManager under specified key and with specified formatting data.
     *
     * @param key  language record key
     * @param data formatting data
     */
    public void setLanguage ( String key, Object... data );

    /**
     * Updates component language and formatting data.
     *
     * @param data new formatting data
     */
    public void updateLanguage ( Object... data );

    /**
     * Changes component language key and updates its language and formatting data.
     *
     * @param key  new language key
     * @param data new formatting data
     */
    public void updateLanguage ( String key, Object... data );

    /**
     * Unregisters component from LanguageManager.
     */
    public void removeLanguage ();

    /**
     * Returns whether this component registered in LanguageManager or not.
     *
     * @return true if component is registered in LanguageManager, false otherwise
     */
    public boolean isLanguageSet ();

    /**
     * Registers custom language updater for this component.
     *
     * @param updater component language updater
     */
    public void setLanguageUpdater ( LanguageUpdater updater );

    /**
     * Unregisters any custom language updater set for this component.
     */
    public void removeLanguageUpdater ();
}