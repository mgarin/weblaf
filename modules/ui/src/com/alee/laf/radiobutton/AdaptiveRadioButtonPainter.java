package com.alee.laf.radiobutton;

import com.alee.api.annotations.Nullable;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link RadioButtonPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WRadioButtonUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveRadioButtonPainter<C extends JRadioButton, U extends WRadioButtonUI<C>>
        extends AdaptivePainter<C, U> implements IRadioButtonPainter<C, U>
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

    @Nullable
    @Override
    public Rectangle getIconBounds ()
    {
        return null;
    }
}