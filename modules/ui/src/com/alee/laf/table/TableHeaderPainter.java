package com.alee.laf.table;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Base interface for JTableHeader component painters.
 *
 * @author Alexandr Zernov
 */

public interface TableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Prepares painter to paint table header.
     *
     * @param rendererPane renderer pane
     */
    public void prepareToPaint ( CellRendererPane rendererPane );
}