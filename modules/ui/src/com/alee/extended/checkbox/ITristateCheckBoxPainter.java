package com.alee.extended.checkbox;

import com.alee.laf.radiobutton.IAbstractStateButtonPainter;

/**
 * Base interface for {@link WebTristateCheckBox} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ITristateCheckBoxPainter<C extends WebTristateCheckBox, U extends WTristateCheckBoxUI<C>>
        extends IAbstractStateButtonPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}