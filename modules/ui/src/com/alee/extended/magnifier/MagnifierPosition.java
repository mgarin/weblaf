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

package com.alee.extended.magnifier;

/**
 * This enumeration represents available magnifier positioning types.
 *
 * @author Mikle Garin
 */
public enum MagnifierPosition
{
    /**
     * Position magnifier straight under the cursor location.
     * This position will force you to always look at the magnified version of UI under the cursor.
     */
    atCursor,

    /**
     * Position magnifier aside of the cursor location.
     * This will allow you to see both magnified and normal versions of UI under the cursor.
     */
    nearCursor,

    /**
     * Position magnifier where its component is located.
     * This will allow you to manually place magnifier on the UI.
     * <p>
     * Be aware that magnifier takes snapshots of the JLayeredPane and to avoid visual confusion is usualy placed on glass pane.
     * Otherwise it will also display magnified version of itself if you hover it in the UI.
     */
    staticComponent
}