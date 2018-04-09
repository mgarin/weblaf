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

package com.alee.managers.language.updaters;

import com.alee.managers.language.Language;

import javax.swing.*;

/**
 * This interface provides basic methods required for updating component language-dependant variables.
 * There is also a predefined set of language updaters registered in {@link com.alee.managers.language.LanguageManager}.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public interface LanguageUpdater<C extends JComponent>
{
    /**
     * Returns class which instance should be updated using this {@link LanguageUpdater}.
     *
     * @return class type to update
     */
    public Class getComponentClass ();

    /**
     * Updates component language-dependant variables.
     *
     * @param component {@link JComponent}
     * @param language  {@link Language}
     * @param key       language key
     * @param data      language data
     */
    public void update ( C component, Language language, String key, Object... data );
}