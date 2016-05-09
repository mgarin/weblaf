package com.alee.laf.list;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ListPainter adapter class.
 * It is used to install simple non-specific painters into WebListUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveListPainter<E extends JList, U extends WebListUI> extends AdaptivePainter<E, U> implements IListPainter<E, U>
{
    /**
     * Constructs new AdaptiveListPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveListPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isHoverDecorationSupported ()
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