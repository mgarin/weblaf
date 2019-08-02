package com.alee.laf.list;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ListPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WListUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveListPainter<C extends JList, U extends WListUI> extends AdaptivePainter<C, U> implements IListPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveListPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveListPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isItemHoverDecorationSupported ()
    {
        return false;
    }

    @Override
    public void prepareToPaint ( final Integer layoutOrientation, final Integer listHeight, final Integer listWidth,
                                 final Integer columnCount, final Integer rowsPerColumn, final Integer preferredHeight, final int cellWidth,
                                 final int cellHeight, final int[] cellHeights )
    {
        // Ignore this method in adaptive class
    }
}