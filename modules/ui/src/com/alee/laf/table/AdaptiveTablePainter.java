package com.alee.laf.table;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple TablePainter adapter class.
 * It is used to install simple non-specific painters into WebTableUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTablePainter<E extends JTable, U extends WebTableUI> extends AdaptivePainter<E, U> implements TablePainter<E, U>
{
    /**
     * Constructs new AdaptiveTablePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTablePainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        // Ignore this method in adaptive class
    }
}