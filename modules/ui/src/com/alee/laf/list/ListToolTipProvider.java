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

import com.alee.managers.tooltip.WebCustomTooltip;

import javax.swing.*;
import java.awt.*;

/**
 * Custom WebLaF tooltip provider for WebList component.
 *
 * @author Mikle Garin
 */

public interface ListToolTipProvider
{
    /**
     * Returns tooltip display delay.
     * Any value below 1 will force tooltips to be displayed instantly.
     *
     * @return tooltip display delay
     */
    public long getDelay ();

    /**
     * Return custom WebLaF tooltip for the specified list cell.
     *
     * @param list       list to provide tooltip for
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether the cell is selected or not
     * @return list cell tooltip
     */
    public WebCustomTooltip getListCellToolTip ( JList list, Object value, int index, boolean isSelected );

    /**
     * Returns custom WebLaF tooltip source bounds.
     * Tooltip will be displayed relative to these bounds using provided TooltipWay.
     *
     * @param list       list to provide tooltip for
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether the cell is selected or not
     * @return custom WebLaF tooltip source bounds
     */
    public Rectangle getTooltipSourceBounds ( JList list, Object value, int index, boolean isSelected );
}