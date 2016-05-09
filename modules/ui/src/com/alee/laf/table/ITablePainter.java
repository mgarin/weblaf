package com.alee.laf.table;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JTable component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITablePainter<E extends JTable, U extends WebTableUI> extends SpecificPainter<E, U>
{
    /**
     * Prepares painter to paint table.
     *
     * @param rendererPane renderer pane
     */
    public void prepareToPaint ( CellRendererPane rendererPane );
}