package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link TextFieldPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTextFieldUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveTextFieldPainter<C extends JTextField, U extends WTextFieldUI> extends AdaptivePainter<C, U>
        implements ITextFieldPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTextFieldPainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
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