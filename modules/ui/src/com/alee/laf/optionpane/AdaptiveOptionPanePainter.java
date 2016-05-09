package com.alee.laf.optionpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple OptionPanePainter adapter class.
 * It is used to install simple non-specific painters into WebOptionPaneUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveOptionPanePainter<E extends JOptionPane, U extends WebOptionPaneUI> extends AdaptivePainter<E, U>
        implements IOptionPanePainter<E, U>
{
    /**
     * Constructs new AdaptiveOptionPanePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveOptionPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}
