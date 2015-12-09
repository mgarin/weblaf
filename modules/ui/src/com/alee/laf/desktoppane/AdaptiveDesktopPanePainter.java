package com.alee.laf.desktoppane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple DesktopPanePainter adapter class.
 * It is used to install simple non-specific painters into WebDesktopPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveDesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI> extends AdaptivePainter<E, U>
        implements IDesktopPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveDesktopPanePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveDesktopPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}