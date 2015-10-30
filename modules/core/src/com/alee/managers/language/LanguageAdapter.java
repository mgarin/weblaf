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

/**
 * Custom LanguageListener adapter.
 *
 * @author Mikle Garin
 */

public abstract class LanguageAdapter implements LanguageListener
{
    @Override
    public void languageChanged ( final String oldLang, final String newLang )
    {
        languageUpdated ();
    }

    @Override
    public void dictionaryAdded ( final Dictionary dictionary )
    {
        languageUpdated ();
    }

    @Override
    public void dictionaryRemoved ( final Dictionary dictionary )
    {
        languageUpdated ();
    }

    @Override
    public void dictionariesCleared ()
    {
        languageUpdated ();
    }

    /**
     * Informs that language was updated in some way.
     */
    public void languageUpdated ()
    {
        //
    }
}