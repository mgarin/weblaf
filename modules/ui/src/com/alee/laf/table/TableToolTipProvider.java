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

package com.alee.laf.table;

import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.AbstractToolTipProvider;

import java.awt.*;

/**
 * Abstract WebLaF tooltip provider for WebTable component.
 *
 * @author Mikle Garin
 */

public abstract class TableToolTipProvider extends AbstractToolTipProvider<WebTable>
{
    @Override
    public Rectangle getSourceBounds ( final WebTable table, final Object value, final int index, final int column,
                                       final boolean isSelected )
    {
        final Rectangle bounds = table.getCellRect ( index, column, false );
        return bounds.intersection ( table.getVisibleRect () );
    }

    @Override
    public TooltipWay getDirection ( final WebTable component, final Object value, final int index, final int column,
                                     final boolean isSelected )
    {
        return TooltipWay.down;
    }

    @Override
    protected Object getValue ( final WebTable table, final int index, final int column )
    {
        return table.getValueAt ( index, column );
    }

    @Override
    protected boolean isSelected ( final WebTable table, final int index, final int column )
    {
        return table.isCellSelected ( index, column );
    }
}