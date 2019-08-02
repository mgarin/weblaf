package com.alee.laf.toolbar;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ToolBarSeparatorPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WToolBarSeparatorUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveToolBarSeparatorPainter<C extends JToolBar.Separator, U extends WToolBarSeparatorUI>
        extends AdaptivePainter<C, U> implements IToolBarSeparatorPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveToolBarSeparatorPainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveToolBarSeparatorPainter ( final Painter painter )
    {
        super ( painter );
    }
}