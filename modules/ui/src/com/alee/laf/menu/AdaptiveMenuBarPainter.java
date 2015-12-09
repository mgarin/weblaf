package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple MenuBarPainter adapter class.
 * It is used to install simple non-specific painters into WebMenuBarUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveMenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends AdaptivePainter<E, U>
        implements IMenuBarPainter<E, U>
{
    /**
     * Constructs new AdaptiveMenuBarPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMenuBarPainter ( final Painter painter )
    {
        super ( painter );
    }
}
