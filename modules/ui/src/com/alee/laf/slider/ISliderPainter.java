package com.alee.laf.slider;

import com.alee.painter.ParameterizedPaint;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JSlider} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ISliderPainter<C extends JSlider, U extends WebSliderUI>
        extends SpecificPainter<C, U>, ParameterizedPaint<SliderPaintParameters>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}