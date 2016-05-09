package com.alee.laf.table;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Base interface for JTableHeader component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends SpecificPainter<E, U>
{
    /**
     * Prepares painter to paint table header.
     *
     * @param rendererPane renderer pane
     */
    public void prepareToPaint ( CellRendererPane rendererPane );
}