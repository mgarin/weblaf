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

package com.alee.laf.tabbedpane;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Enumeration representing differen tab size stretch types.
 * Different settings allow tabs to grow in size to fill-in all available space on the {@link TabContainer}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "TabStretchType" )
public enum TabStretchType
{
    /**
     * Disables tabs stretching for {@link javax.swing.JTabbedPane#WRAP_TAB_LAYOUT}.
     * Disables tabs stretching for {@link javax.swing.JTabbedPane#SCROLL_TAB_LAYOUT}.
     */
    never,

    /**
     * Enables tabs stretching for {@link javax.swing.JTabbedPane#WRAP_TAB_LAYOUT} when there is more than one tab run.
     * Disables tabs stretching for {@link javax.swing.JTabbedPane#SCROLL_TAB_LAYOUT}.
     */
    multiline,

    /**
     * Enables tabs stretching for {@link javax.swing.JTabbedPane#WRAP_TAB_LAYOUT}.
     * Enables tabs stretching for {@link javax.swing.JTabbedPane#SCROLL_TAB_LAYOUT} only when they fit into visible area.
     */
    always
}