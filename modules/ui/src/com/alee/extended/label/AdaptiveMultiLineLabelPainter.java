package com.alee.extended.label;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple MultilineLabelPainter adapter class.
 * It is used to install simple non-specific painters into WebMultiLineLabelUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveMultiLineLabelPainter<E extends JLabel, U extends WebMultiLineLabelUI> extends AdaptivePainter<E, U>
        implements MultiLineLabelPainter<E, U>
{
    /**
     * Constructs new AdaptiveMultilineLabelPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMultiLineLabelPainter ( final Painter painter )
    {
        super ( painter );
    }
}
