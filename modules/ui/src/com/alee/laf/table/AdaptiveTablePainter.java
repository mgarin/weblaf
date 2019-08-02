package com.alee.laf.table;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link TablePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebTableUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveTablePainter<C extends JTable, U extends WebTableUI> extends AdaptivePainter<C, U> implements ITablePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTablePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
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