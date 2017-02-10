package com.alee.laf.button;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JToggleButton} component.
 * It is used as {@link WToggleButtonUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class ToggleButtonPainter<E extends JToggleButton, U extends WToggleButtonUI, D extends IDecoration<E, D>>
        extends AbstractButtonPainter<E, U, D> implements IToggleButtonPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractButtonPainter}.
     */
}