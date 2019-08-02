package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link MenuItemPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebMenuItemUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveMenuItemPainter<C extends JMenuItem, U extends WebMenuItemUI> extends AdaptivePainter<C, U>
        implements IMenuItemPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveMenuItemPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMenuItemPainter ( final Painter painter )
    {
        super ( painter );
    }
}