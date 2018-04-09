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

import java.util.EventListener;

/**
 * This listener can be used to track {@link Language} changes in {@link LanguageManager}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 * @see Language
 */
public interface LanguageListener extends EventListener
{
    /**
     * Notifies when current {@link Language} was changed within {@link LanguageManager}.
     *
     * @param oldLanguage old {@link Language}
     * @param newLanguage new {@link Language}
     */
    public void languageChanged ( Language oldLanguage, Language newLanguage );
}