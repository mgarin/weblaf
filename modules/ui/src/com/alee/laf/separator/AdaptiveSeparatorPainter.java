package com.alee.laf.separator;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link SeparatorPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WSeparatorUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveSeparatorPainter<C extends JSeparator, U extends WSeparatorUI> extends AdaptivePainter<C, U>
        implements ISeparatorPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveSeparatorPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSeparatorPainter ( final Painter painter )
    {
        super ( painter );
    }
}