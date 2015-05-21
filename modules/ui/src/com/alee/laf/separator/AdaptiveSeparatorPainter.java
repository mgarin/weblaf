package com.alee.laf.separator;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Simple SeparatorPainter adapter class.
 * It is used to install simple non-specific painters into WebSeparatorUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveSeparatorPainter<E extends JSeparator, U extends WebSeparatorUI, P extends Painter & SpecificPainter>
        extends AdaptivePainter<E, U, P> implements SeparatorPainter<E, U>
{
    /**
     * Constructs new AdaptiveButtonPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSeparatorPainter ( final P painter )
    {
        super ( painter );
    }
}
