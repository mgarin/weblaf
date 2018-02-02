package com.alee.laf.button;

import javax.swing.*;

/**
 * Base interface for {@link JToggleButton} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IToggleButtonPainter<C extends JToggleButton, U extends WToggleButtonUI> extends IAbstractButtonPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}