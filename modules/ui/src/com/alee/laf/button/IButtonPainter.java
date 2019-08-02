package com.alee.laf.button;

import javax.swing.*;

/**
 * Base interface for {@link JButton} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public interface IButtonPainter<C extends JButton, U extends WButtonUI<C>> extends IAbstractButtonPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}