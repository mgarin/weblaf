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
     * Constructs new {@link JMenuBarGenerator} using default {@link JMenuBar}.
     */
    public JMenuBarGenerator ()
    {
        this ( new JMenuBar () );
    }

    /**
     * Constructs new {@link JMenuBarGenerator} using {@link JMenuBar} with the specified {@link StyleId}.
     *
     * @param id {@link JMenuBar} {@link StyleId}
     */
    public JMenuBarGenerator ( final StyleId id )
    {
        this ( new JMenuBar () );
        id.set ( getMenu () );
    }

    /**
     * Constructs new {@link JMenuBarGenerator} using specified {@link JMenuBar}.
     *
     * @param menu {@link JMenuBar}
     */
    public JMenuBarGenerator ( final JMenuBar menu )
    {
        super ( menu );
    }
}