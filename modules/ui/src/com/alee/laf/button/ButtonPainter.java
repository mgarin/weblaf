package com.alee.laf.button;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JButton component.
 * It is used as WebButtonUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class ButtonPainter<E extends JButton, U extends WebButtonUI, D extends IDecoration<E, D>> extends AbstractButtonPainter<E, U, D>
        implements IButtonPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.laf.button.AbstractButtonPainter}.
     */
}