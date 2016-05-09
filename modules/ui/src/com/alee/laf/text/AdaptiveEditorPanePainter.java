package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple EditorPanePainter adapter class.
 * It is used to install simple non-specific painters into WebEditorPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveEditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI> extends AdaptivePainter<E, U>
        implements IEditorPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveEditorPanePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveEditorPanePainter ( final Painter painter )
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