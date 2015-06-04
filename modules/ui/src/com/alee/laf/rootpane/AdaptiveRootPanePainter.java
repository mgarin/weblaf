package com.alee.laf.rootpane;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple RootPanePainter adapter class.
 * It is used to install simple non-specific painters into WebRootPaneUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveRootPanePainter<E extends JRootPane, U extends WebRootPaneUI> extends AdaptivePainter<E, U>
        implements RootPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveRootPanePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveRootPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}
