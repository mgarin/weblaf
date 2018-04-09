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

import java.util.Locale;

/**
 * Simples {@link LanguageListener} that updates default {@link Locale} whenever global language changes.
 * It is not necessary and by default {@link LanguageManager} doesn't change locale on its own.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 */
public final class LanguageLocaleUpdater implements LanguageListener
{
    @Override
    public void languageChanged ( final Language oldLanguage, final Language newLanguage )
    {
        Locale.setDefault ( newLanguage.getLocale () );
    }
}