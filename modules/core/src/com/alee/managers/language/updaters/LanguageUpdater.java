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

import com.alee.managers.language.data.Value;

import javax.swing.*;

/**
 * This interface provides basic methods required for updating component language-dependant variables.
 * There is also a predefined set of language updaters which are registered on LanguageManager initialization.
 *
 * @author Mikle Garin
 */

public interface LanguageUpdater<E extends JComponent>
{
    /**
     * Returns class which instance should be updated using this LanguageUpdater.
     *
     * @return class type to update
     */
    public Class getComponentClass ();

    /**
     * Updates component language-dependant variables.
     *
     * @param c     component
     * @param key   language key
     * @param value language value
     * @param data  formatting data
     */
    public void update ( E c, String key, Value value, Object... data );
}