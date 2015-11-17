package com.alee.laf.toolbar;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ToolBarPainter adapter class.
 * It is used to install simple non-specific painters into WebToolBarUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveToolBarPainter<E extends JToolBar, U extends WebToolBarUI> extends AdaptivePainter<E, U>
        implements ToolBarPainter<E, U>
{
    /**
     * Constructs new AdaptiveToolBarPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveToolBarPainter ( final Painter painter )
    {
        super ( painter );
    }
}
