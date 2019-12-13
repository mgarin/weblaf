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

import com.alee.api.annotations.Nullable;
import com.alee.painter.PaintParameters;

/**
 * {@link PaintParameters} for {@link IListPainter}.
 * todo This should be removed once proper list UI is implemented
 *
 * @author Mikle Garin
 */
public class ListPaintParameters implements PaintParameters
{
    /**
     * Cached list width.
     */
    public final int listWidth;

    /**
     * Cached list height.
     */
    public final int listHeight;

    /**
     * Cached column count.
     */
    public final int columnCount;

    /**
     * Cached rows per column amount.
     */
    public final int rowsPerColumn;

    /**
     * Cached preferred height.
     */
    public final int preferredHeight;

    /**
     * Cached cell width.
     */
    public final int cellWidth;

    /**
     * Cached cell height.
     */
    public final int cellHeight;

    /**
     * Cached cell heights.
     */
    @Nullable
    public final int[] cellHeights;

    /**
     * Constructs new {@link ListPaintParameters}.
     *
     * @param listWidth         cached list width
     * @param listHeight        cached list height
     * @param columnCount       cached column count
     * @param rowsPerColumn     cached rows per column amount
     * @param preferredHeight   cached preferred height
     * @param cellWidth         cached cell width
     * @param cellHeight        cached cell height
     * @param cellHeights       cached cell heights
     */
    public ListPaintParameters ( final int listWidth, final int listHeight, final int columnCount, final int rowsPerColumn,
                                 final int preferredHeight, final int cellWidth, final int cellHeight, @Nullable final int[] cellHeights )
    {
        this.listWidth = listWidth;
        this.listHeight = listHeight;
        this.columnCount = columnCount;
        this.rowsPerColumn = rowsPerColumn;
        this.preferredHeight = preferredHeight;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellHeights = cellHeights;
    }
}