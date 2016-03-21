package com.alee.laf.menu;

import javax.swing.*;

/**
 * Base interface for JMenuItem component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ICheckBoxMenuItemPainter<E extends JMenuItem, U extends WebCheckBoxMenuItemUI> extends IAbstractMenuItemPainter<E, U>
{
}