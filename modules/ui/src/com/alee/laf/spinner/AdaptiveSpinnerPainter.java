package com.alee.laf.spinner;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link SpinnerPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebSpinnerUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveSpinnerPainter<C extends JSpinner, U extends WebSpinnerUI> extends AdaptivePainter<C, U>
        implements ISpinnerPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveSpinnerPainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveSpinnerPainter ( final Painter painter )
    {
        super ( painter );
    }
}