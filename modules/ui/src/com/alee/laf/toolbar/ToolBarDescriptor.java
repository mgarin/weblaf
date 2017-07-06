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

package com.alee.laf.toolbar;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Custom descriptor for {@link JToolBar} component.
 *
 * @author Mikle Garin
 */

public final class ToolBarDescriptor extends AbstractComponentDescriptor<JToolBar>
{
    /**
     * Constructs new descriptor for {@link JToolBar} component.
     */
    public ToolBarDescriptor ()
    {
        super ( "toolbar", JToolBar.class, "ToolBarUI", WebToolBarUI.class, WebToolBarUI.class, StyleId.toolbar );
    }

    @Override
    public void updateUI ( final JToolBar component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Invalidating component
        component.invalidate ();
    }
}