package com.alee.laf.tabbedpane;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple TabbedPanePainter adapter class.
 * It is used to install simple non-specific painters into WebTabbedPaneUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTabbedPanePainter<E extends JTabbedPane, U extends WebTabbedPaneUI> extends AdaptivePainter<E, U>
        implements TabbedPanePainter<E, U>
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
