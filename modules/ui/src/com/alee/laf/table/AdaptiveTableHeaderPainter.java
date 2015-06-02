package com.alee.laf.table;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Simple TablePainter adapter class.
 * It is used to install simple non-specific painters into WebTableUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends AdaptivePainter<E, U>
        implements TableHeaderPainter<E, U>
{
    /**
     * Constructs new AdaptiveTablePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTableHeaderPainter ( final Painter painter )
    {
        super ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        // Ignore this method in adaptive class
    }
}
