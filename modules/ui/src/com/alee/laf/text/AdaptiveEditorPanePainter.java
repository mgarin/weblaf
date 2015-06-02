package com.alee.laf.text;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple EditorPanePainter adapter class.
 * It is used to install simple non-specific painters into WebEditorPaneUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveEditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI> extends AdaptivePainter<E, U>
        implements EditorPanePainter<E, U>
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
}
