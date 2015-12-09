package com.alee.laf.table;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Simple TablePainter adapter class.
 * It is used to install simple non-specific painters into WebTableUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveTableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends AdaptivePainter<E, U>
        implements ITableHeaderPainter<E, U>
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

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        // Ignore this method in adaptive class
    }
}