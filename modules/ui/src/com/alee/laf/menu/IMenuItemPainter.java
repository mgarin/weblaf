package com.alee.laf.menu;

import javax.swing.*;

/**
 * Base interface for {@link JToolBar} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IMenuItemPainter<C extends JMenuItem, U extends WebMenuItemUI> extends IAbstractMenuItemPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}