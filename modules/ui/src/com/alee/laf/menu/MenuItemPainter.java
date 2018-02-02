package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JMenuItem} component.
 * It is used as {@link WebMenuItemUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class MenuItemPainter<C extends JMenuItem, U extends WebMenuItemUI, D extends IDecoration<C, D>>
        extends AbstractMenuItemPainter<C, U, D> implements IMenuItemPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractMenuItemPainter}.
     */
}