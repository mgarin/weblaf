package com.alee.extended.button;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

/**
 * Simple SplitButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebSplitButtonUI.
 *
 * @author Mikle Garin
 */

public class AdaptiveSplitButtonPainter<E extends WebSplitButton, U extends WebSplitButtonUI> extends AdaptivePainter<E, U>
        implements SplitButtonPainter<E, U>
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnSplit ()
    {
        return false;
    }
}