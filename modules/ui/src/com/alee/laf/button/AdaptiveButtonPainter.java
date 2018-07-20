package com.alee.laf.button;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link IButtonPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WButtonUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public final class AdaptiveButtonPainter<C extends JButton, U extends WButtonUI<C>>
        extends AdaptivePainter<C, U> implements IButtonPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveButtonPainter} for the specified {@link Painter}.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveButtonPainter ( final Painter painter )
    {
        super ( painter );
    }
}