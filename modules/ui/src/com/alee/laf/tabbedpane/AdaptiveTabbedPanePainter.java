package com.alee.laf.tabbedpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link TabbedPanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTabbedPaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveTabbedPanePainter<C extends JTabbedPane, U extends WTabbedPaneUI> extends AdaptivePainter<C, U>
        implements ITabbedPanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTabbedPanePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveTabbedPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}