package com.alee.laf.text;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple TextFieldPainter adapter class.
 * It is used to install simple non-specific painters into WebTextFieldUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTextFieldPainter<E extends JTextField, U extends WebTextFieldUI> extends AdaptivePainter<E, U>
        implements TextFieldPainter<E, U>
{
    /**
     * Constructs new AdaptiveTextFieldPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTextFieldPainter ( final Painter painter )
    {
        super ( painter );
    }
}
