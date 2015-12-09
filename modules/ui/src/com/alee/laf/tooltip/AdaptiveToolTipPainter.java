package com.alee.laf.tooltip;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ToolTipPainter adapter class.
 * It is used to install simple non-specific painters into WebToolTipUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveToolTipPainter<E extends JToolTip, U extends WebToolTipUI> extends AdaptivePainter<E, U>
        implements IToolTipPainter<E, U>
{
    /**
     * Constructs new AdaptiveToolTipPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveToolTipPainter ( final Painter painter )
    {
        super ( painter );
    }
}
