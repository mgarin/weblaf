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

import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Special generator that simplifies and shortens {@link JMenu} creation code.
 *
 * @author Mikle Garin
 * @see AbstractMenuGenerator
 */

public class JMenuGenerator extends AbstractMenuGenerator<JMenu>
{
    /**
     * Constructs new menu generator using default menu.
     */
    public JMenuGenerator ()
    {
        this ( new JMenu () );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param icon menu icon
     */
    public JMenuGenerator ( final Icon icon )
    {
        this ( new JMenu () );
        getMenu ().setIcon ( icon );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param text menu text
     */
    public JMenuGenerator ( final String text )
    {
        this ( new JMenu ( text ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param action menu action
     */
    public JMenuGenerator ( final Action action )
    {
        this ( new JMenu ( action ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param text menu text
     * @param icon menu item icon
     */
    public JMenuGenerator ( final String text, final Icon icon )
    {
        this ( new JMenu ( text ) );
        getMenu ().setIcon ( icon );
    }

    /**
     * Constructs new menu.
     *
     * @param id style ID
     */
    public JMenuGenerator ( final StyleId id )
    {
        this ( new JMenu () );
        id.set ( getMenu () );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id   style ID
     * @param icon menu icon
     */
    public JMenuGenerator ( final StyleId id, final Icon icon )
    {
        this ( new JMenu () );
        getMenu ().setIcon ( icon );
        id.set ( getMenu () );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id   style ID
     * @param text menu text
     */
    public JMenuGenerator ( final StyleId id, final String text )
    {
        this ( new JMenu ( text ) );
        id.set ( getMenu () );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id     style ID
     * @param action menu action
     */
    public JMenuGenerator ( final StyleId id, final Action action )
    {
        this ( new JMenu ( action ) );
        id.set ( getMenu () );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id   style ID
     * @param text menu text
     * @param icon menu item icon
     */
    public JMenuGenerator ( final StyleId id, final String text, final Icon icon )
    {
        this ( new JMenu ( text ) );
        getMenu ().setIcon ( icon );
        id.set ( getMenu () );
    }

    /**
     * Constructs new menu generator using the specified menu.
     *
     * @param menu menu
     */
    public JMenuGenerator ( final JMenu menu )
    {
        super ( menu );
    }

    @Override
    public void setLanguagePrefix ( final String prefix )
    {
        // Perform default actions
        super.setLanguagePrefix ( prefix );

        // Update menu language if any prefix is set
        final String text = getMenu ().getText ();
        UILanguageManager.registerComponent ( getMenu (), getLanguageKey ( text ) );
    }
}