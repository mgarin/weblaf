package com.alee.laf.menu;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple MenuPainter adapter class.
 * It is used to install simple non-specific painters into WebMenuUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveMenuPainter<E extends JMenu, U extends WebMenuUI> extends AdaptivePainter<E, U> implements MenuPainter<E, U>
{
    /**
     * Constructs new AdaptiveMenuPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveMenuPainter ( final Painter painter )
    {
        super ( painter );
    }
}
