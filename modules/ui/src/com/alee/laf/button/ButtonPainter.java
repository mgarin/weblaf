package com.alee.laf.button;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JButton} component.
 * It is used as {@link WButtonUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class ButtonPainter<C extends JButton, U extends WButtonUI<C>, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements IButtonPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractButtonPainter}.
     */
}