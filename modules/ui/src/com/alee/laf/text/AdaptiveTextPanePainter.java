package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple TextPanePainter adapter class.
 * It is used to install simple non-specific painters into WebTextPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveTextPanePainter<E extends JTextPane, U extends WebTextPaneUI> extends AdaptivePainter<E, U>
        implements ITextPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveTextPanePainter for the specified painter.
     *
     * @param painter painter to adapt
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