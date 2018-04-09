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
import com.alee.utils.swing.extensions.MethodExtension;

/**
 * This interface provides a set of methods that should be implemented by components supporting automated translations.
 * Commonly all implementations are leading to similar {@link UILanguageManager} methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see MethodExtension
 * @see LanguageManager
 * @see LanguageUpdater
 */
public interface LanguageMethods extends MethodExtension
{
    /**
     * Returns language key which was used to register specified component in {@link LanguageManager}.
     *
     * @return language key which was used to register specified component in {@link LanguageManager}
     */
    public String getLanguage ();

    /**
     * Registers component in {@link LanguageManager} under specified key and with specified formatting data.
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
     * Unregisters component from {@link LanguageManager}.
     * This method will not change current component settings that came from latest applied translation.
     */
    public void removeLanguage ();

    /**
     * Returns whether this component registered in {@link LanguageManager} or not.
     *
     * @return true if component is registered in {@link LanguageManager}, false otherwise
     */
    public boolean isLanguageSet ();

    /**
     * Registers custom language updater for this component.
     *
     * @param updater {@link LanguageUpdater} for the component
     */
    public void setLanguageUpdater ( LanguageUpdater updater );

    /**
     * Unregisters custom component {@link LanguageUpdater}.
     */
    public void removeLanguageUpdater ();
}