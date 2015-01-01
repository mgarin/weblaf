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

import com.alee.laf.menu.WebMenu;

/**
 * Special generator that simplifies and shortens menu creation code.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.menu.AbstractMenuGenerator
 */

public class MenuGenerator extends AbstractMenuGenerator<WebMenu>
{
    /**
     * Constructs new menu generator using default menu.
     */
    public MenuGenerator ()
    {
        super ( new WebMenu () );
    }

    /**
     * Constructs new menu generator using the specified menu.
     *
     * @param menu menu
     */
    public MenuGenerator ( final WebMenu menu )
    {
        super ( menu );
    }
}