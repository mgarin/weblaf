package com.alee.laf.slider;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple SliderPainter adapter class.
 * It is used to install simple non-specific painters into WebSliderUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveSliderPainter<E extends JSlider, U extends WebSliderUI> extends AdaptivePainter<E, U>
        implements ISliderPainter<E, U>
{
    /**
     * Constructs new AdaptiveSliderPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSliderPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void setDragging ( final boolean dragging )
    {
        // Ignore this method in adaptive class
    }
}