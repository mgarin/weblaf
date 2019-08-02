package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link PopupMenuSeparatorPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WPopupMenuSeparatorUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptivePopupMenuSeparatorPainter<C extends JPopupMenu.Separator, U extends WPopupMenuSeparatorUI>
        extends AdaptivePainter<C, U> implements IPopupMenuSeparatorPainter<C, U>
{
    /**
     * Constructs new {@link AdaptivePopupMenuSeparatorPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePopupMenuSeparatorPainter ( final Painter painter )
    {
        super ( painter );
    }
}