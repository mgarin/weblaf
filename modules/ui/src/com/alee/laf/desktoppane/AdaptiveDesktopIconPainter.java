package com.alee.laf.desktoppane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple DesktopIconPainter adapter class.
 * It is used to install simple non-specific painters into WebDesktopIconUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveDesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI>
        extends AdaptivePainter<E, U> implements IDesktopIconPainter<E, U>
{
    /**
     * Constructs new AdaptiveDesktopIconPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveDesktopIconPainter ( final Painter painter )
    {
        super ( painter );
    }
}