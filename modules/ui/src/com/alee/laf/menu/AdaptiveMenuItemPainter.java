package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple MenuItemPainter adapter class.
 * It is used to install simple non-specific painters into WebMenuItemUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveMenuItemPainter<E extends JMenuItem, U extends WebMenuItemUI> extends AdaptivePainter<E, U>
        implements IMenuItemPainter<E, U>
{
    /**
     * Constructs new AdaptiveMenuItemPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMenuItemPainter ( final Painter painter )
    {
        super ( painter );
    }
}