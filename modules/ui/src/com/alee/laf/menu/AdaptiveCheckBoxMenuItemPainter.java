package com.alee.laf.menu;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple CheckBoxMenuItemPainter adapter class.
 * It is used to install simple non-specific painters into WebCheckBoxMenuItemUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveCheckBoxMenuItemPainter<E extends JMenuItem, U extends WebCheckBoxMenuItemUI> extends AdaptivePainter<E, U>
        implements CheckBoxMenuItemPainter<E, U>
{
    /**
     * Constructs new AdaptiveCheckBoxMenuItemPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveCheckBoxMenuItemPainter ( final Painter painter )
    {
        super ( painter );
    }
}
