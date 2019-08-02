package com.alee.laf.viewport;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ViewportPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WViewportUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveViewportPainter<C extends JViewport, U extends WViewportUI> extends AdaptivePainter<C, U>
        implements IViewportPainter<C, U>
{
    /**
     * Constructs new AdaptiveViewportPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveViewportPainter ( final Painter painter )
    {
        super ( painter );
    }
}