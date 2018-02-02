package com.alee.laf.menu;

import com.alee.laf.separator.AbstractSeparatorPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JPopupMenu.Separator} component.
 * It is used as {@link WPopupMenuSeparatorUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class PopupMenuSeparatorPainter<C extends JPopupMenu.Separator, U extends WPopupMenuSeparatorUI, D extends IDecoration<C, D>>
        extends AbstractSeparatorPainter<C, U, D> implements IPopupMenuSeparatorPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractSeparatorPainter}.
     */
}