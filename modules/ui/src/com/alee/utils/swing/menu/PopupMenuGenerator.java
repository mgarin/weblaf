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

package com.alee.utils.swing.menu;

import com.alee.api.annotations.NotNull;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.managers.style.StyleId;

/**
 * Special generator that simplifies and shortens {@link WebPopupMenu} creation code.
 *
 * @author Mikle Garin
 * @see AbstractMenuGenerator
 */
public class PopupMenuGenerator extends AbstractMenuGenerator<WebPopupMenu>
{
    /**
     * Constructs new {@link PopupMenuGenerator} using default {@link WebPopupMenu}.
     */
    public PopupMenuGenerator ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link PopupMenuGenerator} using default {@link WebPopupMenu} and the specified {@link StyleId}.
     *
     * @param id {@link StyleId}
     */
    public PopupMenuGenerator ( @NotNull final StyleId id )
    {
        this ( new WebPopupMenu ( id ) );
    }

    /**
     * Constructs new {@link PopupMenuGenerator} using the specified {@link WebPopupMenu}.
     *
     * @param menu {@link WebPopupMenu}
     */
    public PopupMenuGenerator ( @NotNull final WebPopupMenu menu )
    {
        super ( menu );
    }
}