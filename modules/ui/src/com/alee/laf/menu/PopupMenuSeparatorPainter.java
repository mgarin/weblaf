package com.alee.laf.menu;

import com.alee.laf.separator.AbstractSeparatorPainter;

import javax.swing.*;

/**
 * Basic painter for JPopupMenu.Separator component.
 * It is used as WebPopupMenuSeparatorUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class PopupMenuSeparatorPainter<E extends JPopupMenu.Separator, U extends WebPopupMenuSeparatorUI>
        extends AbstractSeparatorPainter<E, U> implements IPopupMenuSeparatorPainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.laf.separator.AbstractSeparatorPainter}.
     */
}