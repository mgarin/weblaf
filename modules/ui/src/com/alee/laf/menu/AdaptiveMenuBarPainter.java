package com.alee.laf.menu;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple MenuBarPainter adapter class.
 * It is used to install simple non-specific painters into WebMenuBarUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveMenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends AdaptivePainter<E, U>
        implements MenuBarPainter<E, U>
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
