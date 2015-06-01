package com.alee.laf.text;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple PasswordFieldPainter adapter class.
 * It is used to install simple non-specific painters into WebPasswordFieldUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptivePasswordFieldPainter<E extends JPasswordField, U extends WebPasswordFieldUI> extends AdaptivePainter<E, U>
        implements PasswordFieldPainter<E, U>
{
    /**
     * Constructs new AdaptivePasswordFieldPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePasswordFieldPainter ( final Painter painter )
    {
        super ( painter );
    }
}
