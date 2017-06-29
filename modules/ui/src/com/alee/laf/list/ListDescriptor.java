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

package com.alee.laf.list;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;

/**
 * Custom descriptor for {@link JList} component.
 *
 * @author Mikle Garin
 */

public final class ListDescriptor extends AbstractComponentDescriptor<JList>
{
    /**
     * Constructs new descriptor for {@link JList} component.
     */
    public ListDescriptor ()
    {
        super ( "list", JList.class, "ListUI", WListUI.class, WebListUI.class, StyleId.list );
    }

    @Override
    public void updateUI ( final JList component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Updating renderer UI
        final ListCellRenderer renderer = component.getCellRenderer ();
        if ( renderer instanceof Component )
        {
            SwingUtilities.updateComponentTreeUI ( ( Component ) renderer );
        }
    }
}