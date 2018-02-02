package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link EditorPanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WEditorPaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveEditorPanePainter<C extends JEditorPane, U extends WEditorPaneUI> extends AdaptivePainter<C, U>
        implements IEditorPanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveEditorPanePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
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