package com.alee.laf.spinner;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple SpinnerPainter adapter class.
 * It is used to install simple non-specific painters into WebSpinnerUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveSpinnerPainter<E extends JSpinner, U extends WebSpinnerUI> extends AdaptivePainter<E, U>
        implements ISpinnerPainter<E, U>
{
    /**
     * Constructs new AdaptiveSpinnerPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSpinnerPainter ( final Painter painter )
    {
        super ( painter );
    }
}