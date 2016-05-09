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

package com.alee.laf.tree;

/**
 * Tree selection styles enumeration.
 *
 * @author Mikle Garin
 */

public enum TreeSelectionStyle
{
    /**
     * Disable custom tree selection painting.
     * With this selection style set selection painting will be up to tree renderer.
     */
    none,

    /**
     * Single cell selection style.
     * With this selection style tree will paint separate selection under each selected cell.
     */
    single,

    /**
     * Group cell selection style.
     * With this selection style tree will paint single selection for each group of nodes that are located close to each other.
     */
    group,

    /**
     * Line cell selection style.
     * With this selection style tree will paint selection over the whole line in which node is placed.
     * Selected lines located near each other will also be visually grouped.
     */
    line
}