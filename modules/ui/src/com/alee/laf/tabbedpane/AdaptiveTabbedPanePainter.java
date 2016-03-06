package com.alee.laf.tabbedpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple TabbedPanePainter adapter class.
 * It is used to install simple non-specific painters into WebTabbedPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveTabbedPanePainter<E extends JTabbedPane, U extends WebTabbedPaneUI> extends AdaptivePainter<E, U>
        implements ITabbedPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveTabbedPanePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTabbedPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}
