package com.alee.laf.slider;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSlider component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ISliderPainter<E extends JSlider, U extends WebSliderUI> extends SpecificPainter<E, U>
{
    /**
     * Provide true if the user is dragging the slider.
     *
     * @param dragging true if the user is dragging
     */
    public void setDragging ( boolean dragging );
}