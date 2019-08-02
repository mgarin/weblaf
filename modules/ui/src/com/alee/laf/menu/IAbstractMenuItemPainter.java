package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;

/**
 * Base interface for abstract menu item painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IAbstractMenuItemPainter<C extends JMenuItem, U extends MenuItemUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}