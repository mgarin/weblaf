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

import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * Simple tree node painter based on {@link AbstractSectionDecorationPainter}.
 * It is used within {@link TreePainter} to paint nodes background.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class TreeNodePainter<C extends JTree, U extends WTreeUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements ITreeNodePainter<C, U>
{
    /**
     * Painted node row index.
     */
    protected transient Integer row;

    @Override
    public String getSectionId ()
    {
        return "node";
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        addRowStates ( states );
        return states;
    }

    /**
     * Adds states provided by this painter.
     *
     * @param states list to add states to
     */
    protected void addRowStates ( final List<String> states )
    {
        // Ensure row is specified
        if ( row != null )
        {
            // Ensure path exists
            final TreePath path = component.getPathForRow ( row );
            if ( path != null )
            {
                addPathStates ( states, path );
            }
        }
    }

    /**
     * Adds states provided by specified {@link TreePath}.
     *
     * @param states list to add states to
     * @param path   {@link TreePath} to provide states for
     */
    protected void addPathStates ( final List<String> states, final TreePath path )
    {
        // Adding row type
        addNumerationStates ( states, path );

        // Selection state
        states.add ( component.isRowSelected ( row ) ? DecorationState.selected : DecorationState.unselected );

        // Expansion state
        states.add ( component.isExpanded ( row ) ? DecorationState.expanded : DecorationState.collapsed );

        // Hover state
        if ( row == ui.getHoverRow () )
        {
            states.add ( DecorationState.hover );
        }
        else
        {
            // We have to remove it as the origin might have hover state
            states.remove ( DecorationState.hover );
        }

        // Adding possible extra node states
        final Object value = path.getLastPathComponent ();
        states.addAll ( DecorationUtils.getExtraStates ( value ) );
    }

    /**
     * Adds numeration states for the specified {@link TreePath}.
     *
     * @param states list to add states to
     * @param path   {@link TreePath} to provide numeration states for
     */
    protected void addNumerationStates ( final List<String> states, final TreePath path )
    {
        states.add ( row % 2 == 0 ? DecorationState.odd : DecorationState.even );
    }

    @Override
    public void prepareToPaint ( final int row )
    {
        this.row = row;
        updateDecorationState ();
    }
}