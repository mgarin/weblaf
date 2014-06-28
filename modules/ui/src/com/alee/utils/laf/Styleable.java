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

package com.alee.utils.laf;

/**
 * This interface must be implemented by components and UIs which support styling through WebLaF skins.
 * It provides only two methods to allow default component style ID modifications.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.skin.WebLafSkin
 */

public interface Styleable
{
    /**
     * Returns component style ID.
     * Custom ID can be specified to override default component style using "setStyleId" method.
     * If style for such custom ID is not found in skin descriptor then default style for that component is used.
     *
     * @return component style ID
     */
    public String getStyleId ();

    /**
     * Sets custom component style ID.
     * If style for such custom ID is not found in skin descriptor default style will be used for that component.
     * This method forces component to instantly use component style with the specified ID.
     *
     * @param id custom component style ID
     */
    public void setStyleId ( String id );
}