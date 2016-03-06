package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple CheckBoxMenuItemPainter adapter class.
 * It is used to install simple non-specific painters into WebCheckBoxMenuItemUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveRadioButtonMenuItemPainter<E extends JMenuItem, U extends WebRadioButtonMenuItemUI> extends AdaptivePainter<E, U>
        implements IRadioButtonMenuItemPainter<E, U>
{
    /**
     * Constructs new AdaptiveRadioButtonMenuItemPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveRadioButtonMenuItemPainter ( final Painter painter )
    {
        super ( painter );
    }
}
