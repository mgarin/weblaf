package com.alee.laf.slider;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSlider component painters.
 *
 * @author Alexandr Zernov
 */

public interface SliderPainter<E extends JSlider, U extends WebSliderUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Provide true if the user is dragging the slider.
     *
     * @param dragging true if the user is dragging
     */
    void setDragging ( boolean dragging );
}
