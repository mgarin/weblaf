package com.alee.laf.progressbar;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ProgressBarPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebProgressBarUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveProgressBarPainter<C extends JProgressBar, U extends WebProgressBarUI> extends AdaptivePainter<C, U>
        implements IProgressBarPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveProgressBarPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveProgressBarPainter ( final Painter painter )
    {
        super ( painter );
    }
}