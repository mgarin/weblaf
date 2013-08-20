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

import java.awt.*;

/**
 * WebTable style class.
 *
 * @author Mikle Garin
 */

public final class WebTableStyle
{
    /**
     * Default header height.
     */
    public static int headerHeight = 20;

    /**
     * Default header margin.
     */
    public static Insets headerMargin = new Insets ( 0, 10, 1, 10 );

    /**
     * Default row height.
     */
    public static int rowHeight = 18;

    /**
     * Whether to show horizontal lines by default or not.
     */
    public static boolean showHorizontalLines = true;

    /**
     * Whether to show vertical lines by default or not.
     */
    public static boolean showVerticalLines = true;

    /**
     * Default spacing berween table cells
     */
    public static Dimension cellsSpacing = new Dimension ( 1, 1 );

    /**
     * Table selection background.
     */
    public static Color gridColor = new Color ( 176, 176, 176 );

    /**
     * Table selection background.
     */
    public static Color foreground = Color.BLACK;

    /**
     * Table selection background.
     */
    public static Color background = Color.WHITE;

    /**
     * Table selection background.
     */
    public static Color selectionForeground = Color.WHITE;

    /**
     * Table selection background.
     */
    public static Color selectionBackground = new Color ( 59, 115, 175 );

    /**
     * Clicks required to start table cell editing.
     */
    public static int clickCountToStartEdit = 2;
}