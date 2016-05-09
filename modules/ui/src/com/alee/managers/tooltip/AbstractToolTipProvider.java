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

import com.alee.managers.language.data.TooltipWay;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Abstract WebLaF tooltip provider which defines base methods used across all components.
 *
 * @author Mikle Garin
 */

public abstract class AbstractToolTipProvider<T extends JComponent> implements ToolTipProvider<T>
{
    /**
     * Last displayed tooltip.
     */
    private WebCustomTooltip tooltip;

    /**
     * Delayed tooltip display timer.
     */
    private WebTimer delayTimer;

    @Override
    public long getDelay ()
    {
        return TooltipManager.getDefaultDelay ();
    }

    @Override
    public WebCustomTooltip getToolTip ( final T component, final Object value, final int index, final int column,
                                         final boolean isSelected )
    {
        final String text = getToolTipText ( component, value, index, column, isSelected );
        final TooltipWay direction = getDirection ( component, value, index, column, isSelected );
        return new WebCustomTooltip ( component, text, direction );
    }

    /**
     * Returns custom tooltip direction based on cell value.
     *
     * @param component  component to provide tooltip for
     * @param value      cell value
     * @param index      cell index
     * @param column     cell column index
     * @param isSelected whether the cell is selected or not
     * @return custom tooltip direction based on cell value
     */
    public TooltipWay getDirection ( final T component, final Object value, final int index, final int column, final boolean isSelected )
    {
        return TooltipWay.trailing;
    }

    /**
     * Returns custom cell tooltip text based on cell value.
     *
     * @param component  component to provide tooltip for
     * @param value      cell value
     * @param index      cell index
     * @param column     cell column index
     * @param isSelected whether the cell is selected or not
     * @return custom cell tooltip text based on cell value
     */
    public abstract String getToolTipText ( final T component, final Object value, final int index, final int column,
                                            final boolean isSelected );

    @Override
    public void hoverCellChanged ( final T component, final int oldIndex, final int oldColumn, final int newIndex, final int newColumn )
    {
        // Close previously displayed tooltip
        if ( delayTimer != null )
        {
            delayTimer.stop ();
        }
        if ( tooltip != null )
        {
            tooltip.closeTooltip ();
        }

        // Display or delay new tooltip if needed
        if ( newIndex != -1 && newColumn != -1 )
        {
            final long delay = getDelay ();
            if ( delay <= 0 )
            {
                showTooltip ( component, newIndex, newColumn );
            }
            else
            {
                delayTimer = WebTimer.delay ( delay, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        showTooltip ( component, newIndex, newColumn );
                    }
                } );
            }
        }
    }

    /**
     * Displays custom tooltip for the specified component cell.
     *
     * @param component component to display tooltip for
     * @param index     cell index
     * @param column    cell column index
     */
    protected void showTooltip ( final T component, final int index, final int column )
    {
        // Retrieving information about cell
        final Object value = getValue ( component, index, column );
        final boolean selected = isSelected ( component, index, column );

        // Retrieving tooltip component
        tooltip = getToolTip ( component, value, index, column, selected );

        // Updating tooltip bounds
        tooltip.setRelativeToBounds ( getSourceBounds ( component, value, index, column, selected ) );

        // Displaying one-time tooltip
        TooltipManager.showOneTimeTooltip ( tooltip );
    }

    /**
     * Returns component cell value under the specified index and column.
     *
     * @param component component to retrieve cell value for
     * @param index     cell index
     * @param column    cell column index
     * @return component cell value under the specified index and column
     */
    protected abstract Object getValue ( final T component, final int index, final int column );

    /**
     * Returns whether or not component cell is selected.
     *
     * @param component component to retrieve cell selection state for
     * @param index     cell index
     * @param column    cell column index
     * @return true if component cell is selected, false otherwise
     */
    protected abstract boolean isSelected ( final T component, final int index, final int column );
}