package com.alee.laf.toolbar;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple SeparatorPainter adapter class.
 * It is used to install simple non-specific painters into WebSeparatorUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveToolBarSeparatorPainter<E extends JToolBar.Separator, U extends WebToolBarSeparatorUI>
        extends AdaptivePainter<E, U> implements IToolBarSeparatorPainter<E, U>
{
    /**
     * Constructs new AdaptiveToolBarSeparatorPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveToolBarSeparatorPainter ( final Painter painter )
    {
        super ( painter );
    }
}
