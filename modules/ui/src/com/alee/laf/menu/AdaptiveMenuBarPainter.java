package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link MenuBarPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebMenuBarUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveMenuBarPainter<C extends JMenuBar, U extends WebMenuBarUI> extends AdaptivePainter<C, U>
        implements IMenuBarPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveMenuBarPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMenuBarPainter ( final Painter painter )
    {
        super ( painter );
    }
}