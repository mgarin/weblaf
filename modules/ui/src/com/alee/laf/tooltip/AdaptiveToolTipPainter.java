package com.alee.laf.tooltip;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple ToolTipPainter adapter class.
 * It is used to install simple non-specific painters into WebToolTipUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveToolTipPainter<E extends JComponent, U extends WebToolTipUI> extends AdaptivePainter<E, U>
        implements ToolTipPainter<E, U>
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
