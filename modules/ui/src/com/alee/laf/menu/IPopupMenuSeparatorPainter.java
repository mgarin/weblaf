package com.alee.laf.menu;

import com.alee.laf.separator.IAbstractSeparatorPainter;

import javax.swing.*;

/**
 * Base interface for {@link JPopupMenu.Separator} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IPopupMenuSeparatorPainter<C extends JPopupMenu.Separator, U extends WPopupMenuSeparatorUI>
        extends IAbstractSeparatorPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}