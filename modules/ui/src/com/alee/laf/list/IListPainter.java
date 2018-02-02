package com.alee.laf.list;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JList} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IListPainter<C extends JList, U extends WListUI> extends SpecificPainter<C, U>
{
    /**
     * Returns whether or not item hover decoration is supported by this list painter.
     * This
     *
     * @return {@code true} if item hover decoration is supported by this list painter, {@code false} otherwise
     */
    public boolean isItemHoverDecorationSupported ();

    /**
     * Prepares painter to pain list.
     *
     * @param layoutOrientation cached layout orientation
     * @param listHeight        cached list height
     * @param listWidth         cached list width
     * @param columnCount       cached column count
     * @param rowsPerColumn     cached rows per column amount
     * @param preferredHeight   cached preferred height
     * @param cellWidth         cached cell width
     * @param cellHeight        cached cell height
     * @param cellHeights       cached cell heights
     */
    public void prepareToPaint ( Integer layoutOrientation, Integer listHeight, Integer listWidth, Integer columnCount,
                                 Integer rowsPerColumn, Integer preferredHeight, int cellWidth, int cellHeight, int[] cellHeights );
}