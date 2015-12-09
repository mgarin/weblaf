package com.alee.laf.radiobutton;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple RadioButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebCheckBoxUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveRadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI> extends AdaptivePainter<E, U>
        implements IRadioButtonPainter<E, U>
{
    /**
     * Constructs new AdaptiveCheckBoxPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveRadioButtonPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Rectangle getIconRect ()
    {
        return null;
    }
}