package com.alee.laf.splitpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple SplitPanePainter adapter class.
 * It is used to install simple non-specific painters into WebSplitPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveSplitPanePainter<E extends JSplitPane, U extends WebSplitPaneUI> extends AdaptivePainter<E, U>
        implements ISplitPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveSplitPanePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSplitPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}
