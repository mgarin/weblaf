package com.alee.laf.tree;

import com.alee.api.annotations.NotNull;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link TreePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTreeUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveTreePainter<C extends JTree, U extends WTreeUI> extends AdaptivePainter<C, U> implements ITreePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTreePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveTreePainter ( @NotNull final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isRowHoverDecorationSupported ()
    {
        return false;
    }

    @Override
    public void prepareToPaint ( @NotNull final TreePaintParameters parameters )
    {
        /**
         * Nothing needs to be done for adaptive class.
         */
    }

    @Override
    public void cleanupAfterPaint ()
    {
        /**
         * Nothing needs to be done for adaptive class.
         */
    }
}