package com.alee.laf.menu;

import com.alee.laf.separator.AbstractSeparatorPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JPopupMenu.Separator} component.
 * It is used as {@link WebPopupMenuSeparatorUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class PopupMenuSeparatorPainter<E extends JPopupMenu.Separator, U extends WebPopupMenuSeparatorUI, D extends IDecoration<E, D>>
        extends AbstractSeparatorPainter<E, U, D> implements IPopupMenuSeparatorPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractSeparatorPainter}.
     */
}