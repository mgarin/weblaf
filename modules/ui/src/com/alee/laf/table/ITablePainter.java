package com.alee.laf.table;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JTable} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ITablePainter<C extends JTable, U extends WebTableUI> extends SpecificPainter<C, U>
{
    /**
     * Prepares painter to paint table.
     *
     * @param rendererPane renderer pane
     */
    public void prepareToPaint ( CellRendererPane rendererPane );
}