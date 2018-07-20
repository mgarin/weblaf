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

package com.alee.laf.scroll;

import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Pluggable look and feel interface for {@link WebScrollBar} component.
 *
 * @author Mikle Garin
 */
public abstract class WScrollBarUI extends BasicScrollBarUI
{
    /**
     * Returns whether scroll bar arrow buttons should be displayed or not.
     *
     * @return true if scroll bar arrow buttons should be displayed, false otherwise
     */
    public abstract boolean isDisplayButtons ();

    /**
     * Sets whether scroll bar arrow buttons should be displayed or not.
     *
     * @param displayButtons whether scroll bar arrow buttons should be displayed or not
     */
    public abstract void setDisplayButtons ( boolean displayButtons );

    /**
     * Returns whether scroll bar track should be displayed or not.
     *
     * @return true if scroll bar track should be displayed, false otherwise
     */
    public abstract boolean isDisplayTrack ();

    /**
     * Sets whether scroll bar track should be displayed or not.
     *
     * @param displayTrack whether scroll bar track should be displayed or not
     */
    public abstract void setDisplayTrack ( boolean displayTrack );
}