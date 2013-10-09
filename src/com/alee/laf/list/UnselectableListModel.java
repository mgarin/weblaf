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

import javax.swing.*;

/**
 * Custom list selection model that disables empty selection.
 *
 * @author Mikle Garin
 */

public class UnselectableListModel extends DefaultListSelectionModel
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void clearSelection ()
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSelectionInterval ( int index0, int index1 )
    {
        // Check that at least some selection will be left after this action
        if ( index0 > getMinSelectionIndex () || index1 < getMaxSelectionIndex () )
        {
            super.removeSelectionInterval ( index0, index1 );
        }
    }
}