package com.alee.laf.radiobutton;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JRadioButton component painters.
 *
 * @author Alexandr Zernov
 */

public interface RadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI>
        extends BasicStateButtonPainter<E, U>, SpecificPainter
{
}