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

package com.alee.laf.list;

import javax.swing.*;
import javax.swing.plaf.basic.BasicListUI;

/**
 * Pluggable look and feel interface for {@link WebList} component.
 *
 * @author Mikle Garin
 */
public abstract class WListUI extends BasicListUI
{
    /**
     * Returns current mousover index.
     *
     * @return current mousover index
     */
    public abstract int getHoverIndex ();

    /**
     * Returns list selection style.
     *
     * @return list selection style
     */
    public abstract ListSelectionStyle getSelectionStyle ();

    /**
     * Sets list selection style.
     *
     * @param style list selection style
     */
    public abstract void setSelectionStyle ( ListSelectionStyle style );

    /**
     * Forces list to update all elements layout.
     */
    public abstract void updateListLayout ();

    /**
     * Returns tree cell renderer pane.
     *
     * @return tree cell renderer pane
     */
    public abstract CellRendererPane getCellRendererPane ();
}