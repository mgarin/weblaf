package com.alee.laf.button;

import javax.swing.*;

/**
 * Base interface for {@link JToggleButton} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IToggleButtonPainter<E extends JToggleButton, U extends WToggleButtonUI> extends IAbstractButtonPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}