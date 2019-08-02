package com.alee.laf.optionpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link OptionPanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebOptionPaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveOptionPanePainter<C extends JOptionPane, U extends WebOptionPaneUI> extends AdaptivePainter<C, U>
        implements IOptionPanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveOptionPanePainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveOptionPanePainter ( final Painter painter )
    {
        super ( painter );
    }
}