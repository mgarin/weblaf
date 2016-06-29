package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JCheckBoxMenuItem} component.
 * It is used as {@link WebCheckBoxMenuItemUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class CheckBoxMenuItemPainter<E extends JCheckBoxMenuItem, U extends WebCheckBoxMenuItemUI, D extends IDecoration<E, D>>
        extends AbstractStateMenuItemPainter<E, U, D> implements ICheckBoxMenuItemPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.laf.menu.AbstractStateMenuItemPainter}.
     */
}