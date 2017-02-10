package com.alee.laf.radiobutton;

import javax.swing.*;

/**
 * Base interface for {@link JRadioButton} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IRadioButtonPainter<E extends JRadioButton, U extends WRadioButtonUI> extends IAbstractStateButtonPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}