package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link TextPanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTextPaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveTextPanePainter<C extends JTextPane, U extends WTextPaneUI> extends AdaptivePainter<C, U>
        implements ITextPanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTextPanePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveTextPanePainter ( final Painter painter )
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