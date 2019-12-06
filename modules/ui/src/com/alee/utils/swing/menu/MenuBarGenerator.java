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

import com.alee.laf.menu.WebMenuBar;
import com.alee.managers.style.StyleId;

/**
 * Special generator that simplifies and shortens {@link WebMenuBar} creation code.
 *
 * @author Mikle Garin
 * @see AbstractMenuGenerator
 */
public class MenuBarGenerator extends AbstractMenuGenerator<WebMenuBar>
{
    /**
     * Constructs new {@link MenuBarGenerator} using default {@link WebMenuBar}.
     */
    public MenuBarGenerator ()
    {
        this ( new WebMenuBar () );
    }

    /**
     * Constructs new {@link MenuBarGenerator} using {@link WebMenuBar} with the specified {@link StyleId}.
     *
     * @param id {@link WebMenuBar} {@link StyleId}
     */
    public MenuBarGenerator ( final StyleId id )
    {
        this ( new WebMenuBar ( id ) );
    }

    /**
     * Constructs new {@link MenuBarGenerator} using specified {@link WebMenuBar}.
     *
     * @param menu {@link WebMenuBar}
     */
    public MenuBarGenerator ( final WebMenuBar menu )
    {
        super ( menu );
    }
}