package com.alee.laf.text;

import com.alee.api.annotations.Nullable;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link FormattedTextFieldPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WFormattedTextFieldUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveFormattedTextFieldPainter<C extends JFormattedTextField, U extends WFormattedTextFieldUI>
        extends AdaptivePainter<C, U> implements IFormattedTextFieldPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveFormattedTextFieldPainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveFormattedTextFieldPainter ( final Painter painter )
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

    @Nullable
    @Override
    public Component getLeadingComponent ()
    {
        return null;
    }

    @Nullable
    @Override
    public Component getTrailingComponent ()
    {
        return null;
    }
}