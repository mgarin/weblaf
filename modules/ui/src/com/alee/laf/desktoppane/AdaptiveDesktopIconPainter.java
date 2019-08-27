package com.alee.laf.desktoppane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link DesktopIconPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebDesktopIconUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveDesktopIconPainter<C extends JInternalFrame.JDesktopIcon, U extends WDesktopIconUI>
        extends AdaptivePainter<C, U> implements IDesktopIconPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveDesktopIconPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveDesktopIconPainter ( final Painter painter )
    {
        super ( painter );
    }
}