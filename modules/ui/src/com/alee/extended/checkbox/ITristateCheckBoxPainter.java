package com.alee.extended.checkbox;

import com.alee.laf.radiobutton.IAbstractStateButtonPainter;

/**
 * Base interface for {@link WebTristateCheckBox} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITristateCheckBoxPainter<E extends WebTristateCheckBox, U extends WTristateCheckBoxUI>
        extends IAbstractStateButtonPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}