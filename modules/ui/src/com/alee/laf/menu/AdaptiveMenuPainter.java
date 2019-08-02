package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link MenuPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebMenuUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveMenuPainter<C extends JMenu, U extends WebMenuUI> extends AdaptivePainter<C, U> implements IMenuPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveMenuPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMenuPainter ( final Painter painter )
    {
        super ( painter );
    }
}