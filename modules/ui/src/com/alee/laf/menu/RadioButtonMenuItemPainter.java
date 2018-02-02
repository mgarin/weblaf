package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JRadioButtonMenuItem} component.
 * It is used as {@link WebRadioButtonMenuItemUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class RadioButtonMenuItemPainter<C extends JRadioButtonMenuItem, U extends WebRadioButtonMenuItemUI, D extends IDecoration<C, D>>
        extends AbstractStateMenuItemPainter<C, U, D> implements IRadioButtonMenuItemPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractStateMenuItemPainter}.
     */
}