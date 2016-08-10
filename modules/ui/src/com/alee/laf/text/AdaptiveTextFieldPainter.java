package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link TextFieldPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTextFieldUI}.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveTextFieldPainter<E extends JTextField, U extends WTextFieldUI> extends AdaptivePainter<E, U>
        implements ITextFieldPainter<E, U>
{
    /**
     * Constructs new {@link AdaptiveTextFieldPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTextFieldPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public String getInputPrompt ()
    {
        return null;
    }

    @Override
    public boolean isInputPromptVisible ()
    {
        return false;
    }

    @Override
    public Component getLeadingComponent ()
    {
        return null;
    }

    @Override
    public Component getTrailingComponent ()
    {
        return null;
    }
}