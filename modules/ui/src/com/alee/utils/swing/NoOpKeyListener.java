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

package com.alee.utils.swing;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * This class provides a no-op {@link java.awt.event.KeyListener}.
 * It can be used in case you need to block all key events from passing to underlying components.
 *
 * @author Mikle Garin
 */
public final class NoOpKeyListener extends KeyAdapter
{
    /**
     * Single {@link NoOpKeyListener} instance.
     */
    @Nullable
    private static NoOpKeyListener instance;

    /**
     * Returns {@link NoOpKeyListener} instance.
     *
     * @return {@link NoOpKeyListener} instance
     */
    @NotNull
    public static NoOpKeyListener get ()
    {
        if ( instance == null )
        {
            instance = new NoOpKeyListener ();
        }
        return instance;
    }

    /**
     * Made private to avoid unnecessary instance creation.
     */
    private NoOpKeyListener ()
    {
        super ();
    }

    /**
     * Installs {@link NoOpKeyListener} into the specified {@link Component}.
     *
     * @param component {@link Component} to install {@link NoOpKeyListener} into
     */
    public static void install ( @NotNull final Component component )
    {
        component.addKeyListener ( get () );
    }
}