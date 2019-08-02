package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JCheckBoxMenuItem} component.
 * It is used as {@link WebCheckBoxMenuItemUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class CheckBoxMenuItemPainter<C extends JCheckBoxMenuItem, U extends WebCheckBoxMenuItemUI, D extends IDecoration<C, D>>
        extends AbstractStateMenuItemPainter<C, U, D> implements ICheckBoxMenuItemPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractStateMenuItemPainter}.
     */
}