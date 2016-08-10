package com.alee.laf.separator;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JSeparator} component.
 * It is used as {@link WebSeparatorUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class SeparatorPainter<E extends JSeparator, U extends WebSeparatorUI, D extends IDecoration<E, D>>
        extends AbstractSeparatorPainter<E, U, D> implements ISeparatorPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractSeparatorPainter}.
     */
}