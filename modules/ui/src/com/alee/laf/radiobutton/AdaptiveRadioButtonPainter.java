package com.alee.laf.radiobutton;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;
import com.alee.laf.checkbox.WebCheckBoxUI;

import javax.swing.*;
import java.awt.*;

/**
 * Simple RadioButtonPainter adapter class.
 * It is used to install simple non-specific painters into WebCheckBoxUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveRadioButtonPainter<E extends JCheckBox, U extends WebCheckBoxUI> extends AdaptivePainter<E, U>
        implements RadioButtonPainter<E, U>
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getIconRect ()
    {
        return null;
    }
}
