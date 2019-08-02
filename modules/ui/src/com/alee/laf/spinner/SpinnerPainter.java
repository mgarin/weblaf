package com.alee.laf.spinner;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JSpinner} component.
 * It is used as {@link WebSpinnerUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class SpinnerPainter<C extends JSpinner, U extends WebSpinnerUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements ISpinnerPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractDecorationPainter}.
     */
}