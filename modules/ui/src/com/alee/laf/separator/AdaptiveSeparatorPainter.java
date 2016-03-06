package com.alee.laf.separator;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple SeparatorPainter adapter class.
 * It is used to install simple non-specific painters into WebSeparatorUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveSeparatorPainter<E extends JSeparator, U extends WebSeparatorUI> extends AdaptivePainter<E, U>
        implements ISeparatorPainter<E, U>
{
    /**
     * Constructs new AdaptiveSeparatorPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSeparatorPainter ( final Painter painter )
    {
        super ( painter );
    }
}
