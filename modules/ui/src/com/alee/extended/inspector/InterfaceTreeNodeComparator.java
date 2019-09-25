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

package com.alee.extended.inspector;

import com.alee.api.annotations.NotNull;

import java.awt.*;
import java.util.Comparator;

/**
 * Special comparator for keeping {@link InterfaceTreeNode}s in a strict order.
 *
 * @author Mikle Garin
 */
public class InterfaceTreeNodeComparator implements Comparator<InterfaceTreeNode>
{
    @Override
    public int compare ( @NotNull final InterfaceTreeNode node1, @NotNull final InterfaceTreeNode node2 )
    {
        final Component component1 = node1.getUserObject ();
        final Component component2 = node2.getUserObject ();

        final int result;
        if ( component1 instanceof Window && component2 instanceof Window )
        {
            // No point to compare windows
            result = 0;
        }
        else if ( component1 != null && component2 != null )
        {
            // Checking that parent is the same
            final Container parent1 = component1.getParent ();
            final Container parent2 = component2.getParent ();
            if ( parent1 == null || parent2 == null || parent1 == parent2 )
            {
                // Comparing component's Z-index
                final int zIndex1 = parent1 != null ? parent1.getComponentZOrder ( component1 ) : 0;
                final int zIndex2 = parent2 != null ? parent2.getComponentZOrder ( component2 ) : 0;
                result = new Integer ( zIndex2 ).compareTo ( zIndex1 );
            }
            else
            {
                throw new RuntimeException ( "Compared nodes cannot have different parent" );
            }
        }
        else
        {
            throw new RuntimeException ( "Compared nodes cannot have null components" );
        }
        return result;
    }
}