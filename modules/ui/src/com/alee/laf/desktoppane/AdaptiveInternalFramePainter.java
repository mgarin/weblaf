package com.alee.laf.desktoppane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple InternalFramePainter adapter class.
 * It is used to install simple non-specific painters into WebOptionPaneUI.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveInternalFramePainter<C extends JInternalFrame, U extends WebInternalFrameUI> extends AdaptivePainter<C, U>
        implements IInternalFramePainter<C, U>
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