package com.alee.laf.spinner;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JSpinner component.
 * It is used as WebSpinnerUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class SpinnerPainter<E extends JSpinner, U extends WebSpinnerUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements ISpinnerPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractDecorationPainter}.
     */
}