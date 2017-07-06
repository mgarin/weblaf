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

import com.alee.painter.decoration.*;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * Simple tree node painter based on {@link AbstractSectionDecorationPainter}.
 * It is used within {@link TreePainter} to paint nodes background.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class TreeNodePainter<E extends JTree, U extends WTreeUI, D extends IDecoration<E, D>>
        extends AbstractSectionDecorationPainter<E, U, D> implements ITreeNodePainter<E, U>
{
    /**
     * Painted node row index.
     */
    protected transient Integer row;

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Checking setings initialization
        if ( row != null )
        {
            final TreePath path = component.getPathForRow ( row );
            if ( path != null )
            {
                // Adding row type
                states.add ( row % 2 == 0 ? DecorationState.odd : DecorationState.even );

                // Adding common node states
                states.add ( component.isRowSelected ( row ) ? DecorationState.selected : DecorationState.unselected );
                states.add ( component.isExpanded ( row ) ? DecorationState.expanded : DecorationState.collapsed );

                // Adding possible node states
                final Object value = path.getLastPathComponent ();
                states.addAll ( DecorationUtils.getExtraStates ( value ) );
            }
        }

        return states;
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @Override
    public void prepareToPaint ( final int row )
    {
        this.row = row;
        updateDecorationState ();
    }
}