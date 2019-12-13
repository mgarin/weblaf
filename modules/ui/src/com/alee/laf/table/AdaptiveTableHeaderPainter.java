package com.alee.laf.table;

import com.alee.api.annotations.NotNull;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.table.JTableHeader;

/**
 * Simple {@link TableHeaderPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebTableHeaderUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveTableHeaderPainter<C extends JTableHeader, U extends WTableHeaderUI> extends AdaptivePainter<C, U>
        implements ITableHeaderPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTableHeaderPainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveTableHeaderPainter ( @NotNull final Painter painter )
    {
        super ( painter );
    }
}