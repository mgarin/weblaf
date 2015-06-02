package com.alee.laf.desktoppane;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple DesktopIconPainter adapter class.
 * It is used to install simple non-specific painters into WebDesktopIconUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveDesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI> extends AdaptivePainter<E, U>
        implements DesktopIconPainter<E, U>
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
