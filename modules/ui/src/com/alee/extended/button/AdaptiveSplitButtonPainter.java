package com.alee.extended.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple SplitButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebSplitButtonUI.
 *
 * @author Mikle Garin
 */

public final class AdaptiveSplitButtonPainter<E extends WebSplitButton, U extends WebSplitButtonUI> extends AdaptivePainter<E, U>
        implements ISplitButtonPainter<E, U>
{
    /**
     * Constructs new AdaptiveSplitButtonPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSplitButtonPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isOnSplit ()
    {
        return false;
    }
}