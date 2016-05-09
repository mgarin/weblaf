package com.alee.laf.desktoppane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple InternalFramePainter adapter class.
 * It is used to install simple non-specific painters into WebOptionPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI> extends AdaptivePainter<E, U>
        implements IInternalFramePainter<E, U>
{
    /**
     * Constructs new AdaptiveInternalFramePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveInternalFramePainter ( final Painter painter )
    {
        super ( painter );
    }
}