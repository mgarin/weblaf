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
import com.alee.api.annotations.Nullable;
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
     * Constructs new {@link MenuGenerator} using default {@link WebMenu}.
     */
    public MenuGenerator ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param icon {@link WebMenu} {@link Icon}
     */
    public MenuGenerator ( @Nullable final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param text {@link WebMenu} text
     */
    public MenuGenerator ( @Nullable final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param text {@link WebMenu} text
     * @param icon {@link WebMenu} {@link Icon}
     */
    public MenuGenerator ( @Nullable final String text, @Nullable final Icon icon )
    {
        this ( StyleId.auto, text, icon );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param action {@link WebMenu} {@link Action}
     */
    public MenuGenerator ( @NotNull final Action action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param id {@link StyleId}
     */
    public MenuGenerator ( @NotNull final StyleId id )
    {
        this ( id, null, null );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param id   {@link StyleId}
     * @param icon {@link WebMenu} {@link Icon}
     */
    public MenuGenerator ( @NotNull final StyleId id, @Nullable final Icon icon )
    {
        this ( id, null, icon );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param id   {@link StyleId}
     * @param text {@link WebMenu} text
     */
    public MenuGenerator ( @NotNull final StyleId id, @Nullable final String text )
    {
        this ( id, text, null );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param id   {@link StyleId}
     * @param text {@link WebMenu} text
     * @param icon {@link WebMenu} {@link Icon}
     */
    public MenuGenerator ( @NotNull final StyleId id, @Nullable final String text, @Nullable final Icon icon )
    {
        this ( new WebMenu ( id, text, icon ) );
    }

    /**
     * Constructs new {@link MenuGenerator} using {@link WebMenu} with the specified settings.
     *
     * @param id     {@link StyleId}
     * @param action {@link WebMenu} {@link Action}
     */
    public MenuGenerator ( @NotNull final StyleId id, @NotNull final Action action )
    {
        this ( new WebMenu ( id, action ) );
    }

    /**
     * Constructs new {@link MenuGenerator} using the specified {@link WebMenu}.
     *
     * @param menu {@link WebMenu}
     */
    public MenuGenerator ( @NotNull final WebMenu menu )
    {
        super ( menu );
    }

    @Override
    public void setLanguagePrefix ( @Nullable final String prefix )
    {
        // Perform default actions
        super.setLanguagePrefix ( prefix );

        // Update menu language if any prefix is set
        final WebMenu menu = getMenu ();
        final String text = menu.getLanguage ();
        final String languageKey = getLanguageKey ( text );
        if ( languageKey != null )
        {
            menu.setLanguage ( languageKey );
        }
        else
        {
            menu.removeLanguage ();
            menu.setText ( text );
        }
    }
}