package com.alee.laf.menu;

import com.alee.laf.separator.IAbstractSeparatorPainter;

import javax.swing.*;

/**
 * Base interface for JSeparator component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IPopupMenuSeparatorPainter<E extends JPopupMenu.Separator, U extends WebPopupMenuSeparatorUI>
        extends IAbstractSeparatorPainter<E, U>
{
}