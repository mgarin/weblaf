package com.alee.laf.splitpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ISplitPanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WSplitPaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveSplitPanePainter<C extends JSplitPane, U extends WSplitPaneUI> extends AdaptivePainter<C, U>
        implements ISplitPanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveSplitPanePainter} for the specified {@link Painter}.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveSplitPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}