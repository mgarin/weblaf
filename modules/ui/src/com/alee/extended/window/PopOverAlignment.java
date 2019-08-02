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

package com.alee.extended.window;

/**
 * @author Mikle Garin
 */
public enum PopOverAlignment
{
    /**
     * Determines that WebPopOver should be displayed more to the leading side relative to invoker when possible.
     * This means that dropdown style corner will usually placed closer to right/bottom side of the WebPopOver.
     */
    leading,

    /**
     * Determines that WebPopOver should be centered relative to WebPopOver display source point.
     * This means that dropdown style corner will usually placed in the middle of the WebPopOver.
     */
    centered,

    /**
     * Determines that WebPopOver should be displayed more to the trailing side relative to invoker when possible.
     * This means that dropdown style corner will usually placed closer to left/top side of the WebPopOver.
     */
    trailing
}