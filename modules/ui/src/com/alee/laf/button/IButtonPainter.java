package com.alee.laf.button;

import javax.swing.*;

/**
 * Base interface for JButton component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IButtonPainter<E extends JButton, U extends WebButtonUI> extends IAbstractButtonPainter<E, U>
{
}