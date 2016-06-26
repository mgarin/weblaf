package com.alee.laf.radiobutton;

import com.alee.laf.checkbox.AbstractStateButtonPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JRadioButton} component.
 * It is used as {@link WebRadioButtonUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class RadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements IRadioButtonPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractStateButtonPainter}.
     */
}