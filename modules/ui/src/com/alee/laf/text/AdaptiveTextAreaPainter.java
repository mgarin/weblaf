package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple TextAreaPainter adapter class.
 * It is used to install simple non-specific painters into WebTextAreaUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveTextAreaPainter<E extends JTextArea, U extends WebTextAreaUI> extends AdaptivePainter<E, U>
        implements ITextAreaPainter<E, U>
{
    /**
     * Constructs new AdaptiveTextAreaPainter for the specified painter.
     *
     * @param painter painter to adapt
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