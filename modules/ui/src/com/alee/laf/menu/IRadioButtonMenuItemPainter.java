package com.alee.laf.menu;

import javax.swing.*;

/**
 * Base interface for JMenuItem component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IRadioButtonMenuItemPainter<E extends JMenuItem, U extends WebRadioButtonMenuItemUI> extends IAbstractMenuItemPainter<E, U>
{
}