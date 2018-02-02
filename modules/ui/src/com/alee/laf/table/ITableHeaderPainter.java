package com.alee.laf.table;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Base interface for {@link JTableHeader} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITableHeaderPainter<C extends JTableHeader, U extends WebTableHeaderUI> extends SpecificPainter<C, U>
{
    /**
     * Prepares painter to paint table header.
     *
     * @param rendererPane renderer pane
     */
    public void prepareToPaint ( CellRendererPane rendererPane );
}