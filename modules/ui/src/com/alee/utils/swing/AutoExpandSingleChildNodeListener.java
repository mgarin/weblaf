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

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

/**
 * Tree expansion listener that automatically expands node futher if it has only one child.
 * Be aware that this listener is not suited for async trees.
 *
 * @author Mikle Garin
 */

public class AutoExpandSingleChildNodeListener implements TreeExpansionListener
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void treeExpanded ( final TreeExpansionEvent event )
    {
        final JTree tree = ( JTree ) event.getSource ();
        final TreePath expandedPath = event.getPath ();
        final Object expandedObject = expandedPath.getLastPathComponent ();
        final int childCount = tree.getModel ().getChildCount ( expandedObject );
        if ( childCount == 1 )
        {
            final Object child = tree.getModel ().getChild ( expandedObject, 0 );
            tree.expandPath ( new TreePath ( child ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void treeCollapsed ( final TreeExpansionEvent event )
    {
        // Do nothing
    }
}