package com.alee.laf.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebButtonUI.
 *
 * @author Mikle Garin
 */

public final class AdaptiveButtonPainter<E extends JButton, U extends WebButtonUI> extends AdaptivePainter<E, U>
        implements IButtonPainter<E, U>
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