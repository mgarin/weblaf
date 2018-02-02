package com.alee.laf.separator;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JSeparator} component.
 * It is used as {@link WSeparatorUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class SeparatorPainter<C extends JSeparator, U extends WSeparatorUI, D extends IDecoration<C, D>>
        extends AbstractSeparatorPainter<C, U, D> implements ISeparatorPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractSeparatorPainter}.
     */
}