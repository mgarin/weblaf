package com.alee.laf.desktoppane;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple DesktopPanePainter adapter class.
 * It is used to install simple non-specific painters into WebDesktopPaneUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveDesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI> extends AdaptivePainter<E, U>
        implements DesktopPanePainter<E, U>
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
