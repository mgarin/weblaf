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

package com.alee.extended.layout;

import java.awt.*;

/**
 * Custom layout based on {@link java.awt.CardLayout} that doesn't count invisible children sizes in preferred and minimum size methods.
 *
 * @author Mikle Garin
 */
public class PreferredCardLayout extends CardLayout
{
    /**
     * Determines the preferred size of the container argument using this card layout.
     *
     * @param container the parent container in which to do the layout
     * @return the preferred dimensions to lay out the subcomponents of the specified container
     * @see java.awt.Container#getPreferredSize()
     * @see java.awt.CardLayout#minimumLayoutSize(java.awt.Container)
     */
    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        synchronized ( container.getTreeLock () )
        {
            final Insets insets = container.getInsets ();
            final int ncomponents = container.getComponentCount ();
            int w = 0;
            int h = 0;

            for ( int i = 0; i < ncomponents; i++ )
            {
                final Component component = container.getComponent ( i );
                if ( component.isVisible () )
                {
                    final Dimension d = component.getPreferredSize ();
                    if ( d.width > w )
                    {
                        w = d.width;
                    }
                    if ( d.height > h )
                    {
                        h = d.height;
                    }
                }
            }
            return new Dimension ( insets.left + insets.right + w + getHgap () * 2, insets.top + insets.bottom + h + getVgap () * 2 );
        }
    }

    /**
     * Calculates the minimum size for the specified panel.
     *
     * @param container the parent container in which to do the layout
     * @return the minimum dimensions required to lay out the subcomponents of the specified container
     * @see java.awt.Container#doLayout()
     * @see java.awt.CardLayout#preferredLayoutSize(java.awt.Container)
     */
    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        synchronized ( container.getTreeLock () )
        {
            final Insets insets = container.getInsets ();
            final int ncomponents = container.getComponentCount ();
            int w = 0;
            int h = 0;

            for ( int i = 0; i < ncomponents; i++ )
            {
                final Component component = container.getComponent ( i );
                if ( component.isVisible () )
                {
                    final Dimension d = component.getMinimumSize ();
                    if ( d.width > w )
                    {
                        w = d.width;
                    }
                    if ( d.height > h )
                    {
                        h = d.height;
                    }
                }
            }
            return new Dimension ( insets.left + insets.right + w + getHgap () * 2, insets.top + insets.bottom + h + getVgap () * 2 );
        }
    }
}