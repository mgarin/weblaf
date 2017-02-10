package com.alee.laf.button;

import javax.swing.*;

/**
 * Base interface for {@link JButton} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IButtonPainter<E extends JButton, U extends WButtonUI> extends IAbstractButtonPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}