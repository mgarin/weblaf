package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;

/**
 * Base interface for abstract menu item painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IAbstractMenuItemPainter<E extends JMenuItem, U extends MenuItemUI> extends SpecificPainter<E, U>
{
}