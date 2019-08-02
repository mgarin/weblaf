package com.alee.laf.radiobutton;

import javax.swing.*;

/**
 * Base interface for {@link JRadioButton} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IRadioButtonPainter<C extends JRadioButton, U extends WRadioButtonUI<C>> extends IAbstractStateButtonPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}