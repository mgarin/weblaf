package com.alee.laf.menu;

import javax.swing.*;

/**
 * Base interface for {@link JMenuItem} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ICheckBoxMenuItemPainter<C extends JMenuItem, U extends WebCheckBoxMenuItemUI> extends IAbstractMenuItemPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}