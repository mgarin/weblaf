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

import com.alee.utils.swing.SwingMethods;

/**
 * This interface provides a set of methods that should be added into containers that might contain translate-able components.
 * Basically containers which implement these methods can simplify language keys used for contained components translation.
 *
 * @author Mikle Garin
 */

public interface LanguageContainerMethods extends SwingMethods
{
    /**
     * Sets language container key for container which implements this interface.
     *
     * @param key language container key
     */
    public void setLanguageContainerKey ( String key );

    /**
     * Removes language container key for container which implements this interface.
     */
    public void removeLanguageContainerKey ();

    /**
     * Returns language container key for container which implements this interface.
     *
     * @return language container key for container which implements this interface
     */
    public String getLanguageContainerKey ();
}