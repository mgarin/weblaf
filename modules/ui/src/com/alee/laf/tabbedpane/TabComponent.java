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

import javax.swing.*;

/**
 * Interface that can be implemented by custom {@link Tab} components to provide additional information.
 * Currently this is used by {@link TabMenuButton} to generate accurate menu for all available {@link Tab}s.
 *
 * @author Mikle Garin
 */
public interface TabComponent
{
    /**
     * Returns {@link Tab} component {@link Icon}.
     *
     * @return {@link Tab} component {@link Icon}
     */
    public Icon getIcon ();

    /**
     * Returns {@link Tab} component title.
     *
     * @return {@link Tab} component title
     */
    public String getTitle ();
}