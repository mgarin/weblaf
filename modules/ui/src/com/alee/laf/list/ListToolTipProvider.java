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

import com.alee.managers.tooltip.AbstractToolTipProvider;

import java.awt.*;

/**
 * Abstract WebLaF tooltip provider for WebList component.
 *
 * @author Mikle Garin
 */

public abstract class ListToolTipProvider extends AbstractToolTipProvider<WebList>
{
    @Override
    public Rectangle getSourceBounds ( final WebList list, final Object value, final int index, final int column, final boolean isSelected )
    {
        final Rectangle bounds = list.getCellBounds ( index, index );
        return bounds.intersection ( list.getVisibleRect () );
    }

    @Override
    protected Object getValue ( final WebList list, final int index, final int column )
    {
        return list.getValueAt ( index );
    }

    @Override
    protected boolean isSelected ( final WebList list, final int index, final int column )
    {
        return list.isSelectedIndex ( index );
    }
}