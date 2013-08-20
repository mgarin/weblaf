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

import com.alee.managers.language.data.Dictionary;

import java.util.EventListener;

/**
 * This listener is used to determine language and dictionary changes in LanguageManager.
 *
 * @author Mikle Garin
 */

public interface LanguageListener extends EventListener
{
    /**
     * Notifies when language changed.
     *
     * @param oldLang old language
     * @param newLang new language
     */
    public void languageChanged ( String oldLang, String newLang );

    /**
     * Notifies when new dictionary added.
     *
     * @param dictionary added dictionary
     */
    public void dictionaryAdded ( Dictionary dictionary );

    /**
     * Notifies when new dictionary removed.
     *
     * @param dictionary removed dictionary
     */
    public void dictionaryRemoved ( Dictionary dictionary );

    /**
     * Notifies when dictionaries cleared.
     */
    public void dictionariesCleared ();
}