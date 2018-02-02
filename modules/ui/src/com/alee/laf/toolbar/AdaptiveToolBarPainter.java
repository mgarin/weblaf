package com.alee.laf.toolbar;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ToolBarPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebToolBarUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveToolBarPainter<C extends JToolBar, U extends WebToolBarUI> extends AdaptivePainter<C, U>
        implements IToolBarPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveToolBarPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveToolBarPainter ( final Painter painter )
    {
        super ( painter );
    }
}