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

package com.alee.laf.menu;

import javax.swing.plaf.basic.BasicPopupMenuUI;

/**
 * Pluggable look and feel interface for {@link WebPopupMenu} component.
 *
 * @author Mikle Garin
 */
public abstract class WPopupMenuUI extends BasicPopupMenuUI
{
    /**
     * Assists popup menu to allow it choose the best position relative to invoker.
     * Its value nullified right after first usage to avoid popup menu display issues in future.
     *
     * @param way approximate popup menu display way
     */
    public abstract void setPopupMenuWay ( PopupMenuWay way );

    /**
     * Returns currently set preferred popup menu display way.
     * It might be null in case menu was just shown and this value wasn't updated afterwards.
     *
     * @return currently set preferred popup menu display way
     */
    public abstract PopupMenuWay getPopupMenuWay ();
}