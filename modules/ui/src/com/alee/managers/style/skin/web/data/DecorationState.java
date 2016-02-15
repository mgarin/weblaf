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

package com.alee.managers.style.skin.web.data;

/**
 * Base decoration states used by WebLaF decoration painters.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.web.AbstractDecorationPainter
 * @see com.alee.managers.style.skin.web.AbstractDecorationPainter#getDecorationStates()
 * @see com.alee.managers.style.skin.web.data.decoration.IDecoration
 */

public interface DecorationState
{
    public static final String enabled = "enabled";
    public static final String disabled = "disabled";
    public static final String focused = "focused";
    public static final String hover = "hover";
    public static final String pressed = "pressed";
    public static final String selected = "selected";
    public static final String empty = "empty";
    public static final String collapsed = "collapsed";
    public static final String expanded = "expanded";
    public static final String dragged = "dragged";
    public static final String checked = "checked";
    public static final String mixed = "mixed";
    public static final String floating = "floating";
    public static final String dropOn = "dropOn";
    public static final String dropBetween = "dropBetween";

    /**
     * Used to provide window maximized state for root pane painter.
     *
     * @see com.alee.managers.style.skin.web.WebRootPanePainter#getDecorationStates()
     */
    public static final String maximized = "maximized";
}