package com.alee.laf.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebButtonUI.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public final class AdaptiveButtonPainter<C extends JButton, U extends WebButtonUI> extends AdaptivePainter<C, U>
        implements IButtonPainter<C, U>
{
    /**
     * Constructs new AdaptiveButtonPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveButtonPainter ( final Painter painter )
    {
        super ( painter );
    }
}