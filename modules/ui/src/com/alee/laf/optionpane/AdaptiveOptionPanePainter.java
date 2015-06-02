package com.alee.laf.optionpane;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple OptionPanePainter adapter class.
 * It is used to install simple non-specific painters into WebOptionPaneUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveOptionPanePainter<E extends JOptionPane, U extends WebOptionPaneUI> extends AdaptivePainter<E, U>
        implements OptionPanePainter<E, U>
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
