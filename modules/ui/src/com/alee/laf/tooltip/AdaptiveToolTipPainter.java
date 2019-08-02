package com.alee.laf.tooltip;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ToolTipPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WToolTipUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveToolTipPainter<C extends JToolTip, U extends WToolTipUI> extends AdaptivePainter<C, U>
        implements IToolTipPainter<C, U>
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