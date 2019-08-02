package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link TextAreaPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTextAreaUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveTextAreaPainter<C extends JTextArea, U extends WTextAreaUI> extends AdaptivePainter<C, U>
        implements ITextAreaPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTextAreaPainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveTextAreaPainter ( final Painter painter )
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
}