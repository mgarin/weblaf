package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple FormattedTextFieldPainter adapter class.
 * It is used to install simple non-specific painters into WebFormattedTextFieldUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveFormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends AdaptivePainter<E, U> implements FormattedTextFieldPainter<E, U>
{
    /**
     * Constructs new AdaptiveFormattedTextFieldPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveFormattedTextFieldPainter ( final Painter painter )
    {
        super ( painter );
    }
}
