package com.alee.laf.rootpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link RootPanePainter} adapter class.
 * It is used to install simple non-specific {@link Painter}s into {@link WRootPaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveRootPanePainter<C extends JRootPane, U extends WRootPaneUI> extends AdaptivePainter<C, U>
        implements IRootPanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveRootPanePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveRootPanePainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isDecorated ()
    {
        return false;
    }
}