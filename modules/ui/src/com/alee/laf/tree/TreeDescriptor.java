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

package com.alee.laf.tree;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Custom descriptor for {@link JTree} component.
 *
 * @author Mikle Garin
 */

public final class TreeDescriptor extends AbstractComponentDescriptor<JTree>
{
    /**
     * Constructs new descriptor for {@link JTree} component.
     */
    public TreeDescriptor ()
    {
        super ( "tree", JTree.class, "TreeUI", WTreeUI.class, WebTreeUI.class, StyleId.tree );
    }

    @Override
    public void updateUI ( final JTree component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Updating renderer UI
        final TreeCellRenderer renderer = component.getCellRenderer ();
        if ( renderer instanceof Component )
        {
            SwingUtilities.updateComponentTreeUI ( ( Component ) renderer );
        }

        // Updating editor UI
        final TreeCellEditor editor = component.getCellEditor ();
        if ( editor instanceof Component )
        {
            SwingUtilities.updateComponentTreeUI ( ( Component ) editor );
        }
        else if ( editor instanceof DefaultCellEditor )
        {
            final Component editorComponent = ( ( DefaultCellEditor ) editor ).getComponent ();
            if ( editorComponent != null )
            {
                SwingUtilities.updateComponentTreeUI ( editorComponent );
            }
        }
    }
}