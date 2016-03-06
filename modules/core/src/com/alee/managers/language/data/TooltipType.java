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

package com.alee.managers.language.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Mikle Garin
 */

@XStreamAlias ("TooltipType")
public enum TooltipType
{
    /**
     * Default Swing tooltip.
     * It always appears near the mouse location and doesn't take its source position and bounds into account.
     */
    swing,

    /**
     * Custom WebLaF tooltip.
     * It has better styling and might be displayed in a specific direction from its source.
     * It will take source position, bounds and some other things into account.
     * It can also display tooltips provided through the {@link com.alee.managers.hotkey.HotkeyManager}.
     */
    weblaf
}