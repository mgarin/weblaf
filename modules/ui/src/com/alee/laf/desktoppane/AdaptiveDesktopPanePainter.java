package com.alee.laf.desktoppane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple DesktopPanePainter adapter class.
 * It is used to install simple non-specific painters into WebDesktopPaneUI.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveDesktopPanePainter<C extends JDesktopPane, U extends WDesktopPaneUI> extends AdaptivePainter<C, U>
        implements IDesktopPanePainter<C, U>
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