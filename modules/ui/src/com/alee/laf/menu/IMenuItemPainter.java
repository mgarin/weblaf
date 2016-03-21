package com.alee.laf.menu;

import javax.swing.*;

/**
 * Base interface for JToolBar component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IMenuItemPainter<E extends JMenuItem, U extends WebMenuItemUI> extends IAbstractMenuItemPainter<E, U>
{
}