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
     * Constructs new {@link JMenuGenerator} using default {@link JMenu}.
     */
    public JMenuGenerator ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param icon {@link JMenu} icon
     */
    public JMenuGenerator ( @Nullable final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param text {@link JMenu} text
     */
    public JMenuGenerator ( @Nullable final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param text {@link JMenu} text
     * @param icon {@link JMenu} item icon
     */
    public JMenuGenerator ( @Nullable final String text, @Nullable final Icon icon )
    {
        this ( StyleId.auto, text, icon );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param action {@link JMenu} action
     */
    public JMenuGenerator ( @NotNull final Action action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new {@link JMenu}.
     *
     * @param id {@link StyleId}
     */
    public JMenuGenerator ( @NotNull final StyleId id )
    {
        this ( id, null, null );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param id   {@link StyleId}
     * @param icon {@link JMenu} icon
     */
    public JMenuGenerator ( @NotNull final StyleId id, @Nullable final Icon icon )
    {
        this ( id, null, icon );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param id   {@link StyleId}
     * @param text {@link JMenu} text
     */
    public JMenuGenerator ( @NotNull final StyleId id, @Nullable final String text )
    {
        this ( id, text, null );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param id   {@link StyleId}
     * @param text {@link JMenu} text
     * @param icon {@link JMenu} item icon
     */
    public JMenuGenerator ( @NotNull final StyleId id, @Nullable final String text, @Nullable final Icon icon )
    {
        this ( new JMenu ( text ) );
        getMenu ().setIcon ( icon );
        id.set ( getMenu () );
    }

    /**
     * Constructs new {@link JMenuGenerator} using {@link JMenu} with the specified settings.
     *
     * @param id     {@link StyleId}
     * @param action {@link JMenu} action
     */
    public JMenuGenerator ( @NotNull final StyleId id, @NotNull final Action action )
    {
        this ( new JMenu ( action ) );
        id.set ( getMenu () );
    }

    /**
     * Constructs new {@link JMenuGenerator} using the specified {@link JMenu}.
     *
     * @param menu {@link JMenu}
     */
    public JMenuGenerator ( @NotNull final JMenu menu )
    {
        super ( menu );
    }

    @Override
    public void setLanguagePrefix ( final String prefix )
    {
        // Perform default actions
        super.setLanguagePrefix ( prefix );

        // Update menu language if any prefix is set
        final JMenu menu = getMenu ();
        final String text = menu.getText ();
        final String languageKey = getLanguageKey ( text );
        if ( languageKey != null )
        {
            UILanguageManager.registerComponent ( menu, languageKey );
        }
        else
        {
            UILanguageManager.unregisterComponent ( menu );
            menu.setText ( text );
        }
    }
}