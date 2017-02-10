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
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Special generator that simplifies and shortens {@link WebMenu} creation code.
 *
 * @author Mikle Garin
 * @see AbstractMenuGenerator
 */

public class MenuGenerator extends AbstractMenuGenerator<WebMenu>
{
    /**
     * Constructs new menu generator using default menu.
     */
    public MenuGenerator ()
    {
        this ( new WebMenu () );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param icon menu icon
     */
    public MenuGenerator ( final Icon icon )
    {
        this ( new WebMenu ( icon ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param text menu text
     */
    public MenuGenerator ( final String text )
    {
        this ( new WebMenu ( text ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param action menu action
     */
    public MenuGenerator ( final Action action )
    {
        this ( new WebMenu ( action ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param text menu text
     * @param icon menu item icon
     */
    public MenuGenerator ( final String text, final Icon icon )
    {
        this ( new WebMenu ( text, icon ) );
    }

    /**
     * Constructs new menu.
     *
     * @param id style ID
     */
    public MenuGenerator ( final StyleId id )
    {
        this ( new WebMenu ( id ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id   style ID
     * @param icon menu icon
     */
    public MenuGenerator ( final StyleId id, final Icon icon )
    {
        this ( new WebMenu ( id, icon ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id   style ID
     * @param text menu text
     */
    public MenuGenerator ( final StyleId id, final String text )
    {
        this ( new WebMenu ( id, text ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id     style ID
     * @param action menu action
     */
    public MenuGenerator ( final StyleId id, final Action action )
    {
        this ( new WebMenu ( id, action ) );
    }

    /**
     * Constructs new menu generator using menu with the specified settings.
     *
     * @param id   style ID
     * @param text menu text
     * @param icon menu item icon
     */
    public MenuGenerator ( final StyleId id, final String text, final Icon icon )
    {
        this ( new WebMenu ( id, text, icon ) );
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

    @Override
    public void setLanguagePrefix ( final String prefix )
    {
        // Perform default actions
        super.setLanguagePrefix ( prefix );

        // Update menu language if any prefix is set
        final String text = getMenu ().getLanguage ();
        getMenu ().setLanguage ( getLanguageKey ( text ) );
    }
}