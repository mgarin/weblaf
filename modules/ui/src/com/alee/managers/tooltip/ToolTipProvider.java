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

package com.alee.managers.tooltip;

import javax.swing.*;
import java.awt.*;

/**
 * WebLaF tooltip provider interface.
 * It defines methods used across all components.
 *
 * @author Mikle Garin
 */

public interface ToolTipProvider<T extends JComponent>
{
    /**
     * Returns tooltip display delay.
     * Any value below 1 will force tooltips to be displayed instantly.
     *
     * @return tooltip display delay
     */
    public long getDelay ();

    /**
     * Returns custom WebLaF tooltip source bounds.
     * Tooltip will be displayed relative to these bounds using provided TooltipWay.
     *
     * @param component  component to provide tooltip for
     * @param value      cell value
     * @param index      cell index
     * @param column     cell column index
     * @param isSelected whether the cell is selected or not
     * @return custom WebLaF tooltip source bounds
     */
    public Rectangle getSourceBounds ( T component, Object value, int index, int column, boolean isSelected );

    /**
     * Return custom WebLaF tooltip for the specified cell.
     *
     * @param component  component to provide tooltip for
     * @param value      cell value
     * @param index      cell index
     * @param column     cell column index
     * @param isSelected whether the cell is selected or not
     * @return cell tooltip
     */
    public WebCustomTooltip getToolTip ( T component, Object value, int index, int column, boolean isSelected );

    /**
     * Forces tooltip to update when hover cell changes.
     *
     * @param component component to provide tooltip for
     * @param oldIndex  old hover cell index
     * @param oldColumn old hover cell column
     * @param newIndex  new hover cell index
     * @param newColumn new hover cell column
     */
    public void hoverCellChanged ( final T component, final int oldIndex, final int oldColumn, final int newIndex, final int newColumn );
}