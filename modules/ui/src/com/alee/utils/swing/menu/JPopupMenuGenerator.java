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
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Special generator that simplifies and shortens {@link JPopupMenu} creation code.
 *
 * @author Mikle Garin
 * @see AbstractMenuGenerator
 */
public class JPopupMenuGenerator extends AbstractMenuGenerator<JPopupMenu>
{
    /**
     * Constructs new {@link JPopupMenuGenerator} using default {@link JPopupMenu}.
     */
    public JPopupMenuGenerator ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link JPopupMenuGenerator} using {@link JPopupMenu} with the specified {@link StyleId}.
     *
     * @param id {@link JPopupMenu} {@link StyleId}
     */
    public JPopupMenuGenerator ( @NotNull final StyleId id )
    {
        this ( new JPopupMenu () );
        id.set ( getMenu () );
    }

    /**
     * Constructs new {@link JPopupMenuGenerator} using the specified {@link JPopupMenu}.
     *
     * @param menu {@link JPopupMenu}
     */
    public JPopupMenuGenerator ( @NotNull final JPopupMenu menu )
    {
        super ( menu );
    }
}