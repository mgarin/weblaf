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

import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * Custom tree row painter based on {@link TreeRowPainter}.
 * It provides separate even/odd states for first level groups and all sub-elements.
 * It also provides extra even/odd states for all elements within one group.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class GroupedTreeRowPainter<C extends JTree, U extends WTreeUI, D extends IDecoration<C, D>> extends TreeRowPainter<C, U, D>
{
    @Override
    protected void addNumerationStates ( final List<String> states, final TreePath path )
    {
        // Ensure it is not root and that we are working with nodes
        final Object value = path.getLastPathComponent ();
        final Object root = component.getModel ().getRoot ();
        if ( value instanceof TreeNode && root instanceof TreeNode && value != root )
        {
            // Finding out first level node in path
            final TreeNode rootNode = ( TreeNode ) root;
            TreeNode firstLevel = ( TreeNode ) value;
            TreeNode parent = firstLevel.getParent ();
            while ( parent != rootNode && parent != null )
            {
                firstLevel = parent;
                parent = firstLevel.getParent ();
            }

            // Calculating general even/odd state
            final int indexOnFirstLevel = rootNode.getIndex ( firstLevel );
            final boolean odd = indexOnFirstLevel % 2 == 0;
            states.add ( odd ? DecorationState.odd : DecorationState.even );

            // Calculating extra even/odd state
            final int innerIndex = row - component.getRowForPath ( new TreePath ( TreeUtils.getPath ( firstLevel ) ) );
            final boolean innerOdd = innerIndex % 2 == 0;
            states.add ( innerOdd ? DecorationState.innerOdd : DecorationState.innerEven );
        }
        else
        {
            // Using default implementation
            super.addNumerationStates ( states, path );
        }
    }
}