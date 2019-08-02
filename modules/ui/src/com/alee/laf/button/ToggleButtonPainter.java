package com.alee.laf.button;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JToggleButton} component.
 * It is used as {@link WToggleButtonUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class ToggleButtonPainter<C extends JToggleButton, U extends WToggleButtonUI, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements IToggleButtonPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractButtonPainter}.
     */
}