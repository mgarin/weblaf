package com.alee.laf.separator;

import javax.swing.*;

/**
 * Base interface for {@link JSeparator} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ISeparatorPainter<C extends JSeparator, U extends WSeparatorUI> extends IAbstractSeparatorPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}