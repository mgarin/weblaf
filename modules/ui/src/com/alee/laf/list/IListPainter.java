package com.alee.laf.list;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JList component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IListPainter<E extends JList, U extends WebListUI> extends SpecificPainter<E, U>
{
    /**
     * Returns whether or not hover item decoration is supported by this list painter.
     *
     * @return true if hover item decoration is supported by this list painter, false otherwise
     */
    public boolean isHoverDecorationSupported ();

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