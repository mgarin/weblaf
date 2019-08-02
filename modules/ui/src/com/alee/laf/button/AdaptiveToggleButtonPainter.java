package com.alee.laf.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ToggleButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebToggleButtonUI.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public final class AdaptiveToggleButtonPainter<C extends JToggleButton, U extends WebToggleButtonUI> extends AdaptivePainter<C, U>
        implements IToggleButtonPainter<C, U>
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