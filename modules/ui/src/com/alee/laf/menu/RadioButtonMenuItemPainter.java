package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JRadioButtonMenuItem} component.
 * It is used as {@link WebRadioButtonMenuItemUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class RadioButtonMenuItemPainter<E extends JRadioButtonMenuItem, U extends WebRadioButtonMenuItemUI, D extends IDecoration<E, D>>
        extends AbstractStateMenuItemPainter<E, U, D> implements IRadioButtonMenuItemPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.laf.menu.AbstractStateMenuItemPainter}.
     */
}