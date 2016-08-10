package com.alee.laf.radiobutton;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link RadioButtonPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WRadioButtonUI}.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveRadioButtonPainter<E extends JRadioButton, U extends WRadioButtonUI> extends AdaptivePainter<E, U>
        implements IRadioButtonPainter<E, U>
{
    /**
     * Constructs new {@link AdaptiveRadioButtonPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveRadioButtonPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Rectangle getIconBounds ()
    {
        return null;
    }
}