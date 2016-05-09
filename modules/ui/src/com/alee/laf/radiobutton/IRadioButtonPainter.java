package com.alee.laf.radiobutton;

import javax.swing.*;

/**
 * Base interface for JRadioButton component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IRadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI> extends IAbstractStateButtonPainter<E, U>
{
}