package com.alee.extended.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple {@link SplitButtonPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WSplitButtonUI}.
 *
 * @author Mikle Garin
 */

public final class AdaptiveSplitButtonPainter<E extends WebSplitButton, U extends WSplitButtonUI> extends AdaptivePainter<E, U>
        implements ISplitButtonPainter<E, U>
{
    /**
     * Constructs new {@link AdaptiveSplitButtonPainter} for the specified painter.
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