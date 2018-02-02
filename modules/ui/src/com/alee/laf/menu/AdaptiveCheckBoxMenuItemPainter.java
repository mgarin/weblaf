package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link CheckBoxMenuItemPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebCheckBoxMenuItemUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveCheckBoxMenuItemPainter<C extends JMenuItem, U extends WebCheckBoxMenuItemUI> extends AdaptivePainter<C, U>
        implements ICheckBoxMenuItemPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveCheckBoxMenuItemPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveCheckBoxMenuItemPainter ( final Painter painter )
    {
        super ( painter );
    }
}