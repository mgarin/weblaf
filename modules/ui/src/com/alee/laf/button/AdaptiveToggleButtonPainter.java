package com.alee.laf.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ToggleButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebToggleButtonUI.
 *
 * @author Mikle Garin
 */

public final class AdaptiveToggleButtonPainter<E extends JToggleButton, U extends WebToggleButtonUI> extends AdaptivePainter<E, U>
        implements IToggleButtonPainter<E, U>
{
    /**
     * Constructs new AdaptiveToggleButtonPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveToggleButtonPainter ( final Painter painter )
    {
        super ( painter );
    }
}