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

package com.alee.managers.style;

/**
 * This interface is implemented by components and UIs which support styling through WebLaF skins.
 * It provides only two methods to allow default component style ID modifications.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.StyleManager
 * @see Skin
 */

public interface Styleable
{
    /**
     * Returns component style ID.
     *
     * @return component style ID
     */
    public StyleId getStyleId ();

    /**
     * Sets new component style ID.
     * If style for the specified ID cannot be found in skin then its default style will be used instead.
     * This method forces component to instantly apply style with the specified ID to itself.
     *
     * @param id custom component style ID
     * @return previously used style ID
     */
    public StyleId setStyleId ( StyleId id );
}