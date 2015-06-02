package com.alee.laf.table;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JTable component painters.
 *
 * @author Alexandr Zernov
 */

public interface TablePainter<E extends JTable, U extends WebTableUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Prepares painter to paint table.
     *
     * @param rendererPane renderer pane
     */
    public void prepareToPaint ( CellRendererPane rendererPane );
}
