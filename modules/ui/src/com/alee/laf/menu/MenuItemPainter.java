package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JMenuItem} component.
 * It is used as {@link WebMenuItemUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class MenuItemPainter<E extends JMenuItem, U extends WebMenuItemUI, D extends IDecoration<E, D>>
        extends AbstractMenuItemPainter<E, U, D> implements IMenuItemPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractMenuItemPainter}.
     */
}