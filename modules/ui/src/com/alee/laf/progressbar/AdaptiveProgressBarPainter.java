package com.alee.laf.progressbar;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple ProgressBarPainter adapter class.
 * It is used to install simple non-specific painters into WebProgressBarUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveProgressBarPainter<E extends JProgressBar, U extends WebProgressBarUI> extends AdaptivePainter<E, U>
        implements ProgressBarPainter<E, U>
{
    /**
     * Constructs new AdaptiveProgressBarPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveProgressBarPainter ( final Painter painter )
    {
        super ( painter );
    }
}
