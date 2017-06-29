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

package com.alee.laf.menu;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Custom descriptor for {@link JMenu} component.
 *
 * @author Mikle Garin
 */

public final class MenuDescriptor extends AbstractComponentDescriptor<JMenu>
{
    /**
     * Constructs new descriptor for {@link JMenu} component.
     */
    public MenuDescriptor ()
    {
        super ( "menu", JMenu.class, "MenuUI", WebMenuUI.class, WebMenuUI.class, StyleId.menu );
    }

    @Override
    public void updateUI ( final JMenu component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Updating submenu UI
        final JPopupMenu submenu = component.getPopupMenu ();
        if ( submenu != null )
        {
            submenu.updateUI ();
        }
    }
}