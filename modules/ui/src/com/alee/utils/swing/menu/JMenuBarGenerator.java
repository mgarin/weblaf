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

import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Special generator that simplifies and shortens {@link JMenuBar} creation code.
 *
 * @author Mikle Garin
 * @see AbstractMenuGenerator
 */
public class JMenuBarGenerator extends AbstractMenuGenerator<JMenuBar>
{
    /**
     * Constructs new menu bar generator using default menu bar.
     */
    public JMenuBarGenerator ()
    {
        this ( new JMenuBar () );
    }

    /**
     * Constructs new menu bar generator using menu bar with the specified style.
     *
     * @param id menu bar style ID
     */
    public JMenuBarGenerator ( final StyleId id )
    {
        this ( new JMenuBar () );
        id.set ( getMenu () );
    }

    /**
     * Constructs new menu bar generator using specified menu bar.
     *
     * @param menu menu bar
     */
    public JMenuBarGenerator ( final JMenuBar menu )
    {
        super ( menu );
    }
}