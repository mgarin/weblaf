package com.alee.laf.viewport;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ViewportPainter adapter class.
 * It is used to install simple non-specific painters into WebViewportUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveViewportPainter<E extends JViewport, U extends WebViewportUI> extends AdaptivePainter<E, U>
        implements ViewportPainter<E, U>
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
